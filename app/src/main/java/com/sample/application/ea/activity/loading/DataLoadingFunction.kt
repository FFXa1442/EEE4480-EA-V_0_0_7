package com.sample.application.ea.activity.loading

import android.content.Intent
import android.util.Log
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.database.AttractionList
import com.sample.application.ea.dataset.AttractionItem
import com.sample.application.ea.dataset.readonly.AttractionExtra
import com.sample.application.ea.tasks.AsyncProcessing

fun DataLoadingActivity.loadImage() {

    val processing = AsyncProcessing.newInstance()

    AttractionList.forEach { item ->
        processing.addStep { state ->
            assert(item is AttractionItem)

            when {
                item.imageCacheUrl.isEmpty() -> {
                    (item as AttractionItem).saveImageCache(
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

    AttractionList.initialize()
    AttractionList.addOnDataChangeListener(object :
        AttractionList.OnDataChangeListener() {

        override fun onDataChange(list: List<AttractionExtra>) {
            AttractionList.removeOnDataChangeListener(this)
            list.forEach { item ->
                Log.d("DataLoadingActivity", item.toString())
            }
            state.setFinish()
        }
    })
    AttractionList.loadData()
}