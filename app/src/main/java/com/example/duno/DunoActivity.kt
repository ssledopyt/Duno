package com.example.duno

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.duno.databinding.MapScreenBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DunoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(BuildConfig.YAMAp)
        Timber.e("Zdes1")
        setContent{
            Screen()
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.M)
    override fun onBackPressed() {
        val currentScreen = supportFragmentManager.fragments.lastOrNull()
        super.onBackPressed()
        finish()
    }*/
}