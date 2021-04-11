package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Rekap(
    @SerializedName("kode") val kode: String,
    @SerializedName("kegiatan") val kegiatan: String,
    @SerializedName("anggaran") val anggaran: String,
    @SerializedName("pengeluaran") val pengeluaran: String,
    @SerializedName("sisa") val sisa: String
)