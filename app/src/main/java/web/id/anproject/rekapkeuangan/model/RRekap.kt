package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class RRekap(
    @SerializedName("year") val year: String,
    @SerializedName("month") val month: String,
    @SerializedName("monthTemp") val monthTemp: String,
    @SerializedName("anggaran_bulan") val anggaranBulan: Int,
    @SerializedName("pengeluaran_bulan") val pengeluaranBulan: Int,
    @SerializedName("sisa_anggaran") val sisaAnggaran: Int,
    @SerializedName("rekap") val rekap: ArrayList<Rekap>
)