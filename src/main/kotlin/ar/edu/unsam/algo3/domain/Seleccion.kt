package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.util.validadorDatos

object FIFA {

    private var confederaciones = listOf<Confederacion>(
        Confederacion("AFC"),
        Confederacion("CAF"),
        Confederacion("CONMEBOL"),
        Confederacion("CONCACAF"),
        Confederacion("OFC"),
        Confederacion("UEFA")
    )

    fun buscarConfederacion(nombreConfederacion: String) =
        confederaciones.find { it.nombre == nombreConfederacion }
}

class Seleccion(
    val pais: String,
    val nombreConfederacion: String,
    var copasDelMundo: Int = 0
): Coleccionable() {
    
    private lateinit var confederacion: Confederacion

    init {
        validadorDatos.validarStringsNoVacios(pais, nombreConfederacion)
        this.setConfederacion(nombreConfederacion)
        confederacion.agregarSeleccion(this)
    }

    fun setConfederacion(nombreConfederacion: String) {
        val confederacion = FIFA.buscarConfederacion(nombreConfederacion)
        if (confederacion === null)
            throw Exception("La seleccion tiene una confederacion que no existe!")
        this.confederacion = confederacion
    }

    override fun search(value: String): Boolean =
        validadorDatos.coincidenStringsExactamente(value, pais)

    fun getConfederacion(): Confederacion = confederacion

    fun nombreConfederacion(): String = confederacion.nombre

    fun esCampeonaDelMundo(): Boolean = copasDelMundo >= 1

    fun totalCopasConfederacion(): Int = confederacion.totalCopasConfederacion()
}

class Confederacion (var nombre: String) {

    private val selecciones = mutableListOf<Seleccion>()

    fun agregarSeleccion(seleccion: Seleccion) {
        if(tieneSeleccion(seleccion))
            eliminarSeleccion(seleccion)
        selecciones.add(seleccion)
    }

    fun selecciones(): MutableList<Seleccion> = selecciones

    fun totalCopasConfederacion(): Int =
        selecciones.sumOf{ it.copasDelMundo }

    private fun eliminarSeleccion(seleccion: Seleccion) {
        selecciones.removeIf{ it.pais == seleccion.pais }
    }
    
    private fun tieneSeleccion(seleccion: Seleccion): Boolean =
        selecciones.any{ it.pais == seleccion.pais }
}