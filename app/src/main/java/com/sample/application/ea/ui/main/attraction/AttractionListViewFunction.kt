package com.sample.application.ea.ui.main.attraction

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.application.ea.database.AttractionList
import com.sample.application.ea.dataset.readonly.AttractionExtra
import com.sample.application.ea.text.doOnTextChanged
import com.sample.application.ea.ui.main.description.DescriptionFragment
import com.sample.application.ea.widget.ItineraryListAdapter

fun <TBinding> AttractionListViewFragment<TBinding>.hideKeyboard(
) where TBinding : ViewDataBinding {
    searchView.clearFocus()
    mainActivity.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ).apply {
        (this as InputMethodManager).hideSoftInputFromWindow(
            requireView().windowToken, 0
        )
    }
}

@SuppressLint("ClickableViewAccessibility")
fun <TBinding> AttractionListViewFragment<TBinding>.setupUI(
    view: View,
) where TBinding : ViewDataBinding {
    when (view) {
        !is SearchView -> {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }
    }

    if (view is ViewGroup) {
        (0 until view.childCount).forEach { i ->
            setupUI(view.getChildAt(i))
        }
    }
}

fun <TBinding> AttractionListViewFragment<TBinding>.setupRecyclerView(
    searchViewWatcherCallback: (TextWatcher) -> Unit = {},
) where TBinding : ViewDataBinding {
    if (adapter == null) {
        val newList = mutableListOf<AttractionExtra>()
        AttractionList.forEach { item ->
            when {
                filterByPopularity(item) -> newList += item
            }
        }
        adapter = ItineraryListAdapter.create { mainActivity }.also {
            it.originalList = newList
        }
    }

    adapter!!.apply {
        matchFilter = this@setupRecyclerView::matchFilter
        onItemClickListener = { item ->
            when (sheet) {
                null -> {
                    DescriptionFragment().apply {
                        sheet = this
                        setOnDismissAction { sheet = null }
                        setArguments(Bundle().apply {
                            putParcelable(DescriptionFragment.ARG_TAG, item)
                        })
                    }.show(childFragmentManager, "DescriptionFragment")
                }
            }
        }
    }

    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(
        mainActivity, RecyclerView.VERTICAL, false
    )
    searchViewWatcherCallback(
        searchView.doOnTextChanged(::onSearchTextChanged)
    )
}