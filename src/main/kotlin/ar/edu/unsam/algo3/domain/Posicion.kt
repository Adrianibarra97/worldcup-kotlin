
import ar.edu.unsam.algo3.domain.Jugador

val VALORACION_BASE_ARQUERO = 100.00
val VALORACION_BASE_DEFENSOR = 50.00
val VALORACION_BASE_MEDIOCAMPISTA = 150.00
val VALORACION_BASE_DELANTERO = 200.00

abstract class Posicion() {

    abstract fun valoracionBase(): Double

    abstract fun valoracion(jugador: Jugador): Double

    abstract fun simpleName():String
}

object arquero : Posicion() {

    override fun valoracionBase() = VALORACION_BASE_ARQUERO

    override fun valoracion(jugador: Jugador): Double =
        if (jugador.esAlto()) valoracionBase() * jugador.altura
        else valoracionBase()

    override fun simpleName(): String = "Arquero"
}

object defensor : Posicion() {

    override fun valoracionBase() = VALORACION_BASE_DEFENSOR

    override fun valoracion(jugador: Jugador): Double =
        if (jugador.esLider()) valoracionBase() + (jugador.aniosEnLaSeleccion() * 10)
        else valoracionBase()

    override fun simpleName(): String = "Defensor"
}

object mediocampista : Posicion() {

    override fun valoracionBase() = VALORACION_BASE_MEDIOCAMPISTA

    override fun valoracion(jugador: Jugador): Double =
        if (jugador.esLigero()) valoracionBase() + jugador.peso
        else valoracionBase()

    override fun simpleName(): String = "Mediocampista"
}

object delantero : Posicion() {

    override fun valoracionBase() = VALORACION_BASE_DELANTERO

    override fun valoracion(jugador: Jugador): Double =
        if (jugador.esDeSeleccionCampeona()) valoracionBase() + (jugador.numeroCamiseta * 10)
        else valoracionBase()

    override fun simpleName(): String = "Delantero"
}

class PosicionPolivalentes(private var posiciones: MutableSet<Posicion>) : Posicion() {

    override fun valoracionBase() =
        posiciones.map { it.valoracionBase() }.average()

    override fun valoracion(jugador: Jugador): Double =
        valoracionBase() + 
        if(jugador.esLeyenda() || jugador.esPromesa())
            promedioValoraciones(jugador) - jugador.edad()
        else 0.0
    
    override fun simpleName(): String = ""
    
    fun tienePosiciones(vararg _posiciones: Posicion) =
        _posiciones.filter { posiciones.contains(it) }

    fun promedioValoraciones(jugador:Jugador) =
        posiciones.map { it.valoracion(jugador) }.average()

    fun agregarPosicion(vararg _posiciones: Posicion) {

        if(tienePosiciones(*_posiciones).isNotEmpty())
            throw Exception("alguna de las posiciones ya esta agregada!")

        _posiciones.forEach { posiciones.add(it) }
    }

    fun eliminarPosicion(vararg _posiciones: Posicion){
        _posiciones.forEach { posiciones.remove(it) }
    }
}
