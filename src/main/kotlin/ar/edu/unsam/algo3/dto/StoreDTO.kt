package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.*

data class StoreDTO(
    val id:Int,
    val nombreComercial: String,
    val stockDeSobres: Int,
    val domicilioPuntoDeVenta: DomicilioDTO?,
    val distancia: Double,
    val precioPorSobre: Double,
    val tipoPuntoDeVenta: String,
    val pedidosPendientes: Int
)

data class AllStoreDTO(
    val id: Int,
    val nombreComercial: String,
    var stockDeSobres: Int,
    val domicilioPuntoDeVenta: DomicilioDTO?,
    val tipoPuntoDeVenta: String,
    val pedidosPendientesPuntoDeVenta: Int
)

data class FilterStoreJSON(
   val byDistance: Boolean,
   val byMostStock: Boolean,
   val byClosestPlaces: Boolean,
   val byCheapFigurine: Boolean
)

fun PuntoDeVenta.toDTO() =
    AllStoreDTO(
        id,
        nombreComercial,
        stockDeSobres,
        domicilioPuntoDeVenta.toDTO(),
        tipoPuntoDeVenta,
        pedidosPendientesPuntoDeVenta
    )

fun PuntoDeVenta.toDTO(usuario: Usuario) =
    StoreDTO(
        id,
        nombreComercial,
        stockDeSobres,
        domicilioPuntoDeVenta.toDTO(),
        usuario.domicilioUsuario.distanciaConOtroDomicilio(domicilioPuntoDeVenta),
        costoPorSobre,
        tipoPuntoDeVenta,
        pedidosPendientesPuntoDeVenta
    )

fun cratePuntoDeventaNuevo(puntoDeVentaDTO: AllStoreDTO): PuntoDeVenta {

    lateinit var puntoDeVentaNuevo: PuntoDeVenta

    if(puntoDeVentaDTO.tipoPuntoDeVenta == "Libreria")
        puntoDeVentaNuevo = Libreria(
            puntoDeVentaDTO.id,
            puntoDeVentaDTO.nombreComercial,
            puntoDeVentaDTO.tipoPuntoDeVenta
        )

    if(puntoDeVentaDTO.tipoPuntoDeVenta == "Supermercado")
        puntoDeVentaNuevo = Supermercado(
            puntoDeVentaDTO.id,
            puntoDeVentaDTO.nombreComercial,
            puntoDeVentaDTO.tipoPuntoDeVenta
        )

    if(puntoDeVentaDTO.tipoPuntoDeVenta == "Kiosko")
        puntoDeVentaNuevo = Kiosko(
            puntoDeVentaDTO.id,
            puntoDeVentaDTO.nombreComercial,
            puntoDeVentaDTO.tipoPuntoDeVenta
        )

    puntoDeVentaNuevo.stockDeSobres = puntoDeVentaDTO.stockDeSobres
    puntoDeVentaNuevo.domicilioPuntoDeVenta = Domicilio(
        puntoDeVentaDTO.domicilioPuntoDeVenta!!.ubicacionGeograficaX,
        puntoDeVentaDTO.domicilioPuntoDeVenta.ubicacionGeograficaY
    )
    puntoDeVentaNuevo.domicilioPuntoDeVenta.calle = puntoDeVentaDTO.domicilioPuntoDeVenta.calle
    puntoDeVentaNuevo.domicilioPuntoDeVenta.altura = puntoDeVentaDTO.domicilioPuntoDeVenta.altura
    puntoDeVentaNuevo.domicilioPuntoDeVenta.localidad = puntoDeVentaDTO.domicilioPuntoDeVenta.localidad
    puntoDeVentaNuevo.domicilioPuntoDeVenta.provincia = puntoDeVentaDTO.domicilioPuntoDeVenta.provincia
    puntoDeVentaNuevo.pedidosPendientesPuntoDeVenta = puntoDeVentaDTO.pedidosPendientesPuntoDeVenta

    return puntoDeVentaNuevo
}