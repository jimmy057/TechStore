package com.example.techstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.techstore.data.local.dao.*
import com.example.techstore.data.local.entities.*

@Database(
    entities = [
        ProductoEntity::class,
        CartEntity::class,
        CategoryEntity::class,
        FavoriteEntity::class,
        OrderEntity::class,
        OrderDetailEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val productoDao: ProductoDao
    abstract val cartDao: CartDao

    abstract val categoryDao: CategoryDao
    abstract val favoriteDao: FavoriteDao
    abstract val orderDao: OrderDao
}