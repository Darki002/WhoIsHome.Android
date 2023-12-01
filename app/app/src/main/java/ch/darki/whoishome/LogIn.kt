package ch.darki.whoishome

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.darki.whoishome.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var service: ServiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = applicationContext as ServiceManager
        service.init(getSharedPreferences("userManager", MODE_PRIVATE)!!)

        if(service.logInService.currentPerson != null){
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onLogIn(view: View){
        val email = findViewById<TextView>(R.id.emailLogIn).text.toString()
        val displayName = findViewById<TextView>(R.id.displayNameLogIn).text.toString()

        service.logInService.tryRegister(email, displayName)
        startActivity(Intent(this, MainActivity::class.java))

        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
    }
}