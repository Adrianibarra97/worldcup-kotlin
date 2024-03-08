package ar.edu.unsam.algo3.domain

import java.time.LocalDate
import java.time.DayOfWeek

val CANTIDAD_MAXIMA_PAQUETES_PROMOCION = 200.00

interface Promocion {
    fun descuentoAAplicar(cantidadDePaquetes: Int): Double
}

object promocionJueves: Promocion {

    private var diaPromocion: LocalDate = LocalDate.now()

    override fun descuentoAAplicar(cantidadDePaquetes: Int): Double =
        if (diaPromocion.dayOfWeek == DayOfWeek.THURSDAY) 0.10 else 0.00

    fun setDiaPromocion(dia: LocalDate) {
        diaPromocion = dia
    }
}

object unoAlDiezDelMes: Promocion {

    private var diaPromocion: LocalDate = LocalDate.now()

    override fun descuentoAAplicar(cantidadDePaquetes: Int): Double =
        if (diaPromocion.dayOfMonth in 1..10) 0.05
        else 0.00

    fun setDiaPromocion(dia:LocalDate){
        diaPromocion = dia
    }
}

object compraMayorACantidadPaquetes: Promocion {

    override fun descuentoAAplicar(cantidadDePaquetes: Int): Double =
        if (cantidadDePaquetes > CANTIDAD_MAXIMA_PAQUETES_PROMOCION) 0.45
        else 0.00
}