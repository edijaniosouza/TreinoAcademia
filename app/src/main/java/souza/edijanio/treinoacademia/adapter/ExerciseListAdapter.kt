package souza.edijanio.treinoacademia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.helper.imageLoader
import souza.edijanio.treinoacademia.model.Exercise
import souza.edijanio.treinoacademia.model.Training
import souza.edijanio.treinoacademia.screen.DetailExerciseScreen


class ExerciseListAdapter(
    private val context: Context,
    exerciseList: List<Exercise>
) :
    RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

    val exerciseList = exerciseList.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun binding(exercise: Exercise, context: Context) {
            itemView.findViewById<TextView>(R.id.exercise_name)
                .text = exercise.exerciseName

            itemView.findViewById<TextView>(R.id.exercise_repetitions).text = "Repetições: ${exercise.repetitions}"
            itemView.findViewById<TextView>(R.id.exercise_series).text = "Series: ${exercise.series}"


            /*TODO: CRIAR LISTA COM GIF E IMAGENS PARA CADA EXERCICIO*/
            val imageLoader = imageLoader(context)
          //  itemView.findViewById<ImageView>(R.id.exercise_image).load(R.drawable.musculos_exigidos_nos_exercicios_de_musculacao, imageLoader)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.exercise_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exerciseInPosition = exerciseList[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailExerciseScreen::class.java)
            intent.putExtra("EXERCISE_ID", exerciseInPosition.exerciseId)
            context.startActivity(intent)
        }

        holder.binding(exerciseInPosition, context)
    }

    override fun getItemCount(): Int = exerciseList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newExerciseList: List<Exercise>){
        this.exerciseList.clear()
        this.exerciseList.addAll(newExerciseList)
        notifyDataSetChanged()
    }

}
