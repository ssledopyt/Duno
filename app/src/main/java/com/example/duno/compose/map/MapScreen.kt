package com.example.duno.compose.map

import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.duno.R
import com.example.duno.compose.elements.EventsCard
import com.example.duno.compose.elements.TextInTwoLines
import com.example.duno.databinding.FragmentContainerMapBinding
import com.example.duno.databinding.MapScreenBinding
import com.example.duno.db.ApiLocationOfSP
import com.example.duno.ui.Colors
import com.example.duno.viewmodel.DunoEventUIState
import com.example.duno.viewmodel.MapViewModel
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import timber.log.Timber
import java.time.LocalDateTime

@Composable
fun MapScreenUI(
    goToEvents: () -> Unit,
    goToEventDetails: (Int, Boolean) -> Unit,
    userNickname: String,
    meetingViewModel: MeetingViewModel,
    userViewModel: UserViewModel,
    mapViewModel: MapViewModel,
    innerPadding: PaddingValues
) {
    LaunchedEffect(true){
        userViewModel.getAllPlaces()
    }
    var isShowClubEvents = remember {mutableStateOf(false)}
    //var openPlace = remember {mutableStateOf(mapViewModel.openPlace)}
    var infoPlace = mapViewModel.openPlaceData.observeAsState().value
    val meetingUIState by meetingViewModel.meetingList.observeAsState()
    val club by mapViewModel.openPlaceData.observeAsState()
    BackHandler(
        onBack = {
            if (!mapViewModel.openPlace) goToEvents()
            else
                if (isShowClubEvents.value)
                    isShowClubEvents.value = false
                else
                    mapViewModel.setValueOP(false)
        }
    )
    Timber.e(mapViewModel.toString())
    Timber.e(mapViewModel.openPlace.toString())
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidViewBinding(factory = FragmentContainerMapBinding::inflate){
            val fragment = fragmentContainerMap.getFragment<MapScreen>()
        }
        if (mapViewModel.openPlace) {
            Timber.e("doljno otkrit")
            InfoBottomSheet(
                mapViewModel,
                meetingViewModel,
                infoPlace!!,
                innerPadding,
                meetingUIState,
                club!!,
                goToEventDetails,
                userNickname,
                meetingUIState!!.favEvents,
                isShowClubEvents
            )
        }
        Timber.e("Tapped the point/ from compose")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    mapViewModel: MapViewModel,
    meetingViewModel: MeetingViewModel,
    infoPlace: ApiLocationOfSP,
    innerPadding: PaddingValues,
    meetingUIState: DunoEventUIState?,
    club: ApiLocationOfSP,
    goToEventDetails: (Int, Boolean) -> Unit,
    userNickname: String,
    userLikes: List<Int>,
    isShowClubEvents: MutableState<Boolean>
) {
    var placeHasEvents = remember { mutableStateOf(meetingUIState!!.events.find {it.meetingOrganizer != userNickname
            && LocalDateTime.parse(it.meetingDate).isAfter(LocalDateTime.now())
            && it.meetingGeoMarker==mapViewModel.openPlaceData.value!!.geoMarker}==null)}
    ModalBottomSheet(
        windowInsets = WindowInsets(bottom = innerPadding.calculateBottomPadding()),
        modifier = Modifier.padding(bottom = 20.dp),
        onDismissRequest = {
            isShowClubEvents.value = false
            mapViewModel.setValueOP(false)
                           },
        containerColor = Color.White) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            val listState = rememberLazyListState()
            AnimatedVisibility(visible = isShowClubEvents.value) {
                AnimatedVisibility(visible = placeHasEvents.value) {
                    Box(modifier = Modifier.fillMaxWidth()){
                        Column (modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Text("Похоже здесь ещё нет мероприятий...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis)
                            Spacer(Modifier.height(4.dp))
                            Icon(modifier = Modifier.size(60.dp), painter = painterResource(R.drawable.icon_dice6_48), contentDescription = null, tint=Color.Unspecified)
                            Spacer(Modifier.height(4.dp))
                            Text("Перейди в профиль и создай своё!",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                EventsList(
                    listState,
                    mapViewModel,
                    meetingViewModel,
                    meetingUIState,
                    goToEventDetails,
                    club,
                    userNickname,
                    userLikes
                )
            }
            AnimatedVisibility(visible = !isShowClubEvents.value) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = infoPlace.nameClub,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        color = Colors.md_PrimaryContainer
                    )
                    TextInTwoLines(leftText = "Адрес:", rightText = infoPlace.place)
                    //Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.ss_MainColor),
                        onClick = {isShowClubEvents.value = true }) {
                        Text("Посмотреть мероприятия")
                    }
                }
            }
        }
    }
}

