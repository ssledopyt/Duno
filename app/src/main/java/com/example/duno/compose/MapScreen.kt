package com.example.duno.compose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.duno.BuildConfig
import com.example.duno.R.*
import com.example.duno.databinding.ActivityYndxAuthBinding
import com.example.duno.databinding.MapScreenBinding
import com.example.duno.ui.StandartDp
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import timber.log.Timber


@Preview(apiLevel = 33)
@Composable
fun MapScreenUI(
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidViewBinding(factory = MapScreenBinding::inflate)
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
                        .padding(bottom = StandartDp, end = StandartDp - 6.dp),
                    onClick = {
                        isExpanded = true
                        Timber.e("Button True")
                    }
                ){
                    Icon(modifier = Modifier.size(StandartDp), imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
        //Spacer(modifier = Modifier.)


    }
}


class MapScreen :Fragment(){
    private lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("Zdes2")
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
        //Timber.e("Zdes4")
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}