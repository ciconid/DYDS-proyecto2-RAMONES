# PLAN.md — Perfil Gamer Steam
**Diseño y Desarrollo de Sistemas | Proyecto 2**  
Licenciatura en Ciencias de la Computación — UNS

---

## División de tareas por integrante

> Las etapas 3 y 4 pueden desarrollarse **en paralelo** una vez que la Etapa 2 esté completa.  
> La Etapa 2 es el **contrato compartido** — hacerla primero desbloquea al resto del equipo.

| Integrante | Responsabilidad principal | Etapas |
|---|---|---|
| **Persona A** | Setup del proyecto + capa Domain + tests de use cases | Etapa 1, Etapa 2 |
| **Persona B** | Data remota: Steam + RAWG + GameBroker + tests | Etapa 3 |
| **Persona C** | Data local + Presentation: ViewModels + UI + tests | Etapa 4, Etapa 5 |
| **Todos** | Pulido, revisión cruzada y entrega | Etapa 6 |

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Compose Multiplatform (Desktop) |
| Arquitectura | Clean Architecture + MVVM |
| DI | Koin |
| Red | Retrofit + OkHttp + Gson |
| Local | SQLDelight/SQLite |
| Tests | JUnit4 + MockK + Turbine |
| APIs | Steam Web API + RAWG Video Games Database |

---

## Estructura de paquetes

```
org.example.dyds_proyecto2_ramones/
├── di/                         # Módulos DI
├── presentation/
│   ├── busqueda/               # BusquedaScreen + BusquedaViewModel
│   ├── biblioteca/             # BibliotecaScreen + BibliotecaViewModel
│   ├── detalle/                # DetalleScreen + DetalleViewModel
│   ├── favoritos/              # FavoritosScreen + FavoritosViewModel
│   └── common/                 # UiState, componentes reutilizables
├── domain/
│   ├── model/                  # Juego, Perfil, Logro, DetalleJuego
│   ├── repository/             # Interfaces de repositorio
│   └── usecase/                # Un archivo por caso de uso
└── data/
    ├── remote/
    │   ├── steam/              # SteamApiService, DTOs, SteamRemoteDataSource
    │   └── rawg/               # RawgApiService, DTOs, RawgRemoteDataSource
    ├── local/
    │   ├── dao/                # FavoritoDao
    │   ├── entity/             # FavoritoEntity
    │   └── AppDatabase
    └── repository/             # RepositoryImpl, GameBroker
```

---

## Etapas de implementación

---

### ✅ Etapa 1 — Setup del proyecto `[Persona A]`
**Objetivo:** tener el proyecto desktop compilando con dependencias configuradas y navegación funcional.

**Tareas:**
-  ✅ `[A]` Agregar dependencias mínimas en `build.gradle`: Compose Multiplatform, navegación, Coroutines
- ✅ `[A]` Configurar navegación con las 4 rutas: `busqueda`, `biblioteca/{steamId}`, `detalle/{appId}`, `favoritos`
- ✅ `[A]` Agregar pantallas placeholder (Composables vacíos) para que la navegación compile

**Criterio de éxito:** el proyecto compila, inicia en la pantalla de búsqueda vacía y la navegación entre pantallas funciona sin contenido real.

---

### ✅ Etapa 2a — Modelos e interfaces del dominio `[Persona A]`
**Objetivo:** definir el contrato puro del dominio que desbloquea al resto del equipo.

**Dependencias:**
- ✅ `[A]` Agregar JUnit4 + MockK

**Modelos (`domain/model/`):**
- ✅ `[A]` `Perfil(steamId, nombre, avatarUrl)`
- ✅ `[A]` `Juego(appId, nombre, horasJugadas, iconUrl, generos)`
- ✅ `[A]` `Logro(nombre, descripcion, conseguido, iconUrl)`
- ✅ `[A]` `DetalleJuego(juego, descripcion, metacriticScore, screenshots, logros)`

**Interfaces (`domain/repository/`):**
- ✅ `[A]` `PerfilRepository` → `getPerfil(steamId): Result<Perfil>`
- ✅ `[A]` `BibliotecaRepository` → `getBiblioteca(steamId): Result<List<Juego>>`
- ✅ `[A]` `DetalleRepository` → `getDetalle(steamId, appId): Result<DetalleJuego>`
- ✅ `[A]` `FavoritosRepository` → `getFavoritos(): Flow<List<Juego>>`, `agregar(juego)`, `eliminar(appId)`

