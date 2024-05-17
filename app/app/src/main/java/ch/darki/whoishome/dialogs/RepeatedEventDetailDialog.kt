package ch.darki.whoishome.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ch.darki.whoishome.R
import org.joda.time.DateTime

class RepeatedEventDetailDialog(private val context : Context) {
    var name: String? = null
        private set

    var startTime: DateTime? = null
        private set

    var endTime: DateTime? = null
        private set

    var firstDay: DateTime? = null
        private set

    var lastDay: DateTime? = null
        private set

    fun show(callback: (Boolean) -> Unit){
        showRepeatedEventDetails { detailsSuccess ->
            if(!detailsSuccess){
                callback.invoke(false)
                return@showRepeatedEventDetails
            }
            if(isDetailsValid()){
                showRepeatedEventDates {datesSuccess ->
                    if(!datesSuccess){
                        show(callback)
                    }
                    if(isDatesValid()){
                        callback.invoke(true)
                    }
                    else{
                        Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isDetailsValid() : Boolean {
        return firstDay != null && lastDay != null
    }

    private fun isDatesValid() : Boolean{
        return firstDay != null && lastDay != null
    }

    private fun showRepeatedEventDetails(callback : (Boolean) -> Unit) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_new_repeted_event)

        val nameEt = dialog.findViewById<EditText>(R.id.event_name)
        val startTimeEt = dialog.findViewById<EditText>(R.id.start_time)
        val endTimeEt = dialog.findViewById<EditText>(R.id.end_time)

        val cancelButton = dialog.findViewById<Button>(R.id.back_button)
        val continueButton = dialog.findViewById<Button>(R.id.continueButton)

        var startTime: DateTime? = this.startTime
        var endTime: DateTime? = this.endTime

        nameEt.setText(this.name)
        startTimeEt.setText(this.startTime?.toString("HH:mm"))
        endTimeEt.setText(this.endTime?.toString("HH:mm"))

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
            callback.invoke(false)
        }
        continueButton.setOnClickListener {
            val name = nameEt.text.toString()

            if (name.isEmpty() || startTime == null || endTime == null) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.name = name
            this.startTime = startTime!!
            this.endTime = endTime!!
            dialog.dismiss()
            callback.invoke(true)
        }

        dialog.show()
    }

    private fun showRepeatedEventDates(callback : (Boolean) -> Unit) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_repeated_event_date)

        val firstDayEt = dialog.findViewById<EditText>(R.id.firstDay)
        val lastDayEt = dialog.findViewById<EditText>(R.id.lastDay)

        val backButton = dialog.findViewById<Button>(R.id.back_button)
        val continueButton = dialog.findViewById<Button>(R.id.continueButton)

        var firstDay: DateTime? = this.firstDay
        var lastDay: DateTime? = this.lastDay

        firstDayEt.setText(this.firstDay?.toString("dd.MM.yyyy"))
        lastDayEt.setText(this.lastDay?.toString("dd.MM.yyyy"))

        firstDayEt.setOnClickListener {
            DatePicker(context) {time ->
                firstDayEt.setText(time.toString("dd.MM.yyyy"))
                firstDay = time
            }.show()

        }

        lastDayEt.setOnClickListener {
            DatePicker(context) { time ->
                lastDayEt.setText(time.toString("dd.MM.yyyy"))
                lastDay = time
            }.show()
        }

        backButton.setOnClickListener {
            dialog.dismiss()
            callback.invoke(false)
        }
        continueButton.setOnClickListener {
            if (firstDay == null || lastDay == null) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.firstDay = firstDay!!
            this.lastDay = lastDay!!
            dialog.dismiss()
            callback.invoke(true)
        }

        dialog.show()
    }
}