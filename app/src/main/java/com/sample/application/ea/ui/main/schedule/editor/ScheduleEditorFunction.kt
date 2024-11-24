package com.sample.application.ea.ui.main.schedule.editor

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.databinding.FragmentScheduleEditorBinding
import com.sample.application.ea.dataset.ScheduleSet
import com.sample.application.ea.extension.to24HourTime
import com.sample.application.ea.extension.toSlashSeparatedDate
import java.util.Calendar
import kotlin.reflect.KProperty

@SuppressLint("ClickableViewAccessibility")
fun ScheduleEditorFragment.setupUI(view: View) {
    when (view) {
        !is SearchView -> {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }
    }

    when (view) {
        is ViewGroup -> {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }
}

private fun ScheduleEditorFragment.hideKeyboard() {
    container.mainActivity.apply {
        getSystemService(
            Context.INPUT_METHOD_SERVICE
        ).apply {
            (this as InputMethodManager).hideSoftInputFromWindow(
                requireView().windowToken, 0
            )
        }
    }
}

fun ScheduleEditorFragment.onDatePickerButtonClick() {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        container.mainActivity,
        { _, y, m, d ->
            val c = Calendar.getInstance().apply {
                this[Calendar.YEAR] = y
                this[Calendar.MONTH] = m
                this[Calendar.DATE] = d
            }
            ScheduleSet.fromCalendar(c).apply {
                container.year = year
                container.month = month
                container.day = day
            }
            container.binding.dateBox.setText(c.toSlashSeparatedDate())
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DATE],
    ).show()
}

fun ScheduleEditorFragment.onTimePickerButtonClick() {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        container.mainActivity,
        { _, h, m ->
            val c = Calendar.getInstance().apply {
                this[Calendar.HOUR_OF_DAY] = h
                this[Calendar.MINUTE] = m
            }
            ScheduleSet.fromCalendar(c).apply {
                container.hour = hour
                container.minute = minute
            }
            container.binding.timeBox.setText(c.to24HourTime())
        },
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE],
        true
    ).show()
}

fun ScheduleEditorFragment.onSaveButtonClick() {

    val set = ScheduleSet().apply {
        container.binding.also { binding ->
            title = binding.title.text.toString().trim().also { titleStr ->
                when {
                    titleStr.isEmpty() -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("Title cannot be empty.")
                            .setPositiveButton("OK", null)
                            .create().show()
                        return@onSaveButtonClick
                    }
                }
            }
            content = binding.content.text.toString().trim()
        }
        year = container.year
        month = container.month
        day = container.day
        hour = container.hour
        minute = container.minute
    }

    assert(set.title.isNotEmpty()) { "Title must not be empty." }

    val id = currentUser!!.uid
    val d = container.oldDate.split("/").joinToString("")
    val t = container.oldTime.split(":").joinToString("")

    val node = firebaseDatabase.getReference("user/${currentUser!!.uid}/$d$t")

    node.apply {
        addListenerForSingleValueEvent(object : ValueEventListener {

            private val userData: DatabaseReference
                get() = firebaseDatabase
                    .reference
                    .child("user")
                    .child(id)
                    .child(set.item())

            private fun navigateUp() =
                container.mainActivity.navigateUp()

            override fun onDataChange(snapshot: DataSnapshot) {
                when {
                    snapshot.exists() -> {
                        node.removeValue()
                            .addOnSuccessListener {
                                Log.d(
                                    "ScheduleEditorFragment",
                                    "Item removed successfully."
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.d(
                                    "ScheduleEditorFragment",
                                    "Error removing item: ${e.message}",
                                    e
                                )
                            }
                    }
                }

                userData.get()
                    .addOnSuccessListener {
                        userData.setValue(set).addOnSuccessListener {
                            navigateUp()
                        }
                    }
                    .addOnFailureListener {
                        userData.setValue(set).addOnSuccessListener {
                            navigateUp()
                        }
                    }
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}

class ScheduleEditorContainer(
    private val getMainActivity: () -> MainActivity,
    private val getBinding: () -> FragmentScheduleEditorBinding,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var oldDate: String = "",
    var oldTime: String = "",
) {
    val mainActivity get() = getMainActivity()
    val binding get() = getBinding()


    inner class Delegate<T>(
        private val container: ScheduleEditorContainer,
        private val get: ScheduleEditorContainer.() -> T,
        private val set: ScheduleEditorContainer.(T) -> Unit,
    ) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return container.get()
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            container.set(value)
        }
    }
}


