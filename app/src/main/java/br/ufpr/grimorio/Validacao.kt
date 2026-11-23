package br.ufpr.grimorio

import android.widget.EditText

private const val MINIMO_CARACTERES = 3

fun validarNome(campo: EditText): String? {
    val nome = campo.text.toString().trim()

    if (nome.isEmpty()) {
        campo.error = campo.context.getString(R.string.erro_campo_vazio)
        return null
    }
    if (nome.length < MINIMO_CARACTERES) {
        campo.error = campo.context.getString(R.string.erro_minimo_caracteres)
        return null
    }
    return nome
}
