package souza.edijanio.treinoacademia.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import souza.edijanio.treinoacademia.model.Exercise

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE exercise_id = :exercise")
    suspend fun getExercise(exercise: Exercise): Exercise

    @Insert
    suspend fun addExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}