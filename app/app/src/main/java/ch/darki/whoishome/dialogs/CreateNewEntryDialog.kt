package ch.darki.whoishome.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager
import org.joda.time.DateTime

class CreateNewEntryDialog(private val context : Context, private val service: ServiceManager) {

    private var name: String? = null
    private var startDate: DateTime? = null
    private var endDate: DateTime? = null
    private var relevantForDinner: Boolean = false
    private var dinnerAt: DateTime? = null

    fun show(fill : Boolean = false) {
        showEventDetails(fill) {
            showDinnerDetails(fill, { ok() }, { back() })
        }
    }

    private fun ok(){
        if(name != null && startDate != null && endDate != null){
            createNewEvent(name!!, startDate!!, endDate!!, relevantForDinner, dinnerAt)
            Toast.makeText(context, "Event erstellt", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun back(){
        show(true)
    }

    private fun showEventDetails(fill : Boolean, callback : () -> Unit) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.new_event_dialog)

        val nameEt = dialog.findViewById<EditText>(R.id.event_name)
        val startDateEt = dialog.findViewById<EditText>(R.id.start_date)
        val endDateEt = dialog.findViewById<EditText>(R.id.end_date)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_create_event)
        val continueButton = dialog.findViewById<Button>(R.id.continueButton)

        var startDate: DateTime? = this.startDate
        var endDate: DateTime? = this.endDate

        if(fill){

            nameEt.setText(this.name)
            startDateEt.setText(this.startDate?.toString("dd.MM.yyyy HH:mm"))
            endDateEt.setText(this.endDate?.toString("dd.MM.yyyy HH:mm"))
        }

        startDateEt.setOnClickListener {
            DateTimePicker(context) {d ->
                startDateEt.setText(d.toString("dd.MM.yyyy HH:mm"))
                startDate = d
            }.show()

        }

        endDateEt.setOnClickListener {
            DateTimePicker(context) { d ->
                endDateEt.setText(d.toString("dd.MM.yyyy HH:mm"))
                endDate = d
            }.show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        continueButton.setOnClickListener {
            val name = nameEt.text.toString()

            if (name.isEmpty() || startDate == null || endDate == null) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.name = name
            this.startDate = startDate!!
            this.endDate = endDate!!
            dialog.dismiss()
            callback.invoke()
        }

        dialog.show()
    }

    private fun showDinnerDetails(fill : Boolean, callback : () -> Unit, goBack : () -> Unit) {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.new_event_dinner_details_dialog)

        val relevantForDinnerCb = dialog.findViewById<CheckBox>(R.id.isRelevantForDinner)
        val dinnerAtEt = dialog.findViewById<EditText>(R.id.readyForDinnerAt)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_create_event)
        val createButton = dialog.findViewById<Button>(R.id.create_event)

        var relevantForDinner : Boolean
        var dinnerAt:  DateTime? = null

        if(fill){
            relevantForDinnerCb.isChecked = this.relevantForDinner
            if(dinnerAt != null){
                dinnerAtEt.setText(this.dinnerAt!!.toString("HH:mm"))
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
            goBack.invoke()
        }

        dinnerAtEt.setOnClickListener {
            TimePicker(context){d ->
                dinnerAtEt.setText(d.toString("HH:mm"))
                dinnerAt = d
            }.show()
        }

        createButton.setOnClickListener {
            relevantForDinner = relevantForDinnerCb.isChecked
            if(relevantForDinner && dinnerAt == null){
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            this.relevantForDinner = relevantForDinner
            this.dinnerAt = dinnerAt
            dialog.dismiss()
            callback.invoke()
        }

        dialog.show()
    }

    private fun createNewEvent(name: String, startDate: DateTime, endDate: DateTime,
                               relevantForDinner : Boolean, dinnerAt : DateTime?) {
        if (service.logInService.currentPerson != null) {
            service.presenceService.eventService.createEvent(
                service.logInService.currentPerson!!,
                name,
                startDate,
                endDate,
                relevantForDinner,
                dinnerAt
            )
        }
    }
}