@Composable
fun EventsList(
    listState: LazyListState,
    openPlace: MapViewModel,
    meetingViewModel: MeetingViewModel,
    meetingUIState: DunoEventUIState?,
    goToEventDetails: (Int, Boolean) -> Unit,
    club: ApiLocationOfSP,
    userNickname: String,
    userLikes: List<Int>,
    ) {
    var userEvents by remember{ mutableStateOf(false) }
    var localDTime = LocalDateTime.now()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Timber.e("MapInfoBottomSheetEvents")
        itemsIndexed(meetingUIState!!.events) { index, event ->
            val distance =  FloatArray(1)
            Location.distanceBetween(
                event.meetingGeoMarker[0],
                event.meetingGeoMarker[1],
                club.geoMarker[0],
                club.geoMarker[1],
                distance
            )
            val eventTime = LocalDateTime.parse(event.meetingDate)
            if (event.meetingOrganizer != userNickname
                && eventTime.isAfter(localDTime)
                && calculateDistance(event.meetingGeoMarker[0],
                    event.meetingGeoMarker[1],
                    club.geoMarker[0],
                    club.geoMarker[1])<20f
            ) {
                EventsCard(
                    meetingViewModel = meetingViewModel,
                    userNickname = userNickname,
                    event = event,
                    userLike = userLikes.contains(event.meetingId),
                    goToEventDetails = goToEventDetails,
                    userLikes = userLikes,
                )
                userEvents = true
            } else if (index == meetingUIState.events.size) {
                Text("У вас нет ваших мероприятий. Сначала создайте своё!")
            }
        }

    }
}

class MapScreen : Fragment(){
    private lateinit var binding: MapScreenBinding
    private lateinit var mapView: MapView
    private lateinit var map : Map
    private lateinit var mapWindow: MapWindow
    //private val mapViewModel: MapViewModel by viewModels<MapViewModel>(ownerProducer = { requireActivity() })
    //private val mapViewModel: MapViewModel by activityViewModels<MapViewModel>()
    //private val mapViewModel: MapViewModel by hiltNavGraphViewModels<MapViewModel>(R.id.mapview)
    private val mapViewModel: MapViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var placemarkMapObject: PlacemarkMapObject
    private lateinit var pinsCollection: MapObjectCollection


    private lateinit var pin:ImageProvider
    private lateinit var pinStyle:IconStyle
    private val markerDataList = mutableListOf<PlacemarkMapObject>()

    private lateinit var placesData: List<ApiLocationOfSP>


    private val placemarkTapListener = MapObjectTapListener() { mapObject, point ->
            val data = mapObject.userData as PlacemarkUserData
            mapViewModel.setValue(data.data)
            mapViewModel.openPlace = true
            Timber.e(mapObject.userData.toString())
            map.cameraPosition.run {
                val position = CameraPosition(point, 14.0f, azimuth, tilt)
                map.move(position, SMOOTH_ANIMATION, null)
            }
            Timber.e("Tapped the point (${point.longitude}, ${point.latitude})")
        true
    }
    private val sizeChangedListener = SizeChangedListener { _, _, _ ->
        // Recalculate FocusRect and FocusPoint on every map's size change
        //updateFocusInfo()
    }

/*    private val inputListener = object : InputListener {
        override fun onMapLongTap(map: Map, point: Point) {
            // Move placemark after long tap
            placemarkMapObject.geometry = point
        }

        override fun onMapTap(map: Map, point: Point) {

        }

    }*/


