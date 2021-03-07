package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class AnggaranList (@SerializedName("anggaran") val anggaran: ArrayList<Anggaran>)