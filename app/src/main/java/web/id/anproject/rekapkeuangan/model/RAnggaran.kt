package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class RAnggaran(
    @SerializedName("kode") val kode: String,
    @SerializedName("kegiatan") val kegiatan: String,
    @SerializedName("anggaran") val anggaran: Int,
    @SerializedName("tahun") val tahun: String,
    @SerializedName("bulan_realisasi") val bulanRealisasi: String,
    @SerializedName("volume") val volume: Int
)