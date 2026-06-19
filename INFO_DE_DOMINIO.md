# Arquitectura de la Aplicación

## Capas

---

## 📱 Presentation

### View (Screens)

| Screen | Descripción |
|---|---|
| **BibliotecaScreen** | Lista de biblioteca con filtro por género y ordenamiento por horas jugadas |
| **BusquedaScreen** | Búsqueda de perfil por SteamID |
| **DetalleScreen** | Detalle del juego con datos combinados de ambas APIs (logros propios + información editorial) |
| **FavoritosScreen** | Watchlist de juegos favoritos persistida localmente |

### ViewModel

- BibliotecaViewModel
- BusquedaViewModel
- DetalleViewModel
- FavoritosViewModel

> Las Views se comunican con los ViewModels mediante dependencias (flechas punteadas).

---

## 🧠 Domain

### Entities

- `Juego`
- `Perfil`

### Use Cases

| Use Case | Descripción |
|---|---|
| `FiltrarGeneroUseCase` | Filtra juegos por género |
| `OrdenarPorHorasUseCase` | Ordena juegos por horas jugadas |
| `GetBibliotecaUseCase` | Obtiene la biblioteca del usuario |
| `GetPerfilUseCase` | Obtiene el perfil por SteamID |
| `GetDetalleUseCase` | Obtiene el detalle de un juego |
| `GetFavoritosUseCase` | Obtiene los juegos favoritos |

### Repository (Interfaz)

- `Repository (I)` — interfaz definida en el dominio, implementada en la capa de datos.

> Los ViewModels dependen de los Use Cases. Los Use Cases dependen de la interfaz `Repository (I)` (inversión de dependencias — DI).

---

## 💾 Data

### RepositoryImpl

Implementación concreta de `Repository (I)`.

| Fuente | Descripción |
|---|---|
| **Local** | Persistencia local (favoritos, caché) |
| **Remote** | APIs remotas (Steam, editorial) |

> `RepositoryImpl` implementa `Repository (I)` del dominio (flecha punteada hacia arriba).  
> La inyección de dependencias (DI) conecta la implementación con la interfaz.

---

## Flujo general

```
View  ──►  ViewModel  ──►  UseCase  ──►  Repository (I)
                                              │
                                              ▼  (DI)
                                       RepositoryImpl
                                       ├── Local
                                       └── Remote
```