**Criterio de éxito:** compila sin errores. Las etapas 2b–2i, 3 y 4 pueden arrancar.

---

### ✅ Etapa 2b — `GetPerfilUseCase` `[Persona A]`
**Objetivo:** obtener el perfil de un usuario por SteamID.

- ✅ `[A]` Implementar `GetPerfilUseCase`
- ✅ `[A]` `GetPerfilUseCaseTest` — happy path, SteamID inválido, error de repositorio

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2c — `GetBibliotecaUseCase` `[Persona A]`
**Objetivo:** obtener la lista de juegos de la biblioteca de un usuario.

- ✅ `[A]` Implementar `GetBibliotecaUseCase`
- ✅ `[A]` `GetBibliotecaUseCaseTest` — happy path, biblioteca vacía, error de repositorio

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2d — `FiltrarPorGeneroUseCase` `[Persona A]`
**Objetivo:** filtrar en memoria una lista de juegos por género, sin I/O.

- ✅ `[A]` Implementar `FiltrarPorGeneroUseCase`
- ✅ `[A]` `FiltrarPorGeneroUseCaseTest` — género existente, género sin resultados, lista vacía

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2e — `OrdenarPorHorasUseCase` `[Persona A]`
**Objetivo:** ordenar en memoria una lista de juegos por horas jugadas, sin I/O.

- ✅ `[A]` Implementar `OrdenarPorHorasUseCase`
- ✅ `[A]` `OrdenarPorHorasUseCaseTest` — orden descendente, juegos con mismas horas, lista vacía

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2f — `GetDetalleUseCase` `[Persona A]`
**Objetivo:** obtener el detalle completo de un juego combinando datos de Steam y RAWG.

- ✅ `[A]` Implementar `GetDetalleUseCase`
- ✅ `[A]` `GetDetalleUseCaseTest` — happy path, juego sin datos de RAWG, error de repositorio

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2g — `GetFavoritosUseCase` `[Persona A]`
**Objetivo:** obtener el listado de juegos marcados como favoritos.

- ✅ `[A]` Implementar `GetFavoritosUseCase`
- ✅ `[A]` `GetFavoritosUseCaseTest` — lista con items, lista vacía

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2h — `AgregarFavoritoUseCase` `[Persona A]`
**Objetivo:** agregar un juego a la lista de favoritos.

- ✅ `[A]` Implementar `AgregarFavoritoUseCase`
- ✅ `[A]` `AgregarFavoritoUseCaseTest` — agregar juego nuevo, agregar juego duplicado

**Criterio de éxito:** `./gradlew test` pasa para este use case.

---

### ✅ Etapa 2i — `EliminarFavoritoUseCase` `[Persona A]`
**Objetivo:** eliminar un juego de la lista de favoritos por appId.

- ✅ `[A]` Implementar `EliminarFavoritoUseCase`
- ✅ `[A]` `EliminarFavoritoUseCaseTest` — eliminar favorito existente, eliminar appId inexistente

**Criterio de éxito:** `./gradlew test` pasa para este use case.

### Etapa 3a — Steam API `[Persona B]`
**Objetivo:** conectarse a la API de Steam y mapear respuestas a modelos del dominio.

**Dependencias:**
- ✅ `[B]` Agregar Retrofit + OkHttp + Gson + Turbine

**Implementación (`data/remote/steam/`):**
- ✅ `[B]` `SteamApiService` con Retrofit — endpoints:
  - `GetPlayerSummaries` → perfil del usuario
  - `GetOwnedGames` → biblioteca con horas jugadas
  - `GetPlayerAchievements` → logros por juego
- ✅ `[B]` DTOs de respuesta (`SteamPerfilDto`, `SteamJuegoDto`, `SteamLogroDto`)
- ✅ `[B]` `SteamRemoteDataSource` — convierte DTOs a modelos de dominio

**Repositorios (`data/repository/`):**
- ✅ `[B]` `PerfilRepositoryImpl`
- ✅ `[B]` `BibliotecaRepositoryImpl`

**Tests:**
- ✅ `[B]` `PerfilRepositoryImplTest` — respuesta exitosa, error HTTP, timeout
- ✅ `[B]` `BibliotecaRepositoryImplTest` — lista completa, lista vacía, error HTTP

