package mx.tecnm.cdhidalgo.lostobjectsapp.entities

data class UserDataClass(
    var name : String?,
    var lastName1 : String?,
    var lastName2 : String?,
    var phoneNumber : String?,
    var email : String?
) {
    constructor() : this (null, null, null, null, null)
}
