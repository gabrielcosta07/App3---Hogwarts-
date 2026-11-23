package br.ufpr.grimorio

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonagemActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvResultado: TextView
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
                mostrarCarregando()
                val resposta = withContext(Dispatchers.IO) { hogwartsApi.buscarPersonagens(nome) }
                exibirPersonagem(resposta.personagens.firstOrNull())
            } catch (e: Exception) {
                Log.e("PersonagemActivity", "Erro ao buscar personagem", e)
                tvResultado.text = getString(R.string.erro_conexao)
            } finally {
                esconderCarregando()
            }
        }
    }

    private fun exibirPersonagem(personagem: Personagem?) {
        if (personagem == null) {
            tvResultado.text = getString(R.string.nenhum_personagem)
            return
        }

        val naoInformado = getString(R.string.nao_informado)
        val nome = getString(R.string.nome_personagem, personagem.nome)
        val especie = getString(R.string.especie_personagem, personagem.especie ?: naoInformado)
        val casa = getString(R.string.casa_personagem, personagem.casa ?: naoInformado)
        tvResultado.text = "$nome\n$especie\n$casa"
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
