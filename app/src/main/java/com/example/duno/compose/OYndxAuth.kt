package com.example.duno.compose

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.duno.databinding.ActivityYndxAuthBinding

import com.yandex.authsdk.YandexAuthResult
import timber.log.Timber


class OYndxAuth : Fragment(){


    private lateinit var binding: ActivityYndxAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("Ya rabotayu")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = ActivityYndxAuthBinding.inflate(layoutInflater)
        return binding.root
  /*      val sdk = YandexAuthSdk.create(YandexAuthOptions(requireContext()))
        val launcher = registerForActivityResult(sdk.contract) { result -> handleResult(result) }
        val loginOptions = YandexAuthLoginOptions()

        var view: View = inflater.inflate(R.layout.activity_yndx_auth, container, false)
        view.findViewById<Button>(R.id.yndxauth).setOnClickListener{
            Timber.e("Button yandex!")
            launcher.launch(loginOptions)
        }
        lateinit var binding: ActivityYndxAuthBinding
        binding = ActivityYndxAuthBinding.inflate(layoutInflater)
        binding.yndxauth.setOnClickListener{
            Timber.e("Button yandex!")
            launcher.launch(loginOptions)
        }
        return view*/
    }


    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success ->
            {
                result.token.value
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


    /*    val client = OkHttpClient()

        fun run(url: String) {
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            Timber.e("$name: $value")
                        }

                        Timber.e(response.body!!.string())
                    }
                }
            })
        }*/

    /*
        fun getJwt(token: YandexAuthToken): String{
            run("")
            return TODO("Provide the return value")
        }
    */
}