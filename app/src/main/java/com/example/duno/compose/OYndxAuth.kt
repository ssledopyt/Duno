package com.example.duno.compose

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.duno.databinding.ActivityYndxAuthBinding
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions

import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import timber.log.Timber


class OYndxAuth : Fragment(){
    private lateinit var binding: ActivityYndxAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sdk = YandexAuthSdk.create(YandexAuthOptions(requireContext()))
        val launcher = registerForActivityResult(sdk.contract) { result -> handleResult(result) }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
        //sdk.getJwt(yandexAuthToken)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = ActivityYndxAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success ->
            {
                val yandexAuthToken = result.token.value
                Timber.tag("Login").e(" Success ${result.token.value}")
            }
            is YandexAuthResult.Failure -> {
                Timber.tag("Login").e(result.exception)
            }
            YandexAuthResult.Cancelled -> {
                Timber.tag("Login").e("He is out")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}