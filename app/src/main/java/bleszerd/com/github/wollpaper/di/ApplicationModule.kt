package bleszerd.com.github.wollpaper.di

import android.content.Context
import bleszerd.com.github.wollpaper.core.Constants.API_BASE_URL
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperAPI
import bleszerd.com.github.wollpaper.feature_wallpaper.data.remote.WallpaperRepository
import bleszerd.com.github.wollpaper.feature_wallpaper.repository.WallpaperRepositoryImpl
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.SearchWallpapersByKeyword
import bleszerd.com.github.wollpaper.feature_wallpaper.use_case.util.NestedUseCases
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context)
}