package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentWinBinding

class WinFragment:Fragment(R.layout.fragment_win) {

    private lateinit var binding: FragmentWinBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWinBinding.bind(view)
        binding.nextBtn.setOnClickListener { findNavController().navigate(WinFragmentDirections.actionWinFragmentToGameFragment()) }
        binding.exitBtn.setOnClickListener { requireActivity().finish() }
    }
}