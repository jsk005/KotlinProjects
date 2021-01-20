package com.link2me.android.sqlite

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MyGlideApp : AppGlideModule() {
    // Failed to find GeneratedAppGlideModule 메시지 나오는 것 없애기 위해서
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}