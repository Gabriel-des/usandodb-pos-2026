package com.pastasDevelop.usandodb

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pastasDevelop.usandodb.adapter.ElementoListaAdapter
import com.pastasDevelop.usandodb.database.DatabaseHandler
import com.pastasDevelop.usandodb.databinding.ActivityListarBinding
import com.pastasDevelop.usandodb.entity.Cadastro

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var db: DatabaseHandler

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

        db = DatabaseHandler(this)

        val registros = db.listar()

        val adapter = ElementoListaAdapter(
            this,
            registros
        )

        binding.lvCadastro.adapter = adapter
    }
}