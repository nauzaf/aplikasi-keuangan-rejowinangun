package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Anggaran (
    @SerializedName("id") val id: String,
    @SerializedName("kode") val kode: String,
    @SerializedName("kegiatan") val kegiatan: String,
    @SerializedName("anggaran") val anggaran: String,
    @SerializedName("tahun") val tahun: Int,
    @SerializedName("volume") val volume: String,
    @SerializedName("bulan_realisasi") val bulanRealisasi: String,
    @SerializedName("created_at") val createdAt: String
)