package com.pastasDevelop.usandodb.database

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pastasDevelop.usandodb.entity.Cadastro
import kotlinx.coroutines.tasks.await

class DatabaseHandler(context: Context) {

    private val db = Firebase.firestore

    suspend fun incluir(cadastro: Cadastro) {

        val registro = hashMapOf(
            NOME to cadastro.nome,
            TELEFONE to cadastro.telefone
        )

        db
            .collection(TABLE_NAME)
            .document(cadastro.id.toString())
            .set( registro )
            .await()
    }

    suspend fun alterar(cadastro: Cadastro) {
        val registro = hashMapOf(
            NOME to cadastro.nome,
            TELEFONE to cadastro.telefone
        )

        db
            .collection(TABLE_NAME)
            .document(cadastro.id.toString())
            .set( registro )
            .await()
    }

    suspend fun excluir(id: Int) {
        db
            .collection(TABLE_NAME)
            .document(id.toString())
            .delete()
            .await()
    }

    suspend fun pesquisar(id: Int): Cadastro? {
        val documento = db
            .collection(TABLE_NAME)
            .document(id.toString())
            .get()
            .await()

        if (documento.exists()) {
            val cadastro = Cadastro(
                documento.id.toInt(),
                documento.data?.get(NOME).toString(),
                documento.data?.get(TELEFONE).toString()
            )
            return cadastro
        } else {
            return null
        }
    }

    suspend fun listar(): MutableList<Cadastro> {
        val documento = db
            .collection(TABLE_NAME)
            .get()
            .await()

        val lista = mutableListOf<Cadastro>()

        for (documento in documento) {
            val cadastro = Cadastro(
                documento.id.toInt(),
                documento.data[NOME].toString(),
                documento.data[TELEFONE].toString()
            )
            lista.add(cadastro)
        }

        return lista
    }

    companion object {
        private const val TABLE_NAME = "cadastro"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val TELEFONE = "telefone"
    }
}