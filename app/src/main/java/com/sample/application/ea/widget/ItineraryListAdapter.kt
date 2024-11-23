package com.sample.application.ea.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.R
import com.sample.application.ea.databinding.ViewItineraryItemBinding
import com.sample.application.ea.dataset.readonly.Itinerary
import com.sample.application.ea.dataset.readonly.ItineraryExtra
import java.util.regex.Pattern

abstract class ItineraryListAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ItineraryListAdapter.ViewHolder>(),
    Filterable {

    companion object {

        fun create(supplier: () -> Context) =
            object : ItineraryListAdapter(supplier()) {
                private var _originalList: List<ItineraryExtra>? = null
                override var originalList: List<ItineraryExtra>
                    get() = _originalList!!
                    set(value) {
                        when (_originalList) {
                            null -> {
                                _originalList = value
                                filterList = value
                            }

                            else -> throw RuntimeException("Only set once")
                        }
                    }
            }

    }

    protected var filterList: List<ItineraryExtra> = listOf()

    abstract var originalList: List<ItineraryExtra>

    var onItemClickListener: (Itinerary) -> Unit = {}

    var matchFilter: (Pattern, Itinerary) -> List<Boolean> =
        { _, _ -> listOf() }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ViewHolder(
        ViewItineraryItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
    )

    override fun getItemCount() = filterList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = filterList[position]

        holder.apply {
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            when {
                item.imageCacheUrl.isEmpty() -> {
                    imageView.setImageResource(R.drawable.no_image_yoko)
                }

                else -> {
                    imageView.setImageBitmap(
                        BitmapFactory.decodeFile(item.imageCacheUrl[0])!!
                    )
                }
            }

            labelView.text = item.itinerary
            button.setOnClickListener {
                onItemClickListener(item)
            }
        }


    }

    override fun getFilter() = ItemFilter() as Filter

    class ViewHolder(
        binding: ViewItineraryItemBinding,
        val imageView: ImageView = binding.itemImageView,
        val labelView: TextView = binding.itemTitleView,
        val button: View = binding.button,
    ) : RecyclerView.ViewHolder(binding.root)

    private inner class ItemFilter : Filter() {

        fun matches(
            pattern: String,
            set: ItineraryExtra,
        ): Boolean {
            matchFilter(
                Pattern.compile(pattern, Pattern.CASE_INSENSITIVE),
                set
            ).forEach { b ->
                when {
                    b -> return true
                }
            }
            return false
        }

        override fun performFiltering(
            constraint: CharSequence?,
        ) = FilterResults().apply {
            when {
                constraint.isNullOrEmpty() -> originalList
                else -> mutableListOf<ItineraryExtra>().apply {
                    originalList
                        .asSequence()
                        .filter {
                            matches(
                                constraint.toString(), it
                            )
                        }
                        .forEach { add(it) }
                }
            }.apply {
                values = this
                count = size
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(
            constraint: CharSequence?, results: FilterResults?,
        ) {
            filterList = results!!.values as List<ItineraryExtra>
            notifyDataSetChanged()
        }

    }
}