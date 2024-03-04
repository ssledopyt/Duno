package com.example.duno.compose

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment

@Composable
fun MapScreens() {
    AndroidViewBinding(MyFragmentLayoutBinding::inflate) {
        val myFragment = fragmentContainerView.getFragment<MapScreen>()
        // ...
    }
}

class MapScreen(){

}

/*class MapScreend : Fragment(){
    @Composable
    fun ToastGreetingButton(greeting: String) {
        val context = LocalContext.current
        Button(onClick = {
            Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
        }) {
            Text("Greet")
        }
    }
}*/
/*
@Composable
fun YandexMap(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    zoom: Float = 14f,
    location: LatLng? = null,
    startPosition: LatLng? = null,
    points: List<PointMapModel>,
    onPointClick: (id: Long) -> Unit,
    customPosition: Boolean,
    canSelectPosition: Boolean,
    anotherLocationSelected: Boolean,
    bottomFocusAreaPadding: Int,
    onPositionSelected: (lat: Double, lng: Double) -> Unit,
    onDragged: () -> Unit
) {
}*/
