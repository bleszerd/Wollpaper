package bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses


import com.google.gson.annotations.SerializedName

data class PhotoListResponse(
    @SerializedName("next_page")
    val nextPage: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("photos")
    val photoResponses: List<PhotoResponse>,
    @SerializedName("total_results")
    val totalResults: Int
)