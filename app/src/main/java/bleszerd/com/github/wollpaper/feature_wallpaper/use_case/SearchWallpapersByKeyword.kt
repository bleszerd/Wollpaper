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
    private var lastSearchedTerm = DEFAULT_SEARCH_KEYWORD
    private var currentPage = 0
    private var isLoading = false
    private var totalResults = 0
    private var endReached = false

    suspend operator fun invoke(
        keyword: String? = DEFAULT_SEARCH_KEYWORD,
        limit: Int,
        page: Int = 1,
        listener: SearchWallpaperByKeywordListener? = null,
        onEndReached: () -> Unit = {},
        onResult: (Resource<PhotoList>) -> Unit = {}
    ) {
        if (isLoading)
            return

        val searchTerm = keyword?: lastSearchedTerm

        if (lastSearchedTerm == searchTerm){
            if (endReached){
                onEndReached()
                println("reached end")
                return
            }
            currentPage += 1
        }
        else {
            currentPage = 1
            lastSearchedTerm = searchTerm
            listener?.onNewKeywordSearch(searchTerm)
        }

        try {
            val response = repository.searchPaginatedWallpapersByKeyword(
                searchTerm,
                currentPage,
                limit
            )
            totalResults = response.data?.totalResults!!
            endReached = currentPage * limit > totalResults
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