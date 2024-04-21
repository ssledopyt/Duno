package com.example.duno.compose.map

import android.os.Bundle
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
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.example.duno.R
import com.example.duno.databinding.FragmentContainerMapBinding
import com.example.duno.ui.DunoSizes
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import timber.log.Timber


@Preview(apiLevel = 33)
@Composable
fun MapScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidViewBinding(factory = FragmentContainerMapBinding::inflate){
            val fragment = fragmentContainerMap.getFragment<MapScreen>()
        }
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
                        .padding(bottom = DunoSizes.standartDp, end = DunoSizes.standartDp - 6.dp),
                    onClick = {
                        isExpanded = true
                        Timber.e("Button True")

                    }
                ){
                    Icon(modifier = Modifier.size(DunoSizes.standartDp), imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    }
}


class MapScreen :Fragment(){
    private lateinit var mapView: MapView


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
        return inflater.inflate(R.layout.map_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapview)
 /*       mapView.mapWindow.map.move(
            CameraPosition(
                Point(55.751225, 37.629540),
                *//* zoom = *//* 17.0f,
                *//* azimuth = *//* 150.0f,
                *//* tilt = *//* 30.0f
            )
        )*/
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