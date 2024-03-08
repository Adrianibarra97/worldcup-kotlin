package ar.edu.unsam.algo3.service

import java.io.File

interface IServiceExternoSelecciones {
    fun getSelecciones(): String
}

object serviceSelecciones: IServiceExternoSelecciones {

    override fun getSelecciones(): String =
        // Leer el contenido del archivo json en una cadena
        File("src/main/resources/selecciones.json").readText()
}