package org.example.dyds_proyecto2_ramones.presentation.detalle

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.model.Logro
import org.example.dyds_proyecto2_ramones.data.remote.translation.TranslationRemoteDataSource
import org.example.dyds_proyecto2_ramones.domain.repository.DetalleRepository
import org.example.dyds_proyecto2_ramones.domain.repository.FavoritosRepository
import org.example.dyds_proyecto2_ramones.domain.usecase.AgregarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetDetalleUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.GetFavoritosUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.TranslateDescriptionUseCase
import org.example.dyds_proyecto2_ramones.presentation.common.UiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DetalleViewModelTest {

    private val detalleRepository: DetalleRepository = mockk()
    private val favoritosRepository: FavoritosRepository = mockk()
    private val translationRemoteDataSource: TranslationRemoteDataSource = mockk()

    private val getDetalleUseCase = GetDetalleUseCase(detalleRepository)
    private val getFavoritosUseCase = GetFavoritosUseCase(favoritosRepository)
    private val agregarFavoritoUseCase = AgregarFavoritoUseCase(favoritosRepository)
    private val eliminarFavoritoUseCase = EliminarFavoritoUseCase(favoritosRepository)
    private val translateDescriptionUseCase = TranslateDescriptionUseCase(translationRemoteDataSource)

    private fun createViewModel() = DetalleViewModel(
        getDetalleUseCase = getDetalleUseCase,
        getFavoritosUseCase = getFavoritosUseCase,
        agregarFavoritoUseCase = agregarFavoritoUseCase,
        eliminarFavoritoUseCase = eliminarFavoritoUseCase,
        translateDescriptionUseCase = translateDescriptionUseCase,
    )

    @Test
    fun `detalle completo carga en success con datos de rawg`() = runBlocking {
        val detalle = sampleDetalle(
            descripcion = "Descripcion completa",
            metacritic = 91,
            screenshots = listOf("s1.png"),
        )
        coEvery { detalleRepository.getDetalle("steam-1", "730") } returns Result.success(detalle)
        coEvery { favoritosRepository.getFavoritos() } returns flowOf(emptyList())
        coEvery { translationRemoteDataSource.translateToSpanish(any()) } returns Result.success("Descripcion completa")

        val viewModel = createViewModel()
        viewModel.cargarDetalle("steam-1", "730")
        viewModel.traducirDescripcionActual()

        val state = viewModel.uiState.value
        assertIs<UiState.Success<DetalleJuego>>(state)
        assertEquals("Descripcion completa", state.data.descripcion)
        assertEquals("Descripcion completa", viewModel.descripcionTraducida.value)
        assertEquals(91, state.data.metacriticScore)
        assertEquals(listOf("s1.png"), state.data.screenshots)
        assertFalse(viewModel.esFavorito.value)
    }

    @Test
    fun `fallback sin rawg mantiene datos de steam`() = runBlocking {
        val detalle = sampleDetalle(
            descripcion = "",
            metacritic = null,
            screenshots = emptyList(),
        )
        coEvery { detalleRepository.getDetalle("steam-1", "730") } returns Result.success(detalle)
        coEvery { favoritosRepository.getFavoritos() } returns flowOf(emptyList())
        coEvery { translationRemoteDataSource.translateToSpanish(any()) } returns Result.success("Texto")

        val viewModel = createViewModel()
        viewModel.cargarDetalle("steam-1", "730")

        val state = viewModel.uiState.value
        assertIs<UiState.Success<DetalleJuego>>(state)
        assertEquals("Counter-Strike 2", state.data.juego.nombre)
        assertEquals("", state.data.descripcion)
        assertEquals(null, state.data.metacriticScore)
        assertTrue(state.data.screenshots.isEmpty())
    }

    @Test
    fun `toggle favorito agrega y elimina segun estado actual`() = runBlocking {
        val detalle = sampleDetalle(
            descripcion = "Descripcion",
            metacritic = 80,
            screenshots = emptyList(),
        )
        val favoritos = mutableListOf<Juego>()

        coEvery { detalleRepository.getDetalle("steam-1", "730") } returns Result.success(detalle)
        coEvery { favoritosRepository.getFavoritos() } answers { flowOf(favoritos.toList()) }
        coEvery { translationRemoteDataSource.translateToSpanish(any()) } returns Result.success("Descripcion")
        coEvery { favoritosRepository.agregar(any()) } answers {
            favoritos.add(firstArg<DetalleJuego>().juego)
        }
        coEvery { favoritosRepository.eliminar(any()) } answers {
            val appId = firstArg<String>()
            favoritos.removeAll { it.appId == appId }
        }
        coEvery { favoritosRepository.getDetalleLocal(any()) } returns null

        val viewModel = createViewModel()
        viewModel.cargarDetalle("steam-1", "730")
        assertFalse(viewModel.esFavorito.value)

        viewModel.toggleFavorito()
        assertTrue(viewModel.esFavorito.value)

        viewModel.toggleFavorito()
        assertFalse(viewModel.esFavorito.value)

        coVerify(exactly = 1) { favoritosRepository.agregar(any()) }
        coVerify(exactly = 1) { favoritosRepository.eliminar("730") }
    }

    @Test
    fun `detalle local desde favoritos funciona sin steamId`() = runBlocking {
        val detalleLocal = sampleDetalle(
            descripcion = "Descripcion local favorita",
            metacritic = null,
            screenshots = emptyList(),
        )
        coEvery { detalleRepository.getDetalle("", "730") } returns Result.success(detalleLocal)
        coEvery { favoritosRepository.getFavoritos() } returns flowOf(listOf(detalleLocal.juego))
        coEvery { translationRemoteDataSource.translateToSpanish(any()) } returns Result.success("Descripcion local favorita")
        coEvery { favoritosRepository.getDetalleLocal(any()) } returns detalleLocal

        val viewModel = createViewModel()
        viewModel.cargarDetalle("", "730")

        val state = viewModel.uiState.value
        assertIs<UiState.Success<DetalleJuego>>(state)
        assertEquals("Descripcion local favorita", state.data.descripcion)
        assertTrue(viewModel.esFavorito.value)
    }

    private fun sampleDetalle(
        descripcion: String,
        metacritic: Int?,
        screenshots: List<String>,
    ): DetalleJuego {
        return DetalleJuego(
            juego = Juego(
                appId = "730",
                nombre = "Counter-Strike 2",
                horasJugadas = 310.0,
                iconUrl = "",
                generos = listOf("Shooter", "Accion"),
            ),
            descripcion = descripcion,
            metacriticScore = metacritic,
            screenshots = screenshots,
            logros = listOf(
                Logro(
                    nombre = "Primera sangre",
                    descripcion = "Conseguir una baja",
                    conseguido = true,
                    iconUrl = "",
                ),
            ),
        )
    }
}
