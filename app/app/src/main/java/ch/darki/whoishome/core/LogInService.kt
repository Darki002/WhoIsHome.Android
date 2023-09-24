package ch.darki.whoishome.core

import android.content.SharedPreferences

class LogInService (private val sharedPreferences: SharedPreferences) {

    val key = "email"

    var currentPerson : Person? = null
        private set

    companion object {
        var instance : LogInService? = null

        fun new(sharedPreferences: SharedPreferences){
            instance = LogInService(sharedPreferences)
        }
    }

    fun logout(){
        currentPerson = null
        sharedPreferences.edit().remove(key).apply()
    }

    fun register(email : String, displayName : String){
        currentPerson = Person(displayName, email)
        PresenceService.instance?.personService?.createPersonIfNotExists(currentPerson!!)
        sharedPreferences.edit().apply{
            putString(key, email)
        }.apply()
    }

    fun isLoggedIn() : Boolean {
        val login = sharedPreferences.getString(key, null)
        return login != null
    }

}