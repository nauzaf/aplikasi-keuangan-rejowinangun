package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("role_id") val role_id: String
)