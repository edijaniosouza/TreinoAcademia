package souza.edijanio.treinoacademia.screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souza.edijanio.treinoacademia.adapter.TrainingListAdapter
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DialogNewTrainingBinding
import souza.edijanio.treinoacademia.databinding.TrainingListScreenBinding
import souza.edijanio.treinoacademia.model.Exercise
import souza.edijanio.treinoacademia.model.Training
import kotlin.coroutines.CoroutineContext

class TrainingListScreen : AppCompatActivity() {

    private val binding by lazy {
        TrainingListScreenBinding.inflate(layoutInflater)
    }

    private val trainingDao by lazy {
        DatabaseProvider.getDatabase(this).trainingDao()
    }

    private val exerciseDao by lazy {
        DatabaseProvider.getDatabase(this).exerciseDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.mainRv



        CoroutineScope(IO).launch {
            val trainingList = trainingDao.getAllTrainings()

            withContext(Dispatchers.Main){
                recyclerView.adapter = TrainingListAdapter(this@TrainingListScreen, trainingList = trainingList)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.fabAddTraining.setOnClickListener {
            trainingDialog(this)
        }
    }

    private fun trainingDialog(context: Context) {

        val bindingDialog = DialogNewTrainingBinding.inflate(layoutInflater)

        AlertDialog.Builder(context)
            .setView(bindingDialog.root)
            .setTitle("Adicinar Treino")
            .setNeutralButton("CANCELAR") { _, _ -> }
            .setPositiveButton("CONFIRMAR") { _, _ ->

                CoroutineScope(IO).launch {

                    if (!bindingDialog.dialogFormTrainingName.text.isNullOrEmpty()){
                        trainingDao.addTraining(
                            Training(
                                name = bindingDialog.dialogFormTrainingName.text.toString(),
                                description = bindingDialog.dialogFormTrainingDescription.text.toString()
                            )
                        )
                    }

                    val treinos = trainingDao.getAllTrainingWithExercises()
                    Log.i("TreinoWithExercises", "Treinos: $treinos")
                }
            }
            .show()
    }
}