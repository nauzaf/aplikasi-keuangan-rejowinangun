package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Kegiatans(
    @SerializedName("kegiatans") val kegiatans: ArrayList<String>,
    @SerializedName("ids") val ids: ArrayList<String>
)