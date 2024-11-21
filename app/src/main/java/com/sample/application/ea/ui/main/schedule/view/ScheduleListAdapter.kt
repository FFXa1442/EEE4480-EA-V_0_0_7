package com.sample.application.ea.ui.main.schedule.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.databinding.WidgetScheduleListItemBinding
import com.sample.application.ea.dataset.ScheduleSet
import com.sample.application.ea.extension.to24HourTime

internal abstract class ScheduleListAdapter(private val context: Context) :
    RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {

    companion object {
        @JvmStatic
        fun create(context: Context) = object : ScheduleListAdapter(context) {
            private var hasSet = false
            override var originalList: ArrayList<ScheduleSet> = arrayListOf()
                set(value) {
                    if (hasSet) throw RuntimeException("Only set once")
                    hasSet = true
                    field = value
                    filterList = value
                }
        }
    }

    var onRemoveButtonClick = object : OnRemoveButtonClick {
        override fun onClick(item: ScheduleSet) {
        }
    }

    var onItemClick = object : OnItemClick {
        override fun onClick(item: ScheduleSet) {
        }
    }

    protected lateinit var filterList: List<ScheduleSet>

    abstract var originalList: ArrayList<ScheduleSet>

    abstract class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        abstract val timeTextView: TextView
        abstract val titleTextView: TextView
        abstract val removeButton: ImageButton
        abstract val item: RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ScheduleListItemHolder(
            WidgetScheduleListItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun getItemCount() = originalList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val set = filterList[position]

        holder.removeButton.setOnClickListener {
            onRemoveButtonClick.onClick(set)
        }

        holder.item.setOnClickListener {
            onItemClick.onClick(set)
        }

        holder.timeTextView.text = String.format("Time: %s", set.calender().to24HourTime())
        holder.titleTextView.text = set.title
    }

    interface OnRemoveButtonClick {
        fun onClick(item: ScheduleSet)
    }

    interface OnItemClick {
        fun onClick(item: ScheduleSet)
    }

}

private class ScheduleListItemHolder(
    private val binding: WidgetScheduleListItemBinding,
) : ScheduleListAdapter.ViewHolder(binding.root) {
    override val timeTextView: TextView
        get() = binding.timeView
    override val titleTextView: TextView
        get() = binding.titleView
    override val removeButton: ImageButton
        get() = binding.removeButton
    override val item: RelativeLayout
        get() = binding.root
}