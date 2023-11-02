package souza.edijanio.treinoacademia.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("exercise_id")
    val exerciseId : Int = 0,
    val name : String,
    val image : String? = null,

    @ColumnInfo(name = "is_done")
    val isDone : Boolean = false,
    val series : Int,
    val repetitions : Int,
    val weight : Double? = null,
    val comment : String? = null
) : Serializable
