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
import androidx.navigation.NavController
import ch.darki.whoishome.LogIn
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager
import ch.darki.whoishome.databinding.ActivityMainBinding
import ch.darki.whoishome.dialogs.CreateNewEntryDialog
import ch.darki.whoishome.dialogs.CreateNewRepeatedEventDialog

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var service: ServiceManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ch.darki.whoishome", MODE_PRIVATE)

        service = applicationContext as ServiceManager
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        // If it is in Debug, there will be Dev Functions available, but default hidden for production.
        if(service.devFunctionsActive) {
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
                throw NotImplementedError("TODO!")
            }

            R.id.crash -> {
                throw Error("Test Error for Developer!")
            }

            else -> super.onOptionsItemSelected(item)
        }
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
}