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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pastasDevelop.usandodb.database.DatabaseHandler
import com.pastasDevelop.usandodb.databinding.ActivityMainBinding
import com.pastasDevelop.usandodb.entity.Cadastro

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

        val cadastro = hashMapOf(
            "nome" to binding.etNome.text.toString(),
            "telefone" to binding.etTelefone.text.toString()
        )

        dbFirebase
            .collection("cadastro")
            .document(binding.etCod.text.toString())
            .set( cadastro )
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Operação realizada com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

//        val id = binding.etCod.text.toString().toIntOrNull()
//
//        if (id == null) {
//            val cadastro = Cadastro(
//                0,
//                binding.etNome.text.toString(),
//                binding.etTelefone.text.toString()
//            )
//
//            db.incluir(cadastro)
//        } else {
//            val cadastro = Cadastro(
//                binding.etCod.text.toString().toInt(),
//                binding.etNome.text.toString(),
//                binding.etTelefone.text.toString()
//            )
//
//            db.alterar(cadastro)
//        }

        finish()
    }

    private fun excluir() {
        dbFirebase
            .collection("cadastro")
            .document(binding.etCod.text.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Operação realizada com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Erro ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
//        db.excluir(binding.etCod.text.toString().toInt())
//
//        Toast.makeText(
//            this,
//            "Exclusão realizada com sucesso",
//            Toast.LENGTH_LONG
//        ).show()
//
//        finish()
    }

    private fun pesquisar() {

        val etCodPesquisa = EditText(this)

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Informe o código da pessoa")
        dialog.setView(etCodPesquisa)
        dialog.setCancelable(false)
        dialog.setNegativeButton("Fechar", null)
        dialog.setPositiveButton("Pesquisar", {
                _, _ ->
            val id = etCodPesquisa.text.toString().toIntOrNull()

            if (id == null) {
                Toast.makeText(
                    this,
                    "Informe um código válido",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                dbFirebase
                    .collection("cadastro")
                    .document(id.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        binding.etCod.setText(result.id)
                        binding.etNome.setText(result.data?.get("nome").toString())
                        binding.etTelefone.setText(result.data?.get("telefone").toString())
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Erro ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

//            if (id == null) {
//                Toast.makeText(
//                    this,
//                    "Informe um código válido",
//                    Toast.LENGTH_LONG
//                ).show()
//            } else {
//                val cadastro = db.pesquisar(id)
//
//                if (cadastro != null) {
//                    binding.etCod.setText(cadastro.id.toString())
//                    binding.etNome.setText(cadastro.nome)
//                    binding.etTelefone.setText(cadastro.telefone)
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Nenhum registro encontrado",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
        }
        )
        dialog.show()
    }
}
