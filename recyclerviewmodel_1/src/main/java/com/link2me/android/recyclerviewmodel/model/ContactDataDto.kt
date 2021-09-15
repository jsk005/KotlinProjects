package com.link2me.android.recyclerviewmodel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContactDataDto (
    val status: String,
    val message: List<ContactData>
): Parcelable