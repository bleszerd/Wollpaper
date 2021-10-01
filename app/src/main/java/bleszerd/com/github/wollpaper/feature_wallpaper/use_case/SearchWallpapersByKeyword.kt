package bleszerd.com.github.wollpaper.feature_wallpaper.use_case

import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.PhotoList
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperAPI
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoListResponse
import javax.inject.Inject

class SearchWallpapersByKeyword @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(
        keyword: String,
        page: Int,
        limit: Int
    ): Resource<PhotoList> {
        return try {
            val response = repository.searchPaginatedWallpapersByKeyword(
                keyword,
                page,
                limit
            )
            val totalResults = response.data?.totalResults!!
            val parsedPhotos = parsePhotoListResponseToPhotoListModel(response.data)

            Resource.Success(
                PhotoList(parsedPhotos, totalResults)
            )

        } catch (e: Exception) {
            Resource.Error(message = "Unknown error occurred.")
        }

    }

    private fun parsePhotoListResponseToPhotoListModel(data: PhotoListResponse): List<Photo>{
        val parsedData = mutableListOf<Photo>()

        for (photoResp in data.photoResponses){
            photoResp.apply {
                parsedData.add(
                    Photo(
                        id = id.toString(),
                        width = width,
                        height = height,
                        pexelsUrl = url,
                        photographer = photographer,
                        photographerUrl = photographerUrl,
                        imageUrl = srcResponse.medium,
                    )
                )
            }
        }

        return parsedData
    }
}