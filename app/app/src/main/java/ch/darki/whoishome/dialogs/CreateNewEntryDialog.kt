package ch.darki.whoishome.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager
import org.joda.time.DateTime

class CreateNewEntryDialog(private val context : Context, private val service: ServiceManager) {

    private lateinit var name: String
    private lateinit var startDate: DateTime
    private lateinit var endDate: DateTime

    fun show() {
        var isSuccess = showEventDetails()
        if(isSuccess){
            isSuccess = showDinnerDetails()
            if(isSuccess){
                createNewEvent(name, startDate, endDate)
                Toast.makeText(context, "Event erstellt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEventDetails() : Boolean {
        var wasSuccess = true

        val dialog = Dialog(context)
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
            DateTimePicker(context) { dateTime ->
                startDateEt.setText(dateTime.toString("dd.MM.yyyy HH:mm"))
                startDate = dateTime
            }.show()
        }

        endDateEt.setOnClickListener {
            DateTimePicker(context) { dateTime ->
                endDateEt.setText(dateTime.toString("dd.MM.yyyy HH:mm"))
                endDate = dateTime
            }.show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
            wasSuccess = false
        }

        dialog.setOnCancelListener {
            wasSuccess = false
        }

        createButton.setOnClickListener {
            val name = nameEt.text.toString()

            if (name.isEmpty() || startDate == null || endDate == null) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.name = name
            this.startDate = startDate!!
            this.endDate = endDate!!

            dialog.dismiss()
        }

        dialog.show()
        return wasSuccess
    }

    private fun showDinnerDetails() : Boolean{
        var wasSuccess = true;



        return wasSuccess
    }

    private fun createNewEvent(name: String, startDate: DateTime, endDate: DateTime) {
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