package com.sample.application.ea.database

import android.util.Log
import androidx.core.util.Consumer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.application.ea.dataset.AttractionItem
import com.sample.application.ea.dataset.readonly.AttractionExtra
import java.util.Collections

class AttractionList private constructor() {

    companion object {

        private lateinit var core: AttractionList

        private const val INITIALIZATION_ERROR = "Already initialized"

        private var mHasInitialized = false

        private var mLoadCompleted = false

        private var mImageLoadCompleted = false

        @JvmStatic
        fun hasInitialized() = mHasInitialized

        @JvmStatic
        fun loadCompleted() = mLoadCompleted

        @JvmStatic
        fun imageLoadCompleted() = mImageLoadCompleted

        @JvmStatic
        fun setImageLoadCompleted() {
            mImageLoadCompleted = true
        }

        @JvmStatic
        fun initialize() {
            if (mHasInitialized) {
                Log.d(
                    AttractionList::class.java.name,
                    INITIALIZATION_ERROR,
                    IllegalStateException(INITIALIZATION_ERROR)
                )
            } else {
                core = AttractionList()
                mHasInitialized = true
            }

        }

        @JvmStatic
        fun loadData() {
            core.internalLoadData()
            mLoadCompleted = true
        }

        @JvmStatic
        fun loadData(callback: Consumer<List<AttractionExtra>>) {
            callback.accept(core.itemList)
        }

        @JvmStatic
        fun forEach(callback: Consumer<AttractionExtra>) =
            core.itemList.forEach {
                callback.accept(it)
            }

        @JvmStatic
        fun size() = core.itemList.size

        @JvmStatic
        fun get(
            index: Int,
        ) = core.itemList[index]

        @JvmStatic
        fun addOnDataChangeListener(
            listener: OnDataChangeListener,
        ) {
            core.mOnDataChangeEventListeners += listener
        }

        @JvmStatic
        fun removeOnDataChangeListener(
            listener: OnDataChangeListener,
        ) {
            core.mOnDataChangeEventListeners -= listener
        }

    }

    private lateinit var itemList: ArrayList<AttractionItem>

    private val readonlyList
        get() = Collections.unmodifiableList(itemList)

    private val mOnValueEventListener = OnValueEventListener()

    private val mOnDataChangeEventListeners = hashSetOf<OnDataChangeListener>()

    private fun internalLoadData() {
        if (mLoadCompleted) return
        FirebaseDatabase.getInstance().getReference()
            .child("app")
            .child("information")
            .addListenerForSingleValueEvent(mOnValueEventListener)
    }

    abstract class OnDataChangeListener(
        private val id: Int = System.identityHashCode(this),
    ) {

        abstract fun onDataChange(
            list: List<AttractionExtra>,
        )

        final override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null) throw IllegalArgumentException(
                "Argument 'other' cannot be null"
            )
            if (javaClass != other.javaClass)
                throw IllegalArgumentException(
                    "Argument 'other' must be of " +
                            "type ${this.javaClass.simpleName}"
                )
            other as OnDataChangeListener
            return id == other.id
        }

        final override fun hashCode() = id
    }

    private inner class OnValueEventListener : ValueEventListener {

        override fun onDataChange(
            snapshot: DataSnapshot,
        ) {
            val list = arrayListOf<AttractionItem>()
            for (item in snapshot.children) {
                with(list) {
                    add(item.getValue(AttractionItem::class.java)!!)
                }
            }
            itemList = list
            for (listener in mOnDataChangeEventListeners)
                listener.onDataChange(readonlyList)
        }

        override fun onCancelled(
            error: DatabaseError,
        ) {
            Log.e(
                "InformationValueEventListener",
                error.message,
                error.toException()
            )
        }
    }

}