package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase

actual fun createSqlDriver(): SqlDriver = JdbcSqliteDriver("jdbc:sqlite:favoritos.db").also { driver ->
    runCatching { AppDatabase.Schema.create(driver) }
        .onFailure { throwable ->
            val alreadyExists = throwable.message?.contains("already exists", ignoreCase = true) == true
            if (!alreadyExists) {
                throw throwable
            }
        }
    ensureFavoritosColumns(driver)
}

private fun ensureFavoritosColumns(driver: SqlDriver) {
    runCatching {
        driver.execute(
            identifier = null,
            sql = "ALTER TABLE favorito ADD COLUMN descripcion TEXT NOT NULL DEFAULT ''",
            parameters = 0,
        )
    }.onFailure { throwable ->
        val duplicateColumn = throwable.message?.contains("duplicate column", ignoreCase = true) == true
        if (!duplicateColumn) {
            throw throwable
        }
    }

    runCatching {
        driver.execute(
            identifier = null,
            sql = "ALTER TABLE favorito ADD COLUMN generos TEXT NOT NULL DEFAULT ''",
            parameters = 0,
        )
    }.onFailure { throwable ->
        val duplicateColumn = throwable.message?.contains("duplicate column", ignoreCase = true) == true
        if (!duplicateColumn) {
            throw throwable
        }
    }
}
