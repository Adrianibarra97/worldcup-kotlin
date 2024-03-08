package ar.edu.unsam.algo2.worldcapp

import Posicion
import ar.edu.unsam.algo3.domain.*
import delantero
import io.kotest.assertions.assertSoftly
import java.time.LocalDate
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.matchers.collections.shouldContain

class PuntoDeVentaSpec: DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    //Domicilio Punto De Venta
    val domicilioPuntoDeVenta = Domicilio(-34.613160,-58.37722)
    //Domicilio Cercano
    val domicilioCerca = Domicilio(-34.598627,-58.383036)
    //Domicilio Lejano
    val domicilioLejos = Domicilio(-34.603722,-58.489267)

    //Fecha cercana < 10 dias
    val fechaCercana = LocalDate.now().minusDays(9)
    //Fecha lejana > 10 dias
    val fechaLejana = LocalDate.now().minusDays(11)
    describe("Tests Kioskos") {
        //Kiosko
        val kioskoGenerico = Kiosko(1,"Generico","Kiosko")
        kioskoGenerico.setDomicilio(domicilioPuntoDeVenta)
        kioskoGenerico.agregarStock(100)

        it("Kiosco atendido por sus dueños con envio cerca") {
            //Arrange
            kioskoGenerico.cambiarAtendidoPorDuenio()
            //Assert
            assertSoftly{
                kioskoGenerico.atendidoPorDuenio().shouldBeTrue()
                kioskoGenerico.importeACobrar(domicilioCerca,5).toInt().shouldBe(2035)
            }
        }
        it("Kiosco atendido por empleados con envio lejos") {
            //Assert
            assertSoftly{
                kioskoGenerico.atendidoPorDuenio().shouldBeFalse() //Default
                kioskoGenerico.importeACobrar(domicilioLejos,5).shouldBeGreaterThan(2035.00)
            }
        }
        it("Kiosco atendido por sus duenios con envio cerca sin disponibilidad") {
            //Arrange
            kioskoGenerico.cambiarAtendidoPorDuenio()
            kioskoGenerico.quitarStock(kioskoGenerico.stockDeSobres)
            //Assert
            assertSoftly{
                kioskoGenerico.atendidoPorDuenio().shouldBeTrue()
                shouldThrowMessage("No hay disponibilidad de stock!") {
                    kioskoGenerico.importeACobrar(
                        domicilioCerca,
                        kioskoGenerico.stockDeSobres + 1
                    )
                }
            }
        }
    }

    describe("Tests Librerías") {
        val libreriaGenerica = Libreria(2,"Generico", "Libreria")
        libreriaGenerica.setDomicilio(domicilioPuntoDeVenta)

        it("Libreria con envios pendientes cerca") {
            //Arrange
            val pedidoCerca = Pedido(1,fechaCercana)
            libreriaGenerica.agregarPedidoPendiente(pedidoCerca)
            libreriaGenerica.agregarStock(100)
            //Assert
            libreriaGenerica.importeACobrar(domicilioCerca,5).shouldBe(9712.5)
        }
        it("Libreria con envios pendientes lejos") {
            //Arrange
            val pedidoLejos = Pedido(1,fechaLejana)
            libreriaGenerica.agregarPedidoPendiente(pedidoLejos)
            libreriaGenerica.agregarStock(100)
            //Assert
            libreriaGenerica.importeACobrar(domicilioCerca,5).shouldBeGreaterThan(2035.0)
        }
        it("Libreria con envios pendientes lejos sin disponibilidad") {
            //Arrange
            val pedidoLejos = Pedido(1,LocalDate.now())
            libreriaGenerica.agregarPedidoPendiente(pedidoLejos)
            libreriaGenerica.agregarStock(1)
            //Assert
            shouldThrowMessage("No hay disponibilidad de stock!"){libreriaGenerica.importeACobrar(domicilioCerca,2)}
        }
    }

    describe("Tests Supermercados") {
        //Supermercado
        val supermercadoGenerico = Supermercado(3,"Generico","Supermercado")
        supermercadoGenerico.setDomicilio(domicilioPuntoDeVenta)

        it("Supermercado agrega promocion") {
            //Arrange
            supermercadoGenerico.agregarPromocion(promocionJueves)
            //Assert
            supermercadoGenerico.promociones().shouldContain(promocionJueves)
        }
        it("Supermercado no puede agregar promocion") {
            //Arrange
            supermercadoGenerico.agregarPromocion(promocionJueves)
            //Assert
            shouldThrowMessage("La promocion ya esta agregada!") {supermercadoGenerico.agregarPromocion(promocionJueves)}
        }
        it("Supermercado promo jueves con envio cerca") {
            //Arrange
            promocionJueves.setDiaPromocion(LocalDate.of(2021,1,14))
            supermercadoGenerico.agregarStock(100)
            supermercadoGenerico.agregarPromocion(promocionJueves)
            //Assert
            supermercadoGenerico.importeACobrar(domicilioCerca,5).shouldBe(1665)
        }

        it("Supermercado promo 1 al 10 con envio cerca") {
            //Arrange
            supermercadoGenerico.agregarStock(100)
            supermercadoGenerico.agregarPromocion(unoAlDiezDelMes)
            //Act
            unoAlDiezDelMes.setDiaPromocion(LocalDate.of(2023,8,5))
            //Assert
            supermercadoGenerico.importeACobrar(domicilioCerca,5).shouldBe(1757.5)
        }

        it("Supermercado promo +200 paquetes con envio cerca") {
            //Arrange
            supermercadoGenerico.agregarStock(300)
            supermercadoGenerico.agregarPromocion(compraMayorACantidadPaquetes)
            //Assert
            supermercadoGenerico.importeACobrar(domicilioCerca,201).shouldBe(19343.5)
        }

        it("Supermercado sin promo con envio lejos") {
            //Arrange
            supermercadoGenerico.agregarStock(100)
            //Assert
            println("costo envio" + supermercadoGenerico.costoEnvio(domicilioLejos))
            supermercadoGenerico.importeACobrar(domicilioLejos,10).shouldBe(2700.0)
        }

        it("Supermercado con descuento mayor a 50% -promo jueves +200 paquetes- con domicilio cerca") {
            //Arrange
            supermercadoGenerico.agregarStock(300)
            supermercadoGenerico.agregarPromocion(promocionJueves)
            supermercadoGenerico.agregarPromocion(compraMayorACantidadPaquetes)
            //Assert
            supermercadoGenerico.importeACobrar(domicilioCerca,201).shouldBe(17585)
        }

        it("Supermercado promo jueves con envio lejos sin disponibilidad") {
            //Arrange
            supermercadoGenerico.agregarStock(1)
            supermercadoGenerico.agregarPromocion(promocionJueves)
            //Assert
            shouldThrowMessage("No hay disponibilidad de stock!"){supermercadoGenerico.importeACobrar(domicilioCerca,2)}
        }
    }
})
