package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Transaksi (
    @SerializedName("id") val id: String,
    @SerializedName("kode") val kode: String,
    @SerializedName("kegiatan") val kegiatan: String,
    @SerializedName("pengeluaran") val pengeluaran: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("update_at") val updateAt: String
)