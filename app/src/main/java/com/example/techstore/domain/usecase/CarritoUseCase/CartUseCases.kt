package com.example.techstore.domain.usecase.CarritoUseCase

import javax.inject.Inject

data class CartUseCases @Inject constructor(
    val getCart: GetCartUseCase,
    val addToCart: AddToCartUseCase,
    val removeFromCart: RemoveFromCartUseCase,
    val syncCart: SyncCartUseCase,
    val decrementQuantity: DecrementQuantityUseCase
)