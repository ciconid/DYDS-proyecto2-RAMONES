package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase

actual fun createAppDatabase(): AppDatabase {
    val url = "jdbc:sqlite:favoritos.db"
    val driver = JdbcSqliteDriver(url)
    runCatching { AppDatabase.Schema.create(driver) }
        .onFailure { throwable ->
            val alreadyExists = throwable.message?.contains("already exists", ignoreCase = true) == true
            if (!alreadyExists) {
                throw throwable
            }
        }
    return AppDatabase(driver)
}