**Criterio de éxito:** `./gradlew test` pasa. Test manual con SteamID real loguea el perfil y la biblioteca.

---

### Etapa 3b — RAWG API `[Persona B]`
**Objetivo:** conectarse a la API de RAWG y mapear respuestas a modelos del dominio.

> Puede desarrollarse en paralelo con la Etapa 3a.

**Implementación (`data/remote/rawg/`):**
- ✅ `[B]` `RawgApiService` con Retrofit — endpoints:
  - `/games?search={nombre}` → búsqueda por nombre
  - `/games/{id}` → detalle con Metacritic, géneros, descripción
  - `/games/{id}/screenshots` → imágenes
- ✅ `[B]` DTOs de respuesta (`RawgJuegoDto`, `RawgDetalleDto`)
- ✅ `[B]` `RawgRemoteDataSource` — convierte DTOs a modelos de dominio

**Tests:**
- ✅ `[B]` `RawgRemoteDataSourceTest` — juego encontrado, juego no encontrado, error HTTP

**Criterio de éxito:** `./gradlew test` pasa. Test manual con nombre de juego real loguea el detalle.

---

### Etapa 3c — GameBroker `[Persona B]`
**Objetivo:** combinar datos de Steam y RAWG para construir `DetalleJuego`.

> Depende de 3a y 3b.

**Implementación (`data/repository/`):**
- ✅ `[B]` `GameBroker` — busca el juego en RAWG por nombre, combina con logros de Steam, devuelve `DetalleJuego`
- ✅ `[B]` `DetalleRepositoryImpl` — delega en `GameBroker`

**Tests:**
- ✅ `[B]` `GameBrokerTest` — RAWG encuentra el juego, RAWG no lo encuentra (fallback parcial), error en logros de Steam

**Criterio de éxito:** `./gradlew test` pasa. Test manual con appId real devuelve un `DetalleJuego` completo.

---

### Etapa 3d — NetworkModule (Koin) `[Persona B]`
**Objetivo:** proveer las instancias de red vía inyección de dependencias.

> Depende de 3a y 3b.

**Implementación (`di/`):**
- ✅ `[B]` `NetworkModule` — instancias de Retrofit para Steam y RAWG con base URLs distintas
- ✅ `[B]` Bindings de interfaces a implementaciones (`PerfilRepository`, `BibliotecaRepository`, `DetalleRepository`)

**Criterio de éxito:** la app compila con Koin inicializado y los repositorios inyectables desde la Etapa 5.

### ✅ Etapa 4 — Capa Data: persistencia local `[Persona C]`
**Objetivo:** implementar favoritos con almacenamiento local y testear.

> Puede desarrollarse en paralelo con la Etapa 3 una vez que la Etapa 2 esté completa.

**Dependencias:**
- ✅ `[C]` Agregar SQLDelight + Koin

**Implementación:**
- ✅ `[C]` `FavoritoEntity` — campos: `appId`, `nombre`, `iconUrl`, `horasJugadas`
- ✅ `[C]` `FavoritoDao` / queries equivalentes (SQLDelight `Favoritos.sq`)
- ✅ `[C]` `AppDatabase` o driver SQLite equivalente
- ✅ `[C]` `FavoritosLocalDataSource` — expone `Flow<List<Juego>>`
- ✅ `[C]` `FavoritosRepositoryImpl`
- ✅ `[C]` `DatabaseModule` (Koin)

**Tests:**
- ✅ `[C]` `FavoritosLocalDataSourceTest` — insertar, recuperar, eliminar, duplicados (DB en memoria)
- ✅ `[C]` `FavoritosRepositoryImplTest`

**Criterio de éxito:** test inserta y recupera un favorito sin red. `./gradlew :shared:jvmTest` pasa.

---

### ✅ Etapa 5 — Presentation: ViewModels y UI `[Persona C]`
**Objetivo:** conectar toda la lógica al usuario con manejo correcto de estados, y testear los ViewModels.

**UiState base (`presentation/common/`):**
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

**✅BusquedaScreen:**
- ✅ `[C]` `BusquedaViewModel` — expone `uiState: StateFlow<UiState<Perfil>>`; llama `GetPerfilUseCase`
- ✅ `[C]` `BusquedaScreen` — campo de texto + botón buscar; navega a Biblioteca si el perfil existe
- ✅ `[C]` Manejo visual de Loading, Error (con retry), Success
- ✅ `[C]` `BusquedaViewModelTest` — happy path, SteamID vacío, error de red

