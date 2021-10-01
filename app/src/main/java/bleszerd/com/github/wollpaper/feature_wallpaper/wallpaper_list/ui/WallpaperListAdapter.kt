package bleszerd.com.github.wollpaper.feature_wallpaper.wallpaper_list.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.wollpaper.R
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import coil.load

class WallpaperListAdapter(
    private val context: Activity
) : RecyclerView.Adapter<WallpaperListAdapter.WallpaperListViewHolder>() {

    private val _photoList = mutableListOf<Photo>()
//    private val glideInstance = Glide.with(context)


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
            val wallpaperImageItem: ImageView = itemView.findViewById(R.id.imageWallpaperItem)

            //Populate references with data
            wallpaperImageItem
                .load(photo.imageUrl) {
                    crossfade(true)
                }
        }
    }

    fun insertItems(photos: List<Photo>) {
        var startUpdateIndexAt = _photoList.indices.last
        if (startUpdateIndexAt == -1) startUpdateIndexAt = 0
        _photoList.addAll(photos)
        notifyItemRangeInserted(startUpdateIndexAt, photos.size)
    }
}