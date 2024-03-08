package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.util.validadorDatos
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val MONTO_MINIMO_SOBRES = 170.00
val MINIMA_DISTANCIA_ENVIO_COSTO_EXTRA = 10.00
val COSTO_MINIMO_ENVIO = 1000.00
val COSTO_KM_EXTRA = 100.00

data class Pedido(
    var cantidadDeSobres: Int = 0,
    var fechaEntrega: LocalDate = LocalDate.now()
)

abstract class PuntoDeVenta(
    var nombreComercial: String,
    var tipoPuntoDeVenta: String,
    var costoPorSobre: Double = MONTO_MINIMO_SOBRES
) : Coleccionable() {

    var stockDeSobres:Int = 0
    var pedidosPendientesPuntoDeVenta: Int = 0
    lateinit var domicilioPuntoDeVenta: Domicilio

    abstract fun modificacionImportePorPuntoVenta(cantidadDeSobresSolicitados: Int): Double

    fun setDomicilio(domicilio: Domicilio) {
        this.domicilioPuntoDeVenta = domicilio
    }

    fun agregarStock(cantidadSobres: Int) { 
        stockDeSobres += cantidadSobres
    }

    fun quitarStock(cantidadSobres: Int) {
        stockDeSobres -= cantidadSobres
    }    

    fun importeACobrar(domicilioDeEntrega: Domicilio, cantidadDeSobresSolicitados: Int): Double {
        validarDisponibilidad(cantidadDeSobresSolicitados)
        return (importeSobres(cantidadDeSobresSolicitados) + this.costoEnvio(domicilioDeEntrega)) * modificacionImportePorPuntoVenta(cantidadDeSobresSolicitados)
    }

    fun costoEnvio(domicilioDeEntrega:Domicilio): Double {
        validarDomicilio()
        val distanciaConOtroDomicilio = domicilioPuntoDeVenta.distanciaConOtroDomicilio(domicilioDeEntrega)
        val kilometrosAdicionales = kotlin.math.ceil(distanciaConOtroDomicilio - MINIMA_DISTANCIA_ENVIO_COSTO_EXTRA)
        return COSTO_MINIMO_ENVIO + if(kilometrosAdicionales > 0.0) kilometrosAdicionales * COSTO_KM_EXTRA else 0.0
    }

    override fun search(value: String): Boolean =
        validadorDatos.coincidenStringsExactamente(value, nombreComercial)

    fun disponibilidad(cantidadDeSobresSolicitados: Int): Boolean =
        stockDeSobres >= cantidadDeSobresSolicitados

    private fun validarDisponibilidad(cantidadDeSobresSolicitados: Int) {
        if(!disponibilidad(cantidadDeSobresSolicitados)){
            throw Exception("No hay disponibilidad de stock!")
        }
    }

    private fun validarDomicilio() {
        if (domicilioPuntoDeVenta == null)
            throw Exception("El punto de venta no tiene un domicilio!")
    }

    private fun importeSobres(cantidadDeSobresSolicitados: Int) =
        MONTO_MINIMO_SOBRES * cantidadDeSobresSolicitados
}

class Kiosko : PuntoDeVenta {

    private var atendidoPorDuenio = false

    constructor(
        id: Int,
        nombreComercial: String,
        tipoPuntoDeVenta: String,
        costoPorSobre: Double = MONTO_MINIMO_SOBRES
    ) : super(nombreComercial, tipoPuntoDeVenta, costoPorSobre) {
        this.id = id
    }

    fun cambiarAtendidoPorDuenio(){
        atendidoPorDuenio = !atendidoPorDuenio
    }

    override fun modificacionImportePorPuntoVenta(cantidadDeSobresSolicitados: Int) =
        if (atendidoPorDuenio) 1.10
        else 1.25

    fun atendidoPorDuenio(): Boolean = atendidoPorDuenio
}

class Libreria : PuntoDeVenta {

    var pedidosPendientes: MutableList<Pedido> = mutableListOf()

    constructor(
        id: Int,
        nombreComercial: String,
        tipoPuntoDeVenta: String,
        costoPorSobre: Double = MONTO_MINIMO_SOBRES
    ) : super(nombreComercial, tipoPuntoDeVenta, costoPorSobre) {
        this.id = id
    }

    override fun modificacionImportePorPuntoVenta(cantidadDeSobresSolicitados: Int): Double =
        // imagino que se necesita multiplicar el importe por la cantidad de sobres!!!
        cantidadDeSobresSolicitados *
        if(this.hayPedidosCerca()) 1.05
        else 1.10

    fun agregarPedidoPendiente(pedido: Pedido) {
        pedidosPendientes.add(pedido)
    }

    private fun hayPedidosCerca(): Boolean =
        this.pedidosPendientes.any() {
            ChronoUnit.DAYS.between(it.fechaEntrega, LocalDate.now()) < 10
        }
}

class Supermercado : PuntoDeVenta {

    val TOPE_MAXIMO_DESCUENTO = 0.50
    private val promociones = mutableListOf<Promocion>()

    constructor(
        id: Int,
        nombreComercial: String,
        tipoPuntoDeVenta: String,
        costoPorSobre: Double = MONTO_MINIMO_SOBRES
    ) : super(nombreComercial, tipoPuntoDeVenta, costoPorSobre) {
        this.id = id
    }

    fun agregarPromocion(promocion: Promocion) {
        if(tienePromocion(promocion))
            throw Exception("La promocion ya esta agregada!")
        promociones.add(promocion)
    }

    override fun modificacionImportePorPuntoVenta(cantidadDeSobresSolicitados: Int): Double {
        val totalDescuento = this.descuentoTotal(cantidadDeSobresSolicitados)
        return 1.00 -
            if(totalDescuento < TOPE_MAXIMO_DESCUENTO) totalDescuento
            else TOPE_MAXIMO_DESCUENTO
    }
    
    fun promociones() = promociones

    private fun tienePromocion(promocion: Promocion) = promociones.contains(promocion)

    private fun descuentoTotal(cantidadDeSobresSolicitados: Int): Double =
        this.promociones.sumOf { it.descuentoAAplicar(cantidadDeSobresSolicitados) }
}