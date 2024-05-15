package ch.darki.whoishome.core

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime

class RepeatEvenService {

    private val collection = "repeated-events"

    fun createRepeatedEvent(
        person: Person,
        name: String,
        startTime: DateTime,
        endTime: DateTime,
        firstDay: DateTime,
        lastDay: DateTime,
        relevantForDinner: Boolean,
        dinnerAt: DateTime?,
        callback: (Boolean) -> Unit
    ){
        val repeatEvent = RepeatEvent.create(
            person = person,
            name = name,
            startTime = startTime,
            endTime = endTime,
            firstDay = firstDay,
            lastDay = lastDay,
            relevantForDinner = relevantForDinner,
            dinnerAt = dinnerAt
        )

        Firebase.firestore.collection(collection).document(repeatEvent.id).set(repeatEvent.toDb())
            .addOnSuccessListener {
                callback.invoke(true)
            }
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(false)
            }
    }

    fun getRepeatedEventFromPerson(person: Person, callback: (List<RepeatEvent?>) -> Unit) {
        val result = ArrayList<RepeatEvent?>()
        val db = Firebase.firestore

        db.collection(collection)
            .whereEqualTo(FieldPath.of("person", "email"), person.email).get()
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(listOf())
            }
            .addOnSuccessListener {
                for (doc in it.documents){
                    result.add(RepeatEvent.fromDb(doc))
                }
                callback.invoke(result)
            }
    }
}