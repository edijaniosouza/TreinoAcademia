package souza.edijanio.treinoacademia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import souza.edijanio.treinoacademia.model.Training

@Dao
interface TrainingDao {

    @Query("SELECT * FROM Training")
    suspend fun getAll() : List<Training>

    @Query("SELECT * FROM training WHERE uid == (:trainingID)")
    suspend fun getTraining(trainingID: Int) : Training

    @Update
    suspend fun updateTraining(training: Training)

    @Insert
    suspend fun newTraining(training: Training)
}