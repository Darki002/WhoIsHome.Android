package ch.darki.whoishome

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import ch.darki.whoishome.core.models.Person
import ch.darki.whoishome.core.PresenceService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class ServiceManager : Application() {

    private val _currentPerson = MutableLiveData<Person?>()
    val currentPerson: LiveData<Person?> = _currentPerson

    val presenceService : PresenceService = PresenceService()

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    fun tryLogin(callback : (Boolean) -> Unit) {

        val email = sharedPreferences.getString(KEY, null)

        if (email == null) {
            callback(false)
            return
        }

        presenceService.personService.getPersonByEmail(email) {
            if (it != null) {
                login(it)
            }
            callback(it != null)
        }
    }

    fun login(person: Person) {
        setPerson(person)
        sharedPreferences.edit().putString(KEY, person.email).apply()
    }

    fun logOut() {
        setPerson(null)
        sharedPreferences.edit().remove(KEY).apply()
    }

    private fun setPerson(person: Person?) {
        _currentPerson.postValue(person)

        if(person?.id != null) {
            Firebase.crashlytics.setUserId(person.id)
        }
        else {
            Firebase.crashlytics.setUserId("")
        }
    }

    companion object {
        const val KEY = "email"
    }
}