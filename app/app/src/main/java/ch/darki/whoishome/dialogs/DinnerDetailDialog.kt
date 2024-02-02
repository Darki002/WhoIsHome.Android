package ch.darki.whoishome.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import ch.darki.whoishome.R
import org.joda.time.DateTime

class DinnerDetailDialog(private val context : Context) {

    var relevantForDinner: Boolean = false
        private set

    var notAtHomeForDinner : Boolean = false
        private set

    var dinnerAt: DateTime? = null
        private set

    fun show(callback: (Boolean) -> Unit) {
        showDinnerDetails {
            callback.invoke(it)
        }
    }

    private fun showDinnerDetails(callback : (Boolean) -> Unit) {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.new_event_dinner_details_dialog)

        val relevantForDinnerCb = dialog.findViewById<CheckBox>(R.id.isRelevantForDinner)
        val notAtHomeForDinnerCb = dialog.findViewById<CheckBox>(R.id.notAteHomeForDinner)
        val dinnerAtEt = dialog.findViewById<EditText>(R.id.readyForDinnerAt)

        val backButton = dialog.findViewById<Button>(R.id.back_button)
        val createButton = dialog.findViewById<Button>(R.id.create_event)

        relevantForDinnerCb.isChecked = this.relevantForDinner
        notAtHomeForDinnerCb.isChecked = this.notAtHomeForDinner
        if(dinnerAt != null){
            dinnerAtEt.setText(this.dinnerAt!!.toString("HH:mm"))
        }

        backButton.setOnClickListener {
            relevantForDinner = relevantForDinnerCb.isChecked
            notAtHomeForDinner = notAtHomeForDinnerCb.isChecked

            dialog.dismiss()
            callback.invoke(false)
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

            if(!isValid()) {
                Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dialog.dismiss()
            callback.invoke(true)
        }

        dialog.show()
    }

    private fun isValid() : Boolean {

        if(!relevantForDinner && !notAtHomeForDinner && dinnerAt == null) {
            return true
        }

        if(relevantForDinner && dinnerAt != null) {
            return true
        }

        return relevantForDinner && notAtHomeForDinner
    }
}