package com.pastasDevelop.usandodb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
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
        val imageView = v.findViewById<ImageView>(R.id.imageView)

        if (position % 2 == 0) {
            imageView.setImageResource(android.R.drawable.star_on)
        } else {
            imageView.setImageResource(R.drawable.avatar)
        }

        tvNomeElementoLista.text = registros[position].nome
        tvTelefoneElementoLista.text = registros[position].telefone

        return v
    }
}