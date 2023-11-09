package souza.edijanio.treinoacademia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import souza.edijanio.treinoacademia.model.Training
import souza.edijanio.treinoacademia.model.TrainingWithExercises

@Dao
interface TrainingDao {

    @Query("SELECT * FROM Training")
    suspend fun getAllTrainings() : List<Training>

    @Query("SELECT * FROM training WHERE name == :trainingName")
    suspend fun getTrainingByName(trainingName: String) : Training

    @Update
    suspend fun updateTraining(training: Training)

    @Insert
    suspend fun addTraining(vararg training: Training)

    @Transaction
    @Query("SELECT * FROM training WHERE name = :name")
    suspend fun getAllTrainingWithExercises(name : String): TrainingWithExercises
}