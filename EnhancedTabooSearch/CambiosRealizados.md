# Cambios realizados para la optimización de la búsqueda Taboo.

El primer cambio que se aplicó fue la inicialización voraz, con la que obtenemos 4950km de distancia. Sin embargo, tras su aplicación, se observó que a partir de la tercera iteración el algoritmo de búsqueda se quedaba estacado en unos 4500 kilómetros.

Para evitar que la búsqueda quedase así, se aplicaron los siguientes cambios:

- Por un lado, se aplicaron cambios en la cantidad de iteraciones que se podría estar con resultados peores y se aumentó a 150
- Adicionalmente, se aumentó el tamaño de la lista Taboo a 1000. Previamente, se probó con otros tamaños (como 200, 2000, 500).
- Además, se determinó que la lista taboo no fuera limpiada hasta que se realizasen 15 resets. Dicho esto, con más iteraciones (como 25000) este número se puedo aumentar hasta a 20 y 30, obteniéndose resultados más prometedores.
- Finalmente, posiblemente el cambio más importante fue la aplicación de cambios en la función de generación de nuevos vecinos. Como era de esperar, si estamos solamente cambiando un par de cada, la búsqueda se quedaba muy cerca de la solución voraz y no se conseguían mejora alguna. Se optó por:
    - La inversión en orden de todos los pares entre dos seleccionados, cunado la suma del par de números propuesto en la generación fuese par. El motivo de que se aplicase en unos casos un algoritmo y otro era para que se obtuviesen más soluciones posibles de las que se obtendrían si siempre aplicásemos el mismo algoritmo.
    - `    (parteInicial + (parteCentral.inversión() + parteFinal).inversión())`
    - También se realizaron pruebas con otros tipos de inversiones, pero estas dos fueron las que dieron resultados mejores y más consistentes.
    
La aplicación de estas estrategías llevó a la obtención de soluciones que normalmente rondan los 2350Km, si bien puntualmente se encontraron soluciones cercanas a los 2280Km.
    
    
Por otro lado, se mejoró la velocidad de iteraciones mediante la introducción de técnicas de paralelismo a la hora de generar la lista de soluciones.
    
    
```
MEJOR SOLUCION: 
	RECORRIDO: 93 13 76 81 66 80 38 14 91 78 15 31 72 45 40 75 43 84 92 49 32 57 36 26 73 16 64 27 39 89 23 68 17 94 79 71 34 67 46 37 42 74 18 47 25 22 82 59 77 21 86 12 53 3 52 88 99 61 97 87 55 7 62 5 90 41 60 6 35 1 28 63 10 58 69 20 9 50 70 56 85 54 95 33 8 96 48 19 51 30 24 4 11 65 2 29 83 44 98 
	COSTE (km): 2278
	ITERACION: 1540
```