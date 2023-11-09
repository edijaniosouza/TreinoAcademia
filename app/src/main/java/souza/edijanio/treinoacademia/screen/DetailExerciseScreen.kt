package souza.edijanio.treinoacademia.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DetailExerciseScreenBinding
import souza.edijanio.treinoacademia.model.Exercise

class DetailExerciseScreen : AppCompatActivity() {

    private val exerciseDao by lazy {
        DatabaseProvider.getDatabase(this).exerciseDao()
    }
    private val binding by lazy {
        DetailExerciseScreenBinding.inflate(layoutInflater)
    }

    private var exercise: Exercise? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val exerciseId = intent.getLongExtra("EXERCISE_ID", 0L)

        CoroutineScope(Dispatchers.IO).launch {
            exerciseDao.getExerciseById(exerciseId).apply {
                exercise = this
            }
        }

    }

    override fun onResume() {
        topBarConfigurations()
        super.onResume()
    }
    private fun topBarConfigurations() {
        exercise?.let {
            binding.exerciseToolbar.title = it.exerciseName
            binding.exerciseScreenSeriesReps.text = "${exercise?.series} X ${exercise?.repetitions}"
        }

        binding.exerciseToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.exerciseToolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.edit_item -> {
                    true
                }

                R.id.delete_item -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            exercise?.let {
                                exerciseDao.deleteExercise(it)
                            }
                        } catch (exception: Exception) {
                            Toast.makeText(
                                this@DetailExerciseScreen,
                                "Exercicio não encontrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}