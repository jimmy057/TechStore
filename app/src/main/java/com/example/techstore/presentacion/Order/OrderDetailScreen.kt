package com.example.techstore.presentacion.Order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.techstore.domain.model.Order
import java.util.Locale

@Composable
fun OrderDetailScreen(
    orderId: Int,
    viewModel: OrderViewModel,
    onNavigateBack: () -> Unit
) {
    val order = viewModel.getOrderById(orderId)

    OrderDetailScreenContent(
        order = order,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreenContent(
    order: Order?,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        if (order == null) {
            Text("Pedido no encontrado", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Pedido #${order.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("Fecha: ${order.fecha.take(10)}")
                    Text("Estado: ${order.estado}")
                    Spacer(Modifier.height(8.dp))
                    Text("📍 Envío: ${order.direccionEnvio}", style = MaterialTheme.typography.bodyMedium)
                    Text("💳 Pago: ${order.metodoPago}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos Comprados", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            val detallesAgrupados = order.detalles.groupBy { it.productoId }.map { (_, lista) ->
                val itemReferencia = lista.first()
                val cantidadTotal = lista.sumOf { it.cantidad }
                itemReferencia.copy(cantidad = cantidadTotal)
            }

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(detallesAgrupados) { detalle ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${detalle.cantidad}x ${detalle.nombreProducto}", modifier = Modifier.weight(1f))
                        Text("$${String.format(Locale.US, "%.2f", detalle.precioUnitario * detalle.cantidad)}", fontWeight = FontWeight.Medium)
                    }
                    HorizontalDivider(Modifier.padding(top = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
                }
            }

            val totalReal = detallesAgrupados.sumOf { it.precioUnitario * it.cantidad }

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL FINAL", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
                Text("$${String.format(Locale.US, "%.2f", totalReal)}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderDetailScreenPreview() {
    MaterialTheme {
        OrderDetailScreenContent(
            order = Order(
                id = 1042,
                fecha = "2026-04-14T10:00:00",
                total = 899.98,
                estado = "Enviado",
                direccionEnvio = "Calle Falsa 123, Ciudad",
                metodoPago = "Tarjeta: Jimmy",
                usuarioId = 1,
                detalles = emptyList()
            ),
            onNavigateBack = {}
        )
    }
}