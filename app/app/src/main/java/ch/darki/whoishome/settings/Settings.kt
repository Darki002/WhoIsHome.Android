package ch.darki.whoishome.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ch.darki.whoishome.R
import ch.darki.whoishome.ServiceManager

class Settings : PreferenceFragmentCompat() {

    private lateinit var service: ServiceManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        service = activity?.applicationContext as ServiceManager
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}