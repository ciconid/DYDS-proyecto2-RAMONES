import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildkonfig)
    id("com.squareup.sqldelight") version "1.5.5"
    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.kotlinx.coroutinesCore)
                implementation("com.squareup.sqldelight:runtime:1.5.5")
                implementation(libs.koin.core)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("media.kamel:kamel-image:0.9.5")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.testJunit)
                implementation(libs.junit)
                implementation(libs.mockk)
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")
                implementation("io.ktor:ktor-client-mock:2.3.12")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")
                implementation("io.ktor:ktor-client-cio:2.3.12")
            }
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "org.example.dyds_proyecto2_ramones.data.local.sqldelight"
        sourceFolders = listOf("sqldelight")
    }
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

buildkonfig {
    packageName = "org.example.dyds_proyecto2_ramones.data"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "STEAM_KEY",
            localProperties.getProperty("STEAM_API_KEY") ?: System.getenv("STEAM_API_KEY") ?: ""
        )
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "RAWG_KEY",
            localProperties.getProperty("RAWG_API_KEY") ?: System.getenv("RAWG_API_KEY") ?: ""
        )
    }
}
