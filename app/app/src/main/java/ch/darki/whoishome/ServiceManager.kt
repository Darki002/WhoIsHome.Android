package ch.darki.whoishome

import android.app.Application
import android.content.SharedPreferences
import ch.darki.whoishome.core.LogInService
import ch.darki.whoishome.core.PresenceService

class ServiceManager : Application() {

    lateinit var logInService : LogInService
    lateinit var presenceService : PresenceService

    fun init(sharedPreferences: SharedPreferences){
        logInService = LogInService(sharedPreferences, this)
        presenceService = PresenceService()
    }

}