package com.example.duno.compose.map

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.duno.R
import com.example.duno.databinding.FragmentContainerMapBinding
import com.example.duno.ui.Colors
import com.example.duno.ui.DunoSizes
import com.example.duno.viewmodel.MapViewModel
import com.example.duno.viewmodel.UserViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.Rect
import com.yandex.mapkit.map.SizeChangedListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import timber.log.Timber


@Composable
fun MapScreenUI(
    goToEvents: () -> Unit,
    //userViewModel: UserViewModel,
) {
    BackHandler {
        goToEvents()
    }
    val bundle = Bundle().apply { putParcelableArrayList("places",
    ArrayList())
    }
    var mapScreenUiState: MapViewModel = hiltViewModel()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidViewBinding(factory = FragmentContainerMapBinding::inflate){
            val fragment = fragmentContainerMap.getFragment<MapScreen>()
            val binding: FragmentContainerMapBinding


            fragment.arguments?.putParcelableArrayList("places",
                ArrayList())

        }
        var isExpanded by remember {mutableStateOf(false)}
        Column {
            Row {
                Button(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 24.dp),
                    onClick = { mapScreenUiState.plusZoom() }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription =null )
                }
            }
            Row {
                Button(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 24.dp),
                    onClick = { mapScreenUiState.minusZoom()  }) {
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription =null )
                }
            }
        }

        if (isExpanded) {
            AnimatedVisibility(
                visible = isExpanded,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
            ) {
                Button(
                    modifier = Modifier
                        .size(height = 48.dp, width = 360.dp),
                    onClick = {
                        isExpanded = false
                        Timber.e("Button False")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.ss_BackGround,
                        contentColor = Colors.ss_AccentColor)
                ){
                    Text(text = "Организуем тут")
                }
            }
        } else {
            AnimatedVisibility(
                visible = !isExpanded,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = DunoSizes.standartDp, end = DunoSizes.standartDp - 6.dp),
                    onClick = {
                        isExpanded = true
                        Timber.e("Button True")

                    },
                    containerColor = Colors.es_Background,
                    contentColor = Colors.ss_AccentColor
                ){
                    Icon(modifier = Modifier.size(DunoSizes.standartDp), imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    }
}


class MapScreen :Fragment(){
    private lateinit var mapView: MapView
    private lateinit var map : Map
    val mapViewModel by activityViewModels<MapViewModel>()
    val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this.context)
        Timber.e("Zdes2")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.map_screen,container,false)
        mapView = view.findViewById(R.id.mapview)
        map = mapView.mapWindow.map
        map.cameraBounds.setMinZoomPreference(15f)
        map.cameraBounds.setMinZoomPreference(5f)
        Timber.e(userViewModel.userNickname.value)
        Timber.e(mapViewModel.createEvent.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val placemark = map.mapObjects.addPlacemark().apply {
            geometry = Point(59.935493, 30.327392)
            setIcon(ImageProvider.fromResource(context, R.drawable.icon_d20),
                IconStyle().apply {
                    flat = true
                    anchor = PointF(0.5f, 1.0f)
                    scale = 0.07f
                })
        }
        mapView.mapWindow.map.move(
            CameraPosition(
                Point(59.935493, 30.327392),
                 mapViewModel.zoomOfMap.value,
                 100.0f,
                 60.0f
            )
        )
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
}