package usc.carlos.almuina.varela

// Docs for mutableSetOf -> Hashset
data class TabooList(val list: MutableSet<Pair<Int, Int>> = mutableSetOf()) {
    //Once we fill the list, take the first element on it and add a new one.
    fun insert(P: Pair<Int, Int>) {
        if (list.size == TENANCY) list.remove(list.first())
        list.add(P)
    }

    fun check(P: Pair<Int, Int>): Boolean = list.contains(P)
    fun remove(P : Pair<Int,Int>){
        list.remove(P)
    }
    fun clear() = list.clear()
    fun stringify(): String = list.joinToString("\n") { "\t${it.first} ${it.second}" }


}