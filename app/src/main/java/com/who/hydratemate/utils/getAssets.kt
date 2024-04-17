package com.who.hydratemate.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun loadImageFromAssets(context: Context, fileName: String): Bitmap {
    val assetManager = context.assets
    val inputStream = assetManager.open(fileName)
    return BitmapFactory.decodeStream(inputStream)
}