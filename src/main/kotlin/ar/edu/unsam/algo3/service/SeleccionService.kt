package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.repository.RepositorioSelecciones
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SeleccionService() {

    @Autowired
    lateinit var seleccionRepository: RepositorioSelecciones

    fun createSeleccion(seleccion: Seleccion): Seleccion {
        if(seleccionRepository.contains(seleccion))
            seleccionRepository.getById(seleccion.id)
            
        seleccionRepository.create(seleccion)
        return seleccionRepository.getLastItem()
    }

    fun getSelecciones(): List<Seleccion> =
        seleccionRepository.getAll()

    fun getSeleccionById(id:Int):Seleccion =
        seleccionRepository.getById(id)
}