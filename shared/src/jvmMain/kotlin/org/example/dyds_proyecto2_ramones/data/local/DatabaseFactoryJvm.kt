package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase
import java.io.File

private const val DB_FILE_NAME = "favoritos.db"
private const val DB_PATH_OVERRIDE_PROPERTY = "dyds.db.path"
private const val APP_DIR_NAME = "DyDS-proyecto2Ramones"

actual fun createSqlDriver(): SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${resolveDatabaseFile().absolutePath}").also { driver ->
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

private fun resolveDatabaseFile(): File {
    val overridePath = System.getProperty(DB_PATH_OVERRIDE_PROPERTY)?.trim().orEmpty()
    if (overridePath.isNotEmpty()) {
        val overrideFile = File(overridePath)
        overrideFile.parentFile?.mkdirs()
        return overrideFile
    }

    val baseDirectory = resolvePlatformDataDirectory()
    val appDirectory = File(baseDirectory, APP_DIR_NAME)
    appDirectory.mkdirs()
    return File(appDirectory, DB_FILE_NAME)
}

private fun resolvePlatformDataDirectory(): File {
    val localAppData = System.getenv("LOCALAPPDATA")?.trim().orEmpty()
    if (localAppData.isNotEmpty()) return File(localAppData)

    val appData = System.getenv("APPDATA")?.trim().orEmpty()
    if (appData.isNotEmpty()) return File(appData)

    return File(System.getProperty("user.home"), ".local/share")
}

