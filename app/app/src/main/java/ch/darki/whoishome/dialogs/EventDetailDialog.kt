package ch.darki.whoishome.dialogs;

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ch.darki.whoishome.R
import org.joda.time.DateTime

class EventDetailDialog(private val context : Context) {

    var name: String? = null
        private set

    var date: DateTime? = null
        private set

    var startTime: DateTime? = null
        private set

    var endTime: DateTime? = null
          private set

    fun show(callback: (Boolean) -> Unit) {
        showEventDetails {
            if(!it) {
                callback.invoke(false)
                return@showEventDetails
            }
            if(isValid()) {
                callback.invoke(true)
            }
            else {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValid() : Boolean {
        return name != null && date != null && startTime != null && endTime != null
    }

    private fun showEventDetails(callback : (Boolean) -> Unit) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.new_event_dialog)

        val nameEt = dialog.findViewById<EditText>(R.id.event_name)
        val dateEt = dialog.findViewById<EditText>(R.id.date)
        val startTimeEt = dialog.findViewById<EditText>(R.id.start_time)
        val endTimeEt = dialog.findViewById<EditText>(R.id.end_time)

        val cancelButton = dialog.findViewById<Button>(R.id.back_button)
        val continueButton = dialog.findViewById<Button>(R.id.continueButton)

        var date: DateTime? = this.date
        var startTime: DateTime? = this.startTime
        var endTime: DateTime? = this.endTime

        nameEt.setText(this.name)
        dateEt.setText(this.date?.toString("dd.MM.yyyy"))
        startTimeEt.setText(this.startTime?.toString("HH:mm"))
        endTimeEt.setText(this.endTime?.toString("HH:mm"))

        dateEt.setOnClickListener {
            DatePicker(context) { d ->
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
            callback.invoke(false)
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
            callback.invoke(true)
        }

        dialog.show()
    }
}
