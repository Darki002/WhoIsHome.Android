package ch.darki.whoishome.core

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonService {

    fun getPersonByEmail(email: String, callback: (Person?) -> Unit) {
        val db = Firebase.firestore

        db.collection("person").whereEqualTo("email", email).get()
            .addOnFailureListener {
                callback.invoke(null)
            }
            .addOnSuccessListener {
                val result = it.documents[0].toObject(Person::class.java)
                callback.invoke(result)
            }
    }

    fun createPersonIfNotExists(person: Person, context : Context) {
        val db = Firebase.firestore

        db.collection("person").where(Filter.equalTo("email", person.email))
            .get()
            .addOnFailureListener { Toast.makeText(context, "failed to create Person", Toast.LENGTH_SHORT).show() }
            .addOnSuccessListener { result ->
                if(result.isEmpty){
                    db.collection("person").add(person)
                }
            }
    }
}