package com.pastasDevelop.usandodb

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pastasDevelop.usandodb.database.DatabaseHandler
import com.pastasDevelop.usandodb.databinding.ActivityMainBinding
import com.pastasDevelop.usandodb.entity.Cadastro
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHandler
    private val dbFirebase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHandler(this)

        if (intent.getIntExtra("id", 0) > 0) {
            binding.etCod.setText(intent.getIntExtra("id", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE
        }

        binding.btSalvar.setOnClickListener {
            alterar()
        }
        binding.btExcluir.setOnClickListener {
            excluir()
        }
        binding.btPesquisar.setOnClickListener {
            pesquisar()
        }
    }

    private fun alterar() {
        val id = binding.etCod.text.toString()
        if (id.isEmpty()) {
            Toast.makeText(this, "Informe o código", Toast.LENGTH_SHORT).show()
            return
        }

        val cadastro = hashMapOf(
            "nome" to binding.etNome.text.toString(),
            "telefone" to binding.etTelefone.text.toString()
        )

        // Salvando no Firebase (Firestore)
        dbFirebase.collection("cadastro")
            .document(id)
            .set(cadastro)
            .addOnSuccessListener {
                Toast.makeText(this, "Salvo no Firestore!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun excluir() {
        val id = binding.etCod.text.toString().toIntOrNull()

        if (id == null) {
            binding.etCod.error = "Informe um código válido"
            return
        }

        lifecycleScope.launch {
            db.excluir(binding.etCod.text.toString().toInt())

            finish()
        }
    }

    private fun pesquisar() {
        val etCodPesquisa = EditText( this )

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Informe o código da pessoa")
        dialog.setCancelable(false)
        dialog.setNegativeButton( "Fechar", null )
        dialog.setPositiveButton( "Pesquisar", { _, _ ->
            val id = etCodPesquisa.text.toString().toIntOrNull()

            if (id == null) {
                Toast.makeText(this, "Código deve ser informado...", Toast.LENGTH_LONG ).show()
            } else {
                lifecycleScope.launch {
                    val cadastro = db.pesquisar(id)

                    if (cadastro != null) {
                        binding.etCod.setText(etCodPesquisa.text.toString())
                        binding.etNome.setText(cadastro.nome)
                        binding.etTelefone.setText(cadastro.telefone)
                    } else {
                        Toast.makeText(this@MainActivity, "Pessoa não encontrada...", Toast.LENGTH_LONG ).show()
                    }
                }
            }

        })

        dialog.setView(etCodPesquisa)
        dialog.show()

    }
}
