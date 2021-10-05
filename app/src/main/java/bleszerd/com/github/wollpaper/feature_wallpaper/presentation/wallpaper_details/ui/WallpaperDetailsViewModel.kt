package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_details.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util.NestedWallpaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperDetailsViewModel @Inject constructor(
    private val nestedWallpaperUseCases: NestedWallpaperUseCases
) : ViewModel() {

    private val _photoData = MutableLiveData<Photo>(null)
    val photoData: LiveData<Photo>
        get() = _photoData

    private var _photoResource: Bitmap? = null
    var photoResource: Bitmap? = null
        private set
        get() = _photoResource

    fun getWallpaperById(photoId: String) {
        viewModelScope.launch {
            nestedWallpaperUseCases.getWallpaperById(photoId) { result ->
                when (result) {
                    is Resource.Success -> {
                        _photoData.postValue(result.data!!)
                    }

                    is Resource.Error -> {
                        println(result.message)
                    }

                    is Resource.Loading -> {
                    }
                }
            }
        }
    }

    fun updatePhotoResource(drawable: Drawable) {
        if (drawable is BitmapDrawable) {
            _photoResource = (drawable as BitmapDrawable).bitmap
        }
    }

}