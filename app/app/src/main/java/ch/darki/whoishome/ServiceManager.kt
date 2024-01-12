package ch.darki.whoishome

import android.app.Application
import android.content.SharedPreferences
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.PresenceService

class ServiceManager : Application() {

    var currentPerson : Person? = null

    lateinit var presenceService : PresenceService

    fun init() {
        presenceService = PresenceService()
    }
}