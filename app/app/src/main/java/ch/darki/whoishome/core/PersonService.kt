package ch.darki.whoishome.core

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
}