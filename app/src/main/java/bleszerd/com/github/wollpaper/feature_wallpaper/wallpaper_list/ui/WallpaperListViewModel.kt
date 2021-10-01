package bleszerd.com.github.wollpaper.feature_wallpaper.wallpaper_list.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bleszerd.com.github.wollpaper.core.Constants
import bleszerd.com.github.wollpaper.core.Constants.RESULTS_LIMIT_PER_PAGE
import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util.NestedUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperListViewModel @Inject constructor(
    private val nestedUseCases: NestedUseCases
): ViewModel() {

    val wallpaperList = MutableLiveData<MutableList<Photo>>(null)
    private var lastSearchedTerm = "beach"
    private var currentPage = 1

    init {
        searchWallpaperByKeyword()
    }

    fun searchWallpaperByKeyword(keyword: String = lastSearchedTerm){
        viewModelScope.launch {
            val photoListResponse = nestedUseCases.searchWallpapersByKeyword(
                keyword,
                currentPage,
                RESULTS_LIMIT_PER_PAGE
            )

            when(photoListResponse){
                is Resource.Success -> {
                    wallpaperList.postValue(photoListResponse.data?.photos!!.toMutableList())
                }
                is Resource.Error -> {
                    println(photoListResponse.message)
                }
            }
        }
    }
}