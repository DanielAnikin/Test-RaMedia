package com.otmutienipen.testrameddia.view.screens

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentLoadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class LoadFragment : Fragment(R.layout.fragment_load) {

    private lateinit var binding: FragmentLoadBinding

    private var isAnimatedLoading = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoadBinding.bind(view)
        start()
    }

    private fun start() {
        updateStatus(MODE.LOADING)
    }

    private fun animateLoadingText() = with(binding){
        CoroutineScope(Dispatchers.Main).launch {
            while (isAnimatedLoading) {
                delay(200)
                tvLoadIndicator.text = ""
                for (i in 0..3) {
                    delay(200)
                    if (tvLoadIndicator.text.toString().length > 3) tvLoadIndicator.text = ""
                    tvLoadIndicator.text = tvLoadIndicator.text.toString().plus(".")
                }
                if(!isAnimatedLoading) tvLoadIndicator.text = ""
            }
        }

    }

    private fun updateStatus(status: MODE) {
        when (status) {
            MODE.LOADING ->
                setNewText(R.string.load)
        }
    }

    private fun setNewText(
        @StringRes newText: Int,
        isAnimatedLoading: Boolean? = true
    ) = with(binding) {
        lifecycle.coroutineScope.launch {
            delay(1000)
            tvLoad.setText(newText)
            this@LoadFragment.isAnimatedLoading = isAnimatedLoading ?: true
        }
    }

    override fun onResume() {
        super.onResume()
        isAnimatedLoading = true
        animateLoadingText()
    }

    override fun onStop() {
        super.onStop()
        isAnimatedLoading = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isAnimatedLoading = false
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoadFragment()
    }
}

enum class MODE {
    LOADING
}