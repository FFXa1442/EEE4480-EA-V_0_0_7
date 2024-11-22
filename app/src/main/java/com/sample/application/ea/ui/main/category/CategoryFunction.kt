package com.sample.application.ea.ui.main.category

import android.view.View
import android.widget.EditText
import com.sample.application.ea.databinding.FragmentCategoryBinding
import com.sample.application.ea.extension.getValue
import com.sample.application.ea.extension.setValue
import java.util.concurrent.atomic.AtomicReference

private typealias LabelListener = Pair<String, (View.OnClickListener) -> Unit>

fun CategoryFragment.setupSideViewButton(
    binding: FragmentCategoryBinding,
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
                        filter = filter
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
) : View.OnClickListener {

    override fun onClick(v: View?) {
        var curCate: String by currentCategory
        searchView.setText("")
        curCate = target
        filter(curCate)
    }

}