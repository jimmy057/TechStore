package com.example.techstore.presentacion.Carrito

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.techstore.domain.model.CartItem

@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(CartUiEvent.LimpiarMensaje)
        }
    }

    CartScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onNavigateToCheckout = onNavigateToCheckout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    state: CartUiState,
    onEvent: (CartUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Atrás") }
                }
            )
        },
        bottomBar = {
            if (state.items.isNotEmpty()) {
                Surface(tonalElevation = 8.dp, shadowElevation = 8.dp) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total a pagar:", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$${String.format(java.util.Locale.US, "%.2f", state.total)}",
                                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onNavigateToCheckout,
                            modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Continuar al Pago", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.items.isEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 80.sp)
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.items) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = item.imagenUrl, contentDescription = null,
                                    modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.nombre, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text("$${item.precio}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Surface(
                                            onClick = { onEvent(CartUiEvent.DecrementarCantidad(item.productoId)) },
                                            shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(32.dp)
                                        ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(18.dp)) } }
                                        Text(text = "${item.cantidad}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                        Surface(
                                            onClick = { onEvent(CartUiEvent.IncrementarCantidad(item.productoId)) },
                                            shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp)
                                        ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)) } }
                                    }
                                }
                                IconButton(onClick = { onEvent(CartUiEvent.EliminarItem(item.productoId)) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    MaterialTheme {
        CartScreenContent(
            state = CartUiState(
                items = listOf(
                    CartItem(productoId = 1, nombre = "Auriculares Bluetooth", precio = 59.99, cantidad = 2, imagenUrl = ""),
                    CartItem(productoId = 2, nombre = "Teclado Mecánico", precio = 120.00, cantidad = 1, imagenUrl = "")
                ),
                total = 239.98
            ),
            onEvent = {}, onNavigateBack = {}, onNavigateToCheckout = {}
        )
    }
}