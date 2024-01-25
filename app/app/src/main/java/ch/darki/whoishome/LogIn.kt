package ch.darki.whoishome

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var service: ServiceManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = applicationContext as ServiceManager
        service.init()

        sharedPreferences = getSharedPreferences("ch.darki.whoishome", MODE_PRIVATE)

        if(sharedPreferences.contains("email")){
            service.presenceService.personService.getPersonByEmail(sharedPreferences.getString("email", "")!!) {
                service.currentPerson = it
                if(service.currentPerson != null){
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLogIn(view: View){
        val email = findViewById<TextView>(R.id.emailLogIn).text.toString()
        val displayName = findViewById<TextView>(R.id.displayNameLogIn).text.toString()

        val person = Person(displayName, email)
        service.presenceService.personService.createPersonIfNotExists(person, this)
        service.currentPerson = person
        sharedPreferences.edit().putString("email", email).apply()

        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
    }
}