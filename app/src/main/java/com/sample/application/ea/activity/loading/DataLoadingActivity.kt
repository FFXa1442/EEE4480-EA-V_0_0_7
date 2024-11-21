package com.sample.application.ea.activity.loading

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.ViewDataBindingActivity
import com.sample.application.ea.databinding.ActivityDataLoadingBinding
import com.sample.application.ea.tasks.AsyncProcessing

class DataLoadingActivity :
    ViewDataBindingActivity<ActivityDataLoadingBinding>() {

    override val layoutId: Int =
        R.layout.activity_data_loading

    override fun onViewCreated(
        binding: ActivityDataLoadingBinding,
        savedInstanceState: Bundle?,
    ) {
        AsyncProcessing.newInstance().apply {
            addStep(::loadItineraryData)
            run(::loadImage)
        }
    }

}