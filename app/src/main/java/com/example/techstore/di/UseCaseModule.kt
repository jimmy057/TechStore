package com.example.techstore.di

import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.domain.repository.*
import com.example.techstore.domain.usecase.CarritoUseCase.*
import com.example.techstore.domain.usecase.CategoryUseCase.*
import com.example.techstore.domain.usecase.FavoriteUseCase.*
import com.example.techstore.domain.usecase.LoginUseCase.*
import com.example.techstore.domain.usecase.OrderUseCases.CreateOrderUseCase
import com.example.techstore.domain.usecase.OrderUseCases.GetOrderHistoryUseCase
import com.example.techstore.domain.usecase.OrderUseCases.OrderUseCases
import com.example.techstore.domain.usecase.OrderUseCases.RefreshOrderHistoryUseCase
import com.example.techstore.domain.usecase.ProductosUseCase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideProductoUseCases(repository: ProductoRepository): ProductoUseCases {
        return ProductoUseCases(
            getProductos = GetProductosUseCase(repository),
            getProductoById = GetProductoByIdUseCase(repository),
            refreshProductos = RefreshProductosUseCase(repository),
            buscarProductos = BuscarProductosUseCase(repository),
            createProducto = CreateProductoUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        repository: AuthRepository,
        sessionDataStore: SessionDataStore
    ): AuthUseCases {
        return AuthUseCases(
            login = LoginUseCase(repository, sessionDataStore),
            register = RegisterUseCase(repository),
            logout = LogoutUseCase(sessionDataStore)
        )
    }

    @Provides
    @Singleton
    fun provideCartUseCases(repository: CartRepository): CartUseCases {
        return CartUseCases(
            getCart = GetCartUseCase(repository),
            addToCart = AddToCartUseCase(repository),
            removeFromCart = RemoveFromCartUseCase(repository),
            syncCart = SyncCartUseCase(repository),
            decrementQuantity = DecrementQuantityUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategories = GetCategoriesUseCase(repository),
            refreshCategories = RefreshCategoriesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFavoriteUseCases(repository: FavoriteRepository): FavoriteUseCases {
        return FavoriteUseCases(
            getFavorites = GetFavoritesUseCase(repository),
            toggleFavorite = ToggleFavoriteUseCase(repository),
            refreshFavorites = RefreshFavoritesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideOrderUseCases(repository: OrderRepository): OrderUseCases {
        return OrderUseCases(
            getOrderHistory = GetOrderHistoryUseCase(repository),
            refreshOrderHistory = RefreshOrderHistoryUseCase(repository),
            createOrder = CreateOrderUseCase(repository)
        )
    }
}
