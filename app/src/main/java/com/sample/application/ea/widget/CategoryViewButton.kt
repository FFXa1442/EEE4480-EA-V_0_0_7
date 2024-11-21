package com.sample.application.ea.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.sample.application.ea.R
import com.sample.application.ea.res.TypedArrayContainer

/**
 * Custom button view for category selection.
 */
class CategoryViewButton : ConstraintLayout {

    companion object {
        private const val TEXT_SIZE_NOT_FOUND = -1f
    }

    private lateinit var button: LinearLayout

    private lateinit var textView: TextView

    private lateinit var imageView: ImageView

    // Property to manage button text
    var text: CharSequence
        get() = textView.text
        set(value) {
            textView.text = value
        }

    constructor(
        context: Context
    ) : this(
        context,
        null
    )

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(
        context,
        attrs,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

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
        init(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
        )
    }

    // Initializes the button with attributes
    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {

        var text = ""
        var textSize = TEXT_SIZE_NOT_FOUND
        var drawable: Drawable? = null


        TypedArrayContainer(
            context,
            attrs,
            R.styleable.CategoryViewButton,
            defStyleAttr,
            defStyleRes
        ).use { a ->
            text = a.getString(
                R.styleable.CategoryViewButton_android_text
            ).let { str ->
                when (str) {
                    null -> ""
                    else -> str
                }
            }
            textSize = a.getDimension(
                R.styleable.CategoryViewButton_android_textSize,
                TEXT_SIZE_NOT_FOUND
            )
            val src = a.getResourceId(R.styleable.CategoryViewButton_android_src, -1)
            if (src != -1) {
                drawable = AppCompatResources.getDrawable(context, src);
            }
        }

        inflateLayout(context, text, textSize, drawable)
    }

    // Inflates the layout and sets up the button
    private fun inflateLayout(
        context: Context,
        text: String,
        textSize: Float,
        drawable: Drawable?
    ) = LayoutInflater.from(context).let { inflater ->
        inflater.inflate(
            R.layout.widget_category_view_button,
            this,
            true
        ).let { layout ->
            button = layout.requireViewById(R.id.button)
            textView = layout.requireViewById<TextView>(R.id.text)
                .also { v ->
                    v.text = text
                    when {
                        textSize != TEXT_SIZE_NOT_FOUND -> v.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            textSize
                        )
                    }
                }
            imageView = layout.requireViewById<ImageView>(R.id.image)
                .also { v ->
                    if (drawable != null) {
                        v.setImageDrawable(drawable)
                    } else {
                        v.visibility = GONE
                    }
                }
        }
    }


    // Sets the click listener for the button
    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }


}