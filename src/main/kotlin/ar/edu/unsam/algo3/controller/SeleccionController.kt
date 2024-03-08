package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.SeleccionDTO
import ar.edu.unsam.algo3.dto.toDTO
import ar.edu.unsam.algo3.service.SeleccionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
class SeleccionController {

    @Autowired
    lateinit var seleccionService: SeleccionService

    @GetMapping("/selecciones")
    @Operation(summary="Devuelve todas las selecciones")
    fun getSelecciones(): List<SeleccionDTO> =
        seleccionService.getSelecciones().map { it.toDTO() }
}