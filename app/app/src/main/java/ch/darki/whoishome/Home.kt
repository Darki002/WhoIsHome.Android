package ch.darki.whoishome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ch.darki.whoishome.core.PresenceService
import org.joda.time.DateTime

class Home : Fragment() {

    private var personPresences : List<PresenceService.PersonPresence>? = null
    private var layout : LinearLayout? = null

    private val presentColor = android.R.drawable.presence_online
    private val absentColor = android.R.drawable.presence_busy

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_home, container, false)

        layout = fragment.findViewById(R.id.home_presence_container)

        val presenceService = PresenceService()
        personPresences = presenceService.getPresenceListFrom(DateTime.now())
        personPresences!!.forEach { p ->
            showPerson(p)
        }

        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showPerson(personPresence: PresenceService.PersonPresence){
        val view = layoutInflater.inflate(R.layout.person_presence, null)

        view.findViewById<TextView>(R.id.personName).text = personPresence.person.fullName
        val checkbox = view.findViewById<ImageView>(R.id.isPresent)

        val drawable = if(personPresence.isPresent){ presentColor } else{ absentColor }
        checkbox.setImageResource(drawable)

        layout?.addView(view)
    }
}