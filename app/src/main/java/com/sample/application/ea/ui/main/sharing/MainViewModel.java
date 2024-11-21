package com.sample.application.ea.ui.main.sharing;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

/**
 * MainViewModel class extending ViewModel.
 * Manages user icon data.
 */
public final class MainViewModel extends ViewModel {

    // MutableLiveData for user icon
    @NonNull
    private final MutableLiveData<Drawable> userIcon = new MutableLiveData<>();


    @NonNull
    private final MutableLiveData<Calendar> currentCalendar = new MutableLiveData<>();

    /**
     * Returns the user icon LiveData.
     *
     * @return MutableLiveData of user icon
     */
    @NonNull
    public MutableLiveData<Drawable> getUserIcon() {
        return userIcon;
    }

    /**
     * Updates the user icon with a new drawable.
     *
     * @param drawable the new drawable for the user icon
     */
    public void updateUserIcon(@NonNull Drawable drawable) {
        userIcon.setValue(drawable);
    }



    @NonNull
    public MutableLiveData<Calendar> getCurrentCalendar() {
        return currentCalendar;
    }

    public void updateCalendar(@NonNull Calendar calendar) {
        currentCalendar.setValue(calendar);
    }
}
