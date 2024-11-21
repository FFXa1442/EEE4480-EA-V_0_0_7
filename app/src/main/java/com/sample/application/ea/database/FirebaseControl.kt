package com.sample.application.ea.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

interface FirebaseControl

val FirebaseControl.firebaseDatabase: FirebaseDatabase
    get() = FirebaseDatabase.getInstance()

val FirebaseControl.firebaseAuth: FirebaseAuth
    get() = FirebaseAuth.getInstance()

val FirebaseControl.firebaseStorage: FirebaseStorage
    get() = FirebaseStorage.getInstance()

fun FirebaseControl.userIconStorage(
    name: String,
): StorageReference {
    return firebaseStorage
        .getReference()
        .child("user/icons/${name}.jpg")
}