package bleszerd.com.github.wollpaper.feature_wallpaper.presentation.wallpaper_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.wollpaper.databinding.FragmentWallpaperListBinding
import bleszerd.com.github.wollpaper.feature_wallpaper.data.model.Photo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpaperListFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperListBinding

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

            if (maxScrollValue == scrollY) {
                viewModel.searchPaginatedWallpaperByKeyword()
            }
        }

    private val wallpaperListListener = object : WallpaperListAdapter.WallpaperListAdapterListener {
        override fun onWallpaperSelected(wallpaper: Photo) {
            val action = WallpaperListFragmentDirections
                    .actionWallpaperListFragmentToWallpaperDetailsFragment(wallpaper.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallpaperListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        wallpaperAdapter = WallpaperListAdapter(requireActivity())
        gridLayoutManager = GridLayoutManager(requireContext(), 2)

        recyclerWallpaper.apply {
            adapter = wallpaperAdapter
            layoutManager = gridLayoutManager
        }

        wallpaperAdapter.setListener(wallpaperListListener)
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
        viewModel.wallpaperList.observe(viewLifecycleOwner, { photoList ->
            if (photoList == null) return@observe

            if (photoList.size == 0) {
                wallpaperAdapter.clearAllData()
                return@observe
            }

            wallpaperAdapter.insertItems(photoList)
        })

        viewModel.hasMoreWallpapers.observe(viewLifecycleOwner, { hasMore ->
            if (!hasMore)
                binding.progressWallpaperListHasMoreWallpaper.visibility = View.GONE
        })
    }

    private fun handleSearchAction() {
        val keywordToSearch = searchInput.text.toString()

        if (keywordToSearch.isEmpty()) return

        viewModel.searchPaginatedWallpaperByKeyword(keywordToSearch)
    }
}
