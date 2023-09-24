package ch.darki.whoishome.core

import android.app.Application
import android.content.SharedPreferences
import ch.darki.whoishome.ServiceManager

class LogInService (private val sharedPreferences: SharedPreferences){

    val key = "email"

    var currentPerson : Person? = null
        private set

    fun logout(){
        currentPerson = null
        sharedPreferences.edit().remove(key).apply()
    }

    fun register(email : String, displayName : String, serviceManager: ServiceManager){
        currentPerson = Person(displayName, email)

        serviceManager.presenceService.personService.createPersonIfNotExists(currentPerson!!)

        sharedPreferences.edit().apply{
            putString(key, email)
        }.apply()
    }

    fun isLoggedIn() : Boolean {
        val login = sharedPreferences.getString(key, null)
        return login != null
    }

}