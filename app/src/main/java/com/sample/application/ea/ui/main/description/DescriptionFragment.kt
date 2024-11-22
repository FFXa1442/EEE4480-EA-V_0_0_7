package com.sample.application.ea.ui.main.description

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.os.BundleCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.sample.application.ea.R
import com.sample.application.ea.activity.youtube.web.YoutubeActivity
import com.sample.application.ea.databinding.FragmentDescriptionBinding
import com.sample.application.ea.dataset.readonly.ItineraryExtra
import com.sample.application.ea.ui.main.generic.MainBindingSheetDialogFragment
import kotlin.math.min

class DescriptionFragment :
    MainBindingSheetDialogFragment<FragmentDescriptionBinding>() {

    companion object {
        const val ARG_TAG = "DESCRIPTION"
    }

    private lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat

    private var onDismissAction: () -> Unit = {}

    override val enableFullScreen = true

    private var hasWindowInsetsApplied = false

    override val layoutId: Int =
        R.layout.fragment_description

    fun setOnDismissAction(
        action: () -> Unit,
    ) {
        onDismissAction = action
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissAction()
        super.onDismiss(dialog)
    }


    override fun onViewCreated(
        binding: FragmentDescriptionBinding,
        savedInstanceState: Bundle?,
    ) {
        when {
            enableFullScreen -> {
                dialog!!.window!!.also {
                    windowInsetsControllerCompat = WindowInsetsControllerCompat(
                        it,
                        binding.root
                    ).apply {
                        isAppearanceLightNavigationBars = true
                    }
                }
            }
        }
        val item = BundleCompat.getParcelable(
            requireArguments(),
            ARG_TAG,
            ItineraryExtra::class.java
        )!!.apply {
            itinerary.let { s ->
                binding.title.text = s
                binding.itinerary.text = s
            }
            binding.location.text = location
            binding.description.text = detail
        }

        binding.imageSlider.setImageList(mutableListOf<SlideModel>().apply {
            item.imageUrl.forEach { url ->
                this += SlideModel(url, ScaleTypes.CENTER_CROP)
            }
        })

        binding.location.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                setData(
                    Uri.parse(
                        "geo:0,0?" +
                                "q=${item.latitude},${item.longitude}?" +
                                "z=12 (${item.itinerary})"
                    )
                )
            })
        }

        binding.backButton.setOnClickListener { dismiss() }

        binding.video.setOnClickListener {
            startActivity(Intent(parentActivity, YoutubeActivity::class.java).apply {
                putExtra(YoutubeActivity.TAG, item.youtubeId)
            })
        }

        item.booking.also { s ->
            binding.booking.apply {
                visibility = when (s) {
                    null -> View.GONE
                    else -> View.VISIBLE
                }
                setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
                }
            }
        }

        binding.title.apply {
            requestFocus()
            alpha = 0f
        }

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val alpha = min(
                a = map(
                    x = scrollY,
                    inMin = 0,
                    inMax = dpToPx(400) - binding.appbarLayout.height,
                    outMin = 0,
                    outMax = 255
                ),
                b = 255
            )
            binding.appbarLayout.apply {
                setBackgroundColor(Color.argb(alpha, 255, 255, 255))
            }
            binding.title.alpha = alpha.toFloat() / 255f
        }

    }

    override fun onDestroyView() {
        windowInsetsControllerCompat.apply {
            isAppearanceLightNavigationBars = false
        }
        super.onDestroyView()
    }

    override fun onApplyWindowInsets(
        view: View,
        binding: FragmentDescriptionBinding,
        systemBars: Insets,
    ) {
        when {
            hasWindowInsetsApplied -> return
            else -> {
                hasWindowInsetsApplied = true

                binding.offsetView.apply {
                    layoutParams = layoutParams.apply {
                        height = systemBars.top
                    }
                }

                binding.descriptionView.apply {
                    layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                        bottomMargin = systemBars.bottom
                    }
                }
            }
        }
    }

}