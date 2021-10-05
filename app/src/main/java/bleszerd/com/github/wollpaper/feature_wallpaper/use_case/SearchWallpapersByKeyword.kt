package bleszerd.com.github.wollpaper.feature_wallpaper.use_case

import bleszerd.com.github.wollpaper.core.Constants.DEFAULT_SEARCH_KEYWORD
import bleszerd.com.github.wollpaper.core.Resource
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.PhotoList
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.responses.PhotoListResponse
import javax.inject.Inject

class SearchWallpapersByKeyword @Inject constructor(
    private val repository: WallpaperRepository
) {
    private var lastSearchedKeyword = DEFAULT_SEARCH_KEYWORD
    private var currentPage = 0
    private var totalResults = 0
    private var isLoading = false
    private var endReached = false

    suspend operator fun invoke(
        keyword: String? = DEFAULT_SEARCH_KEYWORD,
        limit: Int,
        page: Int = -1,
        listener: SearchWallpaperByKeywordListener? = null,
        onEndReached: () -> Unit = {},
        onResult: (Resource<PhotoList>) -> Unit = {}
    ) {
        //Already loading
        if (isLoading)
            return

        //Update last search keyword
        val searchKeyword = keyword?: lastSearchedKeyword

        //If search keyword is the same as previous update page count
        if (lastSearchedKeyword == searchKeyword){

            //Check if end of list is reached
            if (endReached){
                onEndReached()
                return
            }

            currentPage += 1
        }
        else {
            //Keyword is different, reset page count and update lastSearchKeyword
            currentPage = 1
            lastSearchedKeyword = searchKeyword

            //Trigger new keyword search callback
            listener?.onNewKeywordSearch(searchKeyword)
        }

        try {
            val response = repository.searchPaginatedWallpapersByKeyword(
                searchKeyword,
                currentPage,
                limit
            )

            //Update total results
            totalResults = response.data?.totalResults!!

            //If current page * limit is greater than total results the end has reached
            endReached = currentPage * limit > totalResults

            //Parse photo list response to photo list data model
            val parsedPhotos = parsePhotoListResponseToPhotoListModel(response.data)

            onResult(
                Resource.Success(
                    PhotoList(parsedPhotos, totalResults)
                )
            )
        } catch (e: Exception) {
            onResult(
                Resource.Error(message = "Unknown error occurred.")
            )
        }

        //Request is done
        isLoading = false
    }

    private fun parsePhotoListResponseToPhotoListModel(data: PhotoListResponse): List<Photo> {
        val parsedData = mutableListOf<Photo>()

        for (photoResp in data.photoResponses) {
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

interface SearchWallpaperByKeywordListener {
    fun onNewKeywordSearch(newKeyword: String){}
}