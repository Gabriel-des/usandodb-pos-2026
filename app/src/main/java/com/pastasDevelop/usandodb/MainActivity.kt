package com.pastasDevelop.usandodb

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pastasDevelop.usandodb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

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

        db = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath(DB_NAME),
            null
        )

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                " $TABLE_NAME( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT, telefone TEXT )")

        binding.btIncluir.setOnClickListener {
            incluir()
        }
        binding.btAlterar.setOnClickListener {
            alterar()
        }
        binding.btExcluir.setOnClickListener {
            excluir()
        }
        binding.btPesquisar.setOnClickListener {
            pesquisar()
        }
        binding.btListar.setOnClickListener {
            listar()
        }
    }

    private fun incluir() {

        val registro = ContentValues()

        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etTelefone.text.toString())

        db.insert(TABLE_NAME, null, registro)

        Toast.makeText(
            this,
            "Inclusão realizada com sucesso",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun alterar() {
        val registro = ContentValues()

        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etTelefone.text.toString())

        db.update(
            TABLE_NAME,
            registro,
            "_id = ${binding.etCod.text}",
            null
        )

        Toast.makeText(
            this,
            "Alteração realizada com sucesso",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun excluir() {

        db.delete(
            TABLE_NAME,
            "_id = ${binding.etCod.text}",
            null
        )

        Toast.makeText(
            this,
            "Exclusão realizada com sucesso",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun pesquisar() {

        val registro = db.query(
            TABLE_NAME,
            null,
            "_id = ${binding.etCod.text}",
            null,
            null,
            null,
            null
        )

        if (registro.moveToNext()) {
            binding.etNome.setText(registro.getString(NOME))
            binding.etTelefone.setText(registro.getString(TELEFONE))
        } else {
            Toast.makeText(
                this,
                "Nenhum registro encontrado",
                Toast.LENGTH_LONG
            ).show()
        }

        registro.close()

    }

    private fun listar() {

        val registro = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val saida = StringBuilder()

        while (registro.moveToNext()) {
            saida.append(registro.getString(NOME))
            saida.append("\n")
        }

        registro.close()

        Toast.makeText(
            this,
            saida.toString(),
            Toast.LENGTH_LONG
        ).show()

    }

    companion object {
        private const val DB_NAME = "banco.db"
        private const val TABLE_NAME = "cadastro"
        private const val ID = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }
}
