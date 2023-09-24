package ch.darki.whoishome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.darki.whoishome.core.LogInService
import ch.darki.whoishome.core.ServiceManager
import ch.darki.whoishome.databinding.ActivityMainBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var logInService: LogInService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = applicationContext as ServiceManager
        logInService = service.logInService

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}