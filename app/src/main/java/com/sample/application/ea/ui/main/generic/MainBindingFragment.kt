package com.sample.application.ea.ui.main.generic

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.database.firebaseAuth
import com.sample.application.ea.database.firebaseDatabase
import com.sample.application.ea.ui.generic.NavigationFragment

abstract class MainBindingFragment<TBinding> :
    NavigationFragment<TBinding, MainActivity> where TBinding : ViewDataBinding {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    val firebaseDatabase: FirebaseDatabase
        get() = parentActivity.firebaseDatabase

    val firebaseAuth: FirebaseAuth
        get() = parentActivity.firebaseAuth

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    protected fun navigateUp() = parentActivity.navigateUp()

    protected fun navigate(
        @IdRes id: Int,
        args: Bundle? = null,
    ) = parentActivity.navigate(id, args)

}