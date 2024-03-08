package ar.edu.unsam.algo2.worldcapp

import Posicion
import ar.edu.unsam.algo3.domain.*
import delantero
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class FiguritaSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    fun crearJugador(posicion: Posicion, seleccion: Seleccion) =
        Jugador("Nombre",
            "Apellido",
            LocalDate.parse("2000-01-01"),
            10,
            seleccion,
            2018,
            1.7,
            70.0,
            posicion,
            MAX_COTIZACION_JUGADOR)

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 3)
    //Jugadores
    val jugadorDelantero = crearJugador(delantero, seleccionArgentina)

    describe("Test de figuritas") {

        describe("valoracion base") {

            it("si la figurita está OnFire aumenta su valoracion base") {
                //Arrange
                val figuritaNoOnFire = Figurita(1, NivelImpresion.BAJA, jugadorDelantero)
                val figuritaOnFire = Figurita(1, NivelImpresion.BAJA, jugadorDelantero)
                //Act
                figuritaOnFire.cambiarEstadoOnFire()
                //Assert
                figuritaNoOnFire.estaOnFire().shouldBeFalse()
                figuritaOnFire.estaOnFire().shouldBeTrue()
                figuritaOnFire.valoracionBase().shouldBeGreaterThan(figuritaNoOnFire.valoracionBase())
            }

            it("si la figurita es Par aumenta su valoracion base") {
                //Arrange
                val figuritaNoEsPar = Figurita(1, NivelImpresion.BAJA, jugadorDelantero)
                val figuritaEsPar = Figurita(2, NivelImpresion.BAJA, jugadorDelantero)
                //Assert
                figuritaEsPar.valoracionBase().shouldBeGreaterThan(figuritaNoEsPar.valoracionBase())
            }

            it("si la figurita tiene Nivel de Impresion Media o Alta disminuye su valoracion base") {
                //Arrange
                val figuritaNivelImpresionBaja = Figurita(1, NivelImpresion.BAJA, jugadorDelantero)
                val figuritaNivelImpresionMedia = Figurita(1, NivelImpresion.MEDIA, jugadorDelantero)
                val figuritaNivelImpresionAlta = Figurita(1, NivelImpresion.ALTA, jugadorDelantero)
                //Assert
                figuritaNivelImpresionMedia.valoracionBase().shouldBeLessThan(figuritaNivelImpresionBaja.valoracionBase())
                figuritaNivelImpresionAlta.valoracionBase().shouldBeLessThan(figuritaNivelImpresionBaja.valoracionBase())
            }
        }

        describe("valoracion figurita: suma valoracion base mas valoracion jugador"){

            it("figurita de valoracion base sin cambios y su jugador es delantero de seleccion campeona"){
                //Arrange
                val figuritaValorBaseSinCambiosConDelantero = Figurita(1, NivelImpresion.BAJA, jugadorDelantero)
                //Assert
                figuritaValorBaseSinCambiosConDelantero.valoracionBase().shouldBe(100)
                figuritaValorBaseSinCambiosConDelantero.valoracionJugador().shouldBe(300)
                figuritaValorBaseSinCambiosConDelantero.valoracion().shouldBe(400)
            }
        }
    }
})
