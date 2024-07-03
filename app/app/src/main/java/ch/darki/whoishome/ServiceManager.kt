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

    private val sharedPreferences = getSharedPreferences("ch.darki.whoishome", MODE_PRIVATE)

    fun tryLogin(callback : (Boolean) -> Unit) {

        val email = sharedPreferences.getString(KEY, null)

        if (email == null) {
            callback(false)
            return
        }

        presenceService.personService.getPersonByEmail(email) {
            if (it != null) {
                login(it)
            }
            callback(it != null)
        }
    }

    fun login(person: Person) {
        setPerson(person)
        sharedPreferences.edit().putString(KEY, person.email).apply()
    }

    fun logOut() {
        setPerson(null)
        sharedPreferences.edit().remove(KEY).apply()
    }

    private fun setPerson(person: Person?) {
        currentPerson = person

        if(currentPerson?.id != null) {
            Firebase.crashlytics.setUserId(currentPerson!!.id!!)
        }
        else {
            Firebase.crashlytics.setUserId("")
        }
    }

    companion object {
        const val KEY = "email"
    }
}