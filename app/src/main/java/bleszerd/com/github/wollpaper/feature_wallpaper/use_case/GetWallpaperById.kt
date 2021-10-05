package bleszerd.com.github.wollpaper.feature_wallpaper.use_case

import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoResponse
import javax.inject.Inject

class GetWallpaperById @Inject constructor(
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(
        id: String,
        onResult: (Resource<Photo>) -> Unit,
    ){
        try {
            val response = repository.getWallpaperById(id)
            val parsedPhoto = parsePhotoResponseToPhoto(response.data!!)
            onResult(
                Resource.Success(parsedPhoto)
            )
        } catch (e: Exception){
            onResult(
                Resource.Error(message = e.message)
            )
        }
    }
    private fun parsePhotoResponseToPhoto(photoResponse: PhotoResponse): Photo {
        photoResponse.apply {
            return Photo(
                id.toString(),
                width,
                height,
                url,
                photographer,
                photographerUrl,
                srcResponse.original
            )
        }
    }
}