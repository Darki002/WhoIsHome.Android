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

        view?.findViewById<TextView>(R.id.event_detail_title)?.text = event.eventName

        val date = getString(R.string.datum_1_s, event.date.toString("dd.MM.yyyy"))
        view?.findViewById<TextView>(R.id.event_detail_date)?.text = date

        val startTime = event.startTime.toString("HH:mm")
        val endTime = event.endTime.toString("HH:mm")
        val duration = getString(R.string.event_duration_format, startTime, endTime)
        view?.findViewById<TextView>(R.id.event_detail_duration)?.text = duration

        val dinnerDetailView = view?.findViewById<TextView>(R.id.dinner_detail_info)
        if(!event.relevantForDinner){
            dinnerDetailView?.text = getString(R.string.event_not_relevant_for_dinner)
        }
        else {
            val dinnerAt = event.dinnerAt?.toString("HH:mm")

            if (dinnerAt == null) {
                dinnerDetailView?.text = getString(R.string.not_at_home_for_dinner)
            }
            else{
                val dinnerInfo = getString(R.string.event_relevant_for_dinner, dinnerAt)
                dinnerDetailView?.text = dinnerInfo
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}