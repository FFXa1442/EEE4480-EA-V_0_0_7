package com.sample.application.ea.functions.imagecropper

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.ViewDataBindingActivity
import com.sample.application.ea.databinding.ActivityImageCropperBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class ImageCropper : ViewDataBindingActivity<ActivityImageCropperBinding>() {

    companion object {
        const val IMAGE_RESULT = "IMAGE_RESULT"
        const val IMAGE_URL = "IMAGE_URL"
    }

    override val layoutId: Int
        get() = R.layout.activity_image_cropper

    override fun onBeforeCreate(
        savedInstanceState: Bundle?,
    ): Boolean {
        enableEdgeToEdge()
        return super.onBeforeCreate(savedInstanceState)
    }

    private fun onApplyWindowInsetsInternal(
        binding: ActivityImageCropperBinding,
        sysStatusBarHeight: Int,
    ) = binding.systemBarOffsetView.updatePadding(
        top = sysStatusBarHeight
    )

    override fun onApplyWindowInsets(
        view: View,
        binding: ActivityImageCropperBinding,
        systemBars: Insets,
    ) {

        onApplyWindowInsetsInternal(
            binding = binding,
            sysStatusBarHeight = systemBars.top,
        )
    }

    override fun onCreate(
        binding: ActivityImageCropperBinding,
        savedInstanceState: Bundle?,
    ) {
        binding.cropImageView.setAspectRatio(1, 1)

        val uri = Uri.parse(intent.getStringExtra(IMAGE_URL)!!)

        contentResolver.openInputStream(uri).use { stream ->
            val target = BitmapFactory.decodeStream(stream!!)
            binding.cropImageView.setImageBitmap(target)
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.submitButton.setOnClickListener {
            val image = binding.cropImageView.getCroppedImage(256, 256)!!
            val file = File(cacheDir, "${UUID.randomUUID()}.png")
            try {
                FileOutputStream(file).use { stream ->
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
                val intent = Intent().apply {
                    putExtra(IMAGE_RESULT, file.absolutePath)
                }
                setResult(Activity.RESULT_OK, intent)
            } catch (e: IOException) {
                if (file.exists()) file.delete()
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }

    }

}