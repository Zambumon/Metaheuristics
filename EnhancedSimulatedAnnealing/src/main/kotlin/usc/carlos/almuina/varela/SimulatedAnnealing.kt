package usc.carlos.almuina.varela

import java.util.*


data class SimulatedAnnealing(
        val initialSolution : Solution,
        val random: Random,
        val distances : Matrix,
        var initialTemperature : Double, //temperatura inicial
        var currentTemperature : Double,
        var cauchyCounter : Int // contador de enfriamiento

){
    fun calculoT0(){
       initialTemperature = (MU/-Math.log(PHI))*initialSolution.cost(distances)
    }

    fun cauchy() :Double{
        cauchyCounter++
        return cauchyCounter/100*initialTemperature/(1+Math.log(cauchyCounter.toDouble()))
    }


    fun go(): Solution{
        var bestSolution = Solution(initialSolution.itinerary.clone())
        var bestCost = bestSolution.cost(distances)
        var working = Solution(initialSolution.itinerary.clone())
        var acceptanceCounter = 0
        var candidateCounter = 0
        var bestIteration = 0
        cauchyCounter= 0
        var enfriamiento = 0
        calculoT0()
        currentTemperature = initialTemperature
        println("SOLUCION INICIAL:")
        working.print(distances)
        println("\tTEMPERATURA INICIAL: " + "%.6f".format(Locale.ENGLISH,currentTemperature))

        repeat(STOP_CRITERIA){
            if(it%100 == 0) {
                currentTemperature = initialTemperature
                working.swap(random.next(), random.next())
            }
            val r = random.next()
            val y = random.nextRaw()
            val candidate = working.getBestNeighbour(r, distances)
            val delta = candidate.first.cost(distances)-working.cost(distances)
            val exponential = Math.pow(Math.E, -delta/currentTemperature)
//            println("\nITERACION: ${it+1}")
//            println("\tINDICE CIUDAD: $r")
//            println("\tCIUDAD: ${working.itinerary[r]}")
//            println("\tINDICE INSERCION: ${candidate.second}")
//            candidate.first.print(distances)
//            println("\tDELTA: $delta")
//            println("\tTEMPERATURA: "+ "%.6f".format(Locale.ENGLISH,currentTemperature))
//            println("\tVALOR DE LA EXPONENCIAL: "+ "%.6f".format(Locale.ENGLISH,exponential))
            if(y < exponential || delta < 0){
//                println("\tSOLUCION CANDIDATA ACEPTADA")
                acceptanceCounter++
                working=candidate.first
                if(candidate.first.cost(distances) < bestCost){
                    bestCost = candidate.first.cost(distances)
                    bestSolution = candidate.first
                    bestIteration = it+1
                }
            }
            candidateCounter++
//            println("\tCANDIDATAS PROBADAS: $candidateCounter, ACEPTADAS: $acceptanceCounter")
            if(candidateCounter == CANDIDATE_LIMIT || acceptanceCounter == ACCEPTANCE_LIMIT) {
                currentTemperature = cauchy()
                candidateCounter=0
                acceptanceCounter=0
                enfriamiento++
//                println("\n============================\nENFRIAMIENTO: $enfriamiento\n============================\nTEMPERATURA: " + "%.6f".format(Locale.ENGLISH,currentTemperature))
            }

        }
        println("\n\nMEJOR SOLUCION: ")
        bestSolution.print(distances)
        println("\tITERACION: $bestIteration")
        println("\tmu = $MU, phi = $PHI")
        return bestSolution
    }






}