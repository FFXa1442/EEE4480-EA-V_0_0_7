package com.sample.application.ea.ui.main.schedule.editor

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentScheduleEditorBinding
import com.sample.application.ea.dataset.ScheduleSet
import com.sample.application.ea.extension.to24HourTime
import com.sample.application.ea.extension.toSlashSeparatedDate
import com.sample.application.ea.ui.main.generic.MainBindingFragment
import com.sample.application.ea.ui.main.schedule.constant.Constant.CONTENT
import com.sample.application.ea.ui.main.schedule.constant.Constant.DATE
import com.sample.application.ea.ui.main.schedule.constant.Constant.TIME
import com.sample.application.ea.ui.main.schedule.constant.Constant.TITLE
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleEditorFragment :
    MainBindingFragment<FragmentScheduleEditorBinding>() {
    override val layoutId: Int = R.layout.fragment_schedule_editor

    internal val container = ScheduleEditorContainer(
        getMainActivity = { parentActivity },
        getBinding = { binding },
    )

    private var year by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::year::get,
        set = ScheduleEditorContainer::year::set
    )

    private var month by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::month::get,
        set = ScheduleEditorContainer::month::set
    )

    private var day by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::day::get,
        set = ScheduleEditorContainer::day::set
    )

    private var hour by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::hour::get,
        set = ScheduleEditorContainer::hour::set
    )

    private var minute by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::minute::get,
        set = ScheduleEditorContainer::minute::set
    )

    private var oldDate by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::oldDate::get,
        set = ScheduleEditorContainer::oldDate::set
    )

    private var oldTime by container.Delegate(
        container = container,
        get = ScheduleEditorContainer::oldTime::get,
        set = ScheduleEditorContainer::oldTime::set
    )

    override fun onCreateView(
        binding: FragmentScheduleEditorBinding,
        savedInstanceState: Bundle?,
    ) {
        setupUI(binding.root)
        binding.apply {
            cancelButton.setOnClickListener { navigateUp() }
            saveButton.setOnClickListener { onSaveButtonClick() }
            timePickerButton.setOnClickListener { onTimePickerButtonClick() }
            datePickerButton.setOnClickListener { onDatePickerButtonClick() }
        }

        val args = arguments
        val currentCalendar = Calendar.getInstance()

        args?.apply {
            val dateStr = getString(DATE)
            val timeStr = getString(TIME)

            try {
                currentCalendar.time = SimpleDateFormat(
                    "yyyy/MM/dd HH:mm",
                    Locale.US
                ).parse("$dateStr $timeStr")!!
            } catch (e: ParseException) {
                throw RuntimeException(e)
            }

            binding.apply {
                title.setText(getString(TITLE))
                content.setText(getString(CONTENT))
            }
        }

        ScheduleSet.fromCalendar(currentCalendar).apply {
            this@ScheduleEditorFragment.year = year
            this@ScheduleEditorFragment.month = month
            this@ScheduleEditorFragment.day = day
            this@ScheduleEditorFragment.hour = hour
            this@ScheduleEditorFragment.minute = minute
        }

        binding.apply {
            dateBox.setText(currentCalendar.toSlashSeparatedDate())
            timeBox.setText(currentCalendar.to24HourTime())
        }

        oldDate = binding.dateBox.text.toString().trim()
        oldTime = binding.timeBox.text.toString().trim()

    }

}
