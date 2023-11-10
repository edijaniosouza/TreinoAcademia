package souza.edijanio.treinoacademia.screen

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DetailExerciseScreenBinding
import souza.edijanio.treinoacademia.databinding.DialogNewExerciseBinding
import souza.edijanio.treinoacademia.helper.EXERCISES_LIST
import souza.edijanio.treinoacademia.helper.imageLoader
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
                binding.exerciseToolbar.title = this.exerciseName
                binding.exerciseScreenSeriesReps.text = "${this.series} X ${this.repetitions}"
                binding.exerciseScreenImage.load(
                    EXERCISES_LIST[this.exerciseName],
                    imageLoader = imageLoader(this@DetailExerciseScreen)
                )
            }
        }
        timer()
    }

    override fun onResume() {
        topBarConfigurations()
        super.onResume()
    }

    private fun timer() {
        binding.exerciseBtnTimer.setOnClickListener {
            var timer = binding.exerciseTimer.text.toString().toInt()
            val backupTimer = timer.toString()

            CoroutineScope(Dispatchers.IO).launch {
                while (timer > 0) {
                    delay(1000)
                    timer -= 1

                    withContext(Dispatchers.Main) {
                        binding.exerciseTimer.text = timer.toString()
                    }
                }

                withContext(Dispatchers.Main) {
                    MediaPlayer.create(this@DetailExerciseScreen, R.raw.alarm).start()
                    binding.exerciseTimer.text = backupTimer
                }
            }
        }
    }

    private fun topBarConfigurations() {


        binding.exerciseToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.exerciseToolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.edit_item -> {
                    val exerciseDialogBinding = DialogNewExerciseBinding.inflate(layoutInflater)

                    exercise?.let {
                        exerciseDialogBinding.dialogFormExerciseName.setText(it.exerciseName)
                        exerciseDialogBinding.dialogFormExerciseSeries.setText(it.series.toString())
                        exerciseDialogBinding.dialogFormExerciseRepetitions.setText(it.repetitions.toString())
                    }

                    val items = EXERCISES_LIST.keys.toList()
                    val adapter = ArrayAdapter(this, R.layout.list_item_exercise_name, items)
                    exerciseDialogBinding.dialogFormExerciseName.setAdapter(adapter)

                    MaterialAlertDialogBuilder(this)
                        .setView(exerciseDialogBinding.root)
                        .setTitle("Editar Treino")
                        .setNegativeButton("CANCELAR") { _, _ -> }
                        .setPositiveButton("CONFIRMAR") { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                exerciseDao.updateExercise(
                                    Exercise(
                                        exerciseId = exercise!!.exerciseId,
                                        exerciseName = exerciseDialogBinding.dialogFormExerciseName.text.toString(),
                                        series = exerciseDialogBinding.dialogFormExerciseSeries.text.toString()
                                            .toInt(),
                                        repetitions = exerciseDialogBinding.dialogFormExerciseRepetitions.text.toString()
                                            .toInt(),
                                        trainingId = exercise!!.trainingId
                                    )
                                )

                                val updatedExercise =
                                    exerciseDao.getExerciseById(exercise!!.exerciseId)
                                withContext(Dispatchers.Main) {
                                    binding.exerciseToolbar.title = updatedExercise.exerciseName
                                    binding.exerciseScreenSeriesReps.text =
                                        "${updatedExercise.series} X ${updatedExercise.repetitions}"
                                    binding.exerciseScreenImage.load(
                                        EXERCISES_LIST[updatedExercise.exerciseName],
                                        imageLoader = imageLoader(this@DetailExerciseScreen)
                                    )
                                }
                            }
                        }
                        .show()
                    true
                }

                R.id.delete_item -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Deseja apagar o exercicio?")
                        .setPositiveButton("SIM") { _, _ ->
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
                        }
                        .setNegativeButton("NÃO") { _, _ -> }
                        .show()

                    true
                }

                else -> false
            }
        }
    }
}