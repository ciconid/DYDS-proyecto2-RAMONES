plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.squareup.sqldelight") version "1.5.5"
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
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")
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