    /*private val geoObjectTapListener = GeoObjectTapListener {
        // Move camera to selected geoObject
        val point = it.geoObject.geometry.firstOrNull()?.point ?: return@GeoObjectTapListener true
        map.cameraPosition.run {
            val position = CameraPosition(point, zoom, azimuth, tilt)
            map.move(position, SMOOTH_ANIMATION, null)
        }

        val selectionMetadata =
            it.geoObject.metadataContainer.getItem(GeoObjectSelectionMetadata::class.java)
        map.selectGeoObject(selectionMetadata)
        Toast.makeText(context,"Tapped ${it.geoObject.name} id = ${selectionMetadata.objectId}", Toast.LENGTH_LONG)

        true
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this.context)
        Timber.e("Zdes2")
        //mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapScreenBinding.inflate(layoutInflater)
        val view = binding.root
        mapView = view.findViewById(R.id.mapview)
        mapWindow = binding.mapview.mapWindow
        map = mapView.mapWindow.map
        map.cameraBounds.setMinZoomPreference(15f)
        map.cameraBounds.setMinZoomPreference(5f)
        //map.addTapListener(geoObjectTapListener)
        placesData = emptyList()
        val points = emptyList<Point>()

        val polyline = Polyline(POINTS)
        binding.apply {
            // Changing camera's zoom by controls on the map
            buttonMinus.setOnClickListener { changeZoomByStep(-ZOOM_STEP) }
            buttonPlus.setOnClickListener { changeZoomByStep(ZOOM_STEP) }

            /*buttonFocusGeometry.setOnClickListener {
                val geometry = Geometry.fromPolyline(polyline)
                val position = map.cameraPosition(geometry)
                map.move(position, SMOOTH_ANIMATION, null)
            }
            buttonFocusPlacemark.setOnClickListener {
                val position = map.cameraPosition.run {
                    CameraPosition(placemarkMapObject.geometry, zoom, azimuth, tilt)
                }
                map.move(position, SMOOTH_ANIMATION, null)
            }
            buttonCreatePlacemark.setOnClickListener {
                Timber.tag("MapScreen_ButtonCreatePlacemark").e(places.toString())
                // Usage of the screen coordinates to display placemarks in the center of a screen.
                val focusPoint = mapWindow.focusPoint ?: return@setOnClickListener
                val point = mapWindow.screenToWorld(focusPoint) ?: return@setOnClickListener
                placemarkMapObject.geometry = point
            }*/
        }
        pin = ImageProvider.fromResource(context, R.drawable.icon_pin_standart)
        pinStyle = IconStyle().apply {
            //tappableArea = Rect(PointF(20f,20f), PointF(48.0f,48.0f))
            scale = 0.1f
        }
        pinsCollection = map.mapObjects.addCollection()
        Timber.tag("MapScreen").e("Ну вот перед моделькой")
        userViewModel.places.observe(viewLifecycleOwner) { list ->
            Timber.tag("MapScreenPlacemarks").e(placesData.toString())
            if (list != null) {
                placesData = list
                addMarkers(placesData)
            }
        }
        //pinsCollection.addTapListener(placemarkTapListener)
        Timber.e(userViewModel.userNickname.value)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recentPoint = mapViewModel.getStartPoint()
        val recentPointPosition = CameraPosition(recentPoint, 14.0f, 0f, 0f)
        if (recentPoint.longitude==0.0 && recentPoint.latitude==0.0) {
            mapView.mapWindow.map.move(GeometryProvider.startPosition)
        }
        else{
            mapView.mapWindow.map.move(recentPointPosition)
        }

    }

    override fun onStart() {
        super.onStart()
        Timber.e("Zdes4")
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        Timber.e("Zdes5")
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    private fun addMarker(
        latitude: Double,
        longitude: Double,
        userData: ApiLocationOfSP
    ): PlacemarkMapObject {
        val marker = map.mapObjects.addPlacemark().apply {
            geometry = Point(latitude, longitude)
            setIcon(pin, pinStyle)
        }
        marker.userData = PlacemarkUserData(userData)
        marker.addTapListener(placemarkTapListener)
        return marker
    }

    private fun addMarkers(dataList: List<ApiLocationOfSP>) {
        for (data in dataList) {
            val marker = addMarker(
                latitude = data.geoMarker[0],
                longitude = data.geoMarker[1],
                userData = data
            )
            markerDataList.add(marker)
        }
    }

    private fun changeZoomByStep(value: Float) {
        with(map.cameraPosition) {
            map.move(
                CameraPosition(target, zoom + value, azimuth, tilt),
                SMOOTH_ANIMATION,
                null,
            )
        }
    }

    /*private fun updateFocusInfo() {
        val defaultPadding = 12f
        val bottomPadding = binding.layoutBottomCard.measuredHeight
        val rightPadding = binding.buttonMinus.measuredWidth
        // Focus rect consider a bottom card UI and map zoom controls.
        mapView.mapWindow.focusRect = ScreenRect(
            ScreenPoint(defaultPadding, defaultPadding),
            ScreenPoint(
                mapView.mapWindow.width() - rightPadding - defaultPadding,
                mapView.mapWindow.height() - bottomPadding - defaultPadding,
            )
        )
        mapView.mapWindow.focusPoint = ScreenPoint(
            mapView.mapWindow.width() / 2f,
            mapView.mapWindow.height() / 2f,
        )
    }*/

    companion object {
        private const val ZOOM_STEP = 1f

        private val START_ANIMATION = Animation(Animation.Type.LINEAR, 1f)
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)

        private val START_POSITION = CameraPosition(Point(54.707590, 20.508898), 15f, 0f, 0f)

        private val POINTS = mutableListOf(
            Point(54.701079, 20.513011),
            Point(54.702409, 20.505102),
            Point(54.709270, 20.508272),
            Point(54.708539, 20.514920),
            Point(54.705865, 20.514524),
            Point(54.706133, 20.511160),)
    }
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0]
}

data class PlacemarkUserData(
    //val name: String,
    val data: ApiLocationOfSP,
)