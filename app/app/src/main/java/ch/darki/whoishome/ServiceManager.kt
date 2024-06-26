package ch.darki.whoishome

import android.app.Application
import android.content.SharedPreferences
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.PresenceService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class ServiceManager : Application() {

    var currentPerson : Person? = null
        private set

    lateinit var presenceService : PresenceService

    fun init() {
        presenceService = PresenceService()
    }

    fun setPerson(person: Person) {
        currentPerson = person

        if(currentPerson?.id != null) {
            Firebase.crashlytics.setUserId(currentPerson!!.id!!)
        }
    }
}