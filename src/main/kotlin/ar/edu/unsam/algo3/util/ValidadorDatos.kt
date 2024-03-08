package ar.edu.unsam.algo3.util

import ar.edu.unsam.algo3.domain.EmptyArgumentException
import ar.edu.unsam.algo3.domain.IncorrectArgumentException

object validadorDatos {

    fun esPar(numero: Int): Boolean = numero % 2 == 0

    fun coincidenStringsParcialmente(stringAValidar: String, vararg atributosStrings: String): Boolean =
        atributosStrings.any{ it.contains(stringAValidar, ignoreCase = true) }

    fun coincidenStringsExactamente(stringAValidar: String, vararg atributosStrings: String): Boolean =
        atributosStrings.any{ it.equals(stringAValidar, ignoreCase = true) }

    fun validarNumerosPositivos(vararg atributosNumericos: Double) {
        atributosNumericos.forEach{ this.validarNumeroPositivo(it) }
    }

    fun validarStringsNoVacios(vararg atributosStrings: String) {
        atributosStrings.forEach{ this.validarStringNoVacios(it) }
    }

    private fun validarNumeroPositivo(numero: Double) {
        if (numero <= 0.0)
            throw IncorrectArgumentException("No se puede tener un atributo del tipo numerico menor a 0!")
    }

    private fun validarStringNoVacios(string: String) {
        if (string.isEmpty())
            throw EmptyArgumentException("No se aceptan campos vacios!")
    }
}