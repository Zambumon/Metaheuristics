package carlos.almuina

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

data class Individual(
        val solution: Solution
) {
    fun toString(matrix: Matrix, id : Int): String {
        return "INDIVIDUO $id = {${solution.toString(matrix)}}"
    }
}


data class Evolutive(
        val random: Random,
        val distances: Matrix,
        var population: ArrayList<Individual>
) {
    private fun populate() {
        repeat(POPULATION - GREEDY_START) { population.add(it, Individual(Solution.init(random, distances))) }
        repeat(GREEDY_START) { population.add(it + GREEDY_START, Individual(Solution.greedyInit(distances, random.next() + 1))) }
        repeat(POPULATION){ println(population[it].toString(distances, it)) }
    }

    fun tournament(): ArrayList<Individual> {
        val torneo = ArrayList<Individual>(POPULATION - TOURNAMENT_SIZE)
        repeat(POPULATION - TOURNAMENT_SIZE) {
            val r1= random.nextChallenger()
            val r2 = random.nextChallenger()
            val challenger1 = Individual(Solution(population[r1].solution.itinerary.clone()))
            val challenger2 = Individual(Solution(population[r2].solution.itinerary.clone()))
            var win = -1
            if (cost(distances, challenger1.solution) <= cost(distances, challenger2.solution)) {
                torneo.add(it, challenger1)
                win = r1
            } else {
                torneo.add(it, challenger2)
                win = r2
            }
            println("\tTORNEO $it: $r1 $r2 GANA $win")
        }
        return torneo
    }

    fun crossover(participants: ArrayList<Individual>) {
//        println("\tTEMPERATURA: "+ "%.6f".format(Locale.ENGLISH,currentTemperature))
        (0 until POPULATION - TOURNAMENT_SIZE)
                .filter { it % 2 == 0 }
                .forEach {
                    val r = random.nextRaw()
                    val p1 = participants[it]
                    val p2 = participants[it + 1]
                    println("\tCRUCE: ($it, ${it + 1}) (ALEATORIO: " + "%.6f".format(Locale.ENGLISH, r) + ")")
                    println("\t\tPADRE: = {${p1.solution.toString(distances)}}")
                    println("\t\tPADRE: = {${p2.solution.toString(distances)}}")
                    if (r <= CROSSOVER_PROBABILITY) {
                        val cut1 = random.next()
                        val cut2 = random.next()
                        println("\t\tCORTES: ($cut1, $cut2)")
                        //cut1 < cut2
                        val lower = min(cut1, cut2)
                        val upper = max(cut1, cut2)

                        val subset1 = ArrayList<Int>(p1.solution.itinerary.toList().subList(lower,upper+1))
                        val subset2 = ArrayList<Int>(p2.solution.itinerary.toList().subList(lower, upper+1))
                        val add1 = ArrayList<Int>()
                        val add2 = ArrayList<Int>()

                        repeat(CITIES) {
                            val location = (upper+1 + it) % CITIES
                            if (!subset1.contains(p2.solution.itinerary[location])) {
                                add1.add(p2.solution.itinerary[location])
                            }
                            if (!subset2.contains(p1.solution.itinerary[location])) {
                                add2.add(p1.solution.itinerary[location])
                            }
                        }

                        var location = upper + 1
                        var end = true

                        repeat(add1.size){
                            if(location>= CITIES){
                                location %= CITIES
                                end = false
                            }
                            if(end){
                                subset1.add(add1[it])
                                subset2.add(add2[it])
                            }else{
                                subset1.add(location, add1[it])
                                subset2.add(location, add2[it])
                            }
                            location++
                        }
                        repeat(CITIES){
                            p1.solution.itinerary[it] = subset1[it]
                            p2.solution.itinerary[it] = subset2[it]
                        }


                        println("\t\tHIJO: = {${p1.solution.toString(distances)}}")
                        println("\t\tHIJO: = {${p2.solution.toString(distances)}}")
                    } else {
                        println("\t\tNO SE CRUZA")
                    }
                    println()
                }

    }


    fun mutate(participants: ArrayList<Individual>) {
        repeat(POPULATION - TOURNAMENT_SIZE){
            println("\tINDIVIDUO $it")
            val individuo = participants[it]
            println("\tRECORRIDO ANTES: ${individuo.solution.recorrido()}")
            repeat(CITIES){
                val r = random.nextRaw()
                print("\t\tPOSICION: $it (ALEATORIO " + "%.6f".format(Locale.ENGLISH, r) + ") ")
                if(r > MUTATION_PROBABILITY)
                    println("NO MUTA")
                else{
                    val cross = random.next()
                    println("INTERCAMBIO CON: $cross")
                    val temp1 = individuo.solution.itinerary[it]
                    val temp2 = individuo.solution.itinerary[cross]
                    individuo.solution.itinerary[it] = temp2
                    individuo.solution.itinerary[cross] = temp1
                }
            }
            println("\tRECORRIDO DESPUES: ${individuo.solution.recorrido()}\n")
        }
    }

    fun replace(participants: ArrayList<Individual>, best : ArrayList<Individual>, bestEver : Solution, currentIt: Int, bestIt : Int) : Pair<Solution, Int> {
        participants.sortBy { individual -> cost(distances, individual.solution) }
        best.sortBy { individual -> cost(distances, individual.solution) }
        var b = bestIt
        var bes = bestEver

        if(cost(distances, participants[0].solution) < cost(distances, bestEver)) {
            bes = Solution(participants[0].solution.itinerary.clone())
            b=currentIt
        }
        repeat(TOURNAMENT_SIZE){
            participants.add(it, best[best.size-1-it])
        }

        repeat(POPULATION){ println(participants[it].toString(distances, it)) }

        return Pair(bes, b)
    }

    fun getBestTwo(): ArrayList<Individual> {
        val best = ArrayList<Individual>(2)
        val costs = ArrayList<Int>(2)
        repeat(2) {
            best.add(it, population[it])
            costs.add(it, cost(distances, population[it].solution))
        }

        for (i in 2 until POPULATION) {
            val c = cost(distances, population[i].solution)
            if (c < costs[0]) {
                best[0] = population[i]
                costs[0] = c
            } else if (c < costs[1]) {
                best[1] = population[i]
                costs[1] = c
            }
        }
        return best
    }

    fun go() {
        println("POBLACION INICIAL")
        populate()
        var p = Pair<Solution, Int>(Solution(population[0].solution.itinerary.clone()), 0)
        for(i in 0 until STOP_CRITERIA) {
            println("\nITERACION: ${i+1}, SELECCION")
            val bestTwo = getBestTwo()
            val torneo = tournament()
            println("\nITERACION: ${i+1}, CRUCE ")
            crossover(torneo)
            println("ITERACION: ${i+1}, MUTACION")
            mutate(torneo)
            println("\nITERACION: ${i+1}, REEMPLAZO")
            p = replace(torneo, bestTwo, p.first, i+1, p.second)
            population = torneo
        }
        println("\n\nMEJOR SOLUCION: ")
        println("RECORRIDO: ${p.first.recorrido()}")
        println("FUNCION OBJETIVO (km): ${cost(distances, p.first)}")
        println("ITERACION: ${p.second}")
    }

    companion object {
        fun init(random: Random, matrix: Matrix): Evolutive {
            return Evolutive(random, matrix, ArrayList<Individual>(POPULATION))
        }
    }
}