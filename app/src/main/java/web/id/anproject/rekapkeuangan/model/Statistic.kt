package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Statistic (
    @SerializedName("anggaran_bulan") val anggaranBulan: Int,
    @SerializedName("pengeluaran_bulan") val pengeluaranBulan: Int,
    @SerializedName("sisa_anggaran") val sisaAnggaran: Int,
    @SerializedName("anggaran_tahunan") val anggaranTahunan: ArrayList<Int>,
    @SerializedName("pengeluaran_tahunan") val pengeluaranTahunan: ArrayList<Int>,
    @SerializedName("sisa_tahunan") val sisaTahunan: ArrayList<Int>
)