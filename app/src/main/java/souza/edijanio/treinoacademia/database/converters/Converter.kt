package souza.edijanio.treinoacademia.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import souza.edijanio.treinoacademia.model.Exercise
import kotlin.reflect.typeOf

class Converter {

    @TypeConverter
    fun fromExerciseToString(exercise: Exercise?): String {
        return Gson().toJson(exercise)
    }

    @TypeConverter
    fun fromStringToExercise(exerciseString: String?): Exercise {
        return Gson().fromJson(exerciseString, Exercise::class.java)
    }
}