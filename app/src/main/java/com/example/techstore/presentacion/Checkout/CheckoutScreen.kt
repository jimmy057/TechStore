package com.example.techstore.presentacion.Checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.techstore.domain.model.CartItem
import java.util.Calendar
import java.util.Locale

@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onPaymentSuccess()
    }

    CheckoutScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreenContent(
    state: CheckoutUiState,
    onEvent: (CheckoutUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var nombreTarjeta by remember { mutableStateOf("") }
    var fechaExp by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var isFechaInvalida by remember { mutableStateOf(false) }

    fun validarFechaExp(fecha: String): Boolean {
        if (fecha.length < 4) return false

        val mesStr = fecha.substring(0, 2)
        val anioStr = fecha.substring(2, 4)

        val mes = mesStr.toIntOrNull() ?: return false
        val anio = anioStr.toIntOrNull() ?: return false

        if (mes !in 1..12) return false

        val cal = Calendar.getInstance()
        val anioActual = cal.get(Calendar.YEAR) % 100
        val mesActual = cal.get(Calendar.MONTH) + 1

        if (anio < anioActual) return false

        if (anio == anioActual && mes < mesActual) return false

        return true
    }

    LaunchedEffect(fechaExp) {
        isFechaInvalida = if (fechaExp.length == 4) !validarFechaExp(fechaExp) else false
    }

    val isFormularioValido = direccion.isNotBlank() &&
            nombreTarjeta.isNotBlank() &&
            numeroTarjeta.length == 16 &&
            fechaExp.length == 4 &&
            !isFechaInvalida &&
            cvv.length == 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen del Pedido", fontWeight = FontWeight.Bold)
                    state.items.forEach { item ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.nombre} x${item.cantidad}")
                            Text("$${String.format(Locale.US, "%.2f", item.precio * item.cantidad)}")
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.ExtraBold)
                        Text("$${String.format(Locale.US, "%.2f", state.total)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Text("📍 Envío", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección completa") },
                modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.LocationOn, null) }
            )

            Text("💳 Pago", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = nombreTarjeta, onValueChange = { nombreTarjeta = it }, label = { Text("Nombre en la tarjeta") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numeroTarjeta, onValueChange = { if (it.length <= 16) numeroTarjeta = it }, label = { Text("Número de tarjeta") },
                modifier = Modifier.fillMaxWidth(), visualTransformation = CardNumberTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = fechaExp,
                    onValueChange = { if (it.length <= 4) fechaExp = it },
                    label = { Text("MM/AA") },
                    modifier = Modifier.weight(1f),
                    visualTransformation = ExpirationDateTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isFechaInvalida,
                    supportingText = { if (isFechaInvalida) Text("Fecha inválida") }
                )
                OutlinedTextField(
                    value = cvv, onValueChange = { if (it.length <= 3) cvv = it }, label = { Text("CVV") }, modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                onClick = { onEvent(CheckoutUiEvent.ConfirmarPedido(direccion, "Tarjeta: $nombreTarjeta")) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isLoading && isFormularioValido,
                shape = MaterialTheme.shapes.large
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Pagar Ahora $${String.format(Locale.US, "%.2f", state.total)}", fontWeight = FontWeight.Bold)
                }
            }

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

class CardNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) { out += trimmed[i]; if (i % 4 == 3 && i != 15) out += " " }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset <= 3) offset else if (offset <= 7) offset + 1 else if (offset <= 11) offset + 2 else if (offset <= 16) offset + 3 else 19
            override fun transformedToOriginal(offset: Int) = if (offset <= 4) offset else if (offset <= 9) offset - 1 else if (offset <= 14) offset - 2 else if (offset <= 19) offset - 3 else 16
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

class ExpirationDateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) { out += trimmed[i]; if (i == 1) out += "/" }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset <= 1) offset else if (offset <= 4) offset + 1 else 5
            override fun transformedToOriginal(offset: Int) = if (offset <= 2) offset else if (offset <= 5) offset - 1 else 4
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    MaterialTheme {
        CheckoutScreenContent(
            state = CheckoutUiState(
                items = listOf(
                    CartItem(productoId = 1, nombre = "Teclado Mecánico", precio = 120.00, cantidad = 1, imagenUrl = "")
                ),
                total = 120.00
            ),
            onEvent = {}, onNavigateBack = {}
        )
    }
}

