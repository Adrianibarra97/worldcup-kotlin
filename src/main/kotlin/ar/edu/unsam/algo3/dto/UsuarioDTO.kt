package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Usuario
import java.time.LocalDate

data class UsuarioDTO(
    var username: String,
    var nombre: String,
    var apellido: String,
    var fechaNacimiento: LocalDate,
    var direccionMail: String,
    var domicilioUsuario: DomicilioDTO,
    var maxDistanciaConOtroUsuario: Int,
    var password: String,
    var url: String
)

data class UsuarioUpdateDTO(
    var id: Int,
    var username: String,
    var nombre: String,
    var apellido: String,
    var fechaNacimiento: String,
    var direccionMail: String,
    var domicilioUsuario: DomicilioDTO,
    var maxDistanciaConOtroUsuario: Int,
    var url: String
)

data class UsuarioCedidaODTO(var nombre: String, var apellido: String)

data class LoginDTO(var username: String, var password: String)

fun Usuario.toDTO() =
    UsuarioDTO(
        username,
        nombre,
        apellido,
        fechaNacimiento,
        direccionMail,
        domicilioUsuario.toDTO(),
        maxDistanciaConOtroUsuario,
        password,
        url
    )

fun Usuario.toCedidaDTO() =
    UsuarioCedidaODTO(nombre, apellido)