package com.example.techstore.presentacion.Order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.techstore.domain.model.Order
import java.util.Locale

@Composable
fun OrderHistoryScreen(
    onNavigateBack: () -> Unit,
    onOrderClick: (Int) -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    OrderHistoryScreenContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onOrderClick = onOrderClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreenContent(
    state: OrderUiState,
    onNavigateBack: () -> Unit,
    onOrderClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.orders.isEmpty() && !state.isLoading) {
                Text("Aún no tienes pedidos.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.orders) { order ->
                        OrderCard(order = order, onClick = { onOrderClick(order.id) })
                    }
                }
            }

            if (state.isLoading && state.orders.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (state.error != null) {
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Pedido #${order.id}", fontWeight = FontWeight.Bold)
                Text(order.fecha.take(10), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estado: ${order.estado}", color = MaterialTheme.colorScheme.secondary)
                Text("$${String.format(Locale.US, "%.2f", order.total)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderHistoryScreenPreview() {
    MaterialTheme {
        OrderHistoryScreenContent(
            state = OrderUiState(
                isLoading = false,
                orders = listOf(
                    Order(
                        id = 1042,
                        fecha = "2026-04-10T10:00:00",
                        total = 899.98,
                        estado = "Enviado",
                        direccionEnvio = "",
                        metodoPago = "",
                        usuarioId = 1,
                        detalles = emptyList()
                    ),
                    Order(
                        id = 1043,
                        fecha = "2026-04-12T15:30:00",
                        total = 120.50,
                        estado = "Entregado",
                        direccionEnvio = "",
                        metodoPago = "",
                        usuarioId = 1,
                        detalles = emptyList()
                    )
                )
            ),
            onNavigateBack = {},
            onOrderClick = {}
        )
    }
}