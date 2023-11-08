package souza.edijanio.treinoacademia.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "training_database"
            ).fallbackToDestructiveMigration().build()
            database = instance
            instance
        }
    }
}