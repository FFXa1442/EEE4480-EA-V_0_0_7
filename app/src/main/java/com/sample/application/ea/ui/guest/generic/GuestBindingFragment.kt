package com.sample.application.ea.ui.guest.generic

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sample.application.ea.activity.guest.GuestActivity
import com.sample.application.ea.database.firebaseAuth
import com.sample.application.ea.database.firebaseDatabase
import com.sample.application.ea.ui.generic.ViewDataBindingFragment

abstract class GuestBindingFragment<TBinding> :
    ViewDataBindingFragment<TBinding, GuestActivity>
        where TBinding : ViewDataBinding {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    protected val firebaseDatabase: FirebaseDatabase
        get() = parentActivity.firebaseDatabase

    protected val firebaseAuth: FirebaseAuth
        get() = parentActivity.firebaseAuth

    protected fun navigateUp() =
        parentActivity.navigateUp()

    protected fun navigate(
        @IdRes id: Int,
        args: Bundle? = null,
    ) = parentActivity.navigate(
        id,
        args
    )
}