package com.otmutienipen.testrameddia.controller

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.fragment.app.FragmentManager
import com.otmutienipen.testrameddia.view.screens.SplashNoInternetDialog


internal class NetworkController(private val context: Application) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    val isConnected get() = isInternetAvailable(context)
    private lateinit var alertDialog: SplashNoInternetDialog

    private fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }

    fun listenConnection(fragmentTransaction: FragmentManager, isContentLoaded: Boolean, callback: () -> Unit) {
        alertDialog = SplashNoInternetDialog()
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (alertDialog.isAdded) {
                    alertDialog.dismiss()
                    if (!isContentLoaded) callback.invoke()
                }

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (!alertDialog.isAdded) {
                    alertDialog.isCancelable = false
                    alertDialog.show(fragmentTransaction, SplashNoInternetDialog::class.simpleName)
                }
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        networkCallback?.let {
            connectivityManager.registerNetworkCallback(networkRequest, it)
        }
    }

    fun unregisteredConnection() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }
}