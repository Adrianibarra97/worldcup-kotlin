package ar.edu.unsam.algo2.worldcapp

import Posicion
import ar.edu.unsam.algo3.domain.*
import delantero
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class JugadorSpec: DescribeSpec({

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

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 3)

    describe("Test de jugadores"){

        it("jugador es lider"){
            //Arrange
            val jugadorLider = crearJugador(delantero, seleccionArgentina)
            //Act
            jugadorLider.cambiarSiEsLider()
            //Assert
            jugadorLider.esLider().shouldBeTrue()
        }

        it("jugador no es lider"){
            //Arrange
            val jugadorNoLider = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorNoLider.esLider().shouldBeFalse()
        }

        it("pais del jugador debe ser las 3 primeras letras del pais de su seleccion"){
            //Arrange
            val jugadorDelanteroArgentina = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorDelanteroArgentina.pais().shouldBeEqualComparingTo("Arg")
        }

        it("edad del jugador"){
            //Arrange
            val jugadorDelanteroArgentina = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorDelanteroArgentina.edad().shouldBe(MAX_EDAD_JUGADOR_PROMESA)
        }

        it("jugador es alto"){
            //Arrange
            val jugadorAlto = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorAlto.esAlto().shouldBeTrue()
        }

        it("jugador no es alto"){
            //Arrange
            val jugadorNoAlto = crearJugador(delantero, seleccionArgentina)
            //Act
            jugadorNoAlto.altura = MIN_ALTURA_JUGADOR_ALTO - 0.1
            //Assert
            jugadorNoAlto.esAlto().shouldBeFalse()
        }

        it("años en la selección"){
            //Arrange
            val jugadorDelanteroArgentina = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorDelanteroArgentina.aniosEnLaSeleccion().shouldBe(LocalDate.now().year - jugadorDelanteroArgentina.anioDebut)
        }

        it("jugador es ligero"){
            //Arrange
            val jugadorLigero = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorLigero.esLigero().shouldBeTrue()
        }

        it("jugador no es ligero"){
            //Arrange
            val jugadorNoLigero = crearJugador(delantero, seleccionArgentina)
            //Act
            jugadorNoLigero.peso = RANGO_PESO_JUGADOR_LIGERO.endInclusive + 1.00
            //Assert
            jugadorNoLigero.esLigero().shouldBeFalse()
        }

        it("jugador es leyenda"){
            //Arrange
            val jugadorLeyenda = crearJugador(delantero, seleccionArgentina)
            //Act
            jugadorLeyenda.anioDebut = LocalDate.now().year - (MIN_ANTIGUEDAD_SELECCION_JUGADOR_LEYENDA + 1)
            jugadorLeyenda.cambiarSiEsLider()
            //Assert
            jugadorLeyenda.esLeyenda().shouldBeTrue()
        }

        it("jugador no es leyenda"){
            //Arrange
            val jugadorNoLeyenda = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorNoLeyenda.esLeyenda().shouldBeFalse()
        }

        it("jugador es promesa"){
            //Arrange
            val jugadorPromesa = crearJugador(delantero, seleccionArgentina)
            //Act
            jugadorPromesa.fechaNacimiento = LocalDate.now().minusMonths(12*(MAX_EDAD_JUGADOR_PROMESA-1).toLong())
            jugadorPromesa.anioDebut = LocalDate.now().year - (MAX_ANTIGUEDAD_SELECCION_JUGADOR_PROMESA-1)
            //Assert
            jugadorPromesa.esPromesa().shouldBeTrue()
        }

        it("jugador no es promesa"){
            //Arrange
            val jugadorNoPromesa = crearJugador(delantero, seleccionArgentina)
            //Assert
            jugadorNoPromesa.esPromesa().shouldBeFalse()
        }
    }
})