package ch.darki.whoishome

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ch.darki.whoishome.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var service: ServiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = applicationContext as ServiceManager
        service.init(getSharedPreferences("userManager", MODE_PRIVATE)!!)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onLogIn(view: View){
        val email = findViewById<TextView>(R.id.emailLogIn).text.toString()
        val displayName = findViewById<TextView>(R.id.displayNameLogIn).text.toString()

        service.logInService.register(email, displayName, service)
        startActivity(Intent(this, MainActivity::class.java))

        val toast = Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.show()
    }
}