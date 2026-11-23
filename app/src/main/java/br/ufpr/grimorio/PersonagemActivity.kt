package br.ufpr.grimorio

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonagemActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvResultado: TextView
    private lateinit var ivPersonagem: ImageView
    private lateinit var hogwartsApi: HogwartsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personagem)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etNome = findViewById(R.id.etNome)
        progressBar = findViewById(R.id.progressBar)
        tvResultado = findViewById(R.id.tvResultado)
        ivPersonagem = findViewById(R.id.ivPersonagem)

        val retrofit = Retrofit.Builder()
            .baseUrl(HogwartsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        hogwartsApi = retrofit.create(HogwartsApi::class.java)
    }

    fun buscarPersonagem(view: View) {
        val nome = validarNome(etNome) ?: return
        if (progressBar.visibility == View.VISIBLE) return
        buscarPersonagemNaApi(nome)
    }

    private fun buscarPersonagemNaApi(nome: String) {
        lifecycleScope.launch {
            try {
                ivPersonagem.visibility = View.GONE
                mostrarCarregando()
                val resposta = withContext(Dispatchers.IO) { hogwartsApi.buscarPersonagens(nome) }
                val personagem = resposta.personagens.firstOrNull()
                if (personagem == null) {
                    tvResultado.text = getString(R.string.nenhum_personagem)
                    esconderCarregando()
                } else {
                    exibirPersonagem(personagem)
                }
            } catch (e: Exception) {
                Log.e("PersonagemActivity", "Erro ao buscar personagem", e)
                tvResultado.text = getString(R.string.erro_conexao)
                esconderCarregando()
            }
        }
    }

    private fun exibirPersonagem(personagem: Personagem) {
        val naoInformado = getString(R.string.nao_informado)
        val nome = getString(R.string.nome_personagem, personagem.nome)
        val especie = getString(R.string.especie_personagem, personagem.especie ?: naoInformado)
        val casa = getString(R.string.casa_personagem, personagem.casa ?: naoInformado)
        tvResultado.text = "$nome\n$especie\n$casa"
        ivPersonagem.visibility = View.VISIBLE

        if (personagem.imagem.isNullOrBlank()) {
            ivPersonagem.setImageResource(R.drawable.imagem_indisponivel)
            esconderCarregando()
            return
        }

        Picasso.get()
            .load(personagem.imagem)
            .error(R.drawable.imagem_indisponivel)
            .into(ivPersonagem, object : Callback {
                override fun onSuccess() {
                    esconderCarregando()
                }

                override fun onError(e: Exception?) {
                    esconderCarregando()
                }
            })
    }

    private fun mostrarCarregando() {
        progressBar.visibility = View.VISIBLE
        tvResultado.visibility = View.GONE
    }

    private fun esconderCarregando() {
        progressBar.visibility = View.GONE
        tvResultado.visibility = View.VISIBLE
    }
}
