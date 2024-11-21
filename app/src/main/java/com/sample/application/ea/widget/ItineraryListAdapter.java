package com.sample.application.ea.widget;

import static com.sample.application.ea.utilities.TryUtil.tryNonNull;
import static com.sample.application.ea.utilities.TryUtil.tryNonNullElse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Supplier;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.application.ea.R;
import com.sample.application.ea.databinding.ViewItineraryItemBinding;
import com.sample.application.ea.dataset.readonly.Itinerary;
import com.sample.application.ea.dataset.readonly.ItineraryExtra;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Abstract adapter for displaying a list of itineraries with filtering capabilities.
 */
public abstract class ItineraryListAdapter extends ItineraryListAdapterFilter {

    /**
     * Constructor that initializes the adapter with a context.
     *
     * @param context The context in which the adapter is operating.
     */
    protected ItineraryListAdapter(@NonNull Context context) {
        super(context);
    }

    /**
     * Factory method to create an instance of ItineraryListAdapter.
     *
     * @param supplier Supplies the context required for adapter creation.
     * @return A new instance of ItineraryListAdapter.
     */
    public static ItineraryListAdapter create(@NonNull Supplier<Context> supplier) {
        return new ItineraryListAdapter(supplier.get()) {

            // List to store the original itineraries
            private List<ItineraryExtra> originalList = null;

            @Override
            public List<ItineraryExtra> getOriginalList() {
                return Objects.requireNonNull(originalList); // Ensure the list is not null
            }

            @Override
            public void setOriginalList(@NonNull List<ItineraryExtra> list) {
                if (originalList == null) {
                    originalList = list;
                    filterList = list;
                } else throw new RuntimeException("Only set once");
            }
        };
    }

}

/**
 * Abstract base class for filtering itinerary lists.
 */
abstract class ItineraryListAdapterFilter extends ItineraryListAdapterBase implements Filterable {

    // Filter used for matching items
    private MatchFilter matchFilter;

    /**
     * Constructor that initializes the adapter with a context.
     *
     * @param context The context in which the adapter is operating.
     */
    protected ItineraryListAdapterFilter(@NonNull Context context) {
        super(context);
        this.matchFilter = null;
    }

    /**
     * Provides a filter for filtering the itinerary list.
     *
     * @return An instance of ItemFilter.
     */
    @Override
    public Filter getFilter() {
        return new ItemFilter();
    }

    /**
     * Inner class implementing the filter logic for itineraries.
     */
    private class ItemFilter extends Filter {

        /**
         * Checks if the itinerary matches the given pattern.
         *
         * @param pattern The regex pattern to match.
         * @param set     The itinerary to check.
         * @return True if a match is found, false otherwise.
         */
        private boolean matches(@NonNull String pattern, @NonNull ItineraryExtra set) {
            final Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (Boolean true_ : getMatchFilter().collections(regex, set))
                if (true_) return true;
            return false;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItineraryExtra> newList;
            if (constraint.length() > 0) {
                newList = new ArrayList<>();
                for (ItineraryExtra item : getOriginalList()) {
                    if (matches(constraint.toString(), item))
                        newList.add(item);
                }
            } else {
                newList = getOriginalList();
            }

            return new FilterResults() {{
                values = newList;
                count = newList.size();
            }};
        }

        /**
         * @noinspection unchecked
         */
        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<ItineraryExtra>) results.values;
            notifyDataSetChanged();
        }
    }

    /**
     * Sets the match filter used for filtering the itinerary list.
     *
     * @param matchFilter The match filter to set.
     */
    final public void setMatchFilter(@NonNull MatchFilter matchFilter) {
        this.matchFilter = matchFilter;
    }

    /**
     * Retrieves the match filter used for filtering.
     *
     * @return The match filter.
     */
    final public MatchFilter getMatchFilter() {
        return Objects.requireNonNull(matchFilter);
    }

    /**
     * Interface for defining match filter logic.
     */
    public interface MatchFilter {
        List<Boolean> collections(@NonNull Pattern pattern, @NonNull Itinerary set);
    }
}

/**
 * Base adapter class for displaying itineraries in a RecyclerView.
 */
abstract class ItineraryListAdapterBase extends RecyclerView.Adapter<ItineraryListAdapter.ViewHolder> {

    // Context for the adapter
    private final Context context;

    // List of filtered itineraries
    protected List<ItineraryExtra> filterList;

    // Click listener for items
    private OnItemClick listener;

    /**
     * Constructor that initializes the adapter with a context.
     *
     * @param context The context in which the adapter is operating.
     */
    protected ItineraryListAdapterBase(@NonNull Context context) {
        this.context = context;
        this.filterList = null;
        this.listener = null;
    }

    // Abstract methods for managing the original list
    public abstract List<ItineraryExtra> getOriginalList();

    public abstract void setOriginalList(@NonNull List<ItineraryExtra> originalList);

    /**
     * Sets the click listener for items.
     *
     * @param l The click listener to set.
     */
    final public void setOnItemClick(@NonNull OnItemClick l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewItineraryItemBinding.inflate(
                LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.progressBar.setVisibility(View.VISIBLE);
//        holder.labelView.setVisibility(View.GONE);

        final ItineraryExtra item = filterList.get(position);

        // Load image and set properties

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (item.getImageCacheUrl().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.no_image_yoko);
        } else {
            final String imageFile = item.getImageCacheUrl().get(0);
            final Bitmap bitmap = BitmapFactory.decodeFile(imageFile);
            holder.imageView.setImageBitmap(Objects.requireNonNull(bitmap));
        }

        holder.labelView.setText(item.getItinerary());

        // Set click listener for the button
        holder.button.setOnClickListener(v ->
                tryNonNull(listener, onItemClick ->
                        onItemClick.onClick(item)));
    }

    @Override
    public int getItemCount() {
        return tryNonNullElse(filterList, List::size, () -> 0);
    }

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClick {
        void onClick(Itinerary item);
    }

    /**
     * ViewHolder class for itinerary items.
     */
    public static final class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        private final TextView labelView;

        private final View button;

//        private final ProgressBar progressBar;

        private ViewHolder(@NonNull ViewItineraryItemBinding binding) {
            super(binding.getRoot());
            imageView = binding.itemImageView;
            labelView = binding.itemTitleView;
            button = binding.button;
//            progressBar = binding.progressBar;
        }
    }

}
