package com.otmutienipen.testrameddia.utilities

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.otmutienipen.testrameddia.utilities.OnBackPressedDelegation


internal class OnBackPressedDelegationImpl: OnBackPressedDelegation, DefaultLifecycleObserver {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed.invoke()
        }
    }

    private var fragmentActivity: FragmentActivity? = null

    private lateinit var onBackPressed: () -> Unit

    override fun registerOnBackPressedDelegation(
        fragmentActivity: FragmentActivity?,
        lifecycle: Lifecycle,
        onBackPressed: () -> Unit
    ) {
        this.fragmentActivity = fragmentActivity
        this.onBackPressed = onBackPressed
        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fragmentActivity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onBackPressedCallback.remove()
    }
}