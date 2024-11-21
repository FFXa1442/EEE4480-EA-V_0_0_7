package com.sample.application.ea.functions.membersystem.ui

import androidx.databinding.ViewDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sample.application.ea.database.firebaseAuth
import com.sample.application.ea.database.firebaseDatabase
import com.sample.application.ea.functions.membersystem.activity.MemberActivity
import com.sample.application.ea.ui.generic.ViewDataBindingFragment

abstract class MemberSystemBindingFragment<TBinding> :
    ViewDataBindingFragment<TBinding, MemberActivity>
        where TBinding : ViewDataBinding {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    protected val firebaseDatabase: FirebaseDatabase
        get() = parentActivity.firebaseDatabase

    protected val firebaseAuth: FirebaseAuth
        get() = parentActivity.firebaseAuth

    protected val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
}