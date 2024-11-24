package com.sample.application.ea.ui.main.attraction

import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.dataset.readonly.Attraction
import com.sample.application.ea.ui.main.description.DescriptionFragment
import com.sample.application.ea.ui.main.generic.MainBindingFragment
import com.sample.application.ea.widget.ItineraryListAdapter
import java.util.regex.Pattern

abstract class AttractionListViewFragment<TBinding> :
    MainBindingFragment<TBinding> where TBinding : ViewDataBinding {

    internal val mainActivity: MainActivity
        get() = parentActivity

    internal abstract val searchView: EditText

    internal abstract val recyclerView: RecyclerView

    internal var adapter: ItineraryListAdapter? = null

    internal var sheet: DescriptionFragment? = null

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    protected fun filter(constraint: CharSequence) =
        adapter!!.filter.filter(constraint)

    @CallSuper
    override fun onCreateView(binding: TBinding, savedInstanceState: Bundle?) {
        setupUI(binding.root)
        setupSideViewButton(binding)
        setupRecyclerView {
            searchViewTextWatcher = it
        }
    }

    protected open fun setupSideViewButton(
        binding: TBinding,
    ) = Unit

    internal open fun filterByPopularity(item: Attraction) = true

    internal abstract fun matchFilter(
        pattern: Pattern,
        set: Attraction,
    ): List<Boolean>

    internal abstract fun onSearchTextChanged(s: CharSequence?)

    internal lateinit var searchViewTextWatcher: TextWatcher
        private set
}