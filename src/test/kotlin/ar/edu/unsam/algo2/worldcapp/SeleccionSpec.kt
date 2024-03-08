package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class SeleccionSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    describe("Test de selecciones"){

        it("al crear una seleccion con nombre de confederacion valido, se agrega a las selecciones de la confederacion") {
            //Arrange
            val seleccionColombia = Seleccion("Colombia", "CONMEBOL", 0)
            //Assert
            seleccionColombia.getConfederacion().selecciones().shouldContain(seleccionColombia)
        }

        it("al crear una seleccion con nombre de confederacion invalido, arroja mensaje de error") {
            //Assert
            //Creando nueva seleccion con nombre erroneo confederacion
            shouldThrowMessage("La seleccion tiene una confederacion que no existe!", {Seleccion("Uruguay", "CONMEBOLL", 2)})
            }

        it("al cambiar nombre de confederacion invalido, arroja mensaje de error"){
            //Arrange
            val seleccionPeru = Seleccion("Peru", "CONMEBOL", 0)
            //Cambiando el nombre de la confederacion
            shouldThrowMessage("La seleccion tiene una confederacion que no existe!", {seleccionPeru.setConfederacion("CONMEBOLL")})
        }

        it("copas mundiales de una seleccion"){
            //Arrage
            val seleccionVenezuela = Seleccion("Venezuela", "CONMEBOL", 0)
            //Assert
            assertSoftly {
                seleccionVenezuela.copasDelMundo.shouldBe(0)
                seleccionVenezuela.copasDelMundo = 1
                seleccionVenezuela.copasDelMundo.shouldBe(1)
            }
        }

        it("total copas de la confederacion de una seleccion"){
            //Arrange
            val seleccionAlemania = Seleccion("Alemania", "UEFA", 4)
            val seleccionItalia = Seleccion("Italia", "UEFA", 4)
            val seleccionFrancia = Seleccion("Francia", "UEFA", 2)
            val seleccionEspania = Seleccion("España", "UEFA", 1)
            val seleccionInglaterra = Seleccion("Inglaterra", "UEFA", 1)
            val confederacionUEFA = listOf<Seleccion>(
                seleccionAlemania,
                seleccionItalia,
                seleccionFrancia,
                seleccionEspania,
                seleccionInglaterra)
            //Assert
            seleccionAlemania.totalCopasConfederacion().shouldBe(confederacionUEFA.sumOf { it.copasDelMundo })
        }
    }

})

