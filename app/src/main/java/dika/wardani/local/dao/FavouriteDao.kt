package dika.wardani.local.dao

import androidx.room.Dao
import androidx.room.Insert
import dika.wardani.local.entity.FavouriteEntity

@Dao
interface FavouriteDao {
    @Insert
    fun save(favouriteEntity: FavouriteEntity)
}