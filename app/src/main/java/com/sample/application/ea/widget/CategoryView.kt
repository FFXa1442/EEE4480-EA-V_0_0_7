package com.sample.application.ea.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.navigationrail.NavigationRailView
import com.sample.application.ea.R

/**
 * Custom view representing a category selector with buttons.
 */
class CategoryView : NavigationRailView {

    // Buttons for each category
    private lateinit var allButton: CategoryViewButton
    private lateinit var jpButton: CategoryViewButton
    private lateinit var krButton: CategoryViewButton
    private lateinit var twButton: CategoryViewButton
    private lateinit var usButton: CategoryViewButton
    private lateinit var ukButton: CategoryViewButton
    private lateinit var cnButton: CategoryViewButton
    private lateinit var myButton: CategoryViewButton
    private lateinit var sgButton: CategoryViewButton

    constructor(
        context: Context
    ) : super(
        context
    ) {
        inflateLayout(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context,
        attrs
    ) {
        inflateLayout(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflateLayout(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        inflateLayout(context)
    }

    // Inflates the layout and initializes buttons
    private fun inflateLayout(context: Context) =
        LayoutInflater.from(context).let { inflater ->
            inflater.inflate(
                R.layout.widget_category_view,
                this,
                true
            ).let { layout ->
                allButton = layout.requireViewById(R.id.all_button)
                jpButton = layout.requireViewById(R.id.jp_button)
                krButton = layout.requireViewById(R.id.kr_button)
                twButton = layout.requireViewById(R.id.tw_button)
                usButton = layout.requireViewById(R.id.us_button)
                ukButton = layout.requireViewById(R.id.uk_button)
                cnButton = layout.requireViewById(R.id.cn_button)
                myButton = layout.requireViewById(R.id.my_button)
                sgButton = layout.requireViewById(R.id.sg_button)
            }
        }


    // Setters for button click listeners

    fun setOnAllButtonClickListener(l: OnClickListener) {
        allButton.setOnClickListener(l)
    }

    fun setOnJPButtonClickListener(l: OnClickListener) {
        jpButton.setOnClickListener(l)
    }

    fun setOnKRButtonClickListener(l: OnClickListener) {
        krButton.setOnClickListener(l)
    }

    fun setOnTWButtonClickListener(l: OnClickListener) {
        twButton.setOnClickListener(l)
    }

    fun setOnUKButtonClickListener(l: OnClickListener) {
        ukButton.setOnClickListener(l)
    }

    fun setOnUSButtonClickListener(l: OnClickListener) {
        usButton.setOnClickListener(l)
    }

    fun setOnCNButtonClickListener(l: OnClickListener) {
        cnButton.setOnClickListener(l)
    }

    fun setOnMYButtonClickListener(l: OnClickListener) {
        myButton.setOnClickListener(l)
    }

    fun setOnSGButtonClickListener(l: OnClickListener) {
        sgButton.setOnClickListener(l)
    }
}