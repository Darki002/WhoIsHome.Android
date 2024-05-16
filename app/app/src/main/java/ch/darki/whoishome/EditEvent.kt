package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.dialogs.DateTimePicker
import ch.darki.whoishome.dialogs.TimePicker
import org.joda.time.DateTime


class EditEvent : Fragment() {

    private lateinit var service: ServiceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.fragment_edit_event, container, false)
        service = activity?.applicationContext as ServiceManager

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: EditEventArgs = EditEventArgs.fromBundle(requireArguments())

        service.presenceService.eventService.getEventById(args.eventId, viewLifecycleOwner.lifecycleScope) {setUpEditWindow(it, view)}
    }

    private fun setUpEditWindow(event : Event?, view : View){
        if(event == null){
            return
        }

        view.findViewById<TextView>(R.id.edit_event_name).text = event.eventName
        val editEventName = view.findViewById<EditText>(R.id.event_name_edit)
        val editDate = view.findViewById<EditText>(R.id.date_edit)
        val editStartTime = view.findViewById<EditText>(R.id.start_time_edit)
        val editEndTime = view.findViewById<EditText>(R.id.end_time_edit)
        val editRelevantForDinner = view.findViewById<CheckBox>(R.id.is_relevant_for_dinner_edit)
        val editNotAtHome = view.findViewById<CheckBox>(R.id.not_at_home_for_dinner_edit)
        val editDinnerAt = view.findViewById<EditText>(R.id.ready_for_dinner_at_edit)

        var date : DateTime = event.date
        var startTime : DateTime = event.startTime
        var endTime : DateTime = event.endTime
        var dinnerAt : DateTime? = event.dinnerAt

        editEventName.setText(event.eventName)
        editDate.setText(event.date.toString("dd.MM.yyyy"))
        editStartTime.setText(event.startTime.toString("HH:mm"))
        editEndTime.setText(event.endTime.toString("HH:mm"))
        editRelevantForDinner.isChecked = event.relevantForDinner
        editNotAtHome.isChecked = event.relevantForDinner &&  event.dinnerAt == null
        editDinnerAt.setText(event.dinnerAt?.toString("HH:mm"))

        editDate.setOnClickListener {
            DateTimePicker(requireContext()) { d ->
                editDate.setText(d.toString("dd.MM.yyyy"))
                date = d
            }.show()
        }

        editStartTime.setOnClickListener {
            TimePicker(requireContext()) { time ->
                editStartTime.setText(time.toString("HH:mm"))
                startTime = time
            }.show()
        }

        editEndTime.setOnClickListener {
            TimePicker(requireContext()) { time ->
                editEndTime.setText(time.toString("HH:mm"))
                endTime = time
            }.show()
        }

        editDinnerAt.setOnClickListener {
            TimePicker(requireContext()){d ->
                editDinnerAt.setText(d.toString("HH:mm"))
                dinnerAt = d
            }.show()
        }

        if(event.dinnerAt != null){
            editDinnerAt.setText(event.dinnerAt.toString("HH:mm"))
        }

        view.findViewById<Button>(R.id.clear_ready_for_dinner).setOnClickListener {
            dinnerAt = null
            editDinnerAt.text.clear()
        }

        view.findViewById<Button>(R.id.cancel_edit_event).setOnClickListener {
            val action = EditEventDirections.actionEditEventViewToHome()
            NavHostFragment.findNavController(this).navigate(action)
        }

        view.findViewById<Button>(R.id.save_button).setOnClickListener {

            val relevantForDinner = editRelevantForDinner.isChecked
            val notAtHomeForDinner = editNotAtHome.isChecked

            if(!(relevantForDinner && notAtHomeForDinner)) {
                if(relevantForDinner && dinnerAt == null){
                    Toast.makeText(context, "Nicht genug Informationen", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            updateEvent(editEventName.text.toString(), date, startTime, endTime, relevantForDinner, dinnerAt, notAtHomeForDinner, event.id)
            Toast.makeText(context, "Event geupdated", Toast.LENGTH_SHORT).show()
            val action = EditEventDirections.actionEditEventViewToHome()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun updateEvent(name: String, date: DateTime, startTime: DateTime, endTime: DateTime,
                            relevantForDinner : Boolean, dinnerAt : DateTime?, notAtHomeForDinner : Boolean, eventId : String) {
        val person = service.currentPerson ?: return

        if(notAtHomeForDinner){
            service.presenceService.eventService.update(
                Event(
                    person,
                    name,
                    date,
                    startTime,
                    endTime,
                    true,
                    null,
                    eventId
                )
            )
            return
        }

        service.presenceService.eventService.update(
            Event(
                person,
                name,
                date,
                startTime,
                endTime,
                relevantForDinner,
                dinnerAt,
                eventId
            )
        )
    }
}