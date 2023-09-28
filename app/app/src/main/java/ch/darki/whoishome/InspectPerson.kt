package ch.darki.whoishome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.core.Person


class InspectPerson : Fragment() {

    private lateinit var service: ServiceManager

    private var person : Person? = null
    private var todayEventsLayout : LinearLayout? = null
    private var thisWeekEventsLayout : LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragment = inflater.inflate(R.layout.inspect_person, container, false)
        service = activity?.applicationContext as ServiceManager

        todayEventsLayout = fragment.findViewById(R.id.todayEvents)
        thisWeekEventsLayout = fragment.findViewById(R.id.thisWeekEvents)

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: InspectPersonArgs = InspectPersonArgs.fromBundle(requireArguments())

        person = service.presenceService.personService.getPersonByEmail(args.email)
        setTitle(view, person?.displayName)

        val eventsForPerson = service.presenceService.eventService.getEventsForPersonByEmail(args.email)
        val todayEvents = eventsForPerson.today
        val thisWeekEvents = eventsForPerson.thisWeek


        showToday(todayEvents)
        showThisWeek(thisWeekEvents)
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

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {

                    if(person?.email == e.person.email){
                        service.presenceService.eventService.deleteEvent(e.id)
                        (view.parent as ViewManager).removeView(view)
                    }
                    else{
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                }

                todayEventsLayout?.addView(view)
            }
        )
    }
    private fun showThisWeek(thisWeekEvents : List<Event>){
        thisWeekEvents.forEach (
            fun (e) {
                val view = layoutInflater.inflate(R.layout.event_view, null)
                view.id = e.id
                view.findViewById<TextView>(R.id.eventName).text = e.eventName
                view.findViewById<TextView>(R.id.date).text = e.startDate.toString()

                view.findViewById<Button>(R.id.deleteEvent).setOnClickListener {

                    if(person?.email != e.person.email){
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        service.presenceService.eventService.deleteEvent(e.id)
                        (view.parent as ViewManager).removeView(view)
                    }
                }

                thisWeekEventsLayout?.addView(view)
            }
        )
    }
}