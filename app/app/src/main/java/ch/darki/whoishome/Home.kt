package ch.darki.whoishome

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import ch.darki.whoishome.core.PresenceService
import org.joda.time.DateTime

class Home : Fragment() {

    private var personPresences : List<PresenceService.PersonPresence>? = null
    private var layout : LinearLayout? = null

    private val presentColor = "#00ff22"
    private val absentColor = "#ff0000"

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
        val checkbox = view.findViewById<CheckBox>(R.id.isPresent)

        val color = if(personPresence.isPresent){ presentColor } else{ absentColor }
        checkbox.buttonTintList = ColorStateList.valueOf(Color.parseColor(color))

        layout?.addView(view)
    }
}