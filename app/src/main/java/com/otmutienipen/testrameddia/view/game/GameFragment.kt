package com.otmutienipen.testrameddia.view.game

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.otmutienipen.testrameddia.R
import com.otmutienipen.testrameddia.databinding.FragmentGameBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class GameFragment:Fragment(R.layout.fragment_game) {
    private var score = "0"
    private var degree = 0
    private var isSpinning = false
    private var isBotSpin = false
    private var wheel: ImageView? = null


    private lateinit var binding: FragmentGameBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        wheel = binding.roulette
        degreeForSectors
        binding.spinBtn.setOnClickListener {
            if (isBotSpin)
                return@setOnClickListener
            if (!isSpinning) {
                spin()
                isSpinning = true
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            ExitDialog().show(parentFragmentManager, "ExitDialog")
        }

    }
    private fun spin() {
        degree = random.nextInt(sectors.size - 1)
        val rotateAnimation =
            RotateAnimation(0F, (360 * sectors.size + sectorDegrees[degree]).toFloat(),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                score = sectors[sectors.size - (degree + 1)]
                if (isBotSpin) binding.botScore.text = score
                else binding.youScore.text = score

                if (binding.youScore.text == "21"){
                    lifecycleScope.launch{
                        binding.textSpin.text = getString(R.string.msg_win)
                        delay(2000)
                        findNavController().navigate(GameFragmentDirections.actionGameFragmentToWinFragment())
                    }
                }
                else if (binding.botScore.text == "21"){
                    lifecycleScope.launch{
                        binding.textSpin.text = getString(R.string.msg_lose)
                        delay(1000)
                        findNavController().navigate(GameFragmentDirections.actionGameFragmentToLoseFragment())
                    }
                }

                isSpinning = false
                isBotSpin = !isBotSpin
                if (isBotSpin){
                    lifecycleScope.launch{
                        binding.textSpin.text = getString(R.string.spin_bot)
                        delay(1000)
                        spin()
                    }
                } else {
                    binding.textSpin.text = getString(R.string.spin_user)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        wheel!!.startAnimation(rotateAnimation)
    }

    private val degreeForSectors: Unit
        get() {
            val sectorDegree = 360 / sectors.size
            for (i in sectors.indices) {
                sectorDegrees[i] = (i + 1) * sectorDegree
            }
        }

    companion object {
        private val sectors = arrayOf("1","3","5","7","9","11","13","15","17","19","21","0")
        private val sectorDegrees = IntArray(sectors.size)
        private val random = Random()
    }
}