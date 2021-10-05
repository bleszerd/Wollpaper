package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_details.ui

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

    fun getWallpaperById(photoId: String) {
        viewModelScope.launch {
            nestedWallpaperUseCases.getWallpaperById(photoId) { result ->
                when(result){
                    is Resource.Success -> {
                        _photoData.postValue(result.data!!)
                    }

                    is Resource.Error -> {
                        println(result.message)
                    }

                    is Resource.Loading -> { }
                }
            }
        }
    }

}