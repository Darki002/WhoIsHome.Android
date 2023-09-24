package ch.darki.whoishome.core

import android.content.SharedPreferences

class LogInService (private val sharedPreferences: SharedPreferences) {

    private var currentPerson : Person? = null

    companion object {
        var instance : LogInService? = null

        fun new(sharedPreferences: SharedPreferences){
            instance = LogInService(sharedPreferences)
        }
    }

    fun register(email : String, displayName : String){
        currentPerson = Person(displayName, email)
        PresenceService.instance?.personService?.createPersonIfNotExists(currentPerson!!)
        sharedPreferences.edit().apply{
            putString("email", email)
        }.apply()
    }

    fun isLoggedIn() : Boolean {
        val login = sharedPreferences.getString("email", null)
        return login != null
    }

}