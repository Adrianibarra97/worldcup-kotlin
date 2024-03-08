package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.domain.UnauthorizedException
import ar.edu.unsam.algo3.domain.StatusDTO
import ar.edu.unsam.algo3.repository.RepositorioUsuarios
import ar.edu.unsam.algo3.repository.RepositorioPuntosDeVenta
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService {

    @Autowired
    lateinit var usuariosRepositorio: RepositorioUsuarios

    @Autowired
    lateinit var storeRepositorio: RepositorioPuntosDeVenta

    fun updateUsuario(usuario: Usuario) {
        usuariosRepositorio.update(usuario)
    }

    fun getUserStatus(idUsuario: String): List<StatusDTO> {
        val searchedUser: Usuario = usuariosRepositorio.getById(idUsuario.toInt())
        return listOf<StatusDTO>(
            StatusDTO("figuritas ofrecidas", searchedUser.getStatusFiguritasRepetidas()),
            StatusDTO("figuritas faltantes", searchedUser.getStatusFiguritasFaltantes()),
            StatusDTO("puntos de venta", storeRepositorio.getAmountObjectsActives()),
            StatusDTO("usuarios activos", usuariosRepositorio.getAmountObjectsActives())
        )
    }

    fun usuarioSolicitaUnaFigurita(usuarioSolicitante: Usuario, usuarioProveedor: Usuario, figurita:Figurita) {
        if(usuarioSolicitante.solicitar(usuarioProveedor, figurita)) {
            usuarioProveedor.eliminarFiguritaDeRepetidas(figurita) //Elimina de REPETIDAS la figurita que CEDE el PROVEEDOR
            usuarioSolicitante.eliminarFiguritasDeFaltantes(figurita) //Elimina de FALTANTES la figurita que RECIBE
            usuarioSolicitante.agregarFiguritaRepetida(figurita)
        } else
            throw UnauthorizedException("Error: El usuario no puede solicitar la figurita ya que no cumple con las condiciones necesarias")
    }

    fun getUsuarioLogin(username:String, password: String): String {
        val searchedUserList: List<Usuario> = usuariosRepositorio.search(username)
        if(searchedUserList.isEmpty())
            throw UnauthorizedException("El usuario y/o contraseña son incorrectos!")
        
        val userSearched: Usuario = searchedUserList.get(0)
        if(userSearched.password != password)
            throw UnauthorizedException("La contraseña no corresponde al usuario!")
        
        return userSearched.id.toString()
    }

    fun getFiguritasFaltantesById(id: Int): Set<Figurita> =
        getUsuarioPorId(id).figuritasFaltantes()

    fun getFiguritasRepetidasById(id: Int): List<Figurita> =
        getUsuarioPorId(id).figuritasRepetidas()

    fun getUsuarios() =
        usuariosRepositorio.getAll()

    fun getUsuarioPorId(id: Int) =
        usuariosRepositorio.getById(id)

    fun getUsuarioByIdFigurita(figurita: Figurita): List<Usuario> =
        this.getUsuarios().filter {
            it.figuritasRegalables().contains(figurita)
        }
}