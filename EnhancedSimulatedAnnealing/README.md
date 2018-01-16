# Metaheurística

## Práctica 3.1

Resolución del problema del viajante mediante la implementación de la búsqueda Temple Simulado escrito en el lenguaje de programación Kotlin.

Compilación y ejecución del programa

Para la compilación del programa será necesario como requisitos previos disponer una conexión a Internet y el JDK 8 instalado.

La compilación se realiza usando la herramienta de gestión de dependencias Gradle, para compilar con la herramienta debemos situarnos en la carpeta del proyecto y ejecutar el siguiente comando:
```
./gradlew build
```
O bien, en caso de que nos encontremos en un ordenador de la USC, será necesario añadir los parámetros:
```
./gradlew build -Dhttps.proxyHost=proxy2.usc.es -Dhttps.proxyPort=8080
```
Una vez el programa ha sido compilado, podemos ejecutarlo con:
```
./gradlew run
```
O bien, en caso de que nos encontremos en un ordenador de la USC:
```
./gradlew run -Dhttps.proxyHost=proxy2.usc.es -Dhttps.proxyPort=8080
```
Alternativamente, podríamos utilizar el Jar generado realizando. Es recomendable utilizar esta opción para la comparación con la traza:
```
java -jar build/libs/SimulatedAnnealing-1.0.jar
```
Uso del programa

El programa debe recibir los argumentos del programa en el siguiente orden: matriz de distancias, números aleatorios. En caso de no especificar argumento alguno, se utilizará el archivo de distancias por defecto y se generarán números aleatorios.

Los ficheros usados para la práctica se encuentran en el directorio "res", de forma que podríamos ejecutar el programa con:
```
./gradlew run -Pargs='res/Taboo/distances.txt res/Taboo/random1.txt'
```
Alternativamente. Es recomendable utilizar esta opción para la comparación con la traza:
```
java -jar build/libs/SimulatedAnnealing-1.0.jar res/distancias_sa_100_2017.txt res/aleatorios_sa_2017_caso1.txt > res/test1.txt
diff res/test1.txt res/traza1.txt
```

---

# Cambios realizados
- Se comenzó realizando modificaciones en los parámetros mu y phi, pero no se detectaron mejoras con respecto a los valores proporcionadas en el enunciado. 
- Se ha realizado una inicialización voraz. Permite obtener ya una solución bastante buena desde la primera iteración, aunque al realizar esta operación es bastante fácil estancarse en las primeras iteraciones, por lo que debemos buscar estrategias para diversificar. Entre estas estrategias destacarían:
  - Reducción del número de soluciones candidatas, evitando así la intensificación
  - Modificación en la función de generación de vecinos: se ha optado por modificar la función y utilizar una que realice cambios por bloques de ciudades. De esta forma, se aumenta la diversidad y se obtienen mejores soluciones
  - Modificación en la función usada para el enfriamiento ha sido modificada y se ha reemplazado por: `T=k/100*T_0/(log(k)+1)`, dicha velocidad así es inferior y permite aumentar la diversidad.