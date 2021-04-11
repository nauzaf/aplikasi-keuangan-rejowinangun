package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

class BRTransaksi (
    @SerializedName("kode") val kode: String,
    @SerializedName("kegiatan") val kegiatan: String,
    @SerializedName("pengeluaran") val pengeluaran: String,
    @SerializedName("tahun") val tahun: String,
    @SerializedName("tanggal") val tanggal: String
)