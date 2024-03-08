package ar.edu.unsam.algo3.repository

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.SeleccionDTO
import ar.edu.unsam.algo3.service.serviceSelecciones
import com.google.gson.Gson
import org.springframework.stereotype.Component
import java.time.LocalDate

abstract class Repositorio<T : Coleccionable> {

    var objetos: MutableList<T> = mutableListOf()
    private var contadorId: Int = 0

    abstract fun updateSegunColeccionable(objeto: T)

    abstract fun updateNuevo(objetoAModificar: T, objetoNuevo: T)

    fun create(objeto: T) {
        objeto.id = contadorId
        objetos.add(objeto)
        contadorId++
    }

    fun createListObject(objectosACrear: List<T>) {
        for (objeto: T in objectosACrear) {
            this.create(objeto)
        }
    }

    fun createForUpdate(idExistente: Int, objeto: T) {
        objeto.id = idExistente
        objetos.add(objeto)
    }

    fun delete(objeto: T) {
        if(!contains(objeto)) {
            throw NotFoundException("El objeto a eliminar no esta en el repositorio")
        }

        objetos.remove(objeto)
    }

    fun update(objeto: T) {
        val objetoAModificar = this.getById(objeto.id)
        updateNuevo(objetoAModificar, objeto)
        updateSegunColeccionable(objetoAModificar)
    }

    fun getAmountObjects(): Int =
        this.objetos.size

    fun getById(id: Int): T = 
        objetos.find { it.id == id } ?:
        throw NotFoundException("El objeto con id ${id} no existe en el repositorio.")

    fun search(value: String): List<T> =
        objetos.filter{ it.search(value) }

    fun getAll(): MutableList<T> = objetos

    fun contains(objeto: T): Boolean =
        objetos.contains(objeto)

    fun getAmountObjectsActives(): Int =
        this.objetos.size

    fun getLastItem(): T =
        objetos.last()
}

@Component
class RepositorioSelecciones: Repositorio<Seleccion>() {

    companion object{
        data class SeleccionData(
            val id: Int? = null,
            val pais: String,
            val confederacion: String,
            val copasDelMundo: Int,
            val copasConfederacion: Int
        )
    }

    override fun updateSegunColeccionable(objeto: Seleccion) {
        val seleccionesLista = convertirJsonAListaObjetos()
        val seleccionesFiltradasPorConfederacion = seleccionesLista.filter {
            it.confederacion.equals(objeto.getConfederacion().nombre)
        }
        val seleccionAModificar = seleccionesFiltradasPorConfederacion.find {
            it.pais.equals(objeto.pais, ignoreCase = true)
        }

        if (seleccionAModificar!=null)
            objeto.copasDelMundo = seleccionAModificar.copasDelMundo
    }

    override fun updateNuevo(objetoAModificar: Seleccion, objetoNuevo: Seleccion) {}

    fun convertirJsonAListaObjetos() : List<SeleccionData> =
        // Convertir JSON a una lista de objetos SeleccionData
        Gson().fromJson(this.getSelecciones(), Array<SeleccionData>::class.java).toList()

    fun getSelecciones() =
        serviceSelecciones.getSelecciones()
}

@Component
class RepositorioFiguritas: Repositorio<Figurita>() {

    override fun updateSegunColeccionable(objeto: Figurita) {
        TODO("Not yet implemented")
    }

    override fun updateNuevo(objetoAModificar: Figurita, objetoNuevo: Figurita) {
        this.delete(objetoAModificar)
        this.createForUpdate(objetoAModificar.id,objetoNuevo)
    }

    fun updateFigurita(id:Int,objeto: Figurita): Figurita {
        val figuritaVieja = this.getById(id)
        figuritaVieja.apply{
            this.numero = objeto.numero
            this.nivelImpresion = objeto.nivelImpresion
            this.jugador = objeto.jugador
            this.onFire = objeto.onFire
            this.url = objeto.url
        }
        this.objetos.replaceAll { if (it.id == id) figuritaVieja else it }
        return figuritaVieja
    }
}

@Component
class RepositorioJugadores: Repositorio<Jugador>() {

    override fun updateSegunColeccionable(objeto: Jugador) {
        TODO("Not yet implemented")
    }

    override fun updateNuevo(objetoAModificar: Jugador, objetoNuevo: Jugador) {
        this.delete(objetoAModificar)
        this.createForUpdate(objetoAModificar.id,objetoNuevo)
    }

    fun updateJugador(id:Int,objeto: Jugador): Jugador {
        val jugadorViejo = this.getById(id)
        jugadorViejo.apply{
            this.nombre = objeto.nombre
            this.apellido =  objeto.apellido
            this.fechaNacimiento = objeto.fechaNacimiento
            this.numeroCamiseta = objeto.numeroCamiseta
            this.seleccion = objeto.seleccion
            this.anioDebut =objeto.anioDebut
            this.altura = objeto.altura
            this.peso = objeto.peso
            this.posicion = objeto.posicion
            this.cotizacion = objeto.cotizacion
            this.esLider = objeto.esLider
        }
        this.objetos.replaceAll { if (it.id == id) jugadorViejo else it }
        return jugadorViejo
    }
}

@Component
class RepositorioUsuarios: Repositorio<Usuario>() {

    override fun updateSegunColeccionable(objeto: Usuario) {}

    override fun updateNuevo(objetoAModificar: Usuario, objetoNuevo: Usuario) {
        this.objetos.add(objetoNuevo.id, objetoNuevo)
    }
}

@Component
class RepositorioPuntosDeVenta: Repositorio<PuntoDeVenta>() {

    override fun updateSegunColeccionable(objeto: PuntoDeVenta) {
        TODO("Not yet implemented")
    }

    override fun updateNuevo(objetoAModificar: PuntoDeVenta, objetoNuevo: PuntoDeVenta) { }

    fun updatePuntoDeVenta(id:Int, puntoDeVentaModificado: PuntoDeVenta): PuntoDeVenta {
        val puntoDeVentaAActualizar = this.getById(id)
        puntoDeVentaAActualizar.apply{
            this.nombreComercial = puntoDeVentaModificado.nombreComercial
            this.domicilioPuntoDeVenta = puntoDeVentaModificado.domicilioPuntoDeVenta
            this.stockDeSobres = puntoDeVentaModificado.stockDeSobres
            this.tipoPuntoDeVenta = puntoDeVentaModificado.tipoPuntoDeVenta
            this.pedidosPendientesPuntoDeVenta = puntoDeVentaModificado.pedidosPendientesPuntoDeVenta
        }
        this.objetos.replaceAll { if (it.id == puntoDeVentaAActualizar.id) puntoDeVentaAActualizar else it }
        return puntoDeVentaAActualizar
    }
}