package com.example.duno.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewbinding.ViewBinding
import com.example.duno.BuildConfig
import com.example.duno.R
import com.yandex.mapkit.MapKitFactory
import com.example.duno.DunoActivity
import com.example.duno.R.*
import com.example.duno.databinding.MapScreenBinding
import com.example.duno.databinding.TestBinding
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import timber.log.Timber

/*
import kotlin.reflect.KClass

@Composable
fun FragmentView(fragmentClass: KClass<out Fragment>): Unit {
    val context = LocalContext.current
    val fragmentManager = (context as AppCompatActivity).supportFragmentManager

    // Создайте экземпляр Fragment
    val fragment = fragmentClass.java.newInstance()

    // Добавьте Fragment в транзакцию
    val transaction = fragmentManager.beginTransaction()
    transaction.add(R.id.fragment_container, fragment)
    transaction.commit()
}
*/



@Preview(apiLevel = 33)
@Composable
fun MapScreenUI(
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //AndroidViewBinding(factory = MapScreenBinding::inflate)

        var isExpanded by remember {mutableStateOf(false)}
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
                    }
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
                        .padding(bottom = 24.dp, end = 20.dp),
                    onClick = {
                        isExpanded = true
                        Timber.e("Button True")
                    }
                ){}
            }
        }
        //Spacer(modifier = Modifier.)


    }
}


/*
@Composable
fun <T : ViewBinding> FragmentHolderScreen(
    androidViewBindingFactory: (inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> T,
    androidViewBindingUpdate: T.() -> Unit = {},
) {
    AndroidViewBinding(
        factory = androidViewBindingFactory,
        modifier = Modifier.fillMaxSize(),
        update = androidViewBindingUpdate,
    )
}
*/

class MapScreen :Fragment(){
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("Zdes2")

//        MapKitFactory.setApiKey(BuildConfig.YAMAp)
        MapKitFactory.initialize(this.context)
        mapView.map.move(
            CameraPosition(
                Point(55.751225, 37.629540),
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 30.0f
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.e("Zdes3")
        val mapView = inflater.inflate(layout.map_screen, container, false)
        Timber.e("Initilaize map")
        return mapView
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
/*
class MapScreen :ComponentActivity(){
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("Zdes2")

//        MapKitFactory.setApiKey(BuildConfig.YAMAp)
        MapKitFactory.initialize(this)
        setContentView(layout.map_screen)
        mapView = findViewById(R.id.mapview)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}*/
