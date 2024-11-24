package com.sample.application.ea.ui.main.attraction

import android.widget.EditText
import androidx.lifecycle.AtomicReference
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentAttractionBinding
import com.sample.application.ea.dataset.readonly.Attraction
import com.sample.application.ea.extension.getValue
import java.util.regex.Pattern

class AttractionFragment :
    AttractionListViewFragment<FragmentAttractionBinding>() {

    private val currentCategory = AtomicReference<String>("")

    override val searchView: EditText
        get() = binding.searchView

    override val recyclerView: RecyclerView
        get() = binding.cateListView

    override fun matchFilter(pattern: Pattern, set: Attraction): List<Boolean> {
        return mutableListOf(
            pattern.matcher("${set.country}|${set.attraction}").find()
        )
    }

    override fun setupSideViewButton(
        binding: FragmentAttractionBinding,
    ) = setupSideViewButton(
        binding = binding,
        currentCategory = currentCategory,
        filter = ::filter,
    )

    override fun onSearchTextChanged(s: CharSequence?) {
        val curCate: String by currentCategory
        filter("^(?=.*$curCate)(?=.*$s).*")
    }

    override val layoutId: Int = R.layout.fragment_attraction
}