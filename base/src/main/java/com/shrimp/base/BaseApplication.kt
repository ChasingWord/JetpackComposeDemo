package com.shrimp.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.memory.MemoryCache
import com.shrimp.base.utils.CrashErrorHandler

/**
 * Created by chasing on 2022/4/28.
 */
open class BaseApplication : Application(), ImageLoaderFactory {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
            .crossfade(true)
            .components(fun ComponentRegistry.Builder.() {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(VideoFrameDecoder.Factory())
            })
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        CrashErrorHandler.init(this)
    }
}