package dika.wardani.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteEntity(
    @PrimaryKey
    var id: Long,
    @ColumnInfo(name = "movie_id")
    var movieId: Long
)