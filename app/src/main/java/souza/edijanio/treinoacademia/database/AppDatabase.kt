package souza.edijanio.treinoacademia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import souza.edijanio.treinoacademia.database.dao.TrainingDao
import souza.edijanio.treinoacademia.model.Exercise
import souza.edijanio.treinoacademia.model.Training

@Database(entities = [Exercise::class, Training::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun trainingDao() : TrainingDao
}