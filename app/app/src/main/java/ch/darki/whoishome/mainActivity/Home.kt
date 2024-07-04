package ch.darki.whoishome.mainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager
import ch.darki.whoishome.core.models.Person
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

        service.currentPerson.observe(viewLifecycleOwner) { person ->
            if (person != null) {
                showPresences(person)
            }
        }

        return fragment
    }

    private fun showPresences(loggedInPerson: Person) {
        service.presenceService.getPresenceListFrom(viewLifecycleOwner.lifecycleScope) {
            personPresences = it

            personPresences!!.forEach { p ->
                showPerson(p, loggedInPerson)
            }
        }
    }

    private fun showPerson(personPresence: PresenceService.PersonPresence, loggedInPerson: Person){
        val view = layoutInflater.inflate(R.layout.view_person_presence, null)

        view.setOnClickListener {
            val action = HomeDirections.actionHomeToPersonView(personPresence.person.email)
            NavHostFragment.findNavController(this).navigate(action)
        }

        view.findViewById<TextView>(R.id.personName).text = getDisplayName(personPresence.person, loggedInPerson)
        view.findViewById<TextView>(R.id.lastEventAt).text = getDinnerAtText(personPresence.dinnerAt, personPresence.isPresent)

        val imageView = view.findViewById<ImageView>(R.id.isPresent)
        val drawable = if(personPresence.isPresent){ presentColor } else{ absentColor }
        imageView.setImageResource(drawable)

        layout?.addView(view)
    }

    private fun getDisplayName(person : Person, loggedInPerson: Person) : String {
        if(loggedInPerson.email.lowercase() == person.email.lowercase()){
            return person.displayName + " (Du)"
        }
        return person.displayName
    }

    private fun getDinnerAtText(dinnerAt : DateTime?, isPresent : Boolean) : String{

        if(!isPresent){
            return "Nicht zuhause"
        }

        if(dinnerAt == null){
            return "Kein Event Heute"
        }
        return "ready für zNacht: ${dinnerAt.toString("HH:mm")}"
    }
}