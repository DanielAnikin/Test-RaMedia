package com.otmutienipen.testrameddia.utilities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.otmutienipen.testrameddia.controller.PlayerController
import okhttp3.OkHttpClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal class RequestManager(context: AppCompatActivity) {

    private val prefs = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
    private val controller = PlayerController(prefs)

    fun reject() {
        controller.savePlayer(true)
    }

    fun saveLink(link: String) {
        controller.saveLink(link)
    }

    fun getLink(): String? {
        return controller.getLink()
    }
}