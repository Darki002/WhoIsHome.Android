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
    private var date: DateTime? = null
    private var startTime: DateTime? = null
    private var endTime: DateTime? = null
    private var relevantForDinner: Boolean = false
    private var notAtHomeForDinner : Boolean = false
    private var dinnerAt: DateTime? = null

    fun show(fill : Boolean = false) {
        showEventDetails(fill) {
            showDinnerDetails(fill, { ok() }, { back() })
        }
    }

    private fun ok(){
        if(name != null && date != null && startTime != null && endTime != null){
            createNewEvent(name!!, date!!, startTime!!, endTime!!, relevantForDinner, dinnerAt, notAtHomeForDinner ){
                if(it){
                    Toast.makeText(context, "Event erstellt", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
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
        val dateEt = dialog.findViewById<EditText>(R.id.date)
        val startTimeEt = dialog.findViewById<EditText>(R.id.start_time)
        val endTimeEt = dialog.findViewById<EditText>(R.id.end_time)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_create_event)
        val continueButton = dialog.findViewById<Button>(R.id.continueButton)

        var date: DateTime? = this.date
        var startTime: DateTime? = this.startTime
        var endTime: DateTime? = this.endTime

        if(fill){
            nameEt.setText(this.name)
            dateEt.setText(this.date?.toString("dd.MM.yyyy"))
            startTimeEt.setText(this.startTime?.toString("HH:mm"))
            endTimeEt.setText(this.endTime?.toString("HH:mm"))
        }

        dateEt.setOnClickListener {
            DateTimePicker(context) { d ->
                dateEt.setText(d.toString("dd.MM.yyyy"))
                date = d
            }.show()
        }

        startTimeEt.setOnClickListener {
            TimePicker(context) {time ->
                startTimeEt.setText(time.toString("HH:mm"))
                startTime = time
            }.show()

        }

        endTimeEt.setOnClickListener {
            TimePicker(context) { time ->
                endTimeEt.setText(time.toString("HH:mm"))
                endTime = time
            }.show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        continueButton.setOnClickListener {
            val name = nameEt.text.toString()

            if (name.isEmpty() || date == null || startTime == null || endTime == null) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.name = name
            this.date = date!!
            this.startTime = startTime!!
            this.endTime = endTime!!
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
        val notAtHomeForDinnerCb = dialog.findViewById<CheckBox>(R.id.notAteHomeForDinner)
        val dinnerAtEt = dialog.findViewById<EditText>(R.id.readyForDinnerAt)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_create_event)
        val createButton = dialog.findViewById<Button>(R.id.create_event)

        var relevantForDinner : Boolean
        var notAtHomeForDinner : Boolean
        var dinnerAt:  DateTime? = null

        if(fill){
            relevantForDinnerCb.isChecked = this.relevantForDinner
            notAtHomeForDinnerCb.isChecked = this.notAtHomeForDinner
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
            notAtHomeForDinner = notAtHomeForDinnerCb.isChecked

            if(!(relevantForDinner && notAtHomeForDinner)) {
                if(relevantForDinner && dinnerAt == null){
                    Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            this.relevantForDinner = relevantForDinner
            this.dinnerAt = dinnerAt
            this.notAtHomeForDinner = notAtHomeForDinner
            dialog.dismiss()
            callback.invoke()
        }

        dialog.show()
    }

    private fun createNewEvent(name: String, date: DateTime, startTime: DateTime, endTime: DateTime,
                               relevantForDinner : Boolean, dinnerAt : DateTime?, notAtHomeForDinner : Boolean,
                               callback: (Boolean) -> Unit) {
        if (service.currentPerson != null) {

            if(notAtHomeForDinner){
                service.presenceService.eventService.createEvent(
                    service.currentPerson!!,
                    name,
                    date,
                    startTime,
                    endTime,
                    true,
                    null
                ){
                    callback.invoke(it)
                }
                return
            }

            service.presenceService.eventService.createEvent(
                service.currentPerson!!,
                name,
                date,
                startTime,
                endTime,
                relevantForDinner,
                dinnerAt
            ){
                callback.invoke(it)
            }
        }
    }
}