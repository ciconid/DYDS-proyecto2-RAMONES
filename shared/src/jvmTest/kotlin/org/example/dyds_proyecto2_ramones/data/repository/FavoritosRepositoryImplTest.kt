package org.example.dyds_proyecto2_ramones.data.repository

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.data.local.FavoritosLocalDataSource
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoritosRepositoryImplTest {
    @Test
    fun agregarYObtenerFavoritos() = runBlocking {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        val db = AppDatabase(driver)
        val dataSource = FavoritosLocalDataSource(db)
        val repo = FavoritosRepositoryImpl(dataSource)

        val detalle = DetalleJuego(
            juego = Juego("1", "Juego Uno", 5.0, "icon", listOf("Puzzle")),
            descripcion = "Descripcion local",
            metacriticScore = null,
            screenshots = emptyList(),
            logros = emptyList(),
        )
        repo.agregar(detalle)

        val favoritos = (repo.getFavoritos() as kotlinx.coroutines.flow.StateFlow).value
        assertEquals(1, favoritos.size)
        assertEquals("Juego Uno", favoritos.first().nombre)
        assertEquals(listOf("Puzzle"), favoritos.first().generos)

        val detalleLocal = repo.getDetalleLocal("1")
        assertEquals("Descripcion local", detalleLocal?.descripcion)
    }
}
