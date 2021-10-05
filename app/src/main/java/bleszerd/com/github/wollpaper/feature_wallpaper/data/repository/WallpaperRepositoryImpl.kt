package bleszerd.com.github.wollpaper.feature_wallpaper.data.repository

import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperAPI
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoListResponse
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoResponse
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val api: WallpaperAPI
) : WallpaperRepository {
    override suspend fun searchPaginatedWallpapersByKeyword(
        keyword: String,
        page: Int,
        limit: Int
    ): Resource<PhotoListResponse> {
        val response = try {
            api.searchWallpapersByKeyword(keyword, page, limit)
        } catch (e: Exception) {
            return Resource.Error(message = e.message)
        }

        return Resource.Success(response)
    }

    override suspend fun getWallpaperById(id: String): Resource<PhotoResponse> {
        val response = try {
            api.getWallpaperById(id)
        } catch (e: Exception) {
            return Resource.Error(message = e.message)
        }

        return Resource.Success(response)
    }
}