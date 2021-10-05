package bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util

import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.GetWallpaperById
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.SearchWallpapersByKeyword

data class NestedWallpaperUseCases(
    val searchWallpapersByKeyword: SearchWallpapersByKeyword,
    val getWallpaperById: GetWallpaperById,
)