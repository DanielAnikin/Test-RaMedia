package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentLoseBinding

class LoseFragment: Fragment(R.layout.fragment_lose) {

    private lateinit var binding: FragmentLoseBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoseBinding.bind(view)
        binding.retryBtn.setOnClickListener { findNavController()
            .navigate(LoseFragmentDirections.actionLoseFragmentToGameFragment()) }
        binding.exitBtn.setOnClickListener { requireActivity().finish() }
    }

}