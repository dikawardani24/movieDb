package dika.wardani.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dika.wardani.local.converter.DateConverter
import dika.wardani.local.dao.FavouriteMovieDao
import dika.wardani.local.entity.FavouriteMovieEntity

@Database(
    version = 1, entities = [
        FavouriteMovieEntity::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val favouriteMovieDao: FavouriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "sale_inv.db"

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            return if (tempInstance != null) {
                tempInstance
            } else {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}