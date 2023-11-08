package souza.edijanio.treinoacademia.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import souza.edijanio.treinoacademia.adapter.ExerciseListAdapter
import souza.edijanio.treinoacademia.databinding.ExerciseListScreenBinding
import souza.edijanio.treinoacademia.model.Training

class ExerciseListScreen : AppCompatActivity() {

    private val binding by lazy {
        ExerciseListScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val extras : Training = intent.getSerializableExtra("TRAINING") as Training

        val detailScreenRv = binding.detailScreenRv
//        detailScreenRv.adapter = ExerciseListAdapter(this, extras.exercise)

        binding.exerciseListToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}