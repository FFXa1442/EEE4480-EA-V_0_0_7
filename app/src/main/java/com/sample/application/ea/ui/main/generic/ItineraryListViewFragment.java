package com.sample.application.ea.ui.main.generic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.sample.application.ea.database.ItineraryList;
import com.sample.application.ea.dataset.readonly.Itinerary;
import com.sample.application.ea.dataset.readonly.ItineraryExtra;
import com.sample.application.ea.ui.main.description.DescriptionFragment;
import com.sample.application.ea.utilities.TryUtil;
import com.sample.application.ea.widget.ItineraryListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Abstract class for fragments displaying a list of itineraries.
 */
public abstract class ItineraryListViewFragment<TBinding extends ViewDataBinding>
        extends ItineraryListViewFragmentBase<TBinding> {

    private DescriptionFragment sheet = null;

    // Adapter for the itinerary list
    private ItineraryListAdapter adapter = null;

    /**
     * Returns the RecyclerView for the itinerary list.
     */
    @NonNull
    protected abstract RecyclerView requireRecyclerView();

    /**
     * Handles search text change events.
     */
    protected abstract void onSearchTextChanged(CharSequence s);

    /**
     * Matches itinerary items against a filter pattern.
     */
    protected abstract List<Boolean> matchFilter(@NonNull Pattern pattern, @NonNull Itinerary set);

    public ItineraryListViewFragment() {
        super();
    }

    public ItineraryListViewFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Filters the itinerary list based on a constraint.
     */
    protected final void filter(@NonNull CharSequence constraint) {
        adapter.getFilter().filter(constraint);
    }

    /**
     * Sets up the RecyclerView.
     */
    @Override
    protected void setupRecyclerView() {
        if (adapter == null) {
            final List<ItineraryExtra> newList = new ArrayList<>();
            ItineraryList.forEach(item -> {
                if (flag(item)) newList.add(item);
            });
            adapter = ItineraryListAdapter.create(this::getParentActivity);
            adapter.setOriginalList(newList);
        }
        adapter.setMatchFilter(this::matchFilter);
        adapter.setOnItemClick(item -> {
            if (sheet == null) {
                final Bundle bundle = new Bundle();
                bundle.putParcelable(DescriptionFragment.ARG_TAG, item);
                sheet = new DescriptionFragment();
                sheet.setOnDismissListener(() -> sheet = null);
                sheet.setArguments(bundle);
                sheet.show(getChildFragmentManager(), "DescriptionFragment");
            }

        });

        requireRecyclerView().setAdapter(adapter);
        requireRecyclerView().setLayoutManager(new RecyclerViewLinearLayoutManager());

        requireSearchView().addTextChangedListener(searchViewTextChanged);
    }


    /**
     * Filter item.
     */
    protected boolean flag(@NonNull Itinerary item) {
        return true;
    }

    // TextWatcher for search view changes
    private final TextWatcher searchViewTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ItineraryListViewFragment.this.onSearchTextChanged(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * Custom LinearLayoutManager for the RecyclerView.
     */
    private final class RecyclerViewLinearLayoutManager
            extends LinearLayoutManager {

        // Constructor initializing the layout manager
        public RecyclerViewLinearLayoutManager() {
            super(ItineraryListViewFragment.this.getParentActivity(),
                    RecyclerView.VERTICAL, false);
        }
    }

}

/**
 * Abstract base class for ItineraryListViewFragment.
 * Handles common setup tasks.
 */
abstract class ItineraryListViewFragmentBase<TBinding extends ViewDataBinding>
        extends MainBindingFragment<TBinding> {

    /**
     * Returns the EditText for search input.
     */
    @NonNull
    protected abstract EditText requireSearchView();

    public ItineraryListViewFragmentBase() {
        super();
    }

    public ItineraryListViewFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Called when the view is created.
     */
    @CallSuper
    @Override
    protected void onCreateView(@NonNull TBinding binding, @Nullable Bundle container) {
        super.onCreateView(binding, container);
        setupUI(binding.getRoot());
//        TryUtil.tryNonNull(currentUser(), user -> setupUI(binding, user));

        setupSideViewButton(binding);
        setupRecyclerView();
    }

    /**
     * Sets up the user interface with user information.
     */
    @Deprecated
    private void setupUI(@NonNull TBinding binding, @NonNull FirebaseUser user) {
        setupUserDisplay(binding, user);
        setupSideViewButton(binding);
        setupRecyclerView();
    }

    /**
     * Sets up user display elements in the UI.
     */
    protected void setupUserDisplay(@NonNull TBinding binding, @NonNull FirebaseUser user) {
    }

    /**
     * Sets up side view buttons in the UI.
     */
    protected void setupSideViewButton(@NonNull TBinding binding) {
    }

    /**
     * Sets up the RecyclerView in the UI.
     */
    protected void setupRecyclerView() {
    }

    /**
     * Sets up the UI for hiding the keyboard.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupUI(View view) {
        if (!(view instanceof SearchView)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    /**
     * Hides the soft keyboard.
     */
    final protected void hideKeyboard() {
        requireSearchView().clearFocus();
        ((InputMethodManager) requireParentActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

}