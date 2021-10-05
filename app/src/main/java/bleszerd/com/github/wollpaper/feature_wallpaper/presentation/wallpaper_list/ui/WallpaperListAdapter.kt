package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_list.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.wollpaper.R
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WallpaperListAdapter(
    private val context: Activity
) : RecyclerView.Adapter<WallpaperListAdapter.WallpaperListViewHolder>() {

    private lateinit var adapterListener: WallpaperListAdapterListener
    private val _photoList = mutableListOf<Photo>()
    private val imageRequest = ImageLoader(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperListViewHolder {
        return WallpaperListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WallpaperListViewHolder, position: Int) {
        holder.bind(_photoList[position])
    }

    override fun getItemCount(): Int {
        return _photoList.size
    }

    inner class WallpaperListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            //Find view references
            val wallpaperProgressIndicator: ProgressBar =
                itemView.findViewById(R.id.progressWallpaperItemPlaceholder)
            val wallpaperImageItem: ImageView =
                itemView.findViewById(R.id.imageWallpaperItemPlaceholder)

            //Populate references with data
            val request = ImageRequest.Builder(context)
                .data(photo.imageUrl)
                .crossfade(true)
                .target {
                    wallpaperImageItem.setImageDrawable(it)
                    wallpaperProgressIndicator.visibility = View.GONE
                }.build()

            CoroutineScope(Dispatchers.Main).launch {
                imageRequest.execute(request)
            }

            //Set listeners
            if (::adapterListener.isInitialized)
                itemView.setOnClickListener {
                    adapterListener.onWallpaperSelected(photo)
                }
        }
    }

    fun setListener(listener: WallpaperListAdapterListener) {
        adapterListener = listener
    }

    fun insertItems(photos: List<Photo>) {
        var startUpdateIndexAt = _photoList.size
        if (startUpdateIndexAt == -1) startUpdateIndexAt = 0
        _photoList.addAll(photos)
        notifyItemRangeInserted(startUpdateIndexAt, photos.size)
    }

    fun clearAllData() {
        _photoList.clear()
        notifyDataSetChanged()
    }

    interface WallpaperListAdapterListener {
        fun onWallpaperSelected(wallpaper: Photo){}
    }
}