package com.example.techstore.presentacion.Home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.techstore.domain.model.Producto

@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onClearFilter: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToAdmin = onNavigateToAdmin,
        onNavigateToCart = onNavigateToCart,
        onNavigateToCategories = onNavigateToCategories,
        onNavigateToFavorites = onNavigateToFavorites,
        onNavigateToProfile = onNavigateToProfile,
        onClearFilter = onClearFilter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onClearFilter: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "TechStore", fontWeight = FontWeight.Bold) },
                    actions = {
                        if (state.isAdmin) {
                            IconButton(onClick = onNavigateToAdmin) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(HomeUiEvent.Buscar(it)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("¿Qué estás buscando?") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )

                if (state.activeCategory != null) {
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Categoría: ${state.activeCategory}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            TextButton(onClick = onClearFilter) { Text("Ver todo", fontWeight = FontWeight.ExtraBold) }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = onNavigateToCategories, icon = { Icon(Icons.Default.Category, "Categorías") }, label = { Text("Categorías") })
                NavigationBarItem(selected = false, onClick = onNavigateToFavorites, icon = { Icon(Icons.Default.Favorite, "Favoritos") }, label = { Text("Favoritos") })
                NavigationBarItem(selected = false, onClick = onNavigateToCart, icon = { Icon(Icons.Default.ShoppingCart, "Carrito") }, label = { Text("Carrito") })
                NavigationBarItem(selected = false, onClick = onNavigateToProfile, icon = { Icon(Icons.Default.Person, "Perfil") }, label = { Text("Perfil") })
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading && state.productos.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.productos.isEmpty() && !state.isLoading) {
                Text(
                    text = "No hay productos en esta categoría.",
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.productos) { producto ->
                        ProductoItem(
                            nombre = producto.nombre,
                            precio = producto.precio,
                            imagenUrl = producto.imagenUrl,
                            onClick = { onNavigateToDetail(producto.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(nombre: String, precio: Double, imagenUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = imagenUrl.ifBlank { "https://via.placeholder.com/150" },
                contentDescription = nombre,
                modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = nombre, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                Text(text = "$${String.format(java.util.Locale.US, "%.2f", precio)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            state = HomeState(
                isAdmin = true,
                searchQuery = "",
                productos = listOf(
                    Producto(
                        id=1, nombre="JBL Boombox", precio=450.0, imagenUrl="", marca="", descripcion="",
                        stock=10, categoriaId=1, procesador="", ram="", almacenamiento="",
                        calificacion = 5.0, categoria = "Audio", galeriaUrls = emptyList(), precioOferta = null
                    ),
                    Producto(
                        id=2, nombre="LG XBOOM", precio=300.0, imagenUrl="", marca="", descripcion="",
                        stock=5, categoriaId=1, procesador="", ram="", almacenamiento="",
                        calificacion = 4.5, categoria = "Audio", galeriaUrls = emptyList(), precioOferta = null
                    )
                )
            ),
            onEvent = {}, onNavigateToDetail = {}, onNavigateToAdmin = {}, onNavigateToCart = {}, onNavigateToCategories = {}, onNavigateToFavorites = {}, onNavigateToProfile = {}, onClearFilter = {}
        )
    }
}