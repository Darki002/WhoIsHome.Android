package ch.darki.whoishome.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class LogInService (private val sharedPreferences: SharedPreferences) : Application(){

    val key = "email"

    var currentPerson : Person? = null
        private set

    fun logout(){
        currentPerson = null
        sharedPreferences.edit().remove(key).apply()
    }

    fun register(email : String, displayName : String){
        currentPerson = Person(displayName, email)

        val serviceManager : ServiceManager = applicationContext as ServiceManager
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