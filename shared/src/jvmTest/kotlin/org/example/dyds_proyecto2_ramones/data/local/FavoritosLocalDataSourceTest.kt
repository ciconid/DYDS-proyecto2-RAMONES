package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoritosLocalDataSourceTest {
    @Test
    fun insertarYRecuperar() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)

        val detalle = detalle(
            appId = "1",
            nombre = "Juego Uno",
            horas = 10.0,
            icon = "http://icon",
            generos = listOf("Arcade"),
            descripcion = "Descripcion uno",
        )
        dataSource.insertar(detalle)

        val favoritos = (dataSource.getFavoritosFlow() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(1, favoritos.size)
        assertEquals("Juego Uno", favoritos.first().nombre)
        assertEquals(listOf("Arcade"), favoritos.first().generos)
        assertEquals("Descripcion uno", dataSource.getDetalleLocal("1")?.descripcion)
    }

    @Test
    fun eliminarFavorito() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)

        dataSource.insertar(detalle("1", "Juego Uno", 10.0, "http://icon", emptyList(), "Desc"))
        dataSource.eliminar("1")

        val favoritos = (dataSource.getFavoritosFlow() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(0, favoritos.size)
    }

    @Test
    fun insertarDuplicadoReemplaza() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)

        val juego1 = detalle("1", "Juego Uno", 10.0, "http://icon", listOf("Aventura"), "Desc 1")
        dataSource.insertar(juego1)
        val juego2 = detalle("1", "Juego Uno Mod", 20.0, "http://icon2", listOf("Estrategia"), "Desc 2")
        dataSource.insertar(juego2)

        val favoritos = (dataSource.getFavoritosFlow() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(1, favoritos.size)
        assertEquals(20.0, favoritos.first().horasJugadas)
        assertEquals("http://icon2", favoritos.first().iconUrl)
        assertEquals(listOf("Estrategia"), favoritos.first().generos)
        assertEquals("Desc 2", dataSource.getDetalleLocal("1")?.descripcion)
    }

    @Test
    fun reinsertarSinDescripcionConservaLaDescripcionGuardada() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)

        dataSource.insertar(
            detalle(
                appId = "730",
                nombre = "Counter-Strike 2",
                horas = 100.0,
                icon = "icon-1",
                generos = listOf("Shooter"),
                descripcion = "Descripcion RAWG inicial",
            ),
        )

        dataSource.insertar(
            detalle(
                appId = "730",
                nombre = "Counter-Strike 2",
                horas = 120.0,
                icon = "icon-2",
                generos = listOf("Accion"),
                descripcion = "",
            ),
        )

        val detalleLocal = dataSource.getDetalleLocal("730")
        assertEquals("Descripcion RAWG inicial", detalleLocal?.descripcion)
        assertEquals(listOf("Accion"), detalleLocal?.juego?.generos)
    }

    private fun detalle(
        appId: String,
        nombre: String,
        horas: Double,
        icon: String,
        generos: List<String>,
        descripcion: String,
    ): DetalleJuego = DetalleJuego(
        juego = Juego(appId, nombre, horas, icon, generos),
        descripcion = descripcion,
        metacriticScore = null,
        screenshots = emptyList(),
        logros = emptyList(),
    )
}
