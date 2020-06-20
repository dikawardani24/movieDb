package dika.wardani.local.dao

import androidx.room.*
import dika.wardani.local.entity.FavouriteMovieEntity

@Dao
interface FavouriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun save(favouriteMovieEntity: FavouriteMovieEntity): Long

    @Query("SELECT *FROM fav_movie WHERE movie_id=:movieId")
    fun findById(movieId: Int): FavouriteMovieEntity?

    @Query("SELECT *FROM fav_movie")
    fun findAll(): List<FavouriteMovieEntity>

    @Query("SELECT *FROM fav_movie ORDER BY title ASC LIMIT :limit OFFSET :offset")
    fun findAll(offset: Int, limit: Int): List<FavouriteMovieEntity>

    @Query("SELECT COUNT(*) FROM fav_movie")
    fun count(): Int

    @Delete
    fun delete(favouriteMovieEntity: FavouriteMovieEntity)
}