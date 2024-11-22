package com.sample.application.ea.ui.main.schedule.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentScheduleViewBinding
import com.sample.application.ea.dataset.ScheduleSet
import com.sample.application.ea.extension.to24HourTime
import com.sample.application.ea.extension.toCompactDate
import com.sample.application.ea.extension.toReadableDate
import com.sample.application.ea.extension.toSlashSeparatedDate
import com.sample.application.ea.ui.main.generic.MainBindingFragment
import com.sample.application.ea.ui.main.schedule.constant.Constant.CONTENT
import com.sample.application.ea.ui.main.schedule.constant.Constant.DATE
import com.sample.application.ea.ui.main.schedule.constant.Constant.TIME
import com.sample.application.ea.ui.main.schedule.constant.Constant.TITLE
import com.sample.application.ea.ui.main.schedule.view.ScheduleListAdapter.Companion.create
import com.sample.application.ea.ui.main.sharing.MainViewModel
import java.util.Calendar
import java.util.Locale

class ScheduleViewFragment : MainBindingFragment<FragmentScheduleViewBinding>() {

    override val layoutId: Int = R.layout.fragment_schedule_view

    private var pattern: String? = null

    private lateinit var viewModel: MainViewModel

    internal val container = ScheduleViewContainer(
        getMainActivity = { parentActivity },
        getBinding = { binding },
        getViewModel = { viewModel },
        getValueEventAction = { snapshot ->
            arrayListOf<ScheduleSet>().also { list ->
                snapshot.children.forEach { item ->
                    item.getValue(ScheduleSet::class.java)!!.apply {
                        when {
                            pattern != null && key().lowercase(Locale.getDefault()).matches(
                                pattern!!.lowercase(Locale.getDefault()).toRegex()
                            ) -> list.add(this)
                        }
                    }
                }
                if (list.isEmpty()) {
                    binding.nothing.visibility = View.VISIBLE
                    binding.listLayout.visibility = View.GONE
                    binding.scheduleListView.adapter = null
                } else {
                    binding.nothing.visibility = View.GONE
                    binding.listLayout.visibility = View.VISIBLE
                    val adapter = create(parentActivity).apply {
                        originalList = list
                        onRemoveButtonClick = { item ->
                            firebaseDatabase
                                .getReference(
                                    java.lang.String.format(
                                        "user/%s/%s",
                                        currentUser!!.uid,
                                        item.item()
                                    )
                                )
                                .removeValue()
                                .addOnSuccessListener {
//                                    Log.d("Testing - onRemoveButtonClick", "Item removed successfully.")
                                }
                                .addOnFailureListener {
//                                    Log.d(
//                                        "Testing - onRemoveButtonClick",
//                                        "Error removing item: " + it.message,
//                                        it
//                                    )
                                }
                        }
                        onItemClick = { item ->
                            val args = Bundle()
                            val c: Calendar = item.calender()
                            args.putString(DATE, c.toSlashSeparatedDate())
                            args.putString(TIME, c.to24HourTime())
                            args.putString(TITLE, item.title)
                            args.putString(CONTENT, item.content)
                            navigate(R.id.action_nav_schedule_view_to_nav_schedule_editor, args)
                        }
                    }

                    binding.scheduleListView.adapter = adapter
                }
            }
        }
    )


    override fun onCreateView(
        binding: FragmentScheduleViewBinding,
        savedInstanceState: Bundle?,
    ) {
        Handler(Looper.getMainLooper()).post(::login)

        setHint()

        viewModel = ViewModelProvider(parentActivity)[MainViewModel::class.java].apply {
            currentCalendar.observe(viewLifecycleOwner) { calendar ->
                binding.dateText.text = calendar.toReadableDate()
                pattern = calendar.toCompactDate()
                setupRecyclerView()
            }
        }

        binding.signInButton.setOnClickListener { login() }

        binding.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            navigateUp()
        }

        binding.fab.setOnClickListener {
            when {
                login() -> navigate(R.id.action_nav_schedule_view_to_nav_schedule_editor)
            }
        }

        binding.datePickerButton.setOnClickListener { v ->
            when {
                login() -> onDatePickerButtonClick()
            }
        }

        binding.scheduleListView.layoutManager = LinearLayoutManager(parentActivity)

        var currentCalendar = viewModel.currentCalendar.value
        if (currentCalendar == null) currentCalendar = Calendar.getInstance()
        viewModel.updateCalendar(currentCalendar!!)

    }

}