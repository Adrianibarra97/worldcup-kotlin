package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Domicilio

data class DomicilioDTO(
    val ubicacionGeograficaX: Double,
    val ubicacionGeograficaY: Double,
    var calle: String = "",
    val altura: Int = 0,
    val localidad: String = "",
    val provincia: String = "",
)

fun Domicilio.toDTO() =
    DomicilioDTO(
        ubicacionGeograficaX,
        ubicacionGeograficaY,
        calle,
        altura,
        localidad,
        provincia
    )