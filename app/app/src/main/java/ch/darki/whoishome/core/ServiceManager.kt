package ch.darki.whoishome.core

import android.app.Application
import android.content.Context

class ServiceManager : Application() {

    val logInService = LogInService(getSharedPreferences("userManager", Context.MODE_PRIVATE))
    val presenceService = PresenceService()

}