package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.util.validadorDatos

val VALOR_DE_PISO = 100.00
val MULTIPLICADOR_ON_FIRE = 1.2 //Equivalente a +20%
val MULTIPLICADOR_NUMERO_PARIDAD = 1.1 //Equivalente a +10%
val MULTIPLICADOR_NIVEL_IMPRESION = 0.85 //Equivalente a -15%

enum class NivelImpresion(val multiplicador: Double) {
    BAJA(1.0),
    MEDIA(MULTIPLICADOR_NIVEL_IMPRESION),
    ALTA(MULTIPLICADOR_NIVEL_IMPRESION)
}

class Figurita(
    var numero: Int,
    var nivelImpresion: NivelImpresion,
    var jugador: Jugador,
    var onFire: Boolean = false,
    var url: String = ""
): Coleccionable() {

    init {
        validadorDatos.validarNumerosPositivos(numero.toDouble())
    }

    fun cambiarEstadoOnFire() {
        onFire = !onFire
    }

    fun estaOnFire(): Boolean = onFire

    fun valoracionPorEstarOnFire(): Double =
        if (onFire) MULTIPLICADOR_ON_FIRE
        else 1.0

    fun valoracionPorSerNumeroPar(): Double = 
        if (validadorDatos.esPar(numero)) MULTIPLICADOR_NUMERO_PARIDAD
        else 1.0

    fun valoracionPorNivelImpresion(): Double = nivelImpresion.multiplicador
    
    fun valoracionBase(): Double =
        VALOR_DE_PISO * 
        valoracionPorEstarOnFire() * 
        valoracionPorSerNumeroPar() * 
        valoracionPorNivelImpresion()

    fun valoracionJugador(): Double = jugador.valoracion()

    fun valoracion(): Double = this.valoracionBase() + this.valoracionJugador()

    override fun search(value: String): Boolean =
        validadorDatos.coincidenStringsParcialmente(value, jugador.nombre, jugador.apellido)
        || validadorDatos.coincidenStringsExactamente(value, numero.toString(), jugador.pais())
}