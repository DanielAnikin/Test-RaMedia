package com.otmutienipen.testrameddia.controller

import android.content.SharedPreferences


internal class PlayerController(private val preferences: SharedPreferences) {

    fun saveLink(link: String) {
        preferences.edit().apply {
            putString(LINK, link)
        }.apply()
    }

    fun getLink(): String? {
        return preferences.getString(LINK, "")
    }

    fun savePlayer(reject: Boolean) {
        preferences.edit().apply {
            putBoolean(RESULT, reject)
        }.apply()
    }

    companion object {
        private const val RESULT = "result"
        private const val LINK = "link"
    }
}