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

class CriaturaActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var ivCriatura: ImageView
    private lateinit var tvNomeCriatura: TextView
    private lateinit var tvMensagem: TextView
    private lateinit var hogwartsApi: HogwartsApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_criatura)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etNome = findViewById(R.id.etNome)
        progressBar = findViewById(R.id.progressBar)
        ivCriatura = findViewById(R.id.ivCriatura)
        tvNomeCriatura = findViewById(R.id.tvNomeCriatura)
        tvMensagem = findViewById(R.id.tvMensagem)

        val retrofit = Retrofit.Builder()
            .baseUrl(HogwartsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        hogwartsApi = retrofit.create(HogwartsApi::class.java)
    }

    fun buscarCriatura(view: View) {
        val nome = validarNome(etNome) ?: return
        buscarCriaturaNaApi(nome)
    }

    private fun buscarCriaturaNaApi(nome: String) {
        lifecycleScope.launch {
            try {
                limparResultado()
                mostrarCarregando()
                val resposta = withContext(Dispatchers.IO) { hogwartsApi.buscarCriaturas(nome) }
                val criatura = escolherCriatura(resposta.criaturas, nome)
                if (criatura == null) {
                    tvMensagem.text = getString(R.string.nenhuma_criatura)
                    esconderCarregando()
                } else {
                    exibirCriatura(criatura)
                }
            } catch (e: Exception) {
                Log.e("CriaturaActivity", "Erro ao buscar criatura", e)
                tvMensagem.text = getString(R.string.erro_conexao)
                esconderCarregando()
            }
        }
    }

    private fun escolherCriatura(criaturas: List<Criatura>, nome: String): Criatura? {
        return criaturas.firstOrNull { it.nome.equals(nome, ignoreCase = true) } ?: criaturas.firstOrNull()
    }

    private fun exibirCriatura(criatura: Criatura) {
        tvNomeCriatura.text = criatura.nome
        ivCriatura.visibility = View.VISIBLE

        if (criatura.imagem.isNullOrBlank()) {
            mostrarImagemIndisponivel()
            esconderCarregando()
            return
        }

        Picasso.get()
            .load(criatura.imagem)
            .error(R.drawable.imagem_indisponivel)
            .into(ivCriatura, object : Callback {
                override fun onSuccess() {
                    esconderCarregando()
                }

                override fun onError(e: Exception?) {
                    tvMensagem.text = getString(R.string.imagem_indisponivel)
                    esconderCarregando()
                }
            })
    }

    private fun mostrarImagemIndisponivel() {
        ivCriatura.setImageResource(R.drawable.imagem_indisponivel)
        tvMensagem.text = getString(R.string.imagem_indisponivel)
    }

    private fun limparResultado() {
        ivCriatura.visibility = View.GONE
        tvNomeCriatura.text = ""
        tvMensagem.text = ""
    }

    private fun mostrarCarregando() {
        progressBar.visibility = View.VISIBLE
    }

    private fun esconderCarregando() {
        progressBar.visibility = View.GONE
    }
}
