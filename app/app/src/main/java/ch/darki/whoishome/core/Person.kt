package ch.darki.whoishome.core

import com.google.firebase.firestore.DocumentSnapshot

data class Person(val displayName : String, val email : String){
    companion object {
        fun new(doc : DocumentSnapshot) : Person {
            return Person(
                doc.getString("displayName").toString(),
                doc.getString("email").toString()
            )
        }
    }
}