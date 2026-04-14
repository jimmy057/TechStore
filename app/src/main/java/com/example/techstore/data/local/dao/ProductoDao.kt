package com.example.techstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.techstore.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    fun getProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    fun getProductosPorCategoria(categoria: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :query || '%'")
    fun buscarProductosLocal(query: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun getProductoById(id: Int): Flow<ProductoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductos(productos: List<ProductoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: ProductoEntity)

    @Update
    suspend fun updateProducto(producto: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun deleteProductoById(id: Int)

    @Query("DELETE FROM productos WHERE isSynced = 1")
    suspend fun clearSyncedProductos()

    @Query("SELECT * FROM productos WHERE isSynced = 0")
    suspend fun getUnsyncedProductos(): List<ProductoEntity>

    @Query("UPDATE productos SET isSynced = 1, id = :nuevoId WHERE id = :idLocal")
    suspend fun markAsSynced(idLocal: Int, nuevoId: Int)
}