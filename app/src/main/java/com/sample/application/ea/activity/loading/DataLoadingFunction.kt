package com.sample.application.ea.activity.loading

import android.content.Intent
import android.util.Log
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.database.ItineraryList
import com.sample.application.ea.dataset.ItineraryItem
import com.sample.application.ea.dataset.readonly.ItineraryExtra
import com.sample.application.ea.tasks.AsyncProcessing

fun DataLoadingActivity.loadImage() {

    val processing = AsyncProcessing.newInstance()

    ItineraryList.forEach { item ->
        processing.addStep { state ->
            assert(item is ItineraryItem)

            when {
                item.imageCacheUrl.isEmpty() -> {
                    (item as ItineraryItem).saveImageCache(
                        context = this,
                        state = state,
                    )
                }

                else -> {
                    state.setFinish()
                }
            }
        }
    }

    processing.run {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }

}

fun DataLoadingActivity.loadItineraryData(state: AsyncProcessing.State) {

    ItineraryList.initialize()
    ItineraryList.addOnDataChangeListener(object :
        ItineraryList.OnDataChangeListener() {

        override fun onDataChange(list: List<ItineraryExtra>) {
            ItineraryList.removeOnDataChangeListener(this)
            list.forEach { item ->
                Log.d("DataLoadingActivity", item.toString())
            }
            state.setFinish()
        }
    })
    ItineraryList.loadData()
}