package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.service.FiguritaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class FiguritaController {

    @Autowired
    lateinit var figuritaService: FiguritaService

    @GetMapping("/figuritas")
    @Operation(summary = "Devuelve todas las figuritas")
    fun getFiguritas(): List<FiguritaDTO> =
        figuritaService.getAllFiguritas().map { it.toDTO() }

    @GetMapping("/figuritasProperties/{id}")
    @Operation(summary = "Devuelve una figurita por id con el dueño de esa figurita")
    fun getFiguritaById(@PathVariable id: Int): FiguritaDTO =
        figuritaService.getFiguritaById(id).toDTO()
    
    @GetMapping("/figuritas/{id}")
    @Operation(summary = "Devuelve una figurita por id con el dueño de esa figurita")
    fun getFiguritaByIdAndOwner(@PathVariable id: Int): FiguritaWithOwnerDTO =
        figuritaService.getFiguritaByIdAndOwner(id)

    @GetMapping("/figuritas/solicitarFigurita/{idUsuario}/{idFigurita}")
    @Operation(summary = "Usuario solicita una figurita de otro usuario")
    fun solicitarFigurita(@PathVariable idUsuario: Int, @PathVariable idFigurita: Int) =
        figuritaService.requestFigurineFromUser(idUsuario, idFigurita)

    @GetMapping("/figuritas/agregarARepetidas/{idUsuario}/{idFigurita}")
    @Operation(summary = "Usuario solicita una figurita de otro usuario")
    fun agregarFiguritaARepetidas(@PathVariable idUsuario: Int, @PathVariable idFigurita: Int) =
        figuritaService.requestRepeatedFigurineFromUser(idUsuario, idFigurita)

    @GetMapping("/figuritas/agregarAFaltantes/{idUsuario}/{idFigurita}")
    @Operation(summary = "Usuario solicita una figurita de otro usuario")
    fun agregarFiguritaAFaltantes(@PathVariable idUsuario: Int, @PathVariable idFigurita: Int) =
        figuritaService.requestMissingFigurineFromUser(idUsuario, idFigurita)

    @PostMapping("/figuritas/filterByName/{idUser}/{name}")
    @Operation(summary = "Devuelve las figuritas que coincidan con el titulo que viene por parámetro")
    fun getFiguritasByName(@RequestBody filter: FilterFigurineJSON, @PathVariable idUser: Int, @PathVariable name: String): List<FiguritaWithOwnerDTO> =
        figuritaService.getFiguritasByName(filter, name, idUser).map { (figurita, usuario) -> figurita.toDTO(usuario) }

    @PostMapping("/figuritas/filterActivated/{idUser}")
    @Operation(summary = "Devuelve las figuritas filtradas")
    fun getFiguritasByFilter(@RequestBody filter: FilterFigurineJSON, @PathVariable idUser: Int): List<FiguritaWithOwnerDTO> =
        figuritaService.getFiguritasByFilter(idUser, filter).map { (figurita, usuario) -> figurita.toDTO(usuario) }

    @GetMapping("/figuritas/faltantes/{id}")
    @Operation(summary = "Devuelve figuritas faltantes del usuario, que estan repetidas y cedidas por otros usuarios")
    fun getFigurita(@PathVariable id: Int): List<FiguritaWithOwnerDTO> =
        figuritaService.getFiguritaFaltanteById(id).map { (figurita, usuario) ->  figurita.toDTO(usuario) }

    @PostMapping("/nuevaFigurita")
    @Operation(summary = "Crea un nueva figurita")
    fun create(@RequestBody figuritaBody: FiguritaDTO): FiguritaDTO =
        figuritaService.create(figuritaBody).toDTO()

    @PutMapping("/updateFigurita")
    @Operation(summary = "Update de una figurita")
    fun update(@RequestBody figuritaBody: FiguritaDTO): FiguritaDTO =
        figuritaService.updateFigurita(figuritaBody).toDTO()

    @DeleteMapping("/figuritas/{id}")
    @Operation(summary = "Borra una figurita")
    fun deleteActividad(@PathVariable id : Int) {
        figuritaService.deleteFigurita(id)
    }
}