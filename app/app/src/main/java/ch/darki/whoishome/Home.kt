package ch.darki.whoishome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.core.Event
import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.PresenceService
import org.joda.time.DateTime

class Home : Fragment() {
    private var personPresences : List<PresenceService.PersonPresence>? = null
    private var layout : LinearLayout? = null
    private lateinit var service: ServiceManager

    private val presentColor = android.R.drawable.presence_online
    private val absentColor = android.R.drawable.presence_busy

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_home, container, false)

        layout = fragment.findViewById(R.id.home_presence_container)
        service = activity?.applicationContext as ServiceManager

        personPresences = service.presenceService.getPresenceListFrom(DateTime.now())
        personPresences!!.forEach { p ->
            showPerson(p)
        }

        return fragment
    }

    private fun showPerson(personPresence: PresenceService.PersonPresence){
        val view = layoutInflater.inflate(R.layout.person_presence, null)

        view.setOnClickListener {
            val action = HomeDirections.actionHomeToPersonView(personPresence.person.email)
            NavHostFragment.findNavController(this).navigate(action)
        }

        view.findViewById<TextView>(R.id.personName).text = getDisplayName(personPresence.person)

        view.findViewById<TextView>(R.id.lastEventAt).text = getLastEventText(personPresence.lastEvent)

        val imageView = view.findViewById<ImageView>(R.id.isPresent)
        val drawable = if(personPresence.isPresent){ presentColor } else{ absentColor }
        imageView.setImageResource(drawable)

        layout?.addView(view)
    }

    private fun getDisplayName(person : Person) : String {
        if(service.logInService.currentPerson?.email?.lowercase() == person.email.lowercase()){
            return person.displayName + " (Du)"
        }
        return person.displayName
    }

    private fun getLastEventText(event : Event?) : String{

        if(event == null){
            return "Kein Event mehr Heute"
        }
        if(event.hasEndDate()){
            return "Letztes Event endet: ${event.endDate?.toString("HH:mm")}"
        }
        else{
            return "Letztes Event startet: ${event.startDate.toString("HH:mm")}"
        }
    }
}