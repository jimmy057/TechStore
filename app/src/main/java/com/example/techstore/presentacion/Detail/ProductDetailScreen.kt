package com.example.techstore.presentacion.Detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.techstore.domain.model.Producto

@Composable
fun ProductDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.cartMessage) {
        state.cartMessage?.let { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(DetailEvent.LimpiarMensajeCarrito)
        }
    }

    ProductDetailScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreenContent(
    state: DetailUiState, //
    onEvent: (DetailEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalles del Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás") }
                },
                actions = {
                    IconButton(onClick = { onEvent(DetailEvent.ToggleFavorito) }) {
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (state.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface, titleContentColor = MaterialTheme.colorScheme.onSurface)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.surface) {
                Button(
                    onClick = { onEvent(DetailEvent.AgregarAlCarrito) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading && state.producto != null
                ) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar al Carrito", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(text = state.error ?: "Error desconocido", modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
            } else {
                state.producto?.let { producto ->
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        AsyncImage(
                            model = producto.imagenUrl.takeIf { it.isNotBlank() } ?: "https://via.placeholder.com/400",
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxWidth().height(300.dp).background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Fit
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = producto.marca.uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = producto.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "$${producto.precio}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "Descripción", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = producto.descripcion.ifBlank { "Sin descripción disponible." }, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    MaterialTheme {
        ProductDetailScreenContent(
            state = DetailUiState(
                isLoading = false,
                isFavorite = true,
                producto = Producto(
                    id = 1, nombre = "JBL Boombox 3", precio = 450.0, imagenUrl = "", marca = "JBL",
                    descripcion = "Bocina bluetooth súper potente con batería de 24 horas y graves profundos. Ideal para fiestas al aire libre.",
                    stock = 10, categoriaId = 1, procesador = "", ram = "", almacenamiento = "",
                    calificacion = 5.0, categoria = "Audio", galeriaUrls = emptyList(), precioOferta = null
                )
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}