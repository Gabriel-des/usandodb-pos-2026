package com.pastasDevelop.usandodb.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pastasDevelop.usandodb.entity.Cadastro

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS " +
                " $TABLE_NAME( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT, telefone TEXT )")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun incluir(cadastro: Cadastro) {

        val db = this.writableDatabase
        val registro = ContentValues()

        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)

        db.insert(TABLE_NAME, null, registro)

    }

    fun alterar(cadastro: Cadastro) {
        val db = this.writableDatabase
        val registro = ContentValues()

        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)

        db.update(
            TABLE_NAME,
            registro,
            "_id = ${cadastro.id}",
            null
        )
    }

    fun excluir(id: Int) {

        val db = this.writableDatabase

        db.delete(
            TABLE_NAME,
            "_id = $id",
            null
        )

    }

    fun pesquisar(id: Int): Cadastro? {

        val db = this.writableDatabase

        val registro = db.query(
            TABLE_NAME,
            null,
            "_id = $id",
            null,
            null,
            null,
            null
        )

        if (registro.moveToNext()) {
            val saida = Cadastro(
                registro.getInt(ID),
                registro.getString(NOME),
                registro.getString(TELEFONE)
            )
            registro.close()
            return saida
        }

        registro.close()
        return null

    }

    fun listar(): MutableList<Cadastro> {

        val db = this.writableDatabase

        val registro = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val saida = mutableListOf<Cadastro>()

        while (registro.moveToNext()) {
            val cadastro = Cadastro(
                registro.getInt(ID),
                registro.getString(NOME),
                registro.getString(TELEFONE)
            )

            saida.add(cadastro)
        }

        registro.close()
        return saida

    }

    companion object {
        private const val DATABASE_NAME = "banco.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cadastro"
        private const val ID = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }
}