package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.FiguritaDTO
import ar.edu.unsam.algo3.dto.FiguritaWithOwnerDTO
import ar.edu.unsam.algo3.dto.FilterFigurineJSON
import ar.edu.unsam.algo3.dto.toDTO
import ar.edu.unsam.algo3.repository.RepositorioFiguritas
import com.google.common.base.Ascii.toLowerCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class FiguritaService() {

    @Autowired
    lateinit var figuritaRepository: RepositorioFiguritas

    @Autowired
    lateinit var usuarioService: UsuarioService

    @Autowired
    @Lazy
    lateinit var jugadorService: JugadorService

    fun deleteFigurita(id: Int) {
        val figuritaAEliminar =  figuritaRepository.getById(id)

        usuarioService.getUsuarios().forEach {
            if(it.estaEnFaltantes(figuritaAEliminar))
                it.eliminarFiguritasDeFaltantes(figuritaAEliminar)

            if(it.estaEnRepetidas(figuritaAEliminar))
                it.eliminarFiguritaDeRepetidas(figuritaAEliminar)
        }

        figuritaRepository.delete(figuritaAEliminar)
    }

    fun updateFigurita(figuritaDTO: FiguritaDTO): Figurita {
        val jugador = jugadorService.getJugadorById(figuritaDTO.jugador.id)
        val figuritaNueva = Figurita(figuritaDTO.numero, figuritaDTO.nivelImpresion,jugador ,figuritaDTO.onFire,figuritaDTO.url)
        return figuritaRepository.updateFigurita(figuritaDTO.id,figuritaNueva)
    }

    fun create(figuritaBody: FiguritaDTO): Figurita {
        val jugador: Jugador = jugadorService.getJugadorById(figuritaBody.jugador.id)
        val nuevaFigurita = Figurita(
            figuritaBody.numero,
            figuritaBody.nivelImpresion,
            jugador,
            figuritaBody.onFire,
            figuritaBody.url
        )
        figuritaRepository.create(nuevaFigurita)
        return nuevaFigurita
    }

    fun getFiguritaFaltanteById(id: Int): Map<Figurita, Usuario> {
        var usuariosMenosElSolicitante: List<Usuario> = usuarioService.getUsuarios().filter{ it.id != id }

        return usuarioService.getFiguritasFaltantesById(id).associateWith { figurita ->
            usuariosMenosElSolicitante.find {
                it.figuritasRegalables().contains(figurita)
            }!!
        }
    }

    fun getFiguritaByIdAndOwner(idFigurita:Int): FiguritaWithOwnerDTO {
        val figurita: Figurita = this.getFiguritaById(idFigurita)
        val usuariosQueTieneEsaFigurita: List<Usuario> = usuarioService.getUsuarios().filter { 
            it.figuritasRegalables().contains(figurita)
        }

        try {
            return figurita.toDTO(usuariosQueTieneEsaFigurita.first())
        } catch (ex: Exception) {
            throw NotFoundException("El objeto no se encuentra en el repositorio.")
        }
    }

    fun getAllFiguritas():List<Figurita> =
        figuritaRepository.getAll()

    fun getFiguritaById(id: Int): Figurita =
        figuritaRepository.getById(id)

    fun figuritasContienenJugador(jugador: Jugador) = 
        getAllFiguritas().any{ it.jugador === jugador }

    fun getFiguritasByName(filter: FilterFigurineJSON, name: String, idUser: Int): Map<Figurita, Usuario> {
        if(name == "''") {
            return this.getFiguritasByFilter(idUser, filter)
        }
        return this.getFiguritasByFilter(idUser, filter).filter{ (figurita) ->
            toLowerCase(figurita.jugador.nombre).contains(toLowerCase(name)) or
                    toLowerCase(figurita.jugador.apellido).contains(toLowerCase(name))
        }
    }

    fun getFiguritasByFilter(idUser: Int, filtros: FilterFigurineJSON): Map<Figurita, Usuario>{
        return this.getFiguritaFaltanteById(idUser).filter { (figurita) ->
            this.esPromesa(filtros, figurita) && this.esOnFire(filtros, figurita)
                    && (figurita.valoracion() >= filtros.valoracionMin && figurita.valoracion() <= filtros.valoracionMax)
        }
    }

    fun requestFigurineFromUser(idUsuario: Int,idFigurita: Int) {
        val usuarioSolicitante = usuarioService.getUsuarioPorId(idUsuario)
        val figurita = this.getFiguritaById(idFigurita)
        val usuariosProveedor = usuarioService.getUsuarioByIdFigurita(figurita) //el usuario que tiene la figurita que yo necesito
        
        if(usuariosProveedor.isEmpty())
            throw NotFoundException("No hay usuario que regale esa figurita para el solicitante!")
        
        usuarioService.usuarioSolicitaUnaFigurita(usuarioSolicitante,usuariosProveedor.first(),figurita)
    }

    fun requestRepeatedFigurineFromUser(idUsuario: Int,idFigurita: Int) {
        val usuarioSolicitante = usuarioService.getUsuarioPorId(idUsuario)
        var figurita = this.getFiguritaById(idFigurita)

        this.deleteFigurita(figurita.id)
        figuritaRepository.create(figurita)
        figurita = figuritaRepository.getLastItem()

        usuarioService.getUsuarios().forEach {
            if(it.id == usuarioSolicitante.id)
                it.agregarFiguritaRepetida(figurita)
            else
                it.agregarFiguritaFaltante(figurita)
        }
    }

    fun requestMissingFigurineFromUser(idUsuario: Int,idFigurita: Int) {
        val usuarioSolicitante = usuarioService.getUsuarioPorId(idUsuario)
        var figurita = this.getFiguritaById(idFigurita)
        val usuariosRestante: Usuario? = usuarioService.getUsuarios().find { it.id != usuarioSolicitante.id }

        this.deleteFigurita(figurita.id)
        figuritaRepository.create(figurita)
        figurita = figuritaRepository.getLastItem()
        usuariosRestante?.agregarFiguritaRepetida(figurita)
        usuarioService.getUsuarios().forEach {
            if(it.id != usuariosRestante?.id)
                it.agregarFiguritaFaltante(figurita)
        }
    }

    private fun esOnFire(filtros: FilterFigurineJSON, figurita: Figurita): Boolean =
        if(filtros.esOnFire) filtros.esOnFire == figurita.estaOnFire()
        else true

    private fun esPromesa(filtros: FilterFigurineJSON, figurita: Figurita): Boolean =
        if(filtros.esPromesa) filtros.esPromesa == figurita.jugador.esPromesa()
        else true
}