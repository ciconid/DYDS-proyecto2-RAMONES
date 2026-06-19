# Skill: Replicar arquitectura Steam (curso DYDS)

## Objetivo
Esta skill define una receta para crear un proyecto nuevo con la misma arquitectura por capas del curso, aplicada al dominio de juegos/plataforma Steam.

El objetivo es mantener:
- separacion por capas (`presentation`, `domain`, `data`)
- inversion de dependencias (`ViewModel -> UseCase -> Repository (I)`)
- composicion por DI (`RepositoryImpl` conectado a `Repository (I)`)
- uso combinado de fuentes `local` y `remote`

## Capas

### Presentation
Responsable de UI, estado y eventos de usuario.

### Domain
Responsable de reglas de negocio, entidades y contratos.

### Data
Responsable de implementaciones concretas, acceso a APIs y persistencia local.

### DI (composicion)
Conecta implementaciones concretas de `data` con contratos de `domain`.

## Componentes por capa

### Presentation

#### Screens
- `BibliotecaScreen`: lista de biblioteca, filtro por genero y orden por horas jugadas.
- `BusquedaScreen`: busqueda de perfil por SteamID.
- `DetalleScreen`: detalle del juego combinando APIs (logros propios + informacion editorial).
- `FavoritosScreen`: watchlist persistida localmente.

#### ViewModels
- `BibliotecaViewModel`
- `BusquedaViewModel`
- `DetalleViewModel`
- `FavoritosViewModel`

Regla: las screens nunca llaman repositorios directos; solo interactuan con ViewModels.

### Domain

#### Entities
- `Juego`
- `Perfil`

#### Use Cases
- `FiltrarGeneroUseCase`
- `OrdenarPorHorasUseCase`
- `GetBibliotecaUseCase`
- `GetPerfilUseCase`
- `GetDetalleUseCase`
- `GetFavoritosUseCase`

#### Repository (Interfaz)
- `Repository (I)` como contrato unico para los casos de uso.

Regla: `domain` no depende de detalles de red, `ktor`, SQL ni frameworks de UI.

### Data

#### RepositoryImpl
- `RepositoryImpl` implementa `Repository (I)`.

#### Sources
- `Local`: persistencia de favoritos y cache.
- `Remote`: integraciones con API de Steam + API editorial.

Regla: el mapeo de modelos remotos a entidades de dominio ocurre en `data`.

## Contratos recomendados
Define primero contratos y luego implementaciones.

```kotlin
interface Repository {
    suspend fun getBiblioteca(steamId: String): List<Juego>
    suspend fun getPerfil(steamId: String): Perfil?
    suspend fun getDetalle(juegoId: String, steamId: String): Juego?
    suspend fun getFavoritos(): List<Juego>
    suspend fun agregarFavorito(juego: Juego)
    suspend fun quitarFavorito(juegoId: String)
}

interface RemoteDataSource {
    suspend fun getBiblioteca(steamId: String): List<Juego>
    suspend fun getPerfil(steamId: String): Perfil?
    suspend fun getDetalle(juegoId: String, steamId: String): Juego?
}

interface LocalDataSource {
    suspend fun getFavoritos(): List<Juego>
    suspend fun guardarFavorito(juego: Juego)
    suspend fun eliminarFavorito(juegoId: String)
    suspend fun getBibliotecaCache(steamId: String): List<Juego>?
    suspend fun guardarBibliotecaCache(steamId: String, juegos: List<Juego>)
}
```

Nota: para detalle, puedes tener dos remotos (`SteamRemoteDataSource` y `EditorialRemoteDataSource`) y combinarlos en un broker dentro de `data`.

## Flujo general

```text
View  ->  ViewModel  ->  UseCase  ->  Repository (I)
                                       |
                                       v   (DI)
                                RepositoryImpl
                                |- Local
                                \- Remote (Steam + Editorial)
```

### Flujos clave
- Biblioteca:
  1. `BibliotecaViewModel` llama `GetBibliotecaUseCase`.
  2. `RepositoryImpl` intenta cache local; si no hay, consulta remoto y guarda cache.
  3. `FiltrarGeneroUseCase` y `OrdenarPorHorasUseCase` aplican reglas de negocio.

- Busqueda de perfil:
  1. `BusquedaViewModel` llama `GetPerfilUseCase` con SteamID.
  2. `RepositoryImpl` consulta remoto y devuelve `Perfil` de dominio.

- Detalle:
  1. `DetalleViewModel` llama `GetDetalleUseCase`.
  2. `RepositoryImpl` combina datos de Steam (logros/usuario) y editorial (metadata).

- Favoritos:
  1. `FavoritosViewModel` llama `GetFavoritosUseCase`.
  2. `RepositoryImpl` usa solo `LocalDataSource` para leer/escribir watchlist.

## Scaffold checklist (paso a paso)
- [ ] Crear paquetes por capa: `presentation`, `domain`, `data`, `di`.
- [ ] Definir entidades `Juego` y `Perfil` en `domain/entity`.
- [ ] Definir `Repository (I)` en `domain`.
- [ ] Crear interfaces de use cases y sus implementaciones.
- [ ] Implementar `RemoteDataSource` (Steam y editorial).
- [ ] Implementar `LocalDataSource` (favoritos + cache).
- [ ] Implementar `RepositoryImpl` con estrategia cache-first.
- [ ] Resolver mapeos DTO -> dominio en `data`.
- [ ] Crear `DependencyInjector` que conecte contratos con implementaciones.
- [ ] Crear ViewModels por pantalla y conectarlos a use cases.

## Testing checklist

### Data
- [ ] si hay cache de biblioteca, no consultar remoto.
- [ ] si no hay cache, consultar remoto y persistir cache.
- [ ] combinar correctamente datos Steam + editorial en detalle.
- [ ] guardar y eliminar favoritos en local.

### Domain
- [ ] `FiltrarGeneroUseCase` filtra correctamente.
- [ ] `OrdenarPorHorasUseCase` ordena correctamente.
- [ ] `GetBibliotecaUseCase`, `GetPerfilUseCase`, `GetDetalleUseCase`, `GetFavoritosUseCase` delegan y/o aplican reglas esperadas.

### Presentation
- [ ] `BibliotecaViewModel` emite estados de loading y resultado.
- [ ] `BusquedaViewModel` maneja SteamID valido/invalido.
- [ ] `DetalleViewModel` emite estado combinado o vacio ante error.
- [ ] `FavoritosViewModel` actualiza estado tras agregar/quitar favoritos.

## Definition of Done
La arquitectura esta bien replicada si:
- `presentation` depende solo de use cases.
- `domain` define contratos y no conoce implementaciones concretas.
- `data` implementa contratos del dominio y encapsula fuentes local/remota.
- DI conecta `RepositoryImpl` con `Repository (I)` sin acoplar UI a data.
- existe al menos un test por capa (`presentation`, `domain`, `data`).

## Comandos de verificacion
```bash
./gradlew :composeApp:build
./gradlew :composeApp:desktopTest
```
