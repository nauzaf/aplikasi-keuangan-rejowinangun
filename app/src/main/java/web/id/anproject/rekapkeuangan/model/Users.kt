package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Users (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("images") val images: String,
    @SerializedName("role_id") val role_id: Int,
    @SerializedName("is_active") val is_active: Int,
    @SerializedName("create_at") val create_at: String?
)