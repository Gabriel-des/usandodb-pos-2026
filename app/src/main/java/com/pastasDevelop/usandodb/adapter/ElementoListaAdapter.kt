package com.pastasDevelop.usandodb.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.pastasDevelop.usandodb.MainActivity
import com.pastasDevelop.usandodb.R
import com.pastasDevelop.usandodb.entity.Cadastro

class ElementoListaAdapter(val context: Context, val registros: MutableList<Cadastro>): BaseAdapter() {
    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(position: Int): Any {
        val cadastro = Cadastro(
            registros[position].id,
            registros[position].nome,
            registros[position].telefone
        )

        return cadastro
    }

    override fun getItemId(position: Int): Long {
        return registros[position].id.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val ibProfile = v.findViewById<ImageButton>(R.id.ibProfile)
        val btEditarElementoLista = v.findViewById<ImageView>(R.id.btEditarElementoLista)

        if (position % 2 == 0) {
            ibProfile.setImageResource(android.R.drawable.star_on)
        } else {
            ibProfile.setImageResource(R.drawable.avatar)
        }

        tvNomeElementoLista.text = registros[position].nome
        tvTelefoneElementoLista.text = registros[position].telefone

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("id", registros[position].id)
            intent.putExtra("nome", registros[position].nome)
            intent.putExtra("telefone", registros[position].telefone)
            context.startActivity(intent)
        }

        return v
    }
}