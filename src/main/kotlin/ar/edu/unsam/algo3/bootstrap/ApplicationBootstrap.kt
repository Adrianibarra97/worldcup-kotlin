package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.*
import delantero
import mediocampista
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ApplicationBootstrap(): InitializingBean {

    // Selecciones
    val seleccionArgentina: Seleccion = Seleccion("Argentina", "CONMEBOL", 3)
    val seleccionBrasil: Seleccion = Seleccion("Brasil", "CONMEBOL", 5)
    val seleccionEspaniola: Seleccion = Seleccion("España", "UEFA", 1)
    val seleccionUruguaya: Seleccion = Seleccion("Uruguay", "CONMEBOL", 2)
    val seleccionFrancesa: Seleccion = Seleccion("Francia", "UEFA", 2)
    val seleccionItaliana: Seleccion = Seleccion("Italia", "UEFA", 4)

    // Domicilios
    val domicilioRandom: Domicilio = Domicilio(-34.54390563752278, -58.46353782449937).apply {
        this.calle = "Monroe"
        this.altura = 111
        this.provincia = "Buenos Aires"
        this.localidad = "CABA"
    }
    val casaRosada: Domicilio =  Domicilio(-34.60795174965722, -58.37025258369331).apply {
        this.calle = "Balcarce"
        this.altura = 78
        this.provincia = "Buenos Aires"
        this.localidad = "CABA"
    }
    val costaneraSur: Domicilio =  Domicilio(-34.61213849138486, -58.35840595086813).apply {
        this.calle = "Av. España"
        this.altura = 2330
        this.provincia = "Buenos Aires"
        this.localidad = "CABA"
    }
    val facultadDeDerecho: Domicilio =  Domicilio(-34.58252434820263, -58.391902273528146).apply {
        this.calle = "Av Figueroa Alcorta"
        this.altura = 2263
        this.provincia = "Buenos Aires"
        this.localidad = "CABA"
    }
    val obelisco: Domicilio =  Domicilio(-34.60355274862765, -58.38164966362821).apply {
        this.calle = "Av. 9 de Julio"
        this.altura = 1535
        this.provincia = "Buenos Aires"
        this.localidad = "CABA"
    }

    //Jugadores
    final val julianAlvarez: Jugador = Jugador("Julian", "Alvarez", LocalDate.of(2000,1,31), 9, seleccionArgentina, 2019, 1.75, 68.0, delantero, 9000000.00, false)
    final val nicolasOtamendi: Jugador = Jugador("Nicolas", "Otamendi", LocalDate.of(2004,1,1), 5, seleccionArgentina, 2022, 1.82, 79.0, mediocampista, 10000.00, true)
    final val ezequielPalacios: Jugador = Jugador("Ezequiel", "Palacios", LocalDate.of(1998,10,5), 9, seleccionArgentina, 2017, 1.78, 80.0, delantero, 100000.00, false)
    final val luisSuarez: Jugador = Jugador("Luis", "Suarez", LocalDate.of(1987,7,24), 2, seleccionUruguaya, 2008, 1.82, 83.0, delantero, 8000000.00, true)
    final val zinedineYazid: Jugador = Jugador("Zinedine", "Yazid", LocalDate.of(1972,6,23), 2, seleccionFrancesa, 1996, 1.82, 72.0, mediocampista, 5000000.00, true)
    final val andreaPirlo: Jugador = Jugador("Andrea", "Pirlo", LocalDate.of(1979,5,19), 2, seleccionItaliana, 1990, 1.82, 75.0, mediocampista, 1000000.00, true)
    final val XabierAlonso: Jugador = Jugador("Xabier Alonso", "Olano", LocalDate.of(1981,11,25), 2, seleccionEspaniola, 1996, 1.82, 80.0, mediocampista, 25000000.00, false)
    final val andresIniestaLujan: Jugador = Jugador("Andres Iniesta", "Lujan", LocalDate.of(1984,5,11), 2, seleccionEspaniola, 1993, 1.82, 74.0, mediocampista, 100000000.00, true)

    // Usuarios
    val usuarioJulian: Usuario = Usuario("Juli2005", "Julian", "Messina", LocalDate.of(2001,1,17), "julimesina@gmail.com", domicilioRandom, 10)
    val usuarioNicolas: Usuario = Usuario("Nicolas2001", "Nicolas", "Stebner", LocalDate.of(2001,9,20), "nicostebner@gmail.com", costaneraSur, 1)
    val tipoDeUsuarioEzequiel: TipoDeUsuario = TipoDeUsuarioNacionalista(mutableListOf(seleccionArgentina,seleccionBrasil,seleccionEspaniola,seleccionItaliana))
    val usuarioEzequiel: Usuario = Usuario("eze91", "Ezequiel", "Iozzia", LocalDate.of(1991,12,1), "ezeok@gmail.com", facultadDeDerecho, 1, tipoDeUsuarioEzequiel)
    val usuarioAdrian: Usuario = Usuario("birra97", "Adrian", "Ibarra", LocalDate.of(1997,3,30), "adribirra@gmail.com", obelisco, 1)

    // Figuritas
    val figuritaJulianAlvarez: Figurita = Figurita(8,NivelImpresion.MEDIA, julianAlvarez,false,"https://media.infocielo.com/p/12f414e8f62eda68e30e4a42fbdbfae1/adjuntos/299/imagenes/001/559/0001559218/1200x675/smart/screenshot_20png.png")
    val figuritaNicolasOtamendi: Figurita = Figurita(4,NivelImpresion.BAJA,nicolasOtamendi,true,"https://assets.goal.com/v3/assets/bltcc7a7ffd2fbf71f5/blt1fc2a5c917434207/6394522b95ed12556872bf11/ota_header.jpg?auto=webp&format=pjpg&width=3840&quality=60")
    val figuritaEzequielPalacios: Figurita = Figurita(5,NivelImpresion.ALTA,ezequielPalacios,false,"https://media.tycsports.com/files/2023/08/18/606509/exequiel-palacios-seleccion-argentina-_1440x810_wmk.webp?v=1")
    val figuritaLuisSuarez: Figurita = Figurita(25,NivelImpresion.BAJA, luisSuarez,true,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5iI56HiLWysnZIwNrAoM6yDtrZxdAgR9DWw&usqp=CAU")
    val figuritaZidane: Figurita = Figurita(27,NivelImpresion.MEDIA, zinedineYazid,true,"https://pbs.twimg.com/media/BsV3CpACYAAGEAH?format=jpg&name=4096x4096")
    val figuritaAndreaPirlo: Figurita = Figurita(35,NivelImpresion.MEDIA, andreaPirlo,false,"https://ca-times.brightspotcdn.com/dims4/default/d83a975/2147483647/strip/true/crop/2208x1528+0+0/resize/1200x830!/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Ff1%2Fe3%2Ff4ebc2aa5be697e731bdcbc9a510%2F2c397560b50f40d8863a39baba2448b5")
    val figuritaXaviAlonso: Figurita = Figurita(40,NivelImpresion.ALTA, XabierAlonso,true,"https://thebigscout.files.wordpress.com/2014/09/gol-xabi-alonso.jpg")
    val figuritaAndresIniesta: Figurita = Figurita(41,NivelImpresion.BAJA, andresIniestaLujan,false,"https://assets.goal.com/v3/assets/bltcc7a7ffd2fbf71f5/blt58119d12282b743f/60dfdca7878686258b9e9c9f/e0fca348805cbb8e5ad889626dbe83d592749ea0.jpg?auto=webp&format=pjpg&width=3840&quality=60")

    // Puntos de venta: orden min.max: uba, obelisc,casa rosda,costanera
    val kioskoLosPibeStore: PuntoDeVenta = Kiosko(1, "Kiosko Los Pibe!", "Kiosko", 0.00).apply {
        this.domicilioPuntoDeVenta = casaRosada
        stockDeSobres = 170
        costoPorSobre = 250.00
        pedidosPendientesPuntoDeVenta = 200
    }
    val libreriaJorgeStore: PuntoDeVenta = Libreria(2, "Libreria Jorge", "Libreria", 0.00).apply {
        this.domicilioPuntoDeVenta = costaneraSur
        stockDeSobres = 40
        costoPorSobre = 350.00
        pedidosPendientesPuntoDeVenta = 125
    }
    val supermercadoChumboStore: PuntoDeVenta = Supermercado(3, "Supermercado Chumbo", "Supermercado", 0.00).apply {
        this.domicilioPuntoDeVenta = facultadDeDerecho
        stockDeSobres = 100
        costoPorSobre = 200.00
        pedidosPendientesPuntoDeVenta = 432
    }
    val libreriaAdrianStore: PuntoDeVenta = Libreria(4, "Libreria Adrian", "Libreria", 0.00).apply {
        this.domicilioPuntoDeVenta = obelisco
        stockDeSobres = 80
        costoPorSobre = 175.00
        pedidosPendientesPuntoDeVenta = 199
    }
    val libreriaLosPibardos: PuntoDeVenta = Libreria(5, "Libreria Los Pibardos", "Libreria", 0.00).apply {
        this.domicilioPuntoDeVenta = domicilioRandom
        stockDeSobres = 133
        costoPorSobre = 210.00
        pedidosPendientesPuntoDeVenta = 87
    }

    override fun afterPropertiesSet() {

        usuarioJulian.password = "1000"
        usuarioJulian.url = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Narig%C3%B3n_Bilardo_1986.jpg/280px-Narig%C3%B3n_Bilardo_1986.jpg"
        usuarioJulian.tipoDeUsuario = TipoDeUsuarioDesprendido(usuarioJulian)
        usuarioJulian.agregarFiguritaRepetida(figuritaJulianAlvarez)
        usuarioJulian.agregarFiguritaRepetida(figuritaNicolasOtamendi)
        usuarioJulian.agregarFiguritaRepetida(figuritaEzequielPalacios)
        usuarioJulian.agregarFiguritaFaltante(figuritaLuisSuarez)
        usuarioJulian.agregarFiguritaFaltante(figuritaZidane)
        usuarioJulian.agregarFiguritaFaltante(figuritaAndreaPirlo)
        usuarioJulian.agregarFiguritaFaltante(figuritaXaviAlonso)
        usuarioJulian.agregarFiguritaFaltante(figuritaAndresIniesta)

        usuarioNicolas.password = "550"
        usuarioNicolas.url = "data:image/jpeg;base64,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"
        usuarioNicolas.tipoDeUsuario = TipoDeUsuarioDesprendido(usuarioNicolas)
        usuarioNicolas.agregarFiguritaRepetida(figuritaLuisSuarez)
        usuarioNicolas.agregarFiguritaRepetida(figuritaZidane)
        usuarioNicolas.agregarFiguritaRepetida(figuritaAndreaPirlo)
        usuarioNicolas.agregarFiguritaFaltante(figuritaJulianAlvarez)
        usuarioNicolas.agregarFiguritaFaltante(figuritaNicolasOtamendi)
        usuarioNicolas.agregarFiguritaFaltante(figuritaEzequielPalacios)
        usuarioNicolas.agregarFiguritaFaltante(figuritaXaviAlonso)
        usuarioNicolas.agregarFiguritaFaltante(figuritaAndresIniesta)

        usuarioAdrian.password = "2587"
        usuarioAdrian.url = "https://image-service.onefootball.com/transform?w=280&h=210&dpr=2&image=https%3A%2F%2Fwww.vermouth-deportivo.com.ar%2Fwp-content%2Fuploads%2F2023%2F08%2Fargentina-manager-carlos-bilardo-1984-scaled.jpg"
        usuarioAdrian.tipoDeUsuario = TipoDeUsuarioDesprendido(usuarioAdrian)
        usuarioAdrian.agregarFiguritaRepetida(figuritaXaviAlonso)
        usuarioAdrian.agregarFiguritaRepetida(figuritaAndresIniesta)
        usuarioAdrian.agregarFiguritaFaltante(figuritaJulianAlvarez)
        usuarioAdrian.agregarFiguritaFaltante(figuritaNicolasOtamendi)
        usuarioAdrian.agregarFiguritaFaltante(figuritaEzequielPalacios)
        usuarioAdrian.agregarFiguritaFaltante(figuritaLuisSuarez)
        usuarioAdrian.agregarFiguritaFaltante(figuritaZidane)
        usuarioAdrian.agregarFiguritaFaltante(figuritaAndreaPirlo)

        usuarioEzequiel.url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQhBBfcCHIApuPlmblyfjXQMCNuzcehecjLuA&usqp=CAU"
    }

    // Repositorios
    @Bean
    fun usuariosRepositorio() = RepositorioUsuarios().apply {
        createListObject(listOf(usuarioJulian, usuarioNicolas, usuarioEzequiel, usuarioAdrian))
    }

    @Bean
    fun figuritaRepository() = RepositorioFiguritas().apply {
        createListObject(listOf(figuritaJulianAlvarez,figuritaNicolasOtamendi,figuritaEzequielPalacios,
                figuritaLuisSuarez,figuritaZidane,figuritaAndreaPirlo,figuritaXaviAlonso,figuritaAndresIniesta))
    }

    @Bean
    fun seleccionRepository() = RepositorioSelecciones().apply {
        createListObject(listOf(seleccionArgentina,seleccionBrasil,seleccionEspaniola,seleccionUruguaya,seleccionFrancesa,seleccionItaliana))
    }

    @Bean
    fun storeRepositorio() = RepositorioPuntosDeVenta().apply {
        createListObject(listOf(kioskoLosPibeStore, libreriaJorgeStore, supermercadoChumboStore, libreriaAdrianStore,libreriaLosPibardos))
    }

    @Bean
    fun jugadoresRepository() = RepositorioJugadores().apply {
        createListObject(listOf(julianAlvarez, nicolasOtamendi, ezequielPalacios,luisSuarez,zinedineYazid,andreaPirlo,XabierAlonso,andresIniestaLujan))
    }
}