package souza.edijanio.treinoacademia.screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import souza.edijanio.treinoacademia.adapter.TrainingListAdapter
import souza.edijanio.treinoacademia.database.DatabaseProvider
import souza.edijanio.treinoacademia.databinding.DialogNewTrainingBinding
import souza.edijanio.treinoacademia.databinding.TrainingListScreenBinding
import souza.edijanio.treinoacademia.model.Training

class TrainingListScreen : AppCompatActivity() {

    private val binding by lazy {
        TrainingListScreenBinding.inflate(layoutInflater)
    }

    private val trainingDao by lazy {
        DatabaseProvider.getDatabase(this).trainingDao()
    }
    private var trainingList = listOf<Training>()
    private val adapter = TrainingListAdapter(this@TrainingListScreen, trainingList = trainingList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.mainRv
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        binding.fabAddTraining.setOnClickListener {
            trainingDialog(this)
        }

        CoroutineScope(IO).launch {
            trainingList = trainingDao.getAllTrainings()
            withContext(Main){
                adapter.updateList(trainingList)
            }
        }

    }

    private fun trainingDialog(context: Context) {

        val bindingDialog = DialogNewTrainingBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(context)
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

                        trainingList = trainingDao.getAllTrainings()
                        withContext(Main){
                            adapter.updateList(trainingList)
                        }
                    }
                }
            }
            .show()
    }
}