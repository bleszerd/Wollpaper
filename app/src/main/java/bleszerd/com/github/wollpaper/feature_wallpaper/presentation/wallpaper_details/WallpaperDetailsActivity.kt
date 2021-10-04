package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bleszerd.com.github.wollpaper.databinding.ActivityWallpaperDetailsBinding

class WallpaperDetailsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}