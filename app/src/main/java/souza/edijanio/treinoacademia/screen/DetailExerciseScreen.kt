package souza.edijanio.treinoacademia.screen

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DetailExerciseScreenBinding
import souza.edijanio.treinoacademia.databinding.DialogNewExerciseBinding
import souza.edijanio.treinoacademia.helper.EXERCISES_LIST
import souza.edijanio.treinoacademia.helper.EXERCISE_ID
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

        val exerciseId = intent.getLongExtra(EXERCISE_ID, 0L)

        CoroutineScope(Dispatchers.IO).launch {
            exerciseDao.getExerciseById(exerciseId).apply {
                exercise = this
                binding.exerciseToolbar.title = this.exerciseName
                binding.exerciseScreenSeriesReps.text = getString(R.string.series_vs_rep, this.series, this.repetitions)
                binding.exerciseScreenImage.load(
                    EXERCISES_LIST[this.exerciseName],
                    imageLoader = imageLoader(this@DetailExerciseScreen)
                )

                if(this.comment.isNullOrEmpty()){
                    binding.exerciseTvCommentsDetails.visibility = View.GONE
                    binding.exerciseTvExerciseComments.visibility = View.GONE
                }else{
                    binding.exerciseTvExerciseComments.text = this.comment
                }

            }
        }
        timer()

    }

    override fun onResume() {
        topBarConfigurations()
        super.onResume()
    }

    private fun timer() {
        var isCounting = false
        var timer = binding.exerciseTimer.text.toString().toInt()
        val backupTimer = timer.toString()

        val exerciseBtnTimer = binding.exerciseBtnTimer
        exerciseBtnTimer.setOnClickListener {

            if(isCounting){
                isCounting = false
                exerciseBtnTimer.text = "Play"
                exerciseBtnTimer.icon = getDrawable(this,R.drawable.play_arrow)
            }else{
                isCounting = true
                exerciseBtnTimer.text = "Pause"
                exerciseBtnTimer.icon = getDrawable(this, R.drawable.baseline_pause_24)
            }

            lifecycleScope.launch {
                while (timer > 0 && isCounting) {
                    delay(1000)
                    timer -= 1
                    binding.exerciseTimer.text = timer.toString()

                    if(timer == 1){
                        MediaPlayer.create(this@DetailExerciseScreen, R.raw.alarm).start()
                    }
                }

                if (timer == 0) {
                    binding.exerciseTimer.text = backupTimer
                    timer = backupTimer.toInt()
                    isCounting = false
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
                        exerciseDialogBinding.dialogFormExerciseTimer.setText(it.timer.toString())
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
                                        trainingId = exercise!!.trainingId,
                                        timer = exerciseDialogBinding.dialogFormExerciseTimer.text.toString()
                                            .toInt()
                                    )
                                )

                                val updatedExercise =
                                    exerciseDao.getExerciseById(exercise!!.exerciseId)
                                withContext(Main) {
                                    binding.exerciseToolbar.title = updatedExercise.exerciseName
                                    binding.exerciseScreenSeriesReps.text =
                                        "${updatedExercise.series} X ${updatedExercise.repetitions}"
                                    binding.exerciseScreenImage.load(
                                        EXERCISES_LIST[updatedExercise.exerciseName],
                                        imageLoader = imageLoader(this@DetailExerciseScreen)
                                    )
                                    binding.exerciseTimer.text =
                                        "${updatedExercise.timer} segundos}"
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