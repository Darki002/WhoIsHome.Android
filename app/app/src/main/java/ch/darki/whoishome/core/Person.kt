package ch.darki.whoishome.core

data class Person(val firstname : String, val name : String, val email : String){
    val fullName : String
        get() = "$firstname $name"
}