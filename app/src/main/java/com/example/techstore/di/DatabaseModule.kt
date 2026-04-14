package com.example.techstore.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.techstore.data.local.dao.CartDao
import com.example.techstore.data.local.dao.CategoryDao
import com.example.techstore.data.local.dao.FavoriteDao
import com.example.techstore.data.local.dao.OrderDao
import com.example.techstore.data.local.dao.ProductoDao
import com.example.techstore.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "techstore_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductoDao(db: AppDatabase): ProductoDao = db.productoDao

    @Provides
    @Singleton
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao

    @Provides
    @Singleton
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao
}