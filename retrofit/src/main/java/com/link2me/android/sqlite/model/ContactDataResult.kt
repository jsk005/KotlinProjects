package com.link2me.android.sqlite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContactDataResult (
    val status: String,
    val message: List<ContactData>
): Parcelable