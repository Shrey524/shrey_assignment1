package com.example.myapplication

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.MavericksViewModelConfigFactory
import com.airbnb.mvrx.withState
import com.example.myapplication.ViewModel.DragHelper
import com.example.myapplication.ViewModel.MainActivityViewModel
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MavericksView {
    private lateinit var binding: ActivityMainBinding
    private var flag: Boolean? =
        null                                                               // Flag for API to check if the response

    // viewModel
    val viewModel = MainActivityViewModel()

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dragSuccessful: Boolean? =
            null                                                         // Flag to check if the view has been successfully dragged to the target

        // hide App bar
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)

        //adding animation to the CRED logo
        animateLogo()

        // arrow image color
        binding.arrow.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.teal_200))

        // custom helper for the drag
        val helper = DragHelper(binding.logo, binding.lottieAnim)
        helper.setVerticalDrag()

        // Called when view reached the target
        helper.onViewReachedTargetListener = {

            // making the logo invisible
            binding.logo.isVisible = false

            // play animation
            binding.lottieAnim.playAnimation()
            binding.lottieAnim.repeatCount = 5

            // make arrow invisible
            binding.arrow.isVisible = false

            //Api Call
            val text = viewModel.makeApiCall(true)

            // animating the bottom circle outside
            binding.response.text = text
            binding.response.isVisible = true
            val animation = AnimationUtils.loadAnimation(this, R.anim.down)
            binding.dropTarget.startAnimation(animation)
            binding.dropTarget.isVisible = false


            // setting dragSuccessful flag to true when it reaches the target
            dragSuccessful = true
        }
        // Called when user releases the view
        helper.onViewReleasedListener = {
            //making logo invisible is drag is successful
            if (dragSuccessful == true) {
                binding.logo.isVisible = false
            }
            //continuing the animation if it is not
            else {
                animateLogo()
            }
        }
        //clearing animation on Touch listener to get the original position of the view for the helper
        helper.onViewTouchedListener = {
            binding.logo.clearAnimation()
        }

        val view = binding.root
        setContentView(view)
    }

    //function for logo animation
    private fun animateLogo() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.logo.startAnimation(animation)
    }

    override fun invalidate() {
        withState(viewModel) {
            binding.response.text = it.text
        }
    }

}