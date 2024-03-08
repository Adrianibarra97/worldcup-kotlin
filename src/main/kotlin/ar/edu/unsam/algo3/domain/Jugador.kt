package ar.edu.unsam.algo3.domain

import Posicion
import ar.edu.unsam.algo3.util.validadorDatos
import java.time.LocalDate
import java.time.Period

val RANGO_NUMERO_CAMISETA = 1..99
val MIN_ALTURA_JUGADOR_ALTO = 1.8
val RANGO_PESO_JUGADOR_LIGERO = 65.00..70.00
val MAX_COTIZACION_JUGADOR = 20000000.00
val RANGO_CAMISETA_JUGADOR_LEYENDA = 5..10
val MIN_ANTIGUEDAD_SELECCION_JUGADOR_LEYENDA = 10
val MAX_EDAD_JUGADOR_PROMESA = 22
val MAX_ANTIGUEDAD_SELECCION_JUGADOR_PROMESA = 2

class Jugador(
    var nombre:String,
    var apellido:String,
    var fechaNacimiento:LocalDate,
    var numeroCamiseta:Int,
    var seleccion:Seleccion,
    var anioDebut:Int,
    var altura:Double,
    var peso:Double,
    var posicion: Posicion,
    var cotizacion:Double,
    var esLider: Boolean = false
): Coleccionable() {

    init {
        validar()
    }

    fun validar() {
        this.validarNumeroCamiseta()
        validadorDatos.validarNumerosPositivos(anioDebut.toDouble(), altura, peso, cotizacion)
        validadorDatos.validarStringsNoVacios(nombre, apellido)
    }

    fun validarNumeroCamiseta(){
        if (numeroCamiseta !in RANGO_NUMERO_CAMISETA)
            throw Exception("Numero camiseta debe estar entre 1 y 99!")
    }

    fun cambiarSiEsLider() {
        esLider = !esLider
    }

    fun esLider(): Boolean = esLider

    fun pais(): String =
        seleccion.pais.take(3)

    fun valoracion(): Double = posicion.valoracion(this)

    fun edad(): Int =
        Period.between(fechaNacimiento, LocalDate.now()).years

    fun esAlto(): Boolean = altura >= MIN_ALTURA_JUGADOR_ALTO

    fun aniosEnLaSeleccion(): Int = LocalDate.now().year - anioDebut

    fun esDeSeleccionCampeona(): Boolean =
        seleccion.esCampeonaDelMundo()

    fun esLigero(): Boolean =
        peso in RANGO_PESO_JUGADOR_LIGERO
    
    fun esLeyenda(): Boolean =
        this.aniosEnLaSeleccion() > MIN_ANTIGUEDAD_SELECCION_JUGADOR_LEYENDA &&
        (cotizacion > MAX_COTIZACION_JUGADOR ||
            numeroCamiseta in RANGO_CAMISETA_JUGADOR_LEYENDA
        ) &&
        esLider
    
    fun esPromesa(): Boolean =
        this.edad() < MAX_EDAD_JUGADOR_PROMESA &&
        cotizacion <= MAX_COTIZACION_JUGADOR &&
        this.aniosEnLaSeleccion() < MAX_ANTIGUEDAD_SELECCION_JUGADOR_PROMESA
    
    override fun search(value: String): Boolean =
        validadorDatos.coincidenStringsParcialmente(value, nombre, apellido)
}