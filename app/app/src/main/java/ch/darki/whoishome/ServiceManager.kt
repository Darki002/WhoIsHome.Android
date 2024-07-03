package ch.darki.whoishome

import android.app.Application
import ch.darki.whoishome.core.models.Person
import ch.darki.whoishome.core.PresenceService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class ServiceManager : Application() {

    var currentPerson : Person? = null
        private set

    val presenceService : PresenceService = PresenceService()

    fun setPerson(person: Person) {
        currentPerson = person

        if(currentPerson?.id != null) {
            Firebase.crashlytics.setUserId(currentPerson!!.id!!)
        }
    }

    fun login(email: String?, callback : (Person?) -> Unit) {
        if (email == null) {
            callback(null)
        }

        presenceService.personService.getPersonByEmail(email!!) {
            currentPerson = it
            callback(it)
        }
    }

    fun logOut() {
        currentPerson = null
        Firebase.crashlytics.setUserId("")
    }
}