package com.example.techstore.presentacion.Navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.presentacion.Admin.AdminScreen
import com.example.techstore.presentacion.Carrito.CartScreen
import com.example.techstore.presentacion.Carrito.SuccessScreen
import com.example.techstore.presentacion.Checkout.CheckoutScreen
import com.example.techstore.presentacion.Detail.ProductDetailScreen
import com.example.techstore.presentacion.Home.HomeScreen
import com.example.techstore.presentacion.Login.LoginScreen
import com.example.techstore.presentacion.Perfil.ProfileScreen
import com.example.techstore.presentacion.Register.RegisterScreen
import com.example.techstore.presentacion.Order.OrderHistoryScreen
import com.example.techstore.presentacion.Order.OrderDetailScreen
import com.example.techstore.presentacion.Order.OrderViewModel
import com.example.techstore.presentacion.Category.CategoryScreen
import com.example.techstore.presentacion.Favorite.FavoriteScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    sessionDataStore: SessionDataStore
) {
    val isLoggedIn by sessionDataStore.isLoggedIn.collectAsState(initial = null)

    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDest = if (isLoggedIn == true) Routes.Home.route else Routes.Login.route

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(route = Routes.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.Home.createRoute()) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.Register.route) }
            )
        }

        composable(route = Routes.Register.route) {
            RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
        }

        composable(
            route = Routes.Home.route,
            arguments = listOf(
                navArgument("categoryName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            HomeScreen(
                onNavigateToDetail = { id -> navController.navigate(Routes.ProductDetail.createRoute(id)) },
                onNavigateToAdmin = { navController.navigate(Routes.Admin.route) },
                onNavigateToCart = { navController.navigate(Routes.Cart.route) },
                onNavigateToProfile = { navController.navigate(Routes.Profile.route) },
                onNavigateToCategories = { navController.navigate(Routes.Categories.route) },
                onNavigateToFavorites = { navController.navigate(Routes.Favorites.route) },
                onClearFilter = {
                    navController.navigate(Routes.Home.createRoute()) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.Categories.route) {
            CategoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onCategoryClick = { nombre ->
                    navController.navigate(Routes.Home.createRoute(nombre)) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.Favorites.route) {
            FavoriteScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate(Routes.ProductDetail.createRoute(id)) }
            )
        }

        composable(
            route = Routes.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductDetailScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(route = Routes.Cart.route) {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(Routes.Checkout.route) }
            )
        }

        composable(route = Routes.Checkout.route) {
            CheckoutScreen(
                onNavigateBack = { navController.popBackStack() },
                onPaymentSuccess = {
                    navController.navigate(Routes.Success.route) {
                        popUpTo(Routes.Cart.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.Success.route) {
            SuccessScreen(
                onGoToHome = {
                    navController.navigate(Routes.Home.createRoute()) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.Profile.route) {
            ProfileScreen(
                sessionDataStore = sessionDataStore,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOrders = { navController.navigate(Routes.OrderHistory.route) },
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.OrderHistory.route) {
            OrderHistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onOrderClick = { id -> navController.navigate(Routes.OrderDetail.createRoute(id)) }
            )
        }

        composable(
            route = Routes.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: return@composable
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.OrderHistory.route)
            }
            val orderViewModel: OrderViewModel = hiltViewModel(parentEntry)

            OrderDetailScreen(
                orderId = orderId,
                viewModel = orderViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Routes.Admin.route) {
            AdminScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}