package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ch.darki.whoishome.core.Event

class EventDetail : Fragment() {

    private lateinit var service: ServiceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.fragment_event_detail, container, false)
        service = activity?.applicationContext as ServiceManager

        val args: EventDetailArgs = EventDetailArgs.fromBundle(requireArguments())
        showDetails(args.eventId)

        return fragment
    }

    private fun showDetails(eventId: String){
        service.presenceService.eventService.getEventById(eventId, viewLifecycleOwner.lifecycleScope) {
            setUpDetailWindow(it)
        }
    }

    private fun setUpDetailWindow(event : Event?){
        if(event == null){
            return
        }

        // TODO: Set up the detail window
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}