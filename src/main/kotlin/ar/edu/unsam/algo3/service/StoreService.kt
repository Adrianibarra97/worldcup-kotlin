package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.AllStoreDTO
import ar.edu.unsam.algo3.dto.FilterStoreJSON
import ar.edu.unsam.algo3.dto.cratePuntoDeventaNuevo
import ar.edu.unsam.algo3.repository.RepositorioPuntosDeVenta
import com.google.common.base.Ascii.toLowerCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StoreService {

    @Autowired
    lateinit var storeRepositorio: RepositorioPuntosDeVenta

    @Autowired
    lateinit var usuarioService: UsuarioService

    fun deletePuntoDeVentaById(id: Int) {
        storeRepositorio.delete(storeRepositorio.getById(id))
    }

    fun createPuntoDeVenta(puntoDeVentaDTO: AllStoreDTO): PuntoDeVenta {
        val puntoDenVentaNuevo: PuntoDeVenta = cratePuntoDeventaNuevo(puntoDeVentaDTO)
        storeRepositorio.create(puntoDenVentaNuevo)
        return puntoDenVentaNuevo
    }

    fun getAllStores(): List<PuntoDeVenta> =
        storeRepositorio.getAll()

    fun getPuntoDeVentaById(id: Int): PuntoDeVenta =
        storeRepositorio.getById(id)

    fun getStoreByName(filter: FilterStoreJSON, idUser: Int, name: String): List<PuntoDeVenta> {
        if(name == "''") {
            return this.getStoresByFilterActive(idUser, filter)
        }
         return this.getStoresByFilterActive(idUser, filter).filter {
             toLowerCase(it.nombreComercial).contains(toLowerCase(name))
         }
    }

    fun updatePuntoDeVenta(puntoDeVentaDTO: AllStoreDTO): PuntoDeVenta =
        storeRepositorio.updatePuntoDeVenta(
            puntoDeVentaDTO.id,
            cratePuntoDeventaNuevo(puntoDeVentaDTO)
        )

    fun getStoresByFilterActive(userId: Int, filterStoreJSON: FilterStoreJSON): List<PuntoDeVenta> =
        if(filterStoreJSON.byDistance) this.getStoreByDistance(userId)
        else if(filterStoreJSON.byMostStock) this.getStoreByMoreStock()
        else if(filterStoreJSON.byClosestPlaces) this.getStoreByClosestPlaces(userId)
        else if(filterStoreJSON.byCheapFigurine) this.getStoreByCheapFigurine()
        else this.getAllStores()

    private fun getStoreByClosestPlaces(idUser: Int): List<PuntoDeVenta> {
        val usuario = usuarioService.getUsuarioPorId(idUser)
        return this.getAllStores().filter{ usuario.domicilioUsuario.distanciaConOtroDomicilio(it.domicilioPuntoDeVenta) <= usuario.maxDistanciaConOtroUsuario}
    }

    private fun getStoreByDistance(idUser: Int): List<PuntoDeVenta> {
        val usuario = usuarioService.getUsuarioPorId(idUser)
        return this.getAllStores().sortedBy{
            usuario.domicilioUsuario.distanciaConOtroDomicilio(it.domicilioPuntoDeVenta)
        }
    }
    
    private fun getStoreByMoreStock(): List<PuntoDeVenta> =
        this.getAllStores().sortedByDescending { it.stockDeSobres }

    private fun getStoreByCheapFigurine(): List<PuntoDeVenta> =
        this.getAllStores().sortedBy { it.costoPorSobre }
}