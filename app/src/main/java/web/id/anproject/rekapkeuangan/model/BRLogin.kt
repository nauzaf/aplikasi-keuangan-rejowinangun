package web.id.anproject.rekapkeuangan.model

import com.google.gson.annotations.SerializedName

data class BRLogin(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)