**✅BibliotecaScreen:**
- ✅ `[C]` `BibliotecaViewModel` — carga lista, expone estado filtrado/ordenado; llama `GetBibliotecaUseCase`, `FiltrarPorGeneroUseCase`, `OrdenarPorHorasUseCase`
- ✅ `[C]` `BibliotecaScreen` — LazyColumn con chips de género y botón de orden; cada item navega a Detalle; estado vacío
- ✅ `[C]` `BibliotecaViewModelTest` — lista completa, filtro por género, ordenamiento por horas, lista vacía

**✅DetalleScreen:**
- ✅ `[C]` `DetalleViewModel` — llama `GetDetalleUseCase`; expone `DetalleJuego` + `esFavorito: StateFlow<Boolean>`
- ✅ `[C]` `DetalleScreen` — nombre, horas, descripción, Metacritic score, screenshots (HorizontalPager), logros, botón toggle favorito
- ✅ `[C]` Manejo de error si RAWG no encuentra el juego (mostrar solo datos de Steam)
- ✅ `[C]` `DetalleViewModelTest` — detalle completo, fallback sin RAWG, toggle favorito

**✅FavoritosScreen:**
- ✅ `[C]` `FavoritosViewModel` — colecta `Flow` de `GetFavoritosUseCase`
- ✅ `[C]` `FavoritosScreen` — LazyColumn con eliminar; estado vacío con mensaje; navega a Detalle
- ✅ `[C]` `FavoritosViewModelTest` — lista con items, lista vacía, eliminar favorito

**Criterio de éxito:** flujo completo navegable: buscar SteamID → ver biblioteca → abrir detalle → agregar favorito → verlo en Favoritos. `./gradlew test` pasa.

---
### Bugs y cosas para corregir
- ✅ No se puede volver a la plantalla de busqueda desde la biblioteca o el detalle
- [ ] Los favoritos se guardan solamente "de nombre". Si reinicio la app y voy a favoritos, aparecen en la lista, pero al hacer click la app me pide que haga una busqueda para cargar el perfil. Deberiamos persistir al menos algunos detalles basicos del juego.
- [ ] DetalleJuego: las descripciones de RAWG estan en ingles.
- ✅ DetalleJuego: No mostramos imagenes (pero si su url).
- [ ] No hay generos en la pantalla de biblioteca, aunque los obtenemos de Steam.
- ✅ En la biblioteca no se muestra la imagen del juego
- ✅ En Favoritos no se muestra la imagen del juego
- ✅ Las horas de juego se muestran con muchos decimales (ej: 123.456789 horas)

### Etapa 6 — Pulido y entrega `[Todos]`
**Objetivo:** dejar la app robusta y el repositorio listo para la presentación.

- [ ] `[Todos]` Mover magic strings y magic numbers a `Constants.kt`
- [ ] `[Todos]` Eliminar imports sin usar, código muerto y TODOs sin resolver
- [ ] `[Todos]` Revisar que las funciones tengan menos de ~20 líneas
- [ ] `[B]` Agregar interceptor de OkHttp para logging en debug
- [ ] `[C]` Pantallas de error con botón "Reintentar" en todas las screens
- [ ] `[C]` Validación del campo SteamID antes de lanzar la petición
- [ ] `[C]` Probar sin internet: favoritos deben seguir visibles desde la DB local
- [ ] `[A]` Limpiar el README con instrucciones de setup (API keys, cómo correr los tests)
- [ ] `[Todos]` Tag de versión `v1.0` en git

---

## Resumen de etapas

| Etapa | Foco | Dependencias previas | Responsable |
|---|---|---|---|
| 1 | Setup y navegación | — | **Persona A** |
| 2 | Domain (modelos, interfaces, use cases + tests) | Etapa 1 | **Persona A** |
| 3 | Data remota (Steam + RAWG + Broker + tests) | Etapa 2 | **Persona B** |
| 4 | Data local (favoritos + tests) | Etapa 2 | **Persona C** |
| 5 | Presentation (ViewModels + UI + tests) | Etapas 3 y 4 | **Persona C** |
| 6 | Pulido y entrega | Etapas 1–5 | **Todos** |

> Las etapas 3 y 4 pueden hacerse en paralelo una vez que la Etapa 2 esté completa.