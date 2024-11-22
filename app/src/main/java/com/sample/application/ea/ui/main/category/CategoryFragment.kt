package com.sample.application.ea.ui.main.category

import android.widget.EditText
import androidx.lifecycle.AtomicReference
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentCategoryBinding
import com.sample.application.ea.dataset.readonly.Itinerary
import com.sample.application.ea.extension.getValue
import com.sample.application.ea.ui.main.generic.ItineraryListViewFragment
import java.util.regex.Pattern

class CategoryFragment :
    ItineraryListViewFragment<FragmentCategoryBinding>() {

    private val currentCategory = AtomicReference<String>("")

    override val searchView: EditText
        get() = binding.searchView

    override val recyclerView: RecyclerView
        get() = binding.cateListView

    override fun matchFilter(pattern: Pattern, set: Itinerary): List<Boolean> {
        return mutableListOf(
            pattern.matcher("${set.country}|${set.itinerary}").find()
        )
    }

    override fun setupSideViewButton(
        binding: FragmentCategoryBinding,
    ) = setupSideViewButton(
        binding = binding,
        currentCategory = currentCategory,
        filter = ::filter,
    )

    override fun onSearchTextChanged(s: CharSequence?) {
        val curCate: String by currentCategory
        filter("^(?=.*$curCate)(?=.*$s).*")
    }

    override val layoutId: Int = R.layout.fragment_category
}