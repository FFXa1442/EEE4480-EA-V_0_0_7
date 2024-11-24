package com.sample.application.ea.ui.main.attraction

import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.sample.application.ea.databinding.FragmentAttractionBinding
import com.sample.application.ea.extension.getValue
import com.sample.application.ea.extension.setValue
import java.util.concurrent.atomic.AtomicReference

private typealias LabelListener = Pair<String, (View.OnClickListener) -> Unit>

fun AttractionFragment.setupSideViewButton(
    binding: FragmentAttractionBinding,
    currentCategory: AtomicReference<String>,
    filter: (String) -> Unit,
) {
    binding.cateMenu.apply {
        listOf(
            LabelListener("korea", ::setOnKRButtonClickListener),
            LabelListener("japan", ::setOnJPButtonClickListener),
            LabelListener("taiwan", ::setOnTWButtonClickListener),
            LabelListener("united kingdom", ::setOnUKButtonClickListener),
            LabelListener("united states", ::setOnUSButtonClickListener),
            LabelListener("china", ::setOnCNButtonClickListener),
            LabelListener("malaysia", ::setOnMYButtonClickListener),
            LabelListener("singapore", ::setOnSGButtonClickListener),
            LabelListener("", ::setOnAllButtonClickListener),
        ).forEach { item ->
            item.apply {
                second(
                    CateMenuButtonOnClickListener(
                        target = first,
                        searchView = searchView,
                        currentCategory = currentCategory,
                        filter = filter,
                        getTextWatcher = { searchViewTextWatcher }
                    )
                )
            }
        }
    }
}

private class CateMenuButtonOnClickListener(
    val target: String,
    val searchView: EditText,
    val currentCategory: AtomicReference<String>,
    val filter: (String) -> Unit,
    val getTextWatcher: () -> TextWatcher,
) : View.OnClickListener {

    override fun onClick(v: View?) {
        var curCate: String by currentCategory
        searchView.apply {
            removeTextChangedListener(getTextWatcher())
            setText("")
            addTextChangedListener(getTextWatcher())
        }
        curCate = target
        filter(curCate)
    }

}