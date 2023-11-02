package souza.edijanio.treinoacademia.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import coil.load
import souza.edijanio.treinoacademia.R
import souza.edijanio.treinoacademia.databinding.DetailExerciseScreenBinding
import souza.edijanio.treinoacademia.helper.imageLoader
import souza.edijanio.treinoacademia.model.Exercise

class DetailExerciseScreen : AppCompatActivity() {

    private val binding by lazy{
        DetailExerciseScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val exercise = intent.getSerializableExtra("EXERCISE") as Exercise

        binding.exerciseScreenImage.load(R.drawable.musculos_exigidos_nos_exercicios_de_musculacao, imageLoader(this))
        binding.exerciseScreenSeriesReps.text = "${exercise.series} X ${exercise.repetitions}"

        binding.exerciseToolbar.setNavigationOnClickListener {
            finish()
        }

    }
}