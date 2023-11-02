package souza.edijanio.treinoacademia.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity()

data class Training(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String,
    val description: String? = null,

    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "uid"
    )
    val exercise: List<Exercise>
) : Serializable
