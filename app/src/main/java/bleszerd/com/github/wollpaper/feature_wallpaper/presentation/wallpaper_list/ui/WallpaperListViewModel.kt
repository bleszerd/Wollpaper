package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_list.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bleszerd.com.github.wollpaper.core.Constants.RESULTS_LIMIT_PER_PAGE
import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.SearchWallpaperByKeywordListener
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util.NestedWallpaperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperListViewModel @Inject constructor(
    private val nestedWallpaperUseCases: NestedWallpaperUseCases
) : ViewModel() {

    val wallpaperList = MutableLiveData<MutableList<Photo>>(null)
    val hasMoreWallpapers = MutableLiveData<Boolean>(true)

    //Callbacks
    private val endIsReachedCallback = {
        hasMoreWallpapers.postValue(false)
    }

    //Listeners
    private val searchListener = object : SearchWallpaperByKeywordListener {
        override fun onNewKeywordSearch(newKeyword: String) {
            wallpaperList.postValue(mutableListOf())
        }
    }

    init {
        searchPaginatedWallpaperByKeyword()
    }

    fun searchPaginatedWallpaperByKeyword(keyword: String? = null) {
        viewModelScope.launch {
            nestedWallpaperUseCases.searchWallpapersByKeyword(
                keyword,
                RESULTS_LIMIT_PER_PAGE,
                onEndReached = endIsReachedCallback,
                listener = searchListener,
            ) { result ->
                when (result) {
                    is Resource.Success -> {
                        wallpaperList.postValue(result.data?.photos!!.toMutableList())
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
}