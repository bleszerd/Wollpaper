package bleszerd.com.github.wollpaper.feature_wallpaper.wallpaper_list.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.wollpaper.databinding.ActivityWallpaperListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpaperListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperListBinding

    private lateinit var wallpaperAdapter: WallpaperListAdapter
    private lateinit var recyclerWallpaper: RecyclerView
    private lateinit var searchInput: EditText
    private val viewModel: WallpaperListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewReferences()
        setupRecyclerWallpaper()
        setupViewListeners()
        setupObservers()
    }

    private fun setupViewReferences() {
        recyclerWallpaper = binding.recyclerWallpaperList
        searchInput = binding.editTextInputSearch
    }

    private fun setupRecyclerWallpaper() {
        wallpaperAdapter = WallpaperListAdapter(this)
        recyclerWallpaper.apply {
            adapter = wallpaperAdapter
            layoutManager = GridLayoutManager(this@WallpaperListActivity, 2)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupViewListeners() {
        binding.frameLayoutSearchBarContainer.setOnClickListener {
            handleSearchAction()
        }
    }

    private fun setupObservers() {
        viewModel.wallpaperList.observe(this, { photoList ->
            if (photoList == null) return@observe

            wallpaperAdapter.insertItems(photoList)
        })
    }

    private fun handleSearchAction() {
        val keywordToSearch = searchInput.text.toString()

        if (keywordToSearch.isEmpty()) return

        viewModel.searchWallpaperByKeyword(keywordToSearch)
    }
}
