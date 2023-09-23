package ch.darki.whoishome.core

import kotlin.jvm.optionals.getOrNull

class PersonService {
    var persons : List<Person>? = null
        private set

    fun loadAllPersons(){
        persons = listOf(
            Person("Llyn", "llyn.baumann@gmx.ch"),
            Person("Ryanne", "ryanne@gmx.ch"),
            Person("Jennifer", "jennifer.baumann@bluewin.ch"),
            Person("Ruth", "ruth.baum@bluewin.ch")
        )
    }

    fun getPersonByEmail(email: String): Person? {
        return persons?.stream()?.filter {
            p -> p.email == email
        }?.findFirst()?.getOrNull()
    }
}