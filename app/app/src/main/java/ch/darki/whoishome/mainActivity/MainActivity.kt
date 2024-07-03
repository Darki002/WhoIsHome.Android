package ch.darki.whoishome.mainActivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import ch.darki.whoishome.LogIn
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager
import ch.darki.whoishome.databinding.ActivityMainBinding
import ch.darki.whoishome.dialogs.CreateNewEntryDialog
import ch.darki.whoishome.dialogs.CreateNewRepeatedEventDialog
import ch.darki.whoishome.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var service: ServiceManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = applicationContext as ServiceManager
        sharedPreferences = getSharedPreferences("ch.darki.whoishome", MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkForLogin()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        val devFunctionsActive = preferenceManager.getBoolean("show_dev_options", false)
        if(devFunctionsActive) {
            menu.setGroupEnabled(R.id.dev_group, true)
            menu.setGroupVisible(R.id.dev_group, true)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_new_event -> {
                showCreateNewEventDialog()
                return true
            }

            R.id.create_new_repeated_event -> {
                showCreateNewRepeatedEventDialog()
                return true
            }

            R.id.log_out -> {
                service.logOut()
                sharedPreferences.edit().remove("email").apply()
                startActivity(Intent(this, LogIn::class.java))
                return true
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }

            R.id.crash -> {
                throw RuntimeException("Test Error for Developer!")
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        invalidateOptionsMenu()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showCreateNewEventDialog() {

        val dialog = CreateNewEntryDialog(this, service)
        dialog.show()
    }

    private fun showCreateNewRepeatedEventDialog(){
        val dialog = CreateNewRepeatedEventDialog(this, service)
        dialog.show()
    }

    private fun checkForLogin() {
        service.tryLogin { success ->
            if (success) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
            } else {
                service.logOut()
                startActivity(Intent(this, LogIn::class.java))
            }
        }
    }
}