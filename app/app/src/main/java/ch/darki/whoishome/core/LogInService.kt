package ch.darki.whoishome.core

import android.content.SharedPreferences
import ch.darki.whoishome.ServiceManager

class LogInService (private val sharedPreferences: SharedPreferences){

    private val key = "email"

    var currentPerson : Person? = null
        private set

    fun logout(){
        currentPerson = null
        sharedPreferences.edit().remove(key).apply()
    }

    fun tryRegister(email : String, displayName : String, serviceManager: ServiceManager){
        currentPerson = Person(displayName, email)

        serviceManager.presenceService.personService.createPersonIfNotExists(currentPerson!!)

        sharedPreferences.edit().apply{
            putString(key, email)
        }.apply()
    }

}