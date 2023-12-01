package ch.darki.whoishome.core

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonService {

    fun getPersonByEmail(email: String, callback: (Person?) -> Unit) {
        val db = Firebase.firestore

        db.collection("person").whereEqualTo("email", email).get()
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(null)
            }
            .addOnSuccessListener {
                val result = it.documents.getOrNull(0)
                val person = if (result == null) null else Person.new(result)
                callback.invoke(person)
            }
    }

    fun createPersonIfNotExists(person: Person, context : Context) {
        val db = Firebase.firestore

        db.collection("person").where(Filter.equalTo("email", person.email))
            .get()
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                Toast.makeText(context, "failed to create Person", Toast.LENGTH_SHORT).show() }
            .addOnSuccessListener { result ->
                if(result.isEmpty){
                    db.collection("person").add(person)
                }
            }
    }
}