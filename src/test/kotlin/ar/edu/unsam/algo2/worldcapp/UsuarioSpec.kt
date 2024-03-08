package ar.edu.unsam.algo2.worldcapp

import Posicion
import ar.edu.unsam.algo3.domain.*
import delantero
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class UsuarioSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    fun crearUsuario() =
        Usuario("username",
            "Nombre",
            "Apellido",
            LocalDate.now().minusMonths(12*30),
            "email@emial.com",
            Domicilio(-34.61315,-58.37723) //Coordenadas de Buenos Aires)
        )

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
    val jugadorDefensor = crearJugador(delantero, seleccionArgentina)
    val jugadorLeyendaArgentina = Jugador("Lionel", "Messi", LocalDate.parse("1987-06-24"), 10, seleccionArgentina, 2005, 1.70, 72.0, delantero, 40000000.00, true)
    val jugadorPromesaArgentina = Jugador("Lionel JR", "Messi", LocalDate.now().minusMonths(12*(MAX_EDAD_JUGADOR_PROMESA-1).toLong()), 8, seleccionArgentina, LocalDate.now().year - 1, 1.70, 72.0, delantero, MAX_COTIZACION_JUGADOR)

    //Figuritas
    val figuritaDelantero = Figurita(10, NivelImpresion.BAJA, jugadorDelantero)
    val figuritaDefensor = Figurita(18, NivelImpresion.MEDIA, jugadorDefensor)
    val figuritaLeyendaArgentina = Figurita(50, NivelImpresion.BAJA, jugadorLeyendaArgentina)
    val figuritaPromesaArgentina = Figurita(40, NivelImpresion.BAJA, jugadorPromesaArgentina)

    //Usuarios
    val usuarioSolicitante = crearUsuario()
    val usuarioProveedor = crearUsuario()
    val tipoDeUsuarioDesprendido = TipoDeUsuarioDesprendido(usuarioProveedor)
    usuarioProveedor.tipoDeUsuario = tipoDeUsuarioDesprendido
    usuarioSolicitante.agregarFiguritaFaltante(figuritaPromesaArgentina)
    usuarioProveedor.agregarFiguritaRepetida(figuritaPromesaArgentina)

    describe("Test de usuarios") {

        it("edad del usuario") {
            //Arrange
            val usuarioGenerico = crearUsuario()
            //Assert
            usuarioGenerico.edad().shouldBe(30)
        }

        describe("figuritas faltantes y repetidas"){

            it("se agrega figurita faltante si no está repetida") {
                //Arrange
                //Usuario
                val usuarioGenerico = crearUsuario()
                //Figurita
                val figuritaFaltante = figuritaDelantero
                //Act
                usuarioGenerico.agregarFiguritaFaltante(figuritaFaltante)
                //Assert
                assertSoftly {
                    usuarioGenerico.figuritasFaltantes().size.shouldBe(1)
                    usuarioGenerico.figuritasFaltantes().shouldContain(figuritaFaltante)
                    usuarioGenerico.figuritasRepetidas().shouldNotContain(figuritaFaltante)
                }
            }

            it("no se agrega figurita faltante si está repetida") {
                //Arrange
                //Usuario
                val usuarioGenerico = crearUsuario()
                //Figurita
                val figuritaFaltante = figuritaDefensor
                //Act
                usuarioGenerico.agregarFiguritaRepetida(figuritaFaltante)
                //Assert
                assertSoftly {
                    shouldThrowMessage("Error: Figurita Repetida") {
                        usuarioGenerico.agregarFiguritaFaltante(
                            figuritaFaltante
                        )
                    }
                    usuarioGenerico.figuritasFaltantes().size.shouldBe(0)
                }
            }

            it("se agrega figurita repetida si no está faltante") {
                //Arrange
                //Usuario
                val usuarioGenerico = crearUsuario()
                //Figurita
                val figuritaRepetida = figuritaDefensor
                //Act
                usuarioGenerico.agregarFiguritaRepetida(figuritaRepetida)
                //Assert
                assertSoftly {
                    usuarioGenerico.figuritasRepetidas().size.shouldBe(1)
                    usuarioGenerico.figuritasRepetidas().shouldContain(figuritaRepetida)
                    usuarioGenerico.figuritasFaltantes().shouldNotContain(figuritaRepetida)
                }
            }

            it("no se agrega figurita repetida si está faltante") {
                //Arrange
                //Usuario
                val usuarioGenerico = crearUsuario()
                //Figurita
                val figuritaRepetida = figuritaDefensor
                //Act
                usuarioGenerico.agregarFiguritaFaltante(figuritaRepetida)
                //Assert
                assertSoftly {
                    shouldThrowMessage("Error: Figurita Faltante") {
                        usuarioGenerico.agregarFiguritaRepetida(
                            figuritaRepetida
                        )
                    }
                    usuarioGenerico.figuritasRepetidas().size.shouldBe(0)
                }
            }
        }

        describe("domicilios de usuarios"){

            it("usuario cercano"){
                //Arrange
                //Usuario Local (El que hace las consultas)
                //Coordenadas YPF 25 de Mayo
                val domicilioUsuarioLocal = Domicilio(ubicacionGeograficaX = -34.57822561988331, ubicacionGeograficaY = -58.529490581155216)
                val usuarioLocal = crearUsuario()
                usuarioLocal.setDomicilio(domicilioUsuarioLocal)
                usuarioLocal.maxDistanciaConOtroUsuario = 5
                //Usuario Cercano
                //Coordenadas Anchorena
                val domicilioUsuarioCercano = Domicilio(-34.5831457947386, -58.536426773029476)
                val usuarioCercano = crearUsuario()
                usuarioCercano.setDomicilio(domicilioUsuarioCercano)
                //Assert
                usuarioLocal.usuarioEsCercano(usuarioCercano).shouldBeTrue()
            }

            it("usuario lejano"){
                //Arrange
                //Usuario Local (El que hace las consultas)
                //Coordenadas YPF 25 de Mayo
                val domicilioUsuarioLocal = Domicilio(ubicacionGeograficaX = -34.57822561988331, ubicacionGeograficaY = -58.529490581155216)
                val usuarioLocal = crearUsuario()
                usuarioLocal.setDomicilio(domicilioUsuarioLocal)
                usuarioLocal.maxDistanciaConOtroUsuario = 6
                //Usuario Cercano
                //Coordenadas Obelisco
                val domicilioUsuarioLejano = Domicilio(-34.60326767667389, -58.381539931257294)
                val usuarioLejano = crearUsuario()
                usuarioLejano.setDomicilio(domicilioUsuarioLejano)
                //Assert
                usuarioLocal.usuarioEsCercano(usuarioLejano).shouldBeTrue()
            }
        }

        describe("usuario solicita una figurita"){

            it("usuario solicita figurita exitosamente"){
                //Assert
                usuarioSolicitante.solicitar(usuarioProveedor, figuritaPromesaArgentina).shouldBeTrue()
            }

            it("usuario solicita figurita no faltante"){
                //Assert
                usuarioSolicitante.solicitar(usuarioProveedor, figuritaLeyendaArgentina).shouldBeFalse()
            }

            it("usuario solicita figurita a proveedor lejano"){
                //Arrange
                val domicilioUsuarioLejano = Domicilio(-34.60326767667389, -58.381539931257294) //dir obelisco
                val usuarioLejano = crearUsuario()
                usuarioLejano.setDomicilio(domicilioUsuarioLejano)
                usuarioLejano.tipoDeUsuario = tipoDeUsuarioDesprendido
                usuarioLejano.agregarFiguritaRepetida(figuritaPromesaArgentina)
                //Assert
                usuarioSolicitante.solicitar(usuarioLejano, figuritaPromesaArgentina).shouldBeTrue()
            }

            it("usuario proveedor no tiene figurita en regalables"){
                //Arrange
                val usuarioPar = crearUsuario()
                usuarioPar.agregarFiguritaRepetida(figuritaPromesaArgentina)
                //Assert
                usuarioSolicitante.solicitar(usuarioPar, figuritaPromesaArgentina).shouldBeFalse()
            }
        }
    }
})