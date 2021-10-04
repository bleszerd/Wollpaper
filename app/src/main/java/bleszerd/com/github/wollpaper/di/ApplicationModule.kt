package bleszerd.com.github.wollpaper.di

import bleszerd.com.github.wollpaper.core.Constants.API_BASE_URL
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperAPI
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.data.repository.WallpaperRepositoryImpl
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.SearchWallpapersByKeyword
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util.NestedUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideWallpaperAPI(): WallpaperAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(API_BASE_URL)
        .build()
        .create(WallpaperAPI::class.java)

    @Provides
    @Singleton
    fun provideWallpaperRepository(
        api: WallpaperAPI
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideNestedUseCases(
        repository: WallpaperRepository
    ) = NestedUseCases(
        searchWallpapersByKeyword = SearchWallpapersByKeyword(repository)
    )
}