package souza.edijanio.treinoacademia.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("exercise_id")
    val exerciseId: Long = 0,

    @ColumnInfo("training_id")
    val trainingId: String, // referencia para model pai "Training"
    @ColumnInfo("exercise_name")
    val exerciseName: String,
    val image: String? = null,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false,
    val series: Int,
    val repetitions: Int,
    val weight: Double? = null,
    val timer: Int = 0,
    val comment: String? = null
) : Serializable
