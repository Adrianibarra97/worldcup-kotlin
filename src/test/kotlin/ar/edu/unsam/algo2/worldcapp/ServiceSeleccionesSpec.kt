package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.RepositorioSelecciones
import ar.edu.unsam.algo3.service.IServiceExternoSelecciones
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

const val STRING_JSON_SELECCIONES = "[{\"id\":1,\"pais\":\"Argentina\",\"confederacion\":\"CONMEBOL\",\"copasDelMundo\":3,\"copasConfederacion\":15},{\"id\":2,\"pais\":\"Brasil\",\"confederacion\":\"CONMEBOL\",\"copasDelMundo\":5,\"copasConfederacion\":9},{\"pais\":\"Alemania\",\"confederacion\":\"UEFA\",\"copasDelMundo\":4,\"copasConfederacion\":3},{\"id\":3,\"pais\":\"Mexico\",\"confederacion\":\"CONCACAF\",\"copasDelMundo\":0,\"copasConfederacion\":1}]"
class ServiceSeleccionesSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 2)

    describe("Test de service selecciones") {

        it("actualizacion copas del mundo de una seleccion"){
            //Arrange
            val repositorioSelecciones = StubRepositorioSelecciones()
            repositorioSelecciones.create(seleccionArgentina)
            seleccionArgentina.copasDelMundo.shouldBe(2)
            //Act
            repositorioSelecciones.update(seleccionArgentina)
            //Assert
            seleccionArgentina.copasDelMundo.shouldBe(3)
        }
    }
})

//Stub Manual
class StubRepositorioSelecciones: RepositorioSelecciones(){
    override fun getSelecciones() = stubServiceSelecciones.getSelecciones()
}
object stubServiceSelecciones: IServiceExternoSelecciones {
    override fun getSelecciones() = STRING_JSON_SELECCIONES
}
