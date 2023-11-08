package souza.edijanio.treinoacademia.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity
data class Training(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val description: String? = null,

)
