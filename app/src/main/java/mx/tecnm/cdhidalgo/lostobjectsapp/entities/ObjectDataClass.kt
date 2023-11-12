package mx.tecnm.cdhidalgo.lostobjectsapp.entities

import android.os.Parcel
import android.os.Parcelable

data class ObjectDataClass(
    var id : String?,
    var email: String?,
    var username: String?,
    var phoneNumber: String?,
    var title : String?,
    var description : String?,
    var location : String?,
    var date : String?,
    var reportType : String?,
    var imageUrl : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this (null, null, null,null, null, null, null, null, null,null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(phoneNumber)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(location)
        parcel.writeString(date)
        parcel.writeString(reportType)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ObjectDataClass> {
        override fun createFromParcel(parcel: Parcel): ObjectDataClass {
            return ObjectDataClass(parcel)
        }

        override fun newArray(size: Int): Array<ObjectDataClass?> {
            return arrayOfNulls(size)
        }
    }
}
