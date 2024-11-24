package com.sample.application.ea.dataset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sample.application.ea.dataset.readonly.AttractionExtra
import com.sample.application.ea.tasks.AsyncProcessing
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

data class AttractionItem(
    override var country: String,
    override var detail: String,
    override var attraction: String,
    override var youtubeUrl: String,
    override var imageUrl: List<String>,
    override var latitude: Double,
    override var longitude: Double,
    override var location: String,
    override var popularity: Int,
    override var youtubeId: String,
    override var booking: String?

) : AttractionExtra {

    private fun internalSaveImageCache(
        context: Context,
        state: AsyncProcessing.State,
        url: String,
        assignItem: (String) -> Unit
    ) {
        val token = url.split("token=")[1]
        val file = File(context.cacheDir, "$token.png")

        if (file.exists()) {
            assignItem(file.absolutePath)
            state.setFinish()
        } else {
            val count = AtomicInteger(0)
            val glide = Glide.with(context).asBitmap().load(url)
            glide.into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    try {
                        FileOutputStream(file).use {
                            resource.compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                it
                            )
                        }
                        Log.d("ItineraryItem", url)
                        assignItem(file.absolutePath)
                    } catch (e: IOException) {
                        Log.d("ItineraryItem", e.message, e)
                    }
                    state.setFinish()

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    val err = RuntimeException("Request failed")
                    if (count.get() > 5) {
                        throw err
                    }
                    Log.d("ItineraryItem", "Request failed: ${count.get() + 1}", err)
                    count.incrementAndGet()
                    glide.into(this)
                }

            })

        }

    }

    fun saveImageCache(
        context: Context,
        state: AsyncProcessing.State
    ) {

//        val list = arrayOfNulls<String>(imageUrl.size)
        val list = arrayOfNulls<String>(1)
        val processing = AsyncProcessing.newInstance()

        processing.addStep { subState ->
            internalSaveImageCache(
                context,
                subState,
                imageUrl[0]
            ) { list[0] = it }
        }

//        imageUrl.forEachIndexed { index, url ->
//            Log.d("Testing", "Prepare: $index, $url")
////            if (index > 0) return
//            processing.addStep { subState ->
//                internalSaveImageCache(
//                    context,
//                    subState,
//                    url.split("token=")[1]
//                ) { list[index] = it }
//            }
//        }

        processing.run {
            imageCacheUrl = list.map { it as String }.toList()
            state.setFinish()
        }
    }


    override var imageCacheUrl: List<String> = listOf()
        private set

    private constructor(parcel: Parcel) : this(
        country = parcel.readString()!!,
        detail = parcel.readString()!!,
        attraction = parcel.readString()!!,
        youtubeUrl = parcel.readString()!!,
        imageUrl = parcel.let { p ->
            val list = listOf<String>()
            p.readStringList(list)
            list
        },
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble(),
        location = parcel.readString()!!,
        popularity = parcel.readInt(),
        youtubeId = parcel.readString()!!,
        booking = parcel.readString()
    ) {
        parcel.readStringList(imageCacheUrl)
    }

    constructor() : this(
        country = "",
        detail = "",
        attraction = "",
        youtubeUrl = "",
        imageUrl = listOf(),
        latitude = 0.0,
        longitude = 0.0,
        location = "",
        popularity = 0,
        youtubeId = "",
        booking = null,
    )

    override fun writeToParcel(
        parcel: Parcel,
        flags: Int
    ) {
        parcel.writeString(country)
        parcel.writeString(detail)
        parcel.writeString(attraction)
        parcel.writeString(youtubeUrl)
        parcel.writeStringList(imageUrl)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(location)
        parcel.writeInt(popularity)
        parcel.writeString(youtubeId)
        parcel.writeString(booking)
        parcel.writeStringList(imageCacheUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AttractionExtra> {
        override fun createFromParcel(
            parcel: Parcel
        ): AttractionExtra {
            return AttractionItem(parcel)
        }

        override fun newArray(
            size: Int
        ): Array<AttractionExtra?> {
            return arrayOfNulls(size)
        }
    }
}