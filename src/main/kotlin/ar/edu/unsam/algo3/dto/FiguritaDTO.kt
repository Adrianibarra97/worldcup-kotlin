package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.NivelImpresion
import ar.edu.unsam.algo3.domain.Usuario

data class FiguritaDTO(
    val id: Int,
    val numero: Int,
    val nivelImpresion: NivelImpresion,
    val onFire: Boolean,
    val url: String,
    val jugador: JugadorDTO
)

data class FiguritaWithOwnerDTO(
    val id: Int,
    val numero: Int,
    val nivelImpresion: NivelImpresion,
    val jugador: JugadorDTO,
    val onFire: Boolean,
    val url: String,
    val cedidaPor: UsuarioCedidaODTO
)

data class FilterFigurineJSON(
   val esPromesa: Boolean,
   val esOnFire: Boolean,
   val valoracionMin: Int,
   val valoracionMax: Int
)

fun Figurita.toDTO() = 
    FiguritaDTO(
        id,
        numero,
        nivelImpresion,
        estaOnFire(),
        url,
        jugador.toDTO()
    )

fun Figurita.toDTO(usuario:Usuario) =
    FiguritaWithOwnerDTO(
        id,
        numero,
        nivelImpresion,
        jugador.toDTO(),
        onFire,
        url,
        usuario.toCedidaDTO()
    )