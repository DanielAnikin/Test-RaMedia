package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.DialogExitBinding

class ExitDialog : DialogFragment(R.layout.dialog_exit) {
    private lateinit var binding: DialogExitBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogExitBinding.bind(view)
        isCancelable = false
        binding.apply {
            btnQuit.setOnClickListener { requireActivity().finish() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
}