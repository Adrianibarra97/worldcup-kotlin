package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import delantero
import ar.edu.unsam.algo3.repository.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class RepositorioSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    //Selecciones
    val seleccionArgentina = Seleccion("Argentina", "CONMEBOL", 2)
    val seleccionBrasil = Seleccion("Brasil", "CONMEBOL", 5)
    val seleccionAlemania = Seleccion("Alemania", "UEFA", 4)

    //Jugadores
    val jugadorLeyendaArgentina = Jugador("Lionel", "Messi", LocalDate.parse("1987-06-24"), 10, seleccionArgentina, 2005, 1.70, 72.0, delantero, 40000000.00, true)
    val jugadorPromesaArgentina = Jugador("Lionel JR", "Messi", LocalDate.now().minusMonths(12*(MAX_EDAD_JUGADOR_PROMESA-1).toLong()), 8, seleccionArgentina, LocalDate.now().year - 1, 1.70, 72.0, delantero, MAX_COTIZACION_JUGADOR)
    val jugadorDelanteroArgentina = Jugador("Julián", "Álvarez", LocalDate.parse("2000-01-31"), 9, seleccionArgentina, 2021, 1.70, 71.0, delantero, 13000000.00)

    //Figuritas
    val figuritaLeyendaArgentina = Figurita(50, NivelImpresion.BAJA, jugadorLeyendaArgentina)
    val figuritaPromesaArgentina = Figurita(40, NivelImpresion.BAJA, jugadorPromesaArgentina)
    val figuritaDelanteroArgentina = Figurita(10, NivelImpresion.MEDIA, jugadorDelanteroArgentina)

    //Usuarios
    fun crearUsuario(username: String, nombre:String, apellido:String) =
        Usuario(username,
            nombre,
            apellido,
            LocalDate.now().minusMonths(12*30),
            "email@emial.com",
            Domicilio(-34.61315,-58.37723) //Coordenadas de Buenos Aires)
        )

    describe("Test de repositorio") {

        describe("agregar, eliminar y actualizar objetos en un repositorio"){

            val repositorioFiguritas = RepositorioFiguritas()

            it("agrega o crea un objeto figurita en un repositorio"){
                val figuritaAAgregar = figuritaLeyendaArgentina
                //Act
                repositorioFiguritas.create(figuritaAAgregar)
                //Assert
                repositorioFiguritas.getById(0) shouldBe figuritaAAgregar
            }

            describe("borrar un objeto figurita en un repositorio"){

                it("lanza una excepcion si se intenta borrar un objeto figurita que no en un repositorio"){
                    //Assert
                    shouldThrow<NotFoundException> { repositorioFiguritas.delete(figuritaLeyendaArgentina) }
                    //shouldThrowMessage("El objeto a eliminar no esta en el repositorio", {repositorioFiguritas.delete(figuritaLeyendaArgentina)})
                }

                it("borra un objeto figurita ya agregado en un repositorio"){
                    //Arrange
                    val figuritaAAgregar = figuritaPromesaArgentina
                    //Act
                    repositorioFiguritas.create(figuritaAAgregar)
                    repositorioFiguritas.delete(figuritaAAgregar)
                    //Assert
                    repositorioFiguritas.getAll() shouldNotContain figuritaAAgregar
                }

                it("despues de borrar un objeto figurita en un repositorio, el nuevo objeto figurita agregado debe tener un id valido"){
                    //Arrange
                    val figuritaPrimeraEnAgregar = figuritaPromesaArgentina
                    val figuritaSegundaEnAgregar = figuritaDelanteroArgentina
                    //Act
                    repositorioFiguritas.create(figuritaPrimeraEnAgregar)
                    repositorioFiguritas.delete(figuritaPrimeraEnAgregar)
                    repositorioFiguritas.create(figuritaDelanteroArgentina)
                    //Assert
                    figuritaSegundaEnAgregar.id shouldBeGreaterThan figuritaPrimeraEnAgregar.id
                }
            }

            describe("actualizar un objeto figurita en un repositorio"){

                it("lanza una excepcion si se intenta actualizar un objeto figurita que no existe en un repositorio"){
                    //Assert
                    shouldThrow<NotFoundException> { repositorioFiguritas.update(figuritaPromesaArgentina)  }
                }
            }
        }

        describe("busqueda de figurita en un repositorio") {
            val repositorioFiguritas = RepositorioFiguritas()
            repositorioFiguritas.create(figuritaLeyendaArgentina)
            repositorioFiguritas.create(figuritaPromesaArgentina)
            repositorioFiguritas.create(figuritaDelanteroArgentina)

            it("busqueda de figurita coincide parcialmente por nombre del jugador"){
                //Act
                val listaConFiguritasMismoNombre = repositorioFiguritas.search("Lio")
                //Assert
                listaConFiguritasMismoNombre.shouldContainAll(figuritaLeyendaArgentina,figuritaPromesaArgentina)
            }

            it("busqueda de figurita no coincide parcialmente por nombre del jugador"){
                //Act
                val listaConFiguritasMismoNombre = repositorioFiguritas.search("zz")
                //Assert
                listaConFiguritasMismoNombre.shouldBeEmpty()
            }

            it("busqueda de figurita coincide parcialmente por apellido del jugador"){
                //Act
                val listaConFiguritasMismoApellido = repositorioFiguritas.search("Álvar")
                //Assert
                listaConFiguritasMismoApellido.shouldContainAll(figuritaDelanteroArgentina)
            }

            it("busqueda de figurita no coincide parcialmente por apellido del jugador"){
                //Act
                val listaConFiguritasMismoApellido = repositorioFiguritas.search("alvarez")
                //Assert
                listaConFiguritasMismoApellido.shouldBeEmpty()
            }

            it("busqueda de figurita coincide exactamente por numero"){
                //Act
                val listaConFiguritasMismoNumero = repositorioFiguritas.search("10")
                //Assert
                listaConFiguritasMismoNumero.shouldContainAll(figuritaDelanteroArgentina)
            }

            it("busqueda de figurita no coincide exactamente por numero"){
                //Act
                val listaConFiguritasMismoNumero = repositorioFiguritas.search("0")
                //Assert
                listaConFiguritasMismoNumero.shouldBeEmpty()
            }

            it("busqueda de figurita coincide exactamente por pais seleccion (abreviado)"){
                //Act
                val listaConFiguritasMismoPais = repositorioFiguritas.search("arg")
                //Assert
                listaConFiguritasMismoPais.shouldContainAll(figuritaLeyendaArgentina,figuritaPromesaArgentina,figuritaDelanteroArgentina)
            }

            it("busqueda de figurita no coincide exactamente por pais seleccion (abreviado)"){
                //Act
                val listaConFiguritasMismoPais = repositorioFiguritas.search("ARGEN")
                //Assert
                listaConFiguritasMismoPais.shouldBeEmpty()
            }
        }

        describe("busqueda de usuario en un repositorio") {
            val repositorioUsuarios = RepositorioUsuarios()
            val usuarioGenerico1 = crearUsuario("Username1","Nombre1", "ApellidoUno")
            val usuarioGenerico2 = crearUsuario("username2","nombre", "APELLIDODOS")
            repositorioUsuarios.create(usuarioGenerico1)
            repositorioUsuarios.create(usuarioGenerico2)

            it("busqueda de usuario coincide exactamente por username"){
                //Act
                val listaConUsuariosMismoUsername = repositorioUsuarios.search("USERNAME2")
                //Assert
                listaConUsuariosMismoUsername.shouldContainAll(usuarioGenerico2)
            }

            it("busqueda de usuario no coincide exactamente por username"){
                //Act
                val listaConUsuariosMismoUsername = repositorioUsuarios.search("username")
                //Assert
                listaConUsuariosMismoUsername.shouldBeEmpty()
            }
        }

        describe("busqueda de jugador en un repositorio") {
            val repositorioJugadores = RepositorioJugadores()
            repositorioJugadores.create(jugadorLeyendaArgentina)
            repositorioJugadores.create(jugadorPromesaArgentina)
            repositorioJugadores.create(jugadorDelanteroArgentina)

            it("busqueda de jugador coincide parcialmente por nombre"){
                //Act
                val listaConJugadoresMismoNombre = repositorioJugadores.search("Lio")

                //Assert
                listaConJugadoresMismoNombre.shouldContainAll(jugadorLeyendaArgentina,jugadorPromesaArgentina)
            }

            it("busqueda de jugador no coincide parcialmente por nombre"){
                //Act
                val listaConJugadoresMismoNombre = repositorioJugadores.search("zz")
                //Assert
                listaConJugadoresMismoNombre.shouldBeEmpty()
            }

            it("busqueda de jugador coincide parcialmente por apellido"){
                //Act
                val listaConJugadoresMismoApellido = repositorioJugadores.search("Álvar")
                //Assert
                listaConJugadoresMismoApellido.shouldContainAll(jugadorDelanteroArgentina)
            }

            it("busqueda de jugador no coincide parcialmente por apellido"){
                //Act
                val listaConJugadoresMismoApellido = repositorioJugadores.search("alvarez")
                //Assert
                listaConJugadoresMismoApellido.shouldBeEmpty()
            }
        }

        describe("busqueda de seleccion en un repositorio") {
            val repositorioSelecciones = RepositorioSelecciones()
            repositorioSelecciones.create(seleccionArgentina)
            repositorioSelecciones.create(seleccionBrasil)
            repositorioSelecciones.create(seleccionAlemania)

            it("busqueda de seleccion coincide exactamente por nombre del pais"){
                //Act
                val listaConSelecciones = repositorioSelecciones.search("alemania")
                //Assert
                listaConSelecciones.shouldContain(seleccionAlemania)
            }

            it("busqueda de seleccion no coincide exactamente por nombre del pais"){
                //Act
                val listaConSelecciones = repositorioSelecciones.search("BRASI")
                //Assert
                listaConSelecciones.shouldNotContain(seleccionBrasil)
            }
        }

        describe("busqueda de punto de venta en un repositorio") {
            val repositorioPuntosDeVenta = RepositorioPuntosDeVenta()
            val kiosko = Kiosko(1,"Kiosko", "Kiosko")
            val libreria = Libreria(2,"LIBRERIA","Libreria")
            val supermercado = Supermercado(3,"supermercado","Supermercado")
            repositorioPuntosDeVenta.create(kiosko)
            repositorioPuntosDeVenta.create(libreria)
            repositorioPuntosDeVenta.create(supermercado)

            it("busqueda de punto de venta coincide exactamente por nombre comercial"){
                //Act
                val listaConPuntosDeVenta = repositorioPuntosDeVenta.search("KIOSKO")
                //Assert
                listaConPuntosDeVenta.shouldContain(kiosko)
            }

            it("busqueda de punto de venta no coincide exactamente por nombre comercial"){
                //Act
                val listaConPuntosDeVenta = repositorioPuntosDeVenta.search("libreri")
                //Assert
                listaConPuntosDeVenta.shouldNotContain(libreria)
            }
        }
    }
})
