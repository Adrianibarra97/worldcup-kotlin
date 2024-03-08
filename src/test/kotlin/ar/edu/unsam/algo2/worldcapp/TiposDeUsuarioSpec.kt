package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import arquero
import defensor
import delantero
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TiposDeUsuarioSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    fun crearUsuario(tipoDeUsuario: TipoDeUsuario = TipoDeUsuarioPar()) =
        Usuario("username",
            "Nombre",
            "Apellido",
            LocalDate.now().minusMonths(12*MIN_EDAD_TIPO_USUARIO_CAMBIANTE.toLong()),
            "email@emial.com",
            tipoDeUsuario = tipoDeUsuario,
            domicilioUsuario = Domicilio(-34.61315,-58.37723) //Coordenadas de Buenos Aires)
        )

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 3)
    val seleccionBrasil = Seleccion("Brasil", "CONMEBOL", 5)
    val seleccionAlemania = Seleccion("Alemania", "UEFA", 4)

    //Jugadores
    val jugadorLeyendaArgentina = Jugador("Lionel", "Messi", LocalDate.parse("1987-06-24"), 10, seleccionArgentina, 2005, 1.70, 72.0, delantero, 40000000.00, true)
    val jugadorPromesaArgentina = Jugador("Lionel JR", "Messi", LocalDate.now().minusMonths(12*(MAX_EDAD_JUGADOR_PROMESA-1).toLong()), 8, seleccionArgentina, LocalDate.now().year - 1, 1.70, 72.0, delantero, MAX_COTIZACION_JUGADOR)
    val jugadorDefensorArgentina = Jugador("Nicolas","Otamendi", LocalDate.parse("1988-02-12"), 19, seleccionArgentina, 2009, 1.83, 82.0, defensor, 18000000.00)
    val jugadorArqueroArgentina = Jugador("Emiliano","Martínez", LocalDate.parse("1992-09-02"), 23, seleccionArgentina, 2021, 1.95, 88.0, arquero, 15000000.00)
    val jugadorDelanteroArgentina = Jugador("Julián", "Álvarez", LocalDate.parse("2000-01-31"), 9, seleccionArgentina, 2021, 1.70, 71.0, delantero, 13000000.00)
    val jugadorDelanteroBrasil = Jugador("Vinicius JR", "Paixão de Oliveira", LocalDate.parse("2000-07-02"), 18, seleccionBrasil, 2019, 1.76, 73.0, delantero, 30000000.00)
    val jugadorDelanteroAlemania = Jugador("Miroslav", "Klose", LocalDate.parse("1978-06-09"), 11, seleccionAlemania, 2001, 1.82, 84.0, delantero, 35000000.00)

    //Figuritas
    val figuritaLeyendaArgentina = Figurita(50, NivelImpresion.BAJA, jugadorLeyendaArgentina)
    val figuritaPromesaArgentina = Figurita(40, NivelImpresion.BAJA, jugadorPromesaArgentina)
    val figuritaDefensorArgentina = Figurita(8, NivelImpresion.ALTA, jugadorDefensorArgentina)
    val figuritaArqueroArgentina = Figurita(13, NivelImpresion.MEDIA, jugadorArqueroArgentina)
    val figuritaDelanteroArgentina = Figurita(10, NivelImpresion.MEDIA, jugadorDelanteroArgentina)
    val figuritaDelanteroBrasil = Figurita(25, NivelImpresion.ALTA, jugadorDelanteroBrasil)
    val figuritaDelanteroAlemania = Figurita(31, NivelImpresion.MEDIA, jugadorDelanteroAlemania)

    val listaFiguritas = mutableListOf<Figurita>(figuritaLeyendaArgentina,figuritaPromesaArgentina,figuritaDefensorArgentina,
        figuritaArqueroArgentina,figuritaDelanteroArgentina,figuritaDelanteroBrasil,figuritaDelanteroAlemania)

    describe("Test de tipos de usuarios") {

        describe("tipo de usuario par") {
            it("no quiere regalar figurita si es número par o número de camiseta par o copas del mundo par") {
                //Arrange
                val usuarioPar = crearUsuario(TipoDeUsuarioPar())
                val figuritaParJugadorCamisetaImparCopasDelMundoImpar = figuritaDelanteroArgentina
                val figuritaImparJugadorCamisetaParCopasDelMundoImpar = figuritaDelanteroBrasil
                val figuritaImparJugadorCamisetaImparCopasDelMundoPar = figuritaDelanteroAlemania
                // Assert
                assertSoftly {
                    usuarioPar.tipoDeUsuario.quiereRegalar(figuritaParJugadorCamisetaImparCopasDelMundoImpar)
                        .shouldBeFalse()
                    usuarioPar.tipoDeUsuario.quiereRegalar(figuritaImparJugadorCamisetaParCopasDelMundoImpar)
                        .shouldBeFalse()
                    usuarioPar.tipoDeUsuario.quiereRegalar(figuritaImparJugadorCamisetaImparCopasDelMundoPar)
                        .shouldBeFalse()
                }
            }
            it("quiere regalar figurita con número impar, número de camiseta impar y copas del mundo impar") {
                //Arrange
                val usuarioPar = crearUsuario(TipoDeUsuarioPar())
                val figuritaImparJugadorCamisetaImparCopasDelMundoImpar = figuritaArqueroArgentina
                // Assert
                usuarioPar.tipoDeUsuario.quiereRegalar(figuritaImparJugadorCamisetaImparCopasDelMundoImpar).shouldBeTrue()
            }
        }

        describe("tipo de usuario nacionalista") {

            it("no quiere regalar figuritas de determinadas selecciones") {
                //Arrange
                val usuarioNacionalista = crearUsuario(TipoDeUsuarioNacionalista(listOf<Seleccion>(seleccionArgentina)))
                val figuritaArgentina = figuritaDefensorArgentina
                //Assert
                usuarioNacionalista.tipoDeUsuario.quiereRegalar(figuritaArgentina).shouldBeFalse()
            }

            it("quiere regalar figuritas de determinadas selecciones") {
                //Arrange
                val usuarioNacionalista = crearUsuario(TipoDeUsuarioNacionalista(listOf<Seleccion>(seleccionArgentina)))
                val figuritaBrasil = figuritaDelanteroBrasil
                //Assert
                usuarioNacionalista.tipoDeUsuario.quiereRegalar(figuritaBrasil).shouldBeTrue()
            }
        }

        describe("tipo de usuario conservador") {

            it("no quiere regalar figurita con nivel de impresion alta si el album no está lleno") {
                //Arrange
                val usuarioConservador = crearUsuario()
                val conservador = TipoDeUsuarioConservador(usuarioConservador)
                usuarioConservador.tipoDeUsuario = conservador
                val figuritaImpresionAlta = figuritaArqueroArgentina
                //Act
                usuarioConservador.agregarFiguritaFaltante(figuritaDelanteroArgentina)
                //Assert
                usuarioConservador.tipoDeUsuario.quiereRegalar(figuritaImpresionAlta).shouldBeFalse()
            }

            it("quiere regalar figurita con nivel de impresion alta si el album está lleno") {
                //Arrange
                val usuarioConservador = crearUsuario()
                val tipoDeUsuarioConservador = TipoDeUsuarioConservador(usuarioConservador)
                usuarioConservador.tipoDeUsuario = tipoDeUsuarioConservador
                val figuritaImpresionAlta = figuritaDefensorArgentina
                //Assert
                usuarioConservador.tipoDeUsuario.quiereRegalar(figuritaImpresionAlta).shouldBeTrue()
            }
        }

        describe("tipo de usuario fanatico"){

            it("no quiere regalar las figuritas de su jugador favorito o de jugador leyenda") {
                //Arrange
                val usuarioFanatico = crearUsuario(TipoDeUsuarioFanatico(jugadorDelanteroBrasil))
                //Assert
                assertSoftly {
                    usuarioFanatico.tipoDeUsuario.quiereRegalar(figuritaDelanteroBrasil).shouldBeFalse()
                    usuarioFanatico.tipoDeUsuario.quiereRegalar(figuritaLeyendaArgentina).shouldBeFalse()
                }
            }

            it("quiere regalar las figuritas que no son de su jugador favorito o de jugador leyenda") {
                //Arrange
                val usuarioFanatico = crearUsuario(TipoDeUsuarioFanatico(jugadorDelanteroBrasil))
                //Assert
                usuarioFanatico.tipoDeUsuario.quiereRegalar(figuritaDelanteroArgentina).shouldBeTrue()
            }
        }

        describe("tipo de usuario desprendido"){

            it("no quiere regalar figurita que no sea repetida") {
                //Arrange
                val usuarioDesprendido = crearUsuario()
                val tipoDeUsuarioDesprendido = TipoDeUsuarioDesprendido(usuarioDesprendido)
                usuarioDesprendido.tipoDeUsuario = tipoDeUsuarioDesprendido
                //Assert
                usuarioDesprendido.tipoDeUsuario.quiereRegalar(figuritaDefensorArgentina).shouldBeTrue()
            }

            it("quiere regalar figurita que sea repetida") {
                //Arrange
                val usuarioDesprendido = crearUsuario()
                val tipoDeUsuarioDesprendido = TipoDeUsuarioDesprendido(usuarioDesprendido)
                usuarioDesprendido.tipoDeUsuario = tipoDeUsuarioDesprendido
                //Act
                usuarioDesprendido.agregarFiguritaRepetida(figuritaDefensorArgentina)
                //Assert
                usuarioDesprendido.tipoDeUsuario.quiereRegalar(figuritaDefensorArgentina).shouldBeTrue()
            }
        }

        describe("tipo de usuario apostador") {

            it("no quiere regalar si la figurita está onfire") {
                //Arrange
                val usuarioApostador = crearUsuario(TipoDeUsuarioApostador())
                val figuritaOnFireNoJugadorPromesa = figuritaDelanteroAlemania
                //Act
                figuritaOnFireNoJugadorPromesa.cambiarEstadoOnFire()
                //Assert
                assertSoftly {
                    figuritaOnFireNoJugadorPromesa.estaOnFire().shouldBeTrue()
                    figuritaOnFireNoJugadorPromesa.jugador.esPromesa().shouldBeFalse()
                    usuarioApostador.tipoDeUsuario.quiereRegalar(figuritaOnFireNoJugadorPromesa).shouldBeFalse()
                }
            }

            it("no quiere regalar si la figurita es de jugador promesa") {
                //Arrange
                val usuarioApostador = crearUsuario(TipoDeUsuarioApostador())
                val figuritaNoOnFireJugadorPromesa = figuritaPromesaArgentina
                //Assert
                assertSoftly {
                    figuritaNoOnFireJugadorPromesa.estaOnFire().shouldBeFalse()
                    figuritaNoOnFireJugadorPromesa.jugador.esPromesa().shouldBeTrue()
                    usuarioApostador.tipoDeUsuario.quiereRegalar(figuritaNoOnFireJugadorPromesa).shouldBeFalse()
                }
            }

            it("quiere regalar si la figurita no está onfire y no es jugador promesa") {
                //Arrange
                val usuarioApostador = crearUsuario(TipoDeUsuarioApostador())
                val figuritaNoOnFireNoJugadorPromesa = figuritaDefensorArgentina
                //Assert
                assertSoftly {
                    figuritaNoOnFireNoJugadorPromesa.estaOnFire().shouldBeFalse()
                    figuritaNoOnFireNoJugadorPromesa.jugador.esPromesa().shouldBeFalse()
                    usuarioApostador.tipoDeUsuario.quiereRegalar(figuritaNoOnFireNoJugadorPromesa).shouldBeTrue()
                }
            }

        }

        describe("tipo de usuario interesado") {

            it("no quiere regalar alguna figurita repetida que esté en su top de figuritas repetidas de mayor valoracion") {
                //Arrange
                val usuarioInteresado = crearUsuario()
                val tipoDeUsuarioInteresado = TipoDeUsuarioInteresado(usuarioInteresado)
                usuarioInteresado.tipoDeUsuario = tipoDeUsuarioInteresado
                //Act
                listaFiguritas.forEach{ usuarioInteresado.agregarFiguritaRepetida(it) }
                val figuritaMayorValoracion = usuarioInteresado.figuritasRepetidas().maxBy{ it.valoracion() }
                //Assert
                usuarioInteresado.tipoDeUsuario.quiereRegalar(figuritaMayorValoracion).shouldBeFalse()
            }

            it("quiere regalar alguna figurita repetida que no esté en su top de figuritas repetidas de mayor valoracion") {
                //Arrange
                val usuarioInteresado = crearUsuario()
                val tipoDeUsuarioInteresado = TipoDeUsuarioInteresado(usuarioInteresado)
                usuarioInteresado.tipoDeUsuario = tipoDeUsuarioInteresado
                //Act
                listaFiguritas.forEach{ usuarioInteresado.agregarFiguritaRepetida(it) }
                val figuritaMenorValoracion = usuarioInteresado.figuritasRepetidas().minBy{ it.valoracion() }
                //Assert
                usuarioInteresado.tipoDeUsuario.quiereRegalar(figuritaMenorValoracion).shouldBeTrue()
            }

        }

        describe("tipo de usuario cambiante") {

            it("si la edad del jugador es menor que la condicion, el usuario regala la figurita como un tipo de usuario desprendido") {
                //Arrange
                val usuarioCambiante = crearUsuario()
                val tipoDeUsuarioCambiante = TipoDeUsuarioCambiante(usuarioCambiante)
                usuarioCambiante.tipoDeUsuario = tipoDeUsuarioCambiante
                //Assert
                assertSoftly {
                    usuarioCambiante.edad().shouldBe(25)
                    usuarioCambiante.tipoDeUsuario.quiereRegalar(figuritaPromesaArgentina).shouldBeTrue()
                    usuarioCambiante.agregarFiguritaRepetida(figuritaPromesaArgentina)
                    usuarioCambiante.tipoDeUsuario.quiereRegalar(figuritaPromesaArgentina).shouldBeTrue()
                }
            }

            it("si la edad del jugador es mayor que la condicion, el usuario regala la figurita como un tipo de usuario conservador") {
                //Arrange
                val usuarioCambiante = crearUsuario()
                val tipoDeUsuarioCambiante = TipoDeUsuarioCambiante(usuarioCambiante)
                usuarioCambiante.tipoDeUsuario = tipoDeUsuarioCambiante
                val figuraNivelImpresionBaja = figuritaPromesaArgentina
                val figuraNivelImpresionMedia = figuritaArqueroArgentina
                val figuraNivelImpresionAlta = figuritaDefensorArgentina
                //Act
                usuarioCambiante.fechaNacimiento = LocalDate.now().minusMonths(12*(MIN_EDAD_TIPO_USUARIO_CAMBIANTE+1).toLong())
                //Assert
                assertSoftly {
                    usuarioCambiante.edad().shouldBe(MIN_EDAD_TIPO_USUARIO_CAMBIANTE+1)
                    usuarioCambiante.tipoDeUsuario.quiereRegalar(figuraNivelImpresionBaja).shouldBeFalse()
                    usuarioCambiante.tipoDeUsuario.quiereRegalar(figuraNivelImpresionMedia).shouldBeFalse()
                    usuarioCambiante.tipoDeUsuario.quiereRegalar(figuraNivelImpresionAlta).shouldBeTrue()
                }
            }
        }

    }
})
