package com.sample.application.ea.ui.main.schedule.editor;

import static com.sample.application.ea.extension.DateTimeFormatterKt.*;
import static com.sample.application.ea.ui.main.schedule.constant.Constant.*;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.sample.application.ea.R;
import com.sample.application.ea.databinding.FragmentScheduleEditorBinding;
import com.sample.application.ea.dataset.ScheduleSet;
import com.sample.application.ea.dataset.readonly.DateTimeSet;
import com.sample.application.ea.ui.main.generic.MainBindingFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public final class ScheduleEditorFragment extends ScheduleEditorFragmentSetup {

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreateView(@NonNull FragmentScheduleEditorBinding binding,
                                @Nullable Bundle savedInstanceState) {
        super.onCreateView(binding, savedInstanceState);

        Bundle args = getArguments();
        final Calendar currentCalendar = Calendar.getInstance();

        if (args != null) {
            final String dateStr = args.getString(DATE);
            final String timeStr = args.getString(TIME);

            try {
                final Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                        .parse(dateStr + " " + timeStr);
                currentCalendar.setTime(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            binding.title.setText(args.getString(TITLE));
            binding.content.setText(args.getString(CONTENT));
        }

        final DateTimeSet dts = ScheduleSet.fromCalendar(currentCalendar);

        year = dts.getYear();
        month = dts.getMonth();
        day = dts.getDay();
        hour = dts.getHour();
        minute = dts.getMinute();

        binding.dateBox.setText(toSlashSeparatedDate(currentCalendar));
        binding.timeBox.setText(to24HourTime(currentCalendar));

        oldDate = binding.dateBox.getText().toString().trim();
        oldTime = binding.timeBox.getText().toString().trim();

    }
}

abstract class ScheduleEditorFragmentSetup extends ScheduleEditorFragmentBase {

    protected int year, month, day, hour, minute;

    protected String oldDate, oldTime;

    public ScheduleEditorFragmentSetup() {
        super();
    }

    public ScheduleEditorFragmentSetup(int contentLayoutId) {
        super(contentLayoutId);
    }

    @CallSuper
    @Override
    protected void onCreateView(@NonNull FragmentScheduleEditorBinding binding,
                                @Nullable Bundle savedInstanceState) {
        super.onCreateView(binding, savedInstanceState);

        binding.cancelButton.setOnClickListener(v -> navigateUp());
        binding.saveButton.setOnClickListener(this::onSaveButtonClick);
        binding.timePickerButton.setOnClickListener(this::onTimePickerButtonClick);
        binding.datePickerButton.setOnClickListener(this::onDatePickerButtonClick);

    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void onDatePickerButtonClick(View view) {

        final DatePickerDialog.OnDateSetListener listener = (view1, y, m, d) -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(y, m, d);
            DateTimeSet dts = ScheduleSet.fromCalendar(calendar);
            year = dts.getYear();
            month = dts.getMonth();
            day = dts.getDay();
            requireBinding().dateBox.setText(toSlashSeparatedDate(calendar));
        };

        final Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(requireParentActivity(), listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void onTimePickerButtonClick(View view) {

        final TimePickerDialog.OnTimeSetListener listener =
                (timePicker, h, m) -> {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    DateTimeSet dts = ScheduleSet.fromCalendar(calendar);
                    this.hour = dts.getHour();
                    this.minute = dts.getMinute();
                    requireBinding().timeBox.setText(to24HourTime(calendar));
                };

        final Calendar calendar = Calendar.getInstance();

        new TimePickerDialog(requireParentActivity(), listener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    @SuppressLint("SimpleDateFormat")
    private void onSaveButtonClick(View view) {
        final String id = Objects.requireNonNull(firebaseAuth().getCurrentUser()).getUid();

        final ScheduleSet set = new ScheduleSet();
        set.setContent(requireBinding().content.getText().toString().trim());
        set.setTitle(requireBinding().title.getText().toString().trim());
        set.setYear(year);
        set.setMonth(month);
        set.setDay(day);
        set.setHour(hour);
        set.setMinute(minute);

        if (set.getContent().isEmpty()) {

            new AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Title cannot be empty.")
                    .setPositiveButton("OK", null)
                    .create().show();

            return;
        }

        final String d = String.join("", oldDate.split("/"));
        final String t = String.join("", oldTime.split(":"));

        final DatabaseReference node = firebaseDatabase()
                .getReference(String.format("user/%s/%s", id, d + t));

        node.addListenerForSingleValueEvent(new ValueEventListener() {

            @NonNull
            @Override
            protected DatabaseReference node() {
                return node;
            }

            @NonNull
            @Override
            protected DatabaseReference userData() {
                return firebaseDatabase().getReference()
                        .child("user").child(id).child(set.item());
            }

            @NonNull
            @Override
            protected ScheduleSet set() {
                return set;
            }

            @Override
            protected void navigateUp() {
                ScheduleEditorFragmentSetup.this.navigateUp();
            }
        });
    }

}

abstract class ScheduleEditorFragmentBase
        extends MainBindingFragment<FragmentScheduleEditorBinding> {

    public ScheduleEditorFragmentBase() {
        super();
    }

    public ScheduleEditorFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    final protected int getLayoutId() {
        return R.layout.fragment_schedule_editor;
    }

    @CallSuper
    @Override
    protected void onCreateView(@NonNull FragmentScheduleEditorBinding binding,
                                @Nullable Bundle savedInstanceState) {
        setupUI(binding.getRoot());
    }

    @SuppressLint("ClickableViewAccessibility")
    final protected void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof SearchView)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    final protected void hideKeyboard() {
        // Clear the focus from the search view
//        binding().searchView.clearFocus();
        // Hide keyboard by user to touch screen
        ((InputMethodManager) requireParentActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}

abstract class ValueEventListener implements com.google.firebase.database.ValueEventListener {

    private final DatabaseReference userData, node;

    private final ScheduleSet set;

    public ValueEventListener() {
        this.userData = userData();
        this.set = set();
        this.node = node();
    }

    @NonNull
    protected abstract DatabaseReference node();

    @NonNull
    protected abstract DatabaseReference userData();

    @NonNull
    protected abstract ScheduleSet set();

    protected abstract void navigateUp();

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if (snapshot.exists()) {
            node.removeValue()
                    .addOnSuccessListener(this::onSuccess)
                    .addOnFailureListener(this::onFailure);
        }

        userData.get()
                .addOnSuccessListener(ds -> onDataExist())
                .addOnFailureListener(e -> onDataNotExist());

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    private void onDataNotExist() {
        userData.setValue(set).addOnSuccessListener(unused -> navigateUp());
    }

    private void onDataExist() {
        userData.setValue(set).addOnSuccessListener(unused -> navigateUp());
    }

    private void onSuccess(Void unused) {
        Log.d("Testing - onSaveButtonClick", "Item removed successfully.");
    }

    private void onFailure(@NonNull Exception e) {
        Log.d("Testing - onSaveButtonClick", "Error removing item: " + e.getMessage());
    }

}