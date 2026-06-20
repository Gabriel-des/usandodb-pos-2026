package com.pastasDevelop.usandodb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pastasDevelop.usandodb.database.DatabaseHandler
import com.pastasDevelop.usandodb.databinding.ActivityMainBinding
import com.pastasDevelop.usandodb.entity.Cadastro

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHandler

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
        val id = binding.etCod.text.toString().toIntOrNull()

        if (id == null) {
            val cadastro = Cadastro(
                0,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )

            db.incluir(cadastro)
        } else {
            val cadastro = Cadastro(
                binding.etCod.text.toString().toInt(),
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )

            db.alterar(cadastro)
        }

        Toast.makeText(
            this,
            "Operação realizada com sucesso",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    private fun excluir() {
        db.excluir(binding.etCod.text.toString().toInt())

        Toast.makeText(
            this,
            "Exclusão realizada com sucesso",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    private fun pesquisar() {
        val cadastro = db.pesquisar(binding.etCod.text.toString().toInt())

        if (cadastro != null) {
            binding.etNome.setText(cadastro.nome)
            binding.etTelefone.setText(cadastro.telefone)
        } else {
            Toast.makeText(
                this,
                "Nenhum registro encontrado",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
