package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_details.ui

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bleszerd.com.github.wollpaper.R
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.builder.ConfirmDialogBuilder
import bleszerd.com.github.wollpaper.core.components.confirm_dialog.ui.ConfirmDialog
import bleszerd.com.github.wollpaper.databinding.FragmentWallpaperDetailsBinding
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WallpaperDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWallpaperDetailsBinding

    private lateinit var backgroundImage: ImageView
    private lateinit var progressIcon: ProgressBar
    private lateinit var photoId: String
    private lateinit var imageRequest: ImageLoader
    private lateinit var photoDrawable: Drawable

    private val viewModel: WallpaperDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallpaperDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPhotoRequest()
        setupViewReferences()
        setupViewListeners()
        getArgumentsFromNav()
        populateViewData(photoId)
        setupObservers()
    }

    private fun setupPhotoRequest() {
        imageRequest = ImageLoader(requireContext())
    }

    private fun setupViewReferences() {
        backgroundImage = binding.wallpaperDetailsBackgroundImage
        progressIcon = binding.wallpaperDetailsProgress
    }

    private fun setupViewListeners() {
        binding.wallpaperDetailsButtonUsePhoto.setOnClickListener {
            handleSetWallpaperClickAction()
        }

        binding.wallpaperDetailsButtonOpenPexels.setOnClickListener {
            openWallpaperOnPexels()
        }
    }

    private fun getArgumentsFromNav() {
        val argsBundle = requireArguments()
        val args = WallpaperDetailsFragmentArgs.fromBundle(argsBundle)
        photoId = args.photoId
    }

    private fun populateViewData(photoId: String) {
        viewModel.getWallpaperById(photoId)
    }

    private fun setupObservers() {
        viewModel.photoData.observe(viewLifecycleOwner, { photo ->
            if (photo == null)
                return@observe

            updateViewData(photo)
        })
    }

    private fun updateViewData(photo: Photo) {
        //Prepare image request with coil
        val request = ImageRequest.Builder(requireContext())
            .data(photo.imageUrl)
            .crossfade(true)
            .target {
                backgroundImage.setImageDrawable(it)
                progressIcon.visibility = View.GONE
                photoDrawable = it
                viewModel.updatePhotoResource(it)
            }
            .build()

        //Update image
        CoroutineScope(Dispatchers.Main).launch {
            imageRequest.execute(request)
        }

        //Update texts
        binding.wallpaperDetailsAuthorName.text = photo.photographer
    }

    private fun handleSetWallpaperClickAction() {
        val dialog = ConfirmDialogBuilder(requireContext())
            .setTitle(R.string.confirm_action)
            .setText(R.string.confirm_photo_as_wallpaper_lock_screen)
            .setPositiveButton(R.string.confirm){
                Toast.makeText(requireContext(), R.string.all_done_smile, Toast.LENGTH_LONG).show()
                setDeviceWallpaper()
            }
            .setNegativeButton(R.string.cancel) { }
            .build()

        dialog.show(parentFragmentManager, dialog.tag)
    }

    private fun setDeviceWallpaper() {
        CoroutineScope(Dispatchers.Main).launch {
            val wallpaperManager = WallpaperManager.getInstance(requireContext())

            try {
                wallpaperManager.setBitmap(viewModel.photoResource)
            } catch (e: Exception){
                println(e.message)
            }
        }
    }

    private fun openWallpaperOnPexels() {
        val photo = viewModel.photoData.value
        if (photo != null) {
            val i = Intent(Intent.ACTION_VIEW, photo.pexelsUrl.toUri())
            startActivity(i)
        }
    }
}
