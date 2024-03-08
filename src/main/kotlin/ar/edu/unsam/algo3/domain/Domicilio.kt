package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.domain.Point
import ar.edu.unsam.algo3.dto.DomicilioDTO

class Domicilio(val ubicacionGeograficaX: Double, val ubicacionGeograficaY: Double) {
    
    var altura:Int = 0
    lateinit var calle: String
    lateinit var localidad: String
    lateinit var provincia: String
    private var ubicacionGeografica: Point

    init {
        this.ubicacionGeografica = Point(ubicacionGeograficaX, ubicacionGeograficaY)
    }

    fun fromJSON(domicilio: DomicilioDTO): Domicilio {
        val nuevoDomicilio: Domicilio = Domicilio(
            domicilio.ubicacionGeograficaX,
            domicilio.ubicacionGeograficaY
        )
        nuevoDomicilio.altura = this.altura
        nuevoDomicilio.calle = this.calle
        nuevoDomicilio.localidad = this.localidad
        nuevoDomicilio.provincia = this.provincia
        nuevoDomicilio.ubicacionGeografica = Point(
            domicilio.ubicacionGeograficaX, 
            domicilio.ubicacionGeograficaY
        )
        return nuevoDomicilio
    }

    fun distanciaConOtroDomicilio(otroDomicilio: Domicilio ): Double =
        ubicacionGeografica.distance(otroDomicilio.ubicacionGeografica)
}