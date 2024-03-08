package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Jugador
import java.time.LocalDate

data class JugadorDTO(
	val id: Int,
	val nombre: String,
	val apellido: String,
	val fechaNacimiento: LocalDate,
	val numeroCamiseta: Int,
	val seleccion: SeleccionDTO,
	val anioDebut: Int,
	val altura: Double,
	val peso: Double,
	val posicion: String,
	val valoracion: Double,
	val esLider: Boolean,
	val cotizacion: Double,
	val esPromesa: Boolean
)

fun Jugador.toDTO() =
	JugadorDTO(
		id,
		nombre,
		apellido,
		fechaNacimiento,
		numeroCamiseta,
		seleccion.toDTO(),
		anioDebut,
		altura,
		peso,
		posicion.simpleName(),
		posicion.valoracion(this),
		esLider(),
		cotizacion,
		esPromesa()
	)