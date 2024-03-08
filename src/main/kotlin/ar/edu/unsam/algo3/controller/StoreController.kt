package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.AllStoreDTO
import ar.edu.unsam.algo3.dto.StoreDTO
import ar.edu.unsam.algo3.dto.FilterStoreJSON
import ar.edu.unsam.algo3.service.StoreService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unsam.algo3.dto.toDTO
import ar.edu.unsam.algo3.service.UsuarioService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class StoreController {

    @Autowired
    lateinit var storeService: StoreService

    @Autowired
    lateinit var usuarioService: UsuarioService

    @GetMapping("/stores/{idUser}")
    @Operation(summary = "Devuelve todas los puntos de venta")
    fun getPuntosDeVentas(@PathVariable idUser: Int) : List<StoreDTO> {
        val user = usuarioService.getUsuarioPorId(idUser)
        return storeService.getAllStores().map { it.toDTO(user) }
    }

    @GetMapping("/puntosDeVentas")
    @Operation(summary = "Devuelve todas los puntos de venta")
    fun getAllPuntosDeVentas() : List<AllStoreDTO> =
        storeService.getAllStores().map { it.toDTO() }

    @GetMapping("/puntosDeVentas/{id}")
    @Operation(summary = "Devuelve un punto de venta por id")
    fun getAllPuntosDeVentasByID(@PathVariable id: Int) : AllStoreDTO = 
        storeService.getPuntoDeVentaById(id).toDTO()

    @PostMapping("/createPuntoDeVenta")
    @Operation(summary = "Crea un nuevo punto de venta")
    fun create(@RequestBody puntoDeVentaBody: AllStoreDTO): AllStoreDTO =
        storeService.createPuntoDeVenta(puntoDeVentaBody).toDTO()
    
    @PostMapping("/puntosDeVentas/filterByName/{idUser}/{name}")
    @Operation(summary = "Devuelve las figuritas que coincidan con el titulo que viene por parámetro")
    fun getPuntosDeVentasByName(@RequestBody filter: FilterStoreJSON, @PathVariable idUser: Int, @PathVariable name: String): List<StoreDTO> {
        val usuario = usuarioService.getUsuarioPorId(idUser)
        return storeService.getStoreByName(filter, idUser, name).map { it.toDTO(usuario) }
    }

    @PostMapping("/stores/filterBy/{idUser}")
    @Operation(summary = "Crea un nuevo punto de venta")
    fun getStoresByFilterActive(@RequestBody filter: FilterStoreJSON, @PathVariable idUser: Int): List<StoreDTO> {
        val usuario = usuarioService.getUsuarioPorId(idUser)
        return storeService.getStoresByFilterActive(idUser, filter).map { it.toDTO(usuario) }
    }

    @PutMapping("/updatePuntoDeVenta")
    @Operation(summary = "Update de un punto de venta")
    fun update(@RequestBody puntoDeVentaBody: AllStoreDTO): AllStoreDTO =
        storeService.updatePuntoDeVenta(puntoDeVentaBody).toDTO()

    @DeleteMapping("/puntoDeVenta/{id}")
    @Operation(summary = "Elimina un punto de venta por id")
    fun deletePointOfSaleById(@PathVariable id: Int) =
        storeService.deletePuntoDeVentaById(id)
}