package ch.darki.whoishome

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import ch.darki.whoishome.databinding.ActivityMainBinding
import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var service: ServiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.create_new_event -> {
                showCreateNewEventDialog()
                return true
            }

            R.id.log_out -> {
                service.logInService.logout()
                startActivity(Intent(this, LogIn::class.java))
                return true
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

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.new_event_dialog)

        val nameEt = dialog.findViewById<EditText>(R.id.event_name)
        val startDateEt = dialog.findViewById<EditText>(R.id.start_date)
        val endDateEt = dialog.findViewById<EditText>(R.id.end_date)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_create_event)
        val createButton = dialog.findViewById<Button>(R.id.create_event)

        var startDate: DateTime? = null
        var endDate: DateTime? = null

        startDateEt.setOnClickListener {
            DateTimePicker(this) { dateTime ->
                startDateEt.setText(dateTime.toString("dd.MM.yyyy HH:mm"))
                startDate = dateTime
            }.show()
        }

        endDateEt.setOnClickListener {
            DateTimePicker(this) { dateTime ->
                endDateEt.setText(dateTime.toString("dd.MM.yyyy HH:mm"))
                endDate = dateTime
            }.show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        createButton.setOnClickListener {
            val name = nameEt.text.toString()

            if (name.isEmpty() || startDate == null) {
                Toast.makeText(this, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createNewEvent(name, startDate!!, endDate)
            dialog.dismiss()
            Toast.makeText(this, "Event erstellt", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    private fun createNewEvent(name: String, startDate: DateTime, endDate: DateTime?) {
        if (service.logInService.currentPerson != null) {
            service.presenceService.eventService.createEvent(
                service.logInService.currentPerson!!,
                name,
                startDate,
                endDate
            )
        }
    }
}