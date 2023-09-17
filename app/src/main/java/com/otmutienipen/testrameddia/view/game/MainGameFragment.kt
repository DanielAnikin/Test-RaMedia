package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentMainGameBinding

class MainGameFragment : Fragment(R.layout.fragment_main_game) {


    private lateinit var binding: FragmentMainGameBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainGameBinding.bind(view)


    }
}