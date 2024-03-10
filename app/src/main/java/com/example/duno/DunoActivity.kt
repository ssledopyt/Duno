package com.example.duno

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.duno.databinding.MapScreenBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DunoActivity : AppCompatActivity() {
/*    private lateinit var mapView: MapView*/


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        Timber.e("Zdes1")
        MapKitFactory.setApiKey(BuildConfig.YAMAp)
        MapKitFactory.initialize(this)
        setContent{
            Screen()
        }
    }
/*    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }*/
}