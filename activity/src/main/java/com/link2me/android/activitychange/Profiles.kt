package com.link2me.android.activitychange

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// https://developer.android.com/kotlin/parcelize 참조
@Parcelize
data class Profiles(
    var name: String="",
    var age:Int = 0,
    var gender: String=""
):Parcelable
