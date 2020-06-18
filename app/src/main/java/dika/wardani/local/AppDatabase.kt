package dika.wardani.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dika.wardani.local.converter.DateConverter

@Database(
    version = 1, entities = [

    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

//    abstract val transactionDao: TransactionDao

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