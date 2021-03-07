package web.id.anproject.rekapkeuangan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_template.view.*
import web.id.anproject.rekapkeuangan.R
import web.id.anproject.rekapkeuangan.model.AnggaranList

class AnggaranAdapter(val anggaranList: AnggaranList, val context: Context):
    RecyclerView.Adapter<AnggaranViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggaranViewHolder {
        return AnggaranViewHolder(LayoutInflater.from(context).inflate(R.layout.item_template, parent, false))
    }

    override fun getItemCount(): Int {
        return anggaranList.anggaran.size
    }

    override fun onBindViewHolder(holder: AnggaranViewHolder, position: Int) {
        val items = anggaranList.anggaran
        val bulanRealisasi = items.get(position).bulanRealisasi
            .replace("\"", "")
            .replace("[", "")
            .replace("]", "")
            .replace(",", ", ")
        holder.root.radius = 20.0f
        holder.root.elevation = 20.0f
        holder.tvTitle.text = items.get(position).kegiatan.toUpperCase()
        holder.tvSubTitle.text = items.get(position).kode
        holder.tvNote.text = bulanRealisasi
        holder.tvPrice.text = "Rp."+"%,d".format(items.get(position).anggaran.toInt())
    }
}

class AnggaranViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvTitle = view.title
    val tvSubTitle = view.subtitle
    val tvNote = view.note
    val tvPrice = view.price
    val root = view.itemTemplate
}