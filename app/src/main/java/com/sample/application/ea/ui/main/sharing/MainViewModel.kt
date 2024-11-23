package com.sample.application.ea.ui.main.sharing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class MainViewModel : ViewModel() {

//    var userIcon = MutableLiveData<Drawable>()
//        private set

    var currentCalendar = MutableLiveData<Calendar>()
        private set

//    fun updateUserIcon(drawable: Drawable) {
//        userIcon.value = drawable
//    }

    fun updateCalendar(calendar: Calendar) {
        currentCalendar.value = calendar
    }
}