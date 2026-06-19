package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase
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

        val juego = Juego("1", "Juego Uno", 10.0, "http://icon", emptyList())
        dataSource.insertar(juego)

        val list = dataSource.getFavoritosFlow()
            .let { flow -> runBlocking { flow } }

        val favoritos = (dataSource.getFavoritosFlow() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(1, favoritos.size)
        assertEquals("Juego Uno", favoritos.first().nombre)
    }

    @Test
    fun eliminarFavorito() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)

        val juego = Juego("1", "Juego Uno", 10.0, "http://icon", emptyList())
        dataSource.insertar(juego)
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

        val juego1 = Juego("1", "Juego Uno", 10.0, "http://icon", emptyList())
        dataSource.insertar(juego1)
        val juego2 = Juego("1", "Juego Uno Mod", 20.0, "http://icon2", emptyList())
        dataSource.insertar(juego2)

        val favoritos = (dataSource.getFavoritosFlow() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(1, favoritos.size)
        assertEquals(20.0, favoritos.first().horasJugadas)
        assertEquals("http://icon2", favoritos.first().iconUrl)
    }
}
