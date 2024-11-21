package com.sample.application.ea.dataset.readonly

import android.os.Parcelable

interface Itinerary : Parcelable {

    val country: String

    val detail: String

    val itinerary: String

    val latitude: Double

    val location: String

    val longitude: Double

    val youtubeUrl: String

    val imageUrl: List<String>

    val popularity: Int

    val youtubeId: String

    val booking: String?
}