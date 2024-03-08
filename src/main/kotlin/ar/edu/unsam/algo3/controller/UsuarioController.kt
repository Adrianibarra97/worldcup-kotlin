package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.domain.StatusDTO
import ar.edu.unsam.algo3.dto.FiguritaDTO
import ar.edu.unsam.algo3.dto.LoginDTO
import ar.edu.unsam.algo3.dto.UsuarioDTO
import ar.edu.unsam.algo3.dto.toDTO
import ar.edu.unsam.algo3.dto.UsuarioUpdateDTO
import ar.edu.unsam.algo3.service.UsuarioService
import ar.edu.unsam.algo3.util.validadorDatos
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class UsuarioController {

    @Autowired
    lateinit var usuarioService: UsuarioService

    @GetMapping("/usuarios")
    @Operation(summary = "Devuelve todos los usuarios")
    fun getUsuarios(): List<UsuarioDTO> =
        usuarioService.getUsuarios().map { it.toDTO() }

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Devuelve un usuario por ID")
    fun getUsuariosPorId(@PathVariable id: Int): UsuarioDTO =
        usuarioService.getUsuarioPorId(id).toDTO()

    @GetMapping("/usuarios/faltantes/{id}")
    @Operation(summary = "Devuelve las figuritas faltantes de un usuario")
    fun getFaltantesPorUsuario(@PathVariable id: Int): List<FiguritaDTO> =
        usuarioService.getFiguritasFaltantesById(id).map { it.toDTO() }

    @GetMapping("/usuarios/repetidas/{id}")
    @Operation(summary = "Devuelve las figuritas repetidas de un usuario")
    fun getRepetidasPorUsuario(@PathVariable id: Int):List<FiguritaDTO> =
        usuarioService.getFiguritasRepetidasById(id).map { it.toDTO() }

    @GetMapping("/statusUsuario/{idUsuario}")
    @Operation(summary = "Devuelve el status del usuario.")
    fun getUserStatus(@PathVariable idUsuario: String): List<StatusDTO> =
        usuarioService.getUserStatus(idUsuario.toString())
 
    @PostMapping("/loginUsuario")
    @Operation(summary = "Valida un usuario")
    fun loginUsuario(@RequestBody usuario: LoginDTO): String {
        validadorDatos.validarStringsNoVacios(usuario.username, usuario.password)
        return usuarioService.getUsuarioLogin(usuario.username, usuario.password)
    }

    @PutMapping("/usuarios")
    @Operation(summary = "Actualiza un usuario")
    fun cambiarUsuario(@RequestBody usuarioUpdateDTO: UsuarioUpdateDTO) {
        val userUpdate: Usuario = usuarioService.getUsuarioPorId(usuarioUpdateDTO.id)
        usuarioService.updateUsuario(userUpdate.fromJSONUpdate(usuarioUpdateDTO))
    }
}