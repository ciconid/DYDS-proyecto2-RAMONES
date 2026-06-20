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
}

