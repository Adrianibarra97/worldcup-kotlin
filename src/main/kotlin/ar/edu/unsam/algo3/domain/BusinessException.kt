package ar.edu.unsam.algo3.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EmptyArgumentException(mensaje: String): RuntimeException(mensaje) {}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IncorrectArgumentException(mensaje: String): RuntimeException(mensaje) {}

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(mensaje: String): RuntimeException(mensaje) {}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(mensaje: String): RuntimeException(mensaje) {}

@ResponseStatus(HttpStatus.CONFLICT)
class ErrorAlEliminar(message: String) : java.lang.RuntimeException(message)