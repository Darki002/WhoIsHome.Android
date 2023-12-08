package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ch.darki.whoishome.core.Event

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

        service.presenceService.eventService.getEventById(args.eventId, viewLifecycleOwner.lifecycleScope) {fillExistingData(it, view)}
    }

    private fun fillExistingData(event : Event?, view : View){
        if(event == null){
            return
        }

        view.findViewById<TextView>(R.id.edit_event_name).text = event.eventName
        view.findViewById<EditText>(R.id.event_name_edit).setText(event.eventName)
        view.findViewById<EditText>(R.id.start_date_edit).setText(event.startDate.toString("dd.MM.yyyy HH:mm"))
        view.findViewById<EditText>(R.id.end_date_edit).setText(event.endDate.toString("dd.MM.yyyy HH:mm"))
        view.findViewById<CheckBox>(R.id.is_relevant_for_dinner_edit).isChecked = event.relevantForDinner
        view.findViewById<CheckBox>(R.id.not_at_home_for_dinner_edit).isChecked = event.relevantForDinner &&  event.dinnerAt != null
        if(event.dinnerAt != null){
            view.findViewById<EditText>(R.id.ready_for_dinner_at_edit).setText(event.dinnerAt.toString("dd.MM.yyyy HH:mm"))
        }
    }
}