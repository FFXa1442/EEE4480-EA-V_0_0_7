package com.sample.application.ea.database

import android.util.Log
import androidx.core.util.Consumer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.application.ea.dataset.ItineraryItem
import com.sample.application.ea.dataset.readonly.ItineraryExtra
import java.util.Collections

/**
 * ItineraryList is a singleton class that manages a list of itinerary items.
 * It provides methods for initializing the list, loading data, and adding/removing data change listeners.
 */
class ItineraryList private constructor() {

    companion object {

        private lateinit var core: ItineraryList

        private const val INITIALIZATION_ERROR = "Already initialized"

        private var mHasInitialized = false

        private var mLoadCompleted = false

        private var mImageLoadCompleted = false

        /**
         * Checks if the ItineraryList has been initialized.
         *
         * @return True if initialized, false otherwise.
         */
        @JvmStatic
        fun hasInitialized() = mHasInitialized

        /**
         * Checks if the data load has been completed.
         *
         * @return True if data load completed, false otherwise.
         */
        @JvmStatic
        fun loadCompleted() = mLoadCompleted

        /**
         * Checks if the image load has been completed.
         *
         * @return True if image load completed, false otherwise.
         */
        @JvmStatic
        fun imageLoadCompleted() = mImageLoadCompleted

        /**
         * Sets the image load as completed.
         */
        @JvmStatic
        fun setImageLoadCompleted() {
            mImageLoadCompleted = true
        }

        /**
         * Initializes the ItineraryList singleton.
         */
        @JvmStatic
        fun initialize() {
            if (mHasInitialized) {
                Log.d(
                    ItineraryList::class.java.name,
                    INITIALIZATION_ERROR,
                    IllegalStateException(INITIALIZATION_ERROR)
                )
            } else {
                core = ItineraryList()
                mHasInitialized = true
            }

        }

        /**
         * Loads data into the ItineraryList.
         */
        @JvmStatic
        fun loadData() {
            core.internalLoadData()
            mLoadCompleted = true
        }

        /**
         * Loads data and invokes the provided callback with the loaded data.
         *
         * @param callback The callback to be invoked with the loaded data.
         */
        @JvmStatic
        fun loadData(callback: Consumer<List<ItineraryExtra>>) {
            callback.accept(core.itemList)
        }

        /**
         * Iterates over each item in the ItineraryList and invokes the provided callback.
         *
         * @param callback The callback to be invoked for each item.
         */
        @JvmStatic
        fun forEach(callback: Consumer<ItineraryExtra>) =
            core.itemList.forEach {
                callback.accept(it)
            }

        /**
         * Returns the size of the ItineraryList.
         *
         * @return The size of the ItineraryList.
         */
        @JvmStatic
        fun size() = core.itemList.size

        /**
         * Returns the item at the specified index in the ItineraryList.
         *
         * @param index The index of the item to be returned.
         * @return The item at the specified index.
         */
        @JvmStatic
        fun get(
            index: Int
        ) = core.itemList[index]

        /**
         * Adds a data change listener to the ItineraryList.
         *
         * @param listener The listener to be added.
         */
        @JvmStatic
        fun addOnDataChangeListener(
            listener: OnDataChangeListener
        ) {
            core.mOnDataChangeEventListeners += listener
        }

        /**
         * Removes a data change listener from the ItineraryList.
         *
         * @param listener The listener to be removed.
         */
        @JvmStatic
        fun removeOnDataChangeListener(
            listener: OnDataChangeListener
        ) {
            core.mOnDataChangeEventListeners -= listener
        }

    }

    private lateinit var itemList: ArrayList<ItineraryItem>

    private val readonlyList
        get() = Collections.unmodifiableList(itemList)

    private val mOnValueEventListener = OnValueEventListener()

    private val mOnDataChangeEventListeners = hashSetOf<OnDataChangeListener>()

    /**
     * Loads data internally from the Firebase Database.
     */
    private fun internalLoadData() {
        if (mLoadCompleted) return
        FirebaseDatabase.getInstance().getReference()
            .child("app")
            .child("information")
            .addListenerForSingleValueEvent(mOnValueEventListener)
    }

    /**
     * Abstract class for listening to data changes in the ItineraryList.
     */
    abstract class OnDataChangeListener(
        private val id: Int = System.identityHashCode(this)
    ) {

        /**
         * Called when the data changes.
         *
         * @param list The list of itinerary extras.
         */
        abstract fun onDataChange(
            list: List<ItineraryExtra>
        )

        final override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null) throw IllegalArgumentException(
                "Argument 'other' cannot be null"
            )
            if (javaClass != other.javaClass)
                throw IllegalArgumentException(
                    "Argument 'other' must be of" +
                            "type ${this.javaClass.simpleName}"
                )
            other as OnDataChangeListener
            return id == other.id
        }

        final override fun hashCode(): Int {
            return id
        }
    }

    /**
     * Inner class for handling value events from the Firebase Database.
     */
    private inner class OnValueEventListener : ValueEventListener {

        /**
         * Called when the data is successfully read from the database.
         *
         * @param snapshot The data snapshot.
         */
        override fun onDataChange(
            snapshot: DataSnapshot
        ) {
            val list = arrayListOf<ItineraryItem>()
            for (item in snapshot.children) {
                with(list) {
                    add(item.getValue(ItineraryItem::class.java)!!)
                }
            }
            itemList = list
            for (listener in mOnDataChangeEventListeners)
                listener.onDataChange(readonlyList)
        }

        /**
         * Called when the database read is cancelled.
         *
         * @param error The database error.
         */
        override fun onCancelled(
            error: DatabaseError
        ) {
            Log.e(
                "InformationValueEventListener",
                error.message,
                error.toException()
            )
        }


    }

}