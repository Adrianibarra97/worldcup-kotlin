package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Seleccion

data class SeleccionDTO(
	val id: Int,
	val pais: String,
	val nombreConfederacion: String,
	val copasDelMundo: Int
)

fun Seleccion.toDTO() =
	SeleccionDTO(
		id,
		pais,
		nombreConfederacion,
		copasDelMundo
	)