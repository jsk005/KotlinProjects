package com.link2me.android.recyclerview.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contacts_Item (
    var contactId: String,
    var contactName: String = "",
    var contactmobileNO: String = "",
    var contactofficeNO: String = "",
    var contactKey: String? = null
): Parcelable