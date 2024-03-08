package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Jugador
import ar.edu.unsam.algo3.dto.JugadorDTO
import ar.edu.unsam.algo3.dto.toDTO
import ar.edu.unsam.algo3.service.JugadorService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class JugadorController {

    @Autowired
    lateinit var jugadorService: JugadorService

    @GetMapping("/jugadores")
    @Operation(summary = "Devuelve todas los jugadores")
    fun getFiguritas(): List<JugadorDTO> =
        jugadorService.getAllJugadores().map { it.toDTO() }

    @GetMapping("/jugadores/{id}")
    @Operation(summary = "Devuelve un jugador por id")
    fun getJugadorById(@PathVariable id: Int): JugadorDTO =
        jugadorService.getJugadorById(id).toDTO()
    
    @GetMapping("/jugadores/filterByName/{name}")
    @Operation(summary = "Devuelve los jugadores que coincidan con el título que viene por parámetro")
    fun getJugadoresByTitle(@PathVariable name: String): List<JugadorDTO> =
        jugadorService.getJugadoresByName(name).map { it.toDTO() }

    @PostMapping("/nuevoJugador")
    @Operation(summary = "Crea un nuevo jugador")
    fun create(@RequestBody jugadorBody: JugadorDTO): Jugador =
        jugadorService.create(jugadorBody)

    @PutMapping("/updateJugador")
    @Operation(summary = "Update de un jugador")
    fun update(@RequestBody jugadorBody: JugadorDTO): JugadorDTO =
        jugadorService.updateJugador(jugadorBody).toDTO()

    @DeleteMapping("/jugadores/{id}")
    @Operation(summary = "Borra un jugador")
    fun deleteActividad(@PathVariable id: Int) =
        jugadorService.deleteJugador(id)
}
