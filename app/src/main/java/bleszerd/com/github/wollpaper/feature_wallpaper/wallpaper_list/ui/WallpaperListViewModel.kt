package bleszerd.com.github.wollpaper.feature_wallpaper.wallpaper_list.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    val wallpaperList = MutableLiveData<MutableList<Photo>>(null)
    private var lastSearchedTerm = "beach"
    private var currentPage = 1
    private var isLoading = false

    init {
        searchPaginatedWallpaperByKeyword()
    }

    fun searchPaginatedWallpaperByKeyword(keyword: String = lastSearchedTerm) {
        if (isLoading)
            return

        viewModelScope.launch {
            //Set loading to true
            isLoading = true

            nestedUseCases.searchWallpapersByKeyword(
                keyword,
                currentPage,
                RESULTS_LIMIT_PER_PAGE
            ) { result ->
                when (result) {
                    is Resource.Success -> {
                        //Update list
                        wallpaperList.postValue(result.data?.photos!!.toMutableList())
                        //Update current page
                        currentPage += 1
                    }
                    is Resource.Error -> {
                        println(result.message)
                    }
                    is Resource.Loading -> {}
                }

                //Set loading to false
                isLoading = false
            }
        }
    }
}