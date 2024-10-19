package com.example.tinytales

data class User(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var address: String? = null,
){
    constructor() : this(null, null, null, null, null)
}
