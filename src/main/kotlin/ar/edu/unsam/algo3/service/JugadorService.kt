package ar.edu.unsam.algo3.service

import Posicion
import ar.edu.unsam.algo3.domain.ErrorAlEliminar
import ar.edu.unsam.algo3.domain.Jugador
import ar.edu.unsam.algo3.domain.NotFoundException
import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.dto.JugadorDTO
import ar.edu.unsam.algo3.dto.SeleccionDTO
import ar.edu.unsam.algo3.repository.RepositorioFiguritas
import ar.edu.unsam.algo3.repository.RepositorioJugadores
import arquero
import com.google.common.base.Ascii
import defensor
import delantero
import mediocampista
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class JugadorService() {

    @Autowired
    lateinit var jugadoresRepository: RepositorioJugadores

    @Autowired
    @Lazy
    lateinit var figuritasService: FiguritaService

    fun deleteJugador(id: Int) {
        val jugadorAEliminar: Jugador = jugadoresRepository.getById(id)
        if (figuritasService.figuritasContienenJugador(jugadorAEliminar))
            throw ErrorAlEliminar("Hay una figurita que contiene a este jugador")
        jugadoresRepository.delete(jugadoresRepository.getById(id))
    }

    fun updateJugador(jugadorDTO: JugadorDTO): Jugador  {
        val jugadorNuevo = Jugador(jugadorDTO.nombre,
            jugadorDTO.apellido,
            jugadorDTO.fechaNacimiento,
            jugadorDTO.numeroCamiseta,
            jugadorDTO.seleccion.toDomain(),
            jugadorDTO.anioDebut, jugadorDTO.altura,
            jugadorDTO.peso,
            obtenerInstanciaPosicion(jugadorDTO.posicion),
            jugadorDTO.cotizacion,
            jugadorDTO.esLider,
        )
        return jugadoresRepository.updateJugador(jugadorDTO.id,jugadorNuevo)
    }

    fun create(jugadorBody: JugadorDTO): Jugador {
        val nuevoJugador = Jugador(
            jugadorBody.nombre,
            jugadorBody.apellido,
            jugadorBody.fechaNacimiento,
            jugadorBody.numeroCamiseta,
            jugadorBody.seleccion.toDomain(),
            jugadorBody.anioDebut,
            jugadorBody.altura,
            jugadorBody.peso,
            obtenerInstanciaPosicion(jugadorBody.posicion),
            jugadorBody.cotizacion,
            jugadorBody.esLider
           )
        jugadoresRepository.create(nuevoJugador)
        return nuevoJugador
    }

    fun createJugador(jugador: Jugador): Jugador {
        if (jugadoresRepository.contains(jugador))
            jugadoresRepository.getById(jugador.id)
            
        jugadoresRepository.create(jugador)
        return jugadoresRepository.getLastItem()
    }

    fun getJugadoresByName(name: String): List<Jugador> {
        val nameLowerCase = Ascii.toLowerCase(name)
        return this.getAllJugadores().filter {
            Ascii.toLowerCase(it.apellido).contains(nameLowerCase) or
            Ascii.toLowerCase(it.nombre).contains(nameLowerCase)
        }
    }

    fun getAllJugadores(): List<Jugador> =
        jugadoresRepository.getAll()

    fun getJugadorById(id: Int): Jugador =
        jugadoresRepository.getById(id)

    fun SeleccionDTO.toDomain(): Seleccion =
        Seleccion(
            pais = this.pais,
            nombreConfederacion = this.nombreConfederacion,
            copasDelMundo = this.copasDelMundo
        )

    fun obtenerInstanciaPosicion(nombrePosicion: String): Posicion =
        when (nombrePosicion) {
            "Arquero" -> arquero
            "Defensor" -> defensor
            "Mediocampista" -> mediocampista
            "Delantero" -> delantero
            else -> throw IllegalArgumentException("Posición no reconocida: $nombrePosicion")
        }
}