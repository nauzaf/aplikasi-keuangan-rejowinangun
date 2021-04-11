package web.id.anproject.rekapkeuangan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_template.view.*
import web.id.anproject.rekapkeuangan.R
import web.id.anproject.rekapkeuangan.model.RRekap

class RekapAdapter(val rrekap: RRekap, val context: Context): RecyclerView.Adapter<RekapViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RekapViewHolder {
        return RekapViewHolder(LayoutInflater.from(context).inflate(R.layout.item_template, parent, false))
    }

    override fun getItemCount(): Int {
        return rrekap.rekap.size
    }

    override fun onBindViewHolder(holder: RekapViewHolder, position: Int) {
        val items = rrekap.rekap
        holder.root.radius = 20.0f
        holder.root.elevation = 20.0f

        if (items.get(position).kegiatan.length < 20) {
            holder.tvTitle.text = items.get(position).kegiatan.toUpperCase()
        } else {
            holder.tvTitle.text = items.get(position).kegiatan.substring(0, 20).toUpperCase() + " ..."
        }

        holder.tvSubTitle.text = items.get(position).kode
        holder.tvNote.text = "Anggaran : Rp." + "%,d".format(items.get(position).anggaran.toInt())
        holder.tvNote2.text = "Sisa : Rp." + "%,d".format(items.get(position).sisa.toInt())
        holder.tvPrice.text = "Rp." + "%,d".format(items.get(position).pengeluaran.toInt())
    }
}

class RekapViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvTitle = view.title
    val tvSubTitle = view.subtitle
    val tvNote = view.note
    val tvNote2 = view.note2
    val tvPrice = view.price
    val root = view.itemTemplate
}