package mx.tecnm.cdhidalgo.lostobjectsapp.entities

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
) {
    constructor() : this (null, null, null,null, null, null, null, null, null,null)
}
