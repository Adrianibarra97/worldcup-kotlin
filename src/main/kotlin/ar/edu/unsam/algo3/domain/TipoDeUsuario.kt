package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.util.validadorDatos

val MIN_EDAD_TIPO_USUARIO_CAMBIANTE = 25

abstract class TipoDeUsuario {

    abstract fun quiereRegalar(figurita: Figurita): Boolean
}

class TipoDeUsuarioPar : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
		!(validadorDatos.esPar(figurita.numero) ||
			validadorDatos.esPar(figurita.jugador.numeroCamiseta) ||
			validadorDatos.esPar(figurita.jugador.seleccion.copasDelMundo))
}

class TipoDeUsuarioNacionalista(private val selecciones: List<Seleccion>) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
	  	!selecciones.contains(figurita.jugador.seleccion)
}

class TipoDeUsuarioConservador(val usuario: Usuario) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
		usuario.figuritasFaltantes().isEmpty() && (figurita.nivelImpresion == NivelImpresion.ALTA)
}

class TipoDeUsuarioFanatico(private val jugadorFavorito: Jugador) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
		!(figurita.jugador.esLeyenda() || 
			figurita.jugador == jugadorFavorito)
}

class TipoDeUsuarioDesprendido(val usuario: Usuario) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
		// usuario.estaEnRepetidas(figurita)
		true
}

class TipoDeUsuarioApostador : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
	    !(figurita.estaOnFire() or
			figurita.jugador.esPromesa())
}

class TipoDeUsuarioInteresado(val usuario: Usuario) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
	   	figurita !in this.repetidasPorValoracion(usuario)

	private fun repetidasPorValoracion(usuario: Usuario): List<Figurita> =
		this.repetidasDistintas(usuario).sortedByDescending{
			it.valoracion()
		}.take(5)

	private fun repetidasDistintas(usuario: Usuario): List<Figurita> =
		usuario.figuritasRepetidas().distinct()
}

class TipoDeUsuarioCambiante(val usuario: Usuario) : TipoDeUsuario() {

	override fun quiereRegalar(figurita: Figurita): Boolean =
		if (usuario.superaEnEdad(MIN_EDAD_TIPO_USUARIO_CAMBIANTE))
			TipoDeUsuarioConservador(usuario).quiereRegalar(figurita)
		else
			TipoDeUsuarioDesprendido(usuario).quiereRegalar(figurita)
}