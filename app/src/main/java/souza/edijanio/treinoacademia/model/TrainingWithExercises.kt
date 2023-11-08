package souza.edijanio.treinoacademia.model

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingWithExercises (
    @Embedded val training: Training,
    @Relation(
        parentColumn = "name",
        entityColumn = "training_id"
    )
    val exercises: List<Exercise>
)