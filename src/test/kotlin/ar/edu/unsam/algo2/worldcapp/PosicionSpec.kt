package ar.edu.unsam.algo2.worldcapp

import Posicion
import PosicionPolivalentes
import ar.edu.unsam.algo3.domain.*
import arquero
import defensor
import delantero
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import mediocampista
import java.time.LocalDate

class PosicionSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    fun crearJugador(posicion: Posicion, seleccion: Seleccion) =
        Jugador("Nombre",
            "Apellido",
            LocalDate.now().minusMonths(12*MAX_EDAD_JUGADOR_PROMESA.toLong()),
            10,
            seleccion,
            LocalDate.now().year - MAX_ANTIGUEDAD_SELECCION_JUGADOR_PROMESA,
            MIN_ALTURA_JUGADOR_ALTO,
            RANGO_PESO_JUGADOR_LIGERO.start,
            posicion,
            MAX_COTIZACION_JUGADOR)

    //Posiciones Polivalentes
    fun crearPosicionPolivalente( posiciones : MutableSet<Posicion>) = PosicionPolivalentes(posiciones)

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 3)
    val seleccionCroacia = Seleccion("Croacia", "UEFA", 0)

    describe("Test de posiciones") {

        it("jugador arquero altura menor a condición") {
            //Arrange
            val arqueroNoAlto = crearJugador(arquero, seleccionArgentina)
            //Act
            arqueroNoAlto.altura = MIN_ALTURA_JUGADOR_ALTO - 0.1
            //Assert
            arqueroNoAlto.valoracion().shouldBe(arquero.valoracionBase())
        }

        it("jugador arquero altura mayor a condición") {
            //Arrange
            val arqueroAlto = crearJugador(arquero, seleccionArgentina)
            //Assert
            arqueroAlto.valoracion().shouldBeGreaterThan(arquero.valoracionBase())
        }

        it("jugador delantero seleccion campeona") {
            //Arrange
            val delanteroCampeon = crearJugador(delantero, seleccionArgentina)
            //Assert
            delanteroCampeon.valoracion().shouldBeGreaterThan(delantero.valoracionBase())
        }

        it("jugador delantero seleccion no campeona") {
            //Arrange
            val delanteroNoCampeon = crearJugador(delantero, seleccionCroacia)
            //Assert
            delanteroNoCampeon.valoracion().shouldBe(delantero.valoracionBase())
        }

        it("jugador defensor lider") {
            //Arrange
            val defensorLider = crearJugador(defensor, seleccionCroacia)
            //Act
            defensorLider.cambiarSiEsLider()
            //Assert
            defensorLider.valoracion().shouldBeGreaterThan(defensor.valoracionBase())
        }

        it("jugador defensor no lider") {
            //Arrange
            val defensorNoLider = crearJugador(defensor, seleccionCroacia)
            //Assert
            defensorNoLider.valoracion().shouldBe(defensor.valoracionBase())
        }

        it("jugador mediocampista ligero") {
            //Arrange
            val mediocampistaLigero = crearJugador(mediocampista, seleccionCroacia)
            //Assert
            mediocampistaLigero.valoracion().shouldBeGreaterThan(mediocampista.valoracionBase())

        }

        it("jugador mediocampista no ligero") {
            //Arrange
            val mediocampistaNoLigero = crearJugador(mediocampista, seleccionCroacia)
            //Act
            mediocampistaNoLigero.peso = RANGO_PESO_JUGADOR_LIGERO.start - 0.1
            //Assert
            mediocampistaNoLigero.valoracion().shouldBe(mediocampista.valoracionBase())

        }
    }

    describe("Test de posiciones polivalentes") {

        //Posiciones Polivalentes
        val posicionDelanteroYMedioCampista = PosicionPolivalentes( mutableSetOf(delantero, mediocampista) )

        //JugadorPolivalente
        val jugadorPolivalenteNoLeyenda = crearJugador(posicionDelanteroYMedioCampista, seleccionArgentina)
        val jugadorPolivalenteLeyenda = Jugador("nombre", "apellido", LocalDate.now().minusMonths(12*30), 10, seleccionArgentina, 2005, 1.70, 70.0, posicionDelanteroYMedioCampista, 40000000.00, true)

        it("Jugador Mediocampista y Delantero: no es leyenda ni promesa"){
            //Arrange
            val jugadorPolivalenteNoLeyendaNoPromesa = jugadorPolivalenteNoLeyenda
            //Assert
            jugadorPolivalenteNoLeyendaNoPromesa.valoracion().shouldBe(175)

        }

        it("Jugador leyenda de 30 anios de edad, Mediocampista ligero de 70kg y Delantero de una selección campeona del mundo y camiseta Nro 10"){
            //Assert
            jugadorPolivalenteLeyenda.valoracion().shouldBe(405)

        }

        it("Jugador intenta agregar una posicion que ya tiene"){
            //Assert
            assertSoftly{
                shouldThrowMessage("alguna de las posiciones ya esta agregada!") {
                    posicionDelanteroYMedioCampista.agregarPosicion(
                        delantero
                    )
                }
                jugadorPolivalenteLeyenda.valoracion().shouldBe(405)
            }
        }
    }
})