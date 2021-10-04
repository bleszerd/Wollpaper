package bleszerd.com.github.wollpaper.feature_wallpaper.wallpaper_list.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.wollpaper.databinding.ActivityWallpaperListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WallpaperListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperListBinding

    private lateinit var wallpaperAdapter: WallpaperListAdapter
    private lateinit var recyclerWallpaper: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var searchInput: EditText
    private lateinit var nestedScrollView: NestedScrollView

    private val viewModel: WallpaperListViewModel by viewModels()

    //Listeners and callbacks
    private val wallpaperListScrollListener =
        NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            val childHeight = nestedScrollView.getChildAt(0).measuredHeight
            val parentHeight = nestedScrollView.measuredHeight
            val maxScrollValue = (parentHeight - childHeight) * -1

            if (maxScrollValue == scrollY){
                viewModel.searchPaginatedWallpaperByKeyword()
            }
        }

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
        nestedScrollView = binding.nestedScrollWallpaperList
    }

    private fun setupRecyclerWallpaper() {
        wallpaperAdapter = WallpaperListAdapter(this)
        gridLayoutManager = GridLayoutManager(this@WallpaperListActivity, 2)

        recyclerWallpaper.apply {
            adapter = wallpaperAdapter
            layoutManager = gridLayoutManager
        }

        nestedScrollView.setOnScrollChangeListener(wallpaperListScrollListener)
    }

    private fun setupViewListeners() {
        binding.frameLayoutButtonSearch.setOnClickListener {
            handleSearchAction()
        }

        binding.fabScrollToTop.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }
    }

    private fun setupObservers() {
        viewModel.wallpaperList.observe(this, { photoList ->
            if (photoList == null) return@observe

            if (photoList.size == 0){
                wallpaperAdapter.clearAllData()
                return@observe
            }

            wallpaperAdapter.insertItems(photoList)
        })


        viewModel.hasMoreWallpapers.observe(this, { hasMore ->
            if(!hasMore)
                binding.progressWallpaperListHasMoreWallpaper.visibility = View.GONE
        })
    }

    private fun handleSearchAction() {
        val keywordToSearch = searchInput.text.toString()

        if (keywordToSearch.isEmpty()) return

        viewModel.searchPaginatedWallpaperByKeyword(keywordToSearch)
    }
}
