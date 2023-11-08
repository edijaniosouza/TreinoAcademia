package souza.edijanio.treinoacademia.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import souza.edijanio.treinoacademia.database.converters.Converter
import souza.edijanio.treinoacademia.database.dao.ExerciseDao
import souza.edijanio.treinoacademia.database.dao.TrainingDao
import souza.edijanio.treinoacademia.model.Exercise
import souza.edijanio.treinoacademia.model.Training

@Database(entities = [Exercise::class, Training::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun trainingDao() : TrainingDao
    abstract fun exerciseDao() : ExerciseDao
}