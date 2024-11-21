package com.sample.application.ea.ui.main.category;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.application.ea.R;
import com.sample.application.ea.databinding.FragmentCategoryBinding;
import com.sample.application.ea.dataset.readonly.Itinerary;
import com.sample.application.ea.ui.main.generic.ItineraryListViewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CategoryFragment class extending CategoryFragmentBase.
 * Represents a fragment that displays a list of categories.
 */
public class CategoryFragment extends CategoryFragmentBase {


}

/**
 * Abstract base class for CategoryFragment.
 * Handles category selection and filtering logic.
 */
abstract class CategoryFragmentBase extends ItineraryListViewFragment<FragmentCategoryBinding> {

    // Holds the current category for filtering
    private String currentCategory = "";

    public CategoryFragmentBase() {
        super();
    }

    public CategoryFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Returns the RecyclerView for the category list.
     */
    @NonNull
    @Override
    protected RecyclerView requireRecyclerView() {
        return requireBinding().cateListView;
    }

    /**
     * Filters the list based on the search text and current category.
     */
    @Override
    protected void onSearchTextChanged(CharSequence s) {
        filter("^(?=.*" + currentCategory + ")(?=.*" + s + ").*");
    }

    /**
     * Sets up the category menu buttons with click listeners.
     */
    @Override
    protected void setupSideViewButton(@NonNull FragmentCategoryBinding binding) {
        binding.cateMenu.setOnKRButtonClickListener(new CateMenuButtonOnClickListener("korea"));
        binding.cateMenu.setOnJPButtonClickListener(new CateMenuButtonOnClickListener("japan"));
        binding.cateMenu.setOnTWButtonClickListener(new CateMenuButtonOnClickListener("taiwan"));
        binding.cateMenu.setOnUKButtonClickListener(new CateMenuButtonOnClickListener("united kingdom"));
        binding.cateMenu.setOnUSButtonClickListener(new CateMenuButtonOnClickListener("united states"));
        binding.cateMenu.setOnCNButtonClickListener(new CateMenuButtonOnClickListener("china"));
        binding.cateMenu.setOnMYButtonClickListener(new CateMenuButtonOnClickListener("malaysia"));
        binding.cateMenu.setOnSGButtonClickListener(new CateMenuButtonOnClickListener("singapore"));
        binding.cateMenu.setOnAllButtonClickListener(new CateMenuButtonOnClickListener(""));
    }

    /**
     * Matches the filter pattern against itinerary items.
     */
    @Override
    protected List<Boolean> matchFilter(@NonNull Pattern pattern, @NonNull Itinerary item) {
        return new ArrayList<Boolean>() {{
            add(pattern.matcher(item.getCountry() + "|" + item.getItinerary()).find());
        }};
    }

    /**
     * Returns the EditText for search input.
     */
    @NonNull
    @Override
    protected EditText requireSearchView() {
        return requireBinding().searchView;
    }

    /**
     * Returns the layout ID for the fragment.
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    /**
     * Listener class for category menu button clicks.
     */
    private final class CateMenuButtonOnClickListener implements View.OnClickListener {

        // Target category for filtering
        @NonNull
        private final String target;

        // Constructor initializing the target category
        public CateMenuButtonOnClickListener(@NonNull String target) {
            this.target = target;
        }

        /**
         * Handles button click events to filter categories.
         */
        @Override
        public void onClick(View v) {
            requireBinding().searchView.setText("");
            currentCategory = target;
            filter(currentCategory);
        }
    }
}