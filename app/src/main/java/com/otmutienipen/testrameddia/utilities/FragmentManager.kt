package com.otmutienipen.testrameddia.utilities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.otmutienipen.testrameddia.R

internal object FragmentManager {

    fun launchFragment(activity: AppCompatActivity, fragment: Fragment, isAddedHistory: Boolean = false) {
        activity.supportFragmentManager.beginTransaction().apply {
            activity.supportFragmentManager.popBackStack()
            if (isAddedHistory) {
                add(R.id.main_container, fragment, fragment::class.simpleName)
                addToBackStack(fragment::class.simpleName)
            } else {
                replace(R.id.main_container, fragment, fragment::class.simpleName)
            }
            commit()
        }
    }
}