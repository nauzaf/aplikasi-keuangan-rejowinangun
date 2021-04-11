package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Tahuns(
    @SerializedName("tahuns") val tahuns: ArrayList<String>,
    @SerializedName("ids") val ids: ArrayList<String>
)