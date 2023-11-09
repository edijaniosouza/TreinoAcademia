package souza.edijanio.treinoacademia.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souza.edijanio.treinoacademia.adapter.ExerciseListAdapter
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DialogNewExerciseBinding
import souza.edijanio.treinoacademia.databinding.ExerciseListScreenBinding
import souza.edijanio.treinoacademia.model.Exercise

class ExerciseListScreen : AppCompatActivity() {

    private val binding by lazy {
        ExerciseListScreenBinding.inflate(layoutInflater)
    }

    private val trainingDao by lazy {
        DatabaseProvider.getDatabase(this).trainingDao()
    }

    private val exerciseDao by lazy {
        DatabaseProvider.getDatabase(this).exerciseDao()
    }
    private var exerciseList = listOf<Exercise>()
    private val adapter = ExerciseListAdapter(this, exerciseList)
    private var trainingName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        trainingName = intent.getStringExtra("TRAINING")

        binding.exerciseListToolbar.title = trainingName

        val detailScreenRv = binding.detailScreenRv
        detailScreenRv.adapter = adapter

        binding.exerciseListToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fabAddExercise.setOnClickListener {
            newExerciseDialog(trainingName)
        }
    }

    override fun onResume() {
        getExercisesByTraining(trainingName)
        super.onResume()
    }

    private fun getExercisesByTraining(trainingName: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            trainingName?.let {
                val treino = trainingDao.getAllTrainingWithExercises(it)
                exerciseList = treino.exercises

                withContext(Dispatchers.Main) {
                    adapter.updateList(exerciseList)
                }
            }
        }
    }

    private fun newExerciseDialog(extras: String?) {
        val exerciseDialogBinding = DialogNewExerciseBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(this)
            .setView(exerciseDialogBinding.root)
            .setTitle("Adicinar Treino")
            .setNeutralButton("CANCELAR") { _, _ -> }
            .setPositiveButton("CONFIRMAR") { _, _ ->

                CoroutineScope(Dispatchers.IO).launch {
                    exerciseDao.addExercise(
                        Exercise(
                            exerciseName = exerciseDialogBinding.dialogFormExerciseName.text.toString(),
                            series = exerciseDialogBinding.dialogFormExerciseSeries.text.toString()
                                .toInt(),
                            repetitions = exerciseDialogBinding.dialogFormExerciseRepetitions.text.toString()
                                .toInt(),
                            trainingId = extras!!
                        )
                    )

                    getExercisesByTraining(extras)
                }

            }
            .show()
    }
}