package com.pastasDevelop.usandodb

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pastasDevelop.usandodb.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding

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

        val registros = listOf("Brasil", "Argentina", "Chile")

        val adpater = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            registros
        )

        binding.lvCadastro.adapter = adpater
    }
}