package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Tahun (
    @SerializedName("id") val id: Int,
    @SerializedName("tahun") val tahun: String,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("created_at") val createdAt: Int
)