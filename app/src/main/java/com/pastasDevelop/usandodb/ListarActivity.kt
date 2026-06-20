package com.pastasDevelop.usandodb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pastasDevelop.usandodb.adapter.ElementoListaAdapter
import com.pastasDevelop.usandodb.databinding.ActivityListarBinding
import com.pastasDevelop.usandodb.entity.Cadastro

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private val dbFirebase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListarBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.main)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        atualizarLista()
    }

    private fun atualizarLista() {
        dbFirebase.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                val registros = mutableListOf<Cadastro>()
                for (document in result) {
                    val cadastro = Cadastro(
                        id = document.id.toIntOrNull() ?: 0,
                        nome = document.getString("nome") ?: "",
                        telefone = document.getString("telefone") ?: ""
                    )
                    registros.add(cadastro)
                }

                val adapter = ElementoListaAdapter(this, registros)
                binding.lvCadastro.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
