package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Dashboard (
    @SerializedName("id") val id: String,
    @SerializedName("anggaran_bulan") val anggaranBulan: String,
    @SerializedName("anggaran_bulan_persen") val anggaranBulanPersen: Float,
    @SerializedName("pengeluaran_bulan") val pengeluaranBulan: String,
    @SerializedName("pengeluaran_bulan_persen") val pengeluaranBulanPersen: Float,
    @SerializedName("sisa_anggaran") val sisaAnggaran: Int,
    @SerializedName("sisa_anggaran_persen") val sisaAnggaranPersen: Float,
    @SerializedName("jumlah_tahunan") val jumlahTahunan: Int,
    @SerializedName("jumlah_tahunan_persen") val jumlahTahunanPersen: Float,
    @SerializedName("pengeluaran_tahunan") val pengeluaranTahunan: String,
    @SerializedName("pengeluaran_tahunan_persen") val pengeluaranTahunanPersen: Float,
    @SerializedName("sisa_tahunan") val sisaTahunan: Int,
    @SerializedName("sisa_tahunan_persen") val sisaTahunanPersen: Float
)