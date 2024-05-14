package com.example.duno

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.duno.databinding.MapScreenBinding
import com.example.duno.viewmodel.MapViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

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