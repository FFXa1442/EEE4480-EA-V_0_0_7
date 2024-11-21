package com.sample.application.ea.ui.main.schedule.view;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sample.application.ea.extension.DateTimeFormatterKt.*;
import static com.sample.application.ea.ui.main.schedule.constant.Constant.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sample.application.ea.R;
import com.sample.application.ea.activity.guest.GuestActivity;
import com.sample.application.ea.databinding.FragmentScheduleViewBinding;
import com.sample.application.ea.dataset.ScheduleSet;
import com.sample.application.ea.ui.main.generic.MainBindingFragment;
import com.sample.application.ea.ui.main.sharing.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ScheduleViewFragment extends ScheduleViewFragmentRecycleView {

}

abstract class ScheduleViewFragmentRecycleView extends ScheduleViewFragmentBase {

    private MainViewModel viewModel;

    public ScheduleViewFragmentRecycleView() {
        super();
    }

    public ScheduleViewFragmentRecycleView(int contentLayoutId) {
        super(contentLayoutId);
    }

    private boolean login() {
        if (currentUser() == null) {
            launcher.launch(new Intent(requireParentActivity(), GuestActivity.class));
            return false;
        }
        return true;
    }

    private final FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final int flag1 = user == null ? INVISIBLE : VISIBLE;
        requireBinding().signOutButton.setVisibility(flag1);
        setHint(user);
    };

    private void setHint() {
        setHint(currentUser());
    }

    private void setHint(@Nullable FirebaseUser user) {
        final int flag2 = user == null ? GONE : VISIBLE;
        final int flag3 = user == null ? VISIBLE : GONE;
        requireBinding().nothing.setVisibility(flag2);
        requireBinding().buttonLayout.setVisibility(flag3);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth().addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth().removeAuthStateListener(authStateListener);
    }

    @CallSuper
    @Override
    protected void onCreateView(@NonNull FragmentScheduleViewBinding binding, @Nullable Bundle savedInstanceState) {
//        viewModel.getUserIcon().observe(this, binding.profileView::setUserIconDrawable);

        new Handler().post(this::login);

        setHint();

        viewModel = new ViewModelProvider(requireParentActivity()).get(MainViewModel.class);
        viewModel.getCurrentCalendar().observe(getViewLifecycleOwner(),
                calendar -> {
                    binding.dateText.setText(toReadableDate(calendar));
                    setupRecyclerView(binding, toCompactDate(calendar));
                });

        binding.signInButton.setOnClickListener(v -> login());

//        binding.addButton.setOnClickListener(view -> navigate(R.id.action_nav_schedule_view_to_nav_schedule_editor));
        binding.signOutButton.setOnClickListener(v -> {
            firebaseAuth().signOut();
            navigateUp();
        });
        binding.fab.setOnClickListener(view -> {
            if (login()) navigate(R.id.action_nav_schedule_view_to_nav_schedule_editor);
        });
        binding.datePickerButton.setOnClickListener(v -> {
            if (login()) onDatePickerButtonClick(binding);
        });
        binding.scheduleListView.setLayoutManager(new LinearLayoutManager(requireParentActivity()));


        Calendar currentCalendar = viewModel.getCurrentCalendar().getValue();
        if (currentCalendar == null) currentCalendar = Calendar.getInstance();
        viewModel.updateCalendar(currentCalendar);
//        binding.dateText.setText(toReadableDate(currentCalendar));
//        setupRecyclerView(binding, toCompactDate(currentCalendar));

    }

    private void onDatePickerButtonClick(@NonNull FragmentScheduleViewBinding binding) {
        @SuppressLint("SimpleDateFormat") DatePickerDialog.OnDateSetListener listener = (view1, y, m, d) -> {
            final Calendar selectedDate = Calendar.getInstance();
            selectedDate.clear();
            selectedDate.set(y, m, d);
            viewModel.updateCalendar(selectedDate);
//            binding.dateText.setText(toReadableDate(selectedDate));
//            setupRecyclerView(binding, toCompactDate(selectedDate));
        };

//        final Calendar currentCalendar = Calendar.getInstance();

        final Calendar currentCalendar = Objects.requireNonNull(viewModel.getCurrentCalendar().getValue());
        final int year = currentCalendar.get(Calendar.YEAR);
        final int month = currentCalendar.get(Calendar.MONTH);
        final int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(requireParentActivity(), listener, year, month, day).show();
    }

    private ValueEventListener valueEventListener = null;

    private void setupRecyclerView(@NonNull FragmentScheduleViewBinding binding, @Nullable String pattern) {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ScheduleSet> list = new ArrayList<>();
                for (DataSnapshot layer1 : snapshot.getChildren()) {
                    ScheduleSet set = layer1.getValue(ScheduleSet.class);
                    if (set == null) throw new NullPointerException();
                    if (pattern != null && set.key().toLowerCase().matches(pattern.toLowerCase())) {
                        list.add(set);
                    }
                }
                if (list.isEmpty()) {
                    binding.nothing.setVisibility(VISIBLE);
                    binding.listLayout.setVisibility(GONE);
                    binding.scheduleListView.setAdapter(null);
                } else {
                    binding.nothing.setVisibility(GONE);
                    binding.listLayout.setVisibility(VISIBLE);
                    ScheduleListAdapter adapter = ScheduleListAdapter.create(requireParentActivity());
                    adapter.setOriginalList(list);
                    adapter.setOnRemoveButtonClick(onRemoveButtonClick);
                    adapter.setOnItemClick(onItemClick);
                    binding.scheduleListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        setupList();
    }

    private void setupList() {
        final FirebaseUser user = firebaseAuth().getCurrentUser();
        if (user == null) return;
        firebaseDatabase().getReference()
                .child("user")
                .child(user.getUid())
                .addValueEventListener(valueEventListener);
    }

    private final ScheduleListAdapter.OnItemClick onItemClick = item -> {
        Bundle args = new Bundle();
        Calendar c = item.calender();
        args.putString(DATE, toSlashSeparatedDate(c));
        args.putString(TIME, to24HourTime(c));
        args.putString(TITLE, item.getTitle());
        args.putString(CONTENT, item.getContent());
        navigate(R.id.action_nav_schedule_view_to_nav_schedule_editor, args);
    };

    private final ScheduleListAdapter.OnRemoveButtonClick onRemoveButtonClick =
            item -> firebaseDatabase()
                    .getReference(String.format("user/%s/%s", Objects.requireNonNull(firebaseAuth().getCurrentUser()).getUid(), item.item()))
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Item removed successfully
//                    Log.d("Testing - onRemoveButtonClick", "Item removed successfully.");
                    }).addOnFailureListener(e -> {
                        // Error occurred
//                    Log.d("Testing - onRemoveButtonClick", "Error removing item: " + e.getMessage());
                    });


    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(startActivityForResult(), o -> {
                if (o.getResultCode() == Activity.RESULT_OK)
                    setupList();
            });

    @NonNull
    final protected ActivityResultContracts.StartActivityForResult startActivityForResult() {
        return new ActivityResultContracts.StartActivityForResult();
    }
}

abstract class ScheduleViewFragmentBase extends MainBindingFragment<FragmentScheduleViewBinding> {


    public ScheduleViewFragmentBase() {
        super();
    }

    public ScheduleViewFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    final protected int getLayoutId() {
        return R.layout.fragment_schedule_view;
    }
}