package web.id.anproject.rekapkeuangan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_template.view.*
import web.id.anproject.rekapkeuangan.R
import web.id.anproject.rekapkeuangan.model.TransaksiList

class TransaksiAdapter(val transaksiList: TransaksiList, val context: Context):
    RecyclerView.Adapter<TransaksiViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        return TransaksiViewHolder(LayoutInflater.from(context).inflate(R.layout.item_template, parent, false))
    }

    override fun getItemCount(): Int {
        return transaksiList.transaksi.size
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val items = transaksiList.transaksi
        holder.root.radius = 20.0f
        holder.root.elevation = 20.0f
        holder.tvTitle.text = items.get(position).kegiatan.toUpperCase()
        holder.tvSubTitle.text = items.get(position).kode
        holder.tvNote.text = "Tanggal : " + items.get(position).createdAt
        holder.tvPrice.text = "Rp." + "%,d".format(items.get(position).pengeluaran.toInt())
    }
}

class TransaksiViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvTitle = view.title
    val tvSubTitle = view.subtitle
    val tvNote = view.note
    val tvPrice = view.price
    val root = view.itemTemplate
}