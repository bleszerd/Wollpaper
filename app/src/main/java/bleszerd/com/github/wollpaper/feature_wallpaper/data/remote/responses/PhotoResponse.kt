package bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("avg_color")
    val avgColor: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("liked")
    val liked: Boolean,
    @SerializedName("photographer")
    val photographer: String,
    @SerializedName("photographer_id")
    val photographerId: Int,
    @SerializedName("photographer_url")
    val photographerUrl: String,
    @SerializedName("src")
    val srcResponse: SrcResponse,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)