# PLAN.md — Perfil Gamer Steam
**Diseño y Desarrollo de Sistemas | Proyecto 2**  
Licenciatura en Ciencias de la Computación — UNS

---

## División de tareas por integrante

> Las etapas 3 y 4 pueden desarrollarse **en paralelo** una vez que la Etapa 2 esté completa.  
> La Etapa 2 es el **contrato compartido** — hacerla primero desbloquea al resto del equipo.

| Integrante | Responsabilidad principal | Etapas |
|---|---|---|
| **Persona A** | Setup del proyecto + capa Domain + tests de use cases | Etapa 1, Etapa 2, tests Domain (Etapa 6) |
| **Persona B** | Data remota: Steam + RAWG + GameBroker + tests de data remota | Etapa 3, tests Data remota (Etapa 6) |
| **Persona C** | Data local: Room/favoritos + Presentation: ViewModels + UI + tests de VM | Etapa 4, Etapa 5, tests Presentation y data local (Etapa 6) |
| **Todos** | Pulido, revisión cruzada y entrega | Etapa 7 |

---

## Stack tecnológico

| Componente | Tecnologia |
|---|---|
| Lenguaje | Kotlin |
| UI | Compose Multiplatform (Desktop) |
| Arquitectura | Clean Architecture + MVVM |
| DI | Koin o inyector manual por constructor |
| Red | Retrofit + OkHttp + Gson |
| Local | SQLDelight/SQLite + DataStore/Preferences |
| Tests | JUnit4 + MockK + Turbine |
| APIs | Steam Web API + RAWG Video Games Database |

---

## Estructura de paquetes

