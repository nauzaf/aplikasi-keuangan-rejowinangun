package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class TransaksiList (@SerializedName("transaksi") val transaksi: ArrayList<Transaksi>)