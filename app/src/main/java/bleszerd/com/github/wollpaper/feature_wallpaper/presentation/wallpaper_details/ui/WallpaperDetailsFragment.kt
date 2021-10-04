package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_details.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import bleszerd.com.github.wollpaper.databinding.FragmentWallpaperDetailsBinding

class WallpaperDetailsFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWallpaperDetailsBinding.inflate(layoutInflater)
    }
}