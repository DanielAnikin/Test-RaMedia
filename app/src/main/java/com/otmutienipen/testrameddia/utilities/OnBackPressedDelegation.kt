package com.otmutienipen.testrameddia.utilities

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle


internal interface OnBackPressedDelegation {

    fun registerOnBackPressedDelegation(
        fragmentActivity: FragmentActivity?,
        lifecycle: Lifecycle,
        onBackPressed: () -> Unit
    )

}