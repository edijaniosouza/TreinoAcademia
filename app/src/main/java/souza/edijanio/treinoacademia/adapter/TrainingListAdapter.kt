package souza.edijanio.treinoacademia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.helper.TRAINING
import souza.edijanio.treinoacademia.model.Training
import souza.edijanio.treinoacademia.screen.ExerciseListScreen

class TrainingListAdapter(
    private val context: Context,
    trainingList: List<Training>
) :
    RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {
    private val trainingList = trainingList.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun binding(training: Training) {
            itemView.findViewById<TextView>(R.id.main_tv_treino)
                .text = training.name
            itemView.findViewById<TextView>(R.id.main_tv_treino_description).text = training.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.training_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainingInPosition = trainingList[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ExerciseListScreen::class.java)
            intent.putExtra(TRAINING, trainingInPosition.name)
            context.startActivity(intent)
        }

        holder.binding(trainingInPosition)
    }

    override fun getItemCount(): Int = trainingList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newTraingList: List<Training>){
        this.trainingList.clear()
        this.trainingList.addAll(newTraingList)
        notifyDataSetChanged()
    }


}