```
com.grupo.steamprofile/
├── di/                         # Modulos DI
├── presentation/
│   ├── busqueda/               # BusquedaScreen + BusquedaViewModel
│   ├── biblioteca/             # BibliotecaScreen + BibliotecaViewModel
│   ├── detalle/                # DetalleScreen + DetalleViewModel
│   ├── favoritos/              # FavoritosScreen + FavoritosViewModel
│   └── common/                 # UiState, componentes reutilizables
├── domain/
│   ├── model/                  # Juego, Perfil, Logro, Detalle
│   ├── repository/             # Repository (interfaces)
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

### Etapa 1 — Setup del proyecto y esqueleto de capas `[Persona A]`
**Objetivo:** tener el proyecto desktop compilando con la estructura de paquetes completa y las dependencias configuradas.

**Tareas:**
- [ ] `[A]` Crear proyecto Kotlin Desktop con Compose Multiplatform
- [ ] `[A]` Agregar dependencias en `build.gradle`: DI, Retrofit/Ktor, Room/SQLDelight, DataStore/Preferences, Coroutines, Turbine, MockK
- [ ] `[A]` Crear la estructura de paquetes vacía (todos los packages de arriba)
- [ ] `[A]` Configurar el módulo base de DI para desktop (sin acoplar a Android)
- [ ] `[A]` Configurar la navegación con las 4 rutas: `busqueda`, `biblioteca/{steamId}`, `detalle/{appId}`, `favoritos`
- [ ] `[A]` Agregar pantallas placeholder (Composables vacíos) para que la navegación compile

**Criterio de éxito:** el proyecto desktop compila, inicia en la pantalla de búsqueda vacía y la navegación entre pantallas funciona sin contenido real.

---

### Etapa 2 — Capa Domain: modelos, interfaces y casos de uso `[Persona A]`
**Objetivo:** definir el contrato completo del dominio sin ninguna dependencia externa.

**Tareas:**

**Modelos (`domain/model/`):**
- [ ] `[A]` `Perfil(steamId, nombre, avatarUrl)`
- [ ] `[A]` `Juego(appId, nombre, horasJugadas, iconUrl, generos)`
- [ ] `[A]` `Logro(nombre, descripcion, conseguido, iconUrl)`
- [ ] `[A]` `DetalleJuego(juego, descripcion, metacriticScore, screenshots, logros)`

**Interfaces (`domain/repository/`):**
- [ ] `[A]` `PerfilRepository` → `getPerfil(steamId): Result<Perfil>`
- [ ] `[A]` `BibliotecaRepository` → `getBiblioteca(steamId): Result<List<Juego>>`
- [ ] `[A]` `DetalleRepository` → `getDetalle(steamId, appId): Result<DetalleJuego>`
- [ ] `[A]` `FavoritosRepository` → `getFavoritos(): Flow<List<Juego>>`, `agregar(juego)`, `eliminar(appId)`

**Casos de uso (`domain/usecase/`):**
- [ ] `[A]` `GetPerfilUseCase`
- [ ] `[A]` `GetBibliotecaUseCase`
- [ ] `[A]` `FiltrarPorGeneroUseCase` — filtra una lista en memoria, sin I/O
- [ ] `[A]` `OrdenarPorHorasUseCase` — ordena una lista en memoria, sin I/O
- [ ] `[A]` `GetDetalleUseCase`
- [ ] `[A]` `GetFavoritosUseCase`
- [ ] `[A]` `AgregarFavoritoUseCase`
- [ ] `[A]` `EliminarFavoritoUseCase`

**Criterio de éxito:** el módulo `domain` no importa ninguna clase de `data` ni de `presentation`. Los use cases son clases puras testeables con JUnit sin Android SDK.

---

### Etapa 3 — Capa Data: APIs remotas `[Persona B]`
**Objetivo:** conectarse a Steam y RAWG y mapear las respuestas a los modelos del dominio.

**Tareas:**

**Steam (`data/remote/steam/`):**
- [ ] `[B]` `SteamApiService` con Retrofit — endpoints:
    - `GetPlayerSummaries` → perfil del usuario
    - `GetOwnedGames` → biblioteca con horas jugadas
    - `GetPlayerAchievements` → logros por juego
- [ ] `[B]` DTOs de respuesta (`SteamPerfilDto`, `SteamJuegoDto`, `SteamLogroDto`)
- [ ] `[B]` `SteamRemoteDataSource` — convierte DTOs a modelos de dominio

**RAWG (`data/remote/rawg/`):**
- [ ] `[B]` `RawgApiService` con Retrofit — endpoints:
    - `/games?search={nombre}` → búsqueda por nombre
    - `/games/{id}` → detalle con Metacritic, géneros, descripción
    - `/games/{id}/screenshots` → imágenes
- [ ] `[B]` DTOs de respuesta (`RawgJuegoDto`, `RawgDetalleDto`)
- [ ] `[B]` `RawgRemoteDataSource` — convierte DTOs a modelos de dominio

**Broker (`data/repository/`):**
- [ ] `[B]` `GameBroker` — combina datos de Steam y RAWG para construir `DetalleJuego`:
    - Busca el juego en RAWG por nombre
    - Combina con logros de Steam
    - Devuelve `DetalleJuego` completo

**Implementaciones de repositorios:**
- [ ] `[B]` `PerfilRepositoryImpl`
- [ ] `[B]` `BibliotecaRepositoryImpl`
- [ ] `[B]` `DetalleRepositoryImpl` — delega en `GameBroker`

**Modulo DI de red (`di/NetworkModule`):**
- [ ] `[B]` Proveer instancias de Retrofit para Steam y RAWG (base URLs distintas)
- [ ] `[B]` Bindings de interfaces a implementaciones

**Criterio de éxito:** test manual o de integración que llame a `GetBibliotecaUseCase` con un SteamID real y loguee la lista de juegos.

---

### Etapa 4 — Capa Data: persistencia local `[Persona C]`
**Objetivo:** implementar favoritos con almacenamiento local y que funcione offline.

**Tareas:**
- [ ] `[C]` `FavoritoEntity` o modelo persistente equivalente — campos: `appId`, `nombre`, `iconUrl`, `horasJugadas`
- [ ] `[C]` `FavoritoDao`/queries equivalentes
- [ ] `[C]` `AppDatabase` o driver SQLite equivalente
- [ ] `[C]` `FavoritosLocalDataSource` — wrapper que expone `Flow<List<Juego>>`
- [ ] `[C]` `FavoritosRepositoryImpl`
- [ ] `[C]` Modulo `DatabaseModule`/storage — proveer persistencia local

**Criterio de exito:** test unitario que inserte y recupere un favorito usando una base local en memoria.

---

### Etapa 5 — Presentation: ViewModels y UI `[Persona C]`
**Objetivo:** conectar toda la lógica al usuario mediante Composables con manejo correcto de estados.

**UiState base (`presentation/common/`):**
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

**BusquedaScreen:**
- [ ] `[C]` `BusquedaViewModel` — expone `uiState: StateFlow<UiState<Perfil>>`; llama `GetPerfilUseCase`
- [ ] `[C]` `BusquedaScreen` — campo de texto + botón buscar; navega a Biblioteca si el perfil existe
- [ ] `[C]` Manejo visual de Loading (CircularProgressIndicator), Error (mensaje + retry), Success

**BibliotecaScreen:**
- [ ] `[C]` `BibliotecaViewModel` — carga la lista, expone estado filtrado/ordenado; llama `GetBibliotecaUseCase`, `FiltrarPorGeneroUseCase`, `OrdenarPorHorasUseCase`
- [ ] `[C]` `BibliotecaScreen` — LazyColumn con chips de género y botón de orden; cada item navega a Detalle
- [ ] `[C]` Estado vacío si la biblioteca no tiene juegos

**DetalleScreen:**
- [ ] `[C]` `DetalleViewModel` — llama `GetDetalleUseCase`; expone `DetalleJuego` + `esFavorito: StateFlow<Boolean>`
- [ ] `[C]` `DetalleScreen` — nombre, horas, descripción, Metacritic score, screenshots (HorizontalPager), lista de logros, botón toggle favorito
- [ ] `[C]` Manejo de error si RAWG no encuentra el juego (mostrar solo datos de Steam)

**FavoritosScreen:**
- [ ] `[C]` `FavoritosViewModel` — colecta `Flow` de `GetFavoritosUseCase`
- [ ] `[C]` `FavoritosScreen` — LazyColumn; swipe-to-delete o botón de eliminar; estado vacío con mensaje
- [ ] `[C]` Navegación a Detalle desde cada favorito

**Criterio de éxito:** flujo completo navegable: buscar SteamID → ver biblioteca → abrir detalle → agregar favorito → verlo en Favoritos.

---

### Etapa 6 — Tests unitarios `[distribuido: ver etiquetas]`
**Objetivo:** cubrir todos los componentes exigidos por la cátedra.

**ViewModels — con MockK + Turbine `[Persona C]`:**
- [ ] `[C]` `BusquedaViewModelTest` — happy path (perfil encontrado), SteamID vacío, error de red
- [ ] `[C]` `BibliotecaViewModelTest` — lista completa, filtro por género, ordenamiento por horas, lista vacía
- [ ] `[C]` `DetalleViewModelTest` — detalle completo, RAWG sin resultados (fallback), toggle favorito
- [ ] `[C]` `FavoritosViewModelTest` — lista con items, lista vacía, eliminar favorito

**Use Cases — JUnit puro `[Persona A]`:**
- [ ] `[A]` `GetPerfilUseCaseTest`
- [ ] `[A]` `GetBibliotecaUseCaseTest`
- [ ] `[A]` `FiltrarPorGeneroUseCaseTest` — género existente, género sin resultados, lista vacía
- [ ] `[A]` `OrdenarPorHorasUseCaseTest` — orden descendente, juegos con mismas horas
- [ ] `[A]` `GetDetalleUseCaseTest`
- [ ] `[A]` `AgregarFavoritoUseCaseTest`, `EliminarFavoritoUseCaseTest`

**Repositorios e implementaciones remotas — con MockK `[Persona B]`:**
- [ ] `[B]` `PerfilRepositoryImplTest` — respuesta exitosa, error HTTP, timeout
- [ ] `[B]` `BibliotecaRepositoryImplTest`

**Repositorio local — con MockK `[Persona C]`:**
- [ ] `[C]` `FavoritosRepositoryImplTest`

**DataSources locales — Room in-memory `[Persona C]`:**
- [ ] `[C]` `FavoritosLocalDataSourceTest` — insertar, recuperar, eliminar, duplicados

**GameBroker `[Persona B]`:**
- [ ] `[B]` `GameBrokerTest` — RAWG encuentra el juego, RAWG no lo encuentra (fallback parcial), error en logros de Steam

**Criterio de éxito:** `./gradlew test` pasa sin errores; cobertura de happy paths y al menos un edge case por componente.

---

### Etapa 7 — Pulido, manejo de errores y entrega `[Todos]`
**Objetivo:** dejar la app robusta y el repositorio listo para la presentación.

**Tareas:**
- [ ] `[Todos]` Revisar que no existan magic strings ni magic numbers (mover a `Constants.kt`)
- [ ] `[Todos]` Eliminar imports sin usar, código muerto y TODO sin resolver
- [ ] `[Todos]` Revisar que todas las funciones tengan menos de ~20 líneas (extraer si no)
- [ ] `[B]` Agregar interceptor de OkHttp para logging en debug
- [ ] `[C]` Pantallas de error con botón "Reintentar" en todas las screens
- [ ] `[C]` Validación del campo SteamID antes de lanzar la petición
- [ ] `[C]` Probar sin internet: favoritos deben seguir visibles desde Room
- [ ] `[A]` Limpiar el README con instrucciones de setup (API keys, cómo correr los tests)
- [ ] `[Todos]` Tag de versión `v1.0` en git

---

## Resumen de etapas

| Etapa | Foco | Dependencias previas | Responsable |
|---|---|---|---|
| 1 | Setup y esqueleto | — | **Persona A** |
| 2 | Domain (modelos, interfaces, use cases) | Etapa 1 | **Persona A** |
| 3 | Data remota (Steam + RAWG + Broker) | Etapa 2 | **Persona B** |
| 4 | Data local (Room, favoritos) | Etapa 2 | **Persona C** |
| 5 | Presentation (ViewModels + UI) | Etapas 2, 3, 4 | **Persona C** |
| 6 | Tests unitarios | Etapas 2, 3, 4, 5 | **A** (domain) · **B** (data remota) · **C** (local + VM) |
| 7 | Pulido y entrega | Etapas 1–6 | **Todos** |

> **Recomendación:** las etapas 3 y 4 pueden hacerse en paralelo por distintos integrantes del grupo una vez que el Domain está definido (Etapa 2). La Etapa 2 es el contrato compartido — hacerla primero evita que el resto del grupo quede bloqueado.