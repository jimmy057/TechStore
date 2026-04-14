package com.example.techstore.presentacion.Navegacion

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")

    object Home : Routes("home?category={categoryName}") {
        fun createRoute(category: String? = null) =
            if (category != null) "home?category=$category" else "home"
    }

    object Admin : Routes("admin")
    object Profile : Routes("profile")

    object Categories : Routes("categories")
    object Favorites : Routes("favorites")

    object Cart : Routes("cart")
    object Checkout : Routes("checkout")
    object Success : Routes("success")

    object OrderHistory : Routes("order_history")
    object OrderDetail : Routes("order_detail/{orderId}") {
        fun createRoute(orderId: Int) = "order_detail/$orderId"
    }

    object ProductDetail : Routes("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
}