package dika.wardani.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_movie")
data class FavouriteMovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    var movieId: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "release_date")
    var releaseDate: String,
    @ColumnInfo(name = "overview")
    var overview: String,
    @ColumnInfo(name = "back_drop_path")
    var backDropPath: String?,
    @ColumnInfo(name = "poster_path")
    var posterPath: String?
)