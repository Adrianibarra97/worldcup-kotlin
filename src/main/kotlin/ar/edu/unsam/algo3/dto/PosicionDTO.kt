package ar.edu.unsam.algo3.dto

import Posicion
import ar.edu.unsam.algo3.domain.Jugador
import com.fasterxml.jackson.annotation.JsonIgnore

data class PosicionDTO(val valoracion: Double)

@JsonIgnore
fun Posicion.toDTO(jugador: Jugador) =
    PosicionDTO(
        this.valoracion(jugador)
    )