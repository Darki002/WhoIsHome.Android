package ch.darki.whoishome.core

import android.content.SharedPreferences
import ch.darki.whoishome.ServiceManager

class LogInService (private val sharedPreferences: SharedPreferences, private val serviceManager: ServiceManager){

    private val key = "email"

    var currentPerson : Person? = null
        private set

    init {
        val email = sharedPreferences.getString(key, "")
        if(!email.isNullOrEmpty()){
            serviceManager.presenceService.personService.getPersonByEmail(email){
                currentPerson = it
            }
        }
    }

    fun logout(){
        currentPerson = null
        sharedPreferences.edit().remove(key).apply()
    }

    fun tryRegister(email : String, displayName : String){
        currentPerson = Person(displayName, email)

        serviceManager.presenceService.personService.createPersonIfNotExists(currentPerson!!, serviceManager)

        sharedPreferences.edit().apply{
            putString(key, email)
        }.apply()
    }

}