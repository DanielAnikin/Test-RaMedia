package com.otmutienipen.testrameddia.view.screens

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.otmutienipen.testrameddia.R


internal class SplashNoInternetDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.no_internet)
            .setCancelable(false)
            .create()
    }
}