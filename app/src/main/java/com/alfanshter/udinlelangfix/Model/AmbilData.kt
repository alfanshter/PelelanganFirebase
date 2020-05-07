package com.alfanshter.udinlelangfix.Model

class AmbilData {

    /// MOdel class
    var nameMember : String? = null
    constructor(){

    }

    constructor(email: String?,nameMember : String?,saldo :String?,photo:String?) {
        this.nameMember = nameMember
    }
}