package bleszerd.com.github.wollpaper.feature_wallpaper.data.remote

import bleszerd.com.github.wollpaper.core.Constants.API_KEY
import bleszerd.com.github.wollpaper.core.Constants.RESULTS_LIMIT_PER_PAGE
import bleszerd.com.github.wollpaper.core.Constants.SEARCH_QUERY_ORIENTATION
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoListResponse
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperAPI {

    @GET("search") //?query=forest
    suspend fun searchWallpapersByKeyword(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int = RESULTS_LIMIT_PER_PAGE,
        @Query("orientation") orientation: String = SEARCH_QUERY_ORIENTATION,
        @Header("Authorization") authToken: String = API_KEY,
    ): PhotoListResponse

    @GET("photos/{id}")
    suspend fun getWallpaperById(
        @Path("id") id: String,
        @Header("Authorization") authToken: String = API_KEY,
    ): PhotoResponse
}