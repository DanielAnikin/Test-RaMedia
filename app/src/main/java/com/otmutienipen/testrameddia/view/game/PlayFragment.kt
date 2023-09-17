package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentPlayBinding

class PlayFragment:Fragment(R.layout.fragment_play) {

    private lateinit var binding: FragmentPlayBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayBinding.bind(view)

        binding.apply {
            startBtn.setOnClickListener { findNavController().navigate(PlayFragmentDirections.actionPlayFragmentToGameFragment()) }
        }
    }
}