package souza.edijanio.treinoacademia.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import souza.edijanio.treinoacademia.adapter.TrainingListAdapter
import souza.edijanio.treinoacademia.databinding.TrainingListScreenBinding
import souza.edijanio.treinoacademia.model.Exercise
import souza.edijanio.treinoacademia.model.Training

class TrainingListScreen : AppCompatActivity() {

    private val binding by lazy {
        TrainingListScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.mainRv

        val trainingList = listOf(
            Training(
                name = "Peito e Costas",
                exercise = listOf(
                    Exercise(name = "Supino", series = 3, repetitions = 15)
                )
            ),
            Training(
                name = "Pernas",
                exercise = listOf(
                    Exercise(name = "Stiff", series = 3, repetitions = 15)
                )
            )
        )

        recyclerView.adapter = TrainingListAdapter(this, trainingList = trainingList)
    }
}