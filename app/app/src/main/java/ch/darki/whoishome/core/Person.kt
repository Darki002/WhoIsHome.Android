package ch.darki.whoishome.core

import com.google.firebase.firestore.DocumentSnapshot

data class Person(val displayName : String, val email : String, val id : String? = null){

    fun toDb() : Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()

        map["displayName"] = displayName
        map["email"] = email

        return map
    }

    companion object {
        fun new(doc : DocumentSnapshot) : Person {
            return Person(
                doc.getString("displayName").toString(),
                doc.getString("email").toString(),
                doc.id
            )
        }
    }
}