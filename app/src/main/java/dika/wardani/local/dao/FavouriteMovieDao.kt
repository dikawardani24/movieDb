package dika.wardani.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dika.wardani.local.entity.FavouriteMovieEntity

@Dao
interface FavouriteMovieDao {
    @Insert
    fun save(favouriteMovieEntity: FavouriteMovieEntity)

    @Query("SELECT *FROM fav_movie WHERE movie_id=:movieId")
    fun findById(movieId: Int): FavouriteMovieEntity?

    @Query("SELECT *FROM fav_movie")
    fun findAll(): List<FavouriteMovieEntity>
}