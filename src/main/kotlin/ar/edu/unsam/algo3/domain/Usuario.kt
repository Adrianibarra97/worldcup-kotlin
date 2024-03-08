package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.util.validadorDatos
import ar.edu.unsam.algo3.dto.UsuarioDTO
import ar.edu.unsam.algo3.dto.UsuarioUpdateDTO
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.time.Period

class Usuario(
    var username: String,
    var nombre: String,
    var apellido: String,
    var fechaNacimiento: LocalDate,
    var direccionMail: String,
    var domicilioUsuario: Domicilio,
    var maxDistanciaConOtroUsuario: Int = 999999,
    @JsonIgnore
    var tipoDeUsuario: TipoDeUsuario = TipoDeUsuarioPar(),
    var password: String = "1234",
    var url: String = ""
): Coleccionable() {

    var figuritasFaltantes: MutableSet<Figurita> = mutableSetOf<Figurita>()
    var figuritasRepetidas: MutableList<Figurita> = mutableListOf<Figurita>()
    
    init {
        validadorDatos.validarStringsNoVacios(nombre, apellido, direccionMail)
    }

    fun setDomicilio(domicilio: Domicilio) {
        this.domicilioUsuario = domicilio
    }

    fun agregarFiguritaFaltante(figurita: Figurita) {
        if (this.estaEnRepetidas(figurita))
            throw UnauthorizedException("Error: Figurita Repetida")
        figuritasFaltantes.add(figurita) 
    }

    fun agregarFiguritaRepetida(figurita: Figurita) {
        if (this.estaEnFaltantes(figurita))
            throw UnauthorizedException("Error: Figurita Faltante")
        figuritasRepetidas.add(figurita)
    }

    fun eliminarFiguritaDeRepetidas(figurita: Figurita) {
        if (this.estaEnRepetidas(figurita))
            this.figuritasRepetidas.remove(figurita)
        else 
            throw UnauthorizedException("Error: La figurita no esta en Repetidas")
    }

    fun eliminarFiguritasDeFaltantes(figurita: Figurita) {
        if (this.estaEnFaltantes(figurita))
            this.figuritasFaltantes.remove(figurita)
        else
            throw UnauthorizedException("Error: La figurita no esta en Faltantes")
    }

    fun fromJSON(usuarioDTO: UsuarioDTO): Usuario {
        val nuevoUsuario: Usuario = Usuario(
            usuarioDTO.username,
            usuarioDTO.nombre,
            usuarioDTO.apellido,
            usuarioDTO.fechaNacimiento,
            usuarioDTO.direccionMail,
            this.domicilioUsuario.fromJSON(usuarioDTO.domicilioUsuario),
            this.maxDistanciaConOtroUsuario,
            this.tipoDeUsuario,
            this.password
        )
        nuevoUsuario.id = this.id
        nuevoUsuario.figuritasFaltantes = this.figuritasFaltantes
        nuevoUsuario.figuritasRepetidas = this.figuritasRepetidas
        return nuevoUsuario
    }

    fun fromJSONUpdate(usuarioDTO: UsuarioUpdateDTO): Usuario {
        val splitDate: List<String> = usuarioDTO.fechaNacimiento.split('-')
        val nuevoUsuario: Usuario = Usuario(
            usuarioDTO.username,
            usuarioDTO.nombre,
            usuarioDTO.apellido,
            LocalDate.of(
                splitDate.get(0).toInt(),
                splitDate.get(1).toInt(),
                splitDate.get(2).toInt()
            ),
            usuarioDTO.direccionMail,
            this.domicilioUsuario.fromJSON(usuarioDTO.domicilioUsuario),
            this.maxDistanciaConOtroUsuario,
            this.tipoDeUsuario
        )
        nuevoUsuario.id = this.id
        nuevoUsuario.figuritasFaltantes = this.figuritasFaltantes
        nuevoUsuario.figuritasRepetidas = this.figuritasRepetidas
        nuevoUsuario.url = this.url
        return nuevoUsuario
    }

    override fun search(value: String): Boolean =
        validadorDatos.coincidenStringsExactamente(value, this.username)

    fun edad(): Int =
        Period.between(fechaNacimiento, LocalDate.now()).years

    fun superaEnEdad(edadMinima: Int): Boolean =  this.edad() > edadMinima

    fun figuritasFaltantes(): MutableSet<Figurita> = figuritasFaltantes

    fun figuritasRepetidas(): MutableList<Figurita> = figuritasRepetidas
    
    fun getStatusFiguritasRepetidas(): Int =
        this.figuritasRegalables().size

    fun getStatusFiguritasFaltantes(): Int =
        this.figuritasFaltantes().size

    fun estaEnFaltantes(figurita: Figurita): Boolean =
        figuritasFaltantes.any { it.id == figurita.id }

    fun estaEnRepetidas(figurita: Figurita): Boolean =
        figuritasRepetidas.contains(figurita) // toDO: Revisar funcionalidad!
    
    fun figuritasRegalables(): List<Figurita> =
        figuritasRepetidas.filter{ tipoDeUsuario.quiereRegalar(it) }
    
    fun usuarioEsCercano(otroUsuario: Usuario): Boolean =
        this.domicilioUsuario.distanciaConOtroDomicilio(otroUsuario.domicilioUsuario) <=
        this.maxDistanciaConOtroUsuario

    fun solicitar(proveedor: Usuario, figurita: Figurita): Boolean =
        this.estaEnFaltantes(figurita) &&
        this.usuarioEsCercano(proveedor) &&
        proveedor.figuritasRegalables().contains(figurita)
}