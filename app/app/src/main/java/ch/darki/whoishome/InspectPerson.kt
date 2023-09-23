package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.PresenceService

class InspectPerson : Fragment() {

    var person : Person? = null
    private var todayEventsLayout : LinearLayout? = null
    private var thisWeekEventsLayout : LinearLayout? = null

    var todayEvents : List<Event>? = null
    var thisWeekEvents : List<Event>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.inspect_person, container, false)

        todayEventsLayout = fragment.findViewById(R.id.todayEvents)
        thisWeekEventsLayout = fragment.findViewById(R.id.thisWeekEvents)

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InspectPersonArgs = InspectPersonArgs.fromBundle(requireArguments())

        person = PresenceService.instance?.personService?.getPersonByEmail(args.email)
        setTitle(view, person?.displayName)

        val eventsForPerson = PresenceService.instance?.eventService?.getEventsForPersonByEmail(args.email)
        todayEvents = eventsForPerson?.today
        thisWeekEvents = eventsForPerson?.thisWeek


        showToday(todayEvents!!)
        showThisWeek(thisWeekEvents!!)
    }

    private fun setTitle(view : View, displayName : String?){
        view.findViewById<TextView>(R.id.person_inspect_name).text = displayName
    }

    private fun showToday(todayEvents : List<Event>){

        todayEvents.forEach (
            fun (e){
                val view = layoutInflater.inflate(R.layout.event_view, null)
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = "Heute"

                todayEventsLayout?.addView(view)
            }
        )
    }
    private fun showThisWeek(thisWeekEvents : List<Event>){
        thisWeekEvents.forEach (
            fun (e) {
                val view = layoutInflater.inflate(R.layout.event_view, null)
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = e.startDate.toString()

                todayEventsLayout?.addView(view)
            }
        )
    }
}