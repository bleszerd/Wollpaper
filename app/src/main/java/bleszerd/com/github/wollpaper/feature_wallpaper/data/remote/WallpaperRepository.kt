package bleszerd.com.github.wollpaper.feature_wallpaper.data.remote

import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoListResponse
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoResponse

interface WallpaperRepository {
    suspend fun searchPaginatedWallpapersByKeyword(
        keyword: String,
        page: Int,
        limit: Int,
    ): Resource<PhotoListResponse>

    suspend fun getWallpaperById(
        id: String,
    ): Resource<PhotoResponse>
}