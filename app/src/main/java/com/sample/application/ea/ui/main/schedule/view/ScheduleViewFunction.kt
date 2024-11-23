package com.sample.application.ea.ui.main.schedule.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sample.application.ea.activity.guest.GuestActivity
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.databinding.FragmentScheduleViewBinding
import com.sample.application.ea.ui.main.sharing.MainViewModel
import java.util.Calendar


fun ScheduleViewFragment.login() = when (currentUser) {
    null -> {
        launcher.launch(
            Intent(
                container.mainActivity,
                GuestActivity::class.java
            )
        )
        false
    }

    else -> true
}

fun ScheduleViewFragment.setupRecyclerView() {
    setupList()
}

fun ScheduleViewFragment.onDatePickerButtonClick() {
    container.viewModel.also { viewModel ->
        viewModel.currentCalendar.value!!.also { currentCalendar ->
            DatePickerDialog(
                container.mainActivity,
                { _, y, m, d ->
                    Calendar.getInstance().apply {
                        clear()
                        set(y, m, d)
                        viewModel.updateCalendar(this)
                    }
                },
                currentCalendar[Calendar.YEAR],
                currentCalendar[Calendar.MONTH],
                currentCalendar[Calendar.DAY_OF_MONTH],
            ).show()
        }
    }
}

fun ScheduleViewFragment.setHint() = setHint(currentUser)

fun ScheduleViewFragment.setHint(user: FirebaseUser?) {
    container.binding.nothing.visibility = when (user) {
        null -> View.GONE
        else -> View.VISIBLE
    }
    container.binding.buttonLayout.visibility = when (user) {
        null -> View.VISIBLE
        else -> View.GONE
    }
}

val ScheduleViewFragment.authStateListener: FirebaseAuth.AuthStateListener
    get() = (container as ScheduleViewContainerInternal).authStateListener

private val ScheduleViewFragment.launcher: ActivityResultLauncher<Intent>
    get() = (container as ScheduleViewContainerInternal).launcher

private fun ScheduleViewFragment.setupList() {
    val user = currentUser ?: return
    firebaseDatabase.reference
        .child("user")
        .child(user.uid)
        .addValueEventListener(container.valueEventListener)
}

fun ScheduleViewFragment.ScheduleViewContainer(
    getMainActivity: () -> MainActivity,
    getBinding: () -> FragmentScheduleViewBinding,
    getValueEventAction: (DataSnapshot) -> Unit,
    getViewModel: () -> MainViewModel,
) = ScheduleViewContainerInternal(
    getMainActivity = getMainActivity,
    getBinding = getBinding,
    getValueEventAction = getValueEventAction,
    getViewModel = getViewModel,
    setupList = ::setupList,
    registerForActivityResult = ::registerForActivityResult,
    setHint = ::setHint,
) as ScheduleViewContainer

private typealias RegisterForActivityResult<I, O> = (
    ActivityResultContract<I, O>,
    ActivityResultCallback<O>,
) -> ActivityResultLauncher<I>

private class ScheduleViewContainerInternal(
    val getMainActivity: () -> MainActivity,
    val getBinding: () -> FragmentScheduleViewBinding,
    val getValueEventAction: (DataSnapshot) -> Unit,
    val getViewModel: () -> MainViewModel,
    val setHint: (FirebaseUser?) -> Unit,
    val setupList: () -> Unit,
    val registerForActivityResult: RegisterForActivityResult<Intent, ActivityResult>,
) : ScheduleViewContainer(
    getMainActivity = getMainActivity,
    getBinding = getBinding,
    getValueEventAction = getValueEventAction,
    getViewModel = getViewModel,
) {
    val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { o ->
        if (o.resultCode == Activity.RESULT_OK) {
            setupList()
        }
    }

    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        binding.signOutButton.visibility = when (user) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        setHint(user)
    }
}

abstract class ScheduleViewContainer(
    private val getMainActivity: () -> MainActivity,
    private val getBinding: () -> FragmentScheduleViewBinding,
    private val getValueEventAction: (DataSnapshot) -> Unit,
    private val getViewModel: () -> MainViewModel,
) {
    val mainActivity get() = getMainActivity()
    val viewModel get() = getViewModel()
    val binding get() = getBinding()
    val valueEventListener
        get() = object : ValueEventListener {
            override fun onDataChange(
                snapshot: DataSnapshot,
            ) = getValueEventAction(snapshot)

            override fun onCancelled(
                error: DatabaseError,
            ) = Unit
        }

}

