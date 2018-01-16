# Metaheurística

## Práctica 4.1

Resolución del problema del viajante mediante la implementación de computación Evolutiva escrito en el lenguaje de programación Kotlin.

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
java -jar build/libs/Evolutive-1.0.jar
```
Uso del programa

El programa debe recibir los argumentos del programa en el siguiente orden: matriz de distancias, números aleatorios. En caso de no especificar argumento alguno, se utilizará el archivo de distancias por defecto y se generarán números aleatorios.

Los ficheros usados para la práctica se encuentran en el directorio "res", de forma que podríamos ejecutar el programa con:
```
./gradlew run -Pargs='res/Taboo/distances.txt res/Taboo/random1.txt'
```
Alternativamente. Es recomendable utilizar esta opción para la comparación con la traza:
```
java -jar build/libs/Evolutive-1.0.jar res/distancias_ce_10_2016.txt res/aleatorios_ce_2016.txt > res/test1.txt
diff res/test1.txt res/traza1.txt
```