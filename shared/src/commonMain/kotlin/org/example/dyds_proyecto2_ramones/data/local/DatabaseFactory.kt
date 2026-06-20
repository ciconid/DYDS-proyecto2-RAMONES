package org.example.dyds_proyecto2_ramones.data.local

import com.squareup.sqldelight.db.SqlDriver
import org.example.dyds_proyecto2_ramones.data.local.sqldelight.AppDatabase

expect fun createSqlDriver(): SqlDriver

fun createDatabase(): AppDatabase = AppDatabase(createSqlDriver())


