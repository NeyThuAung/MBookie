package com.example.mbookie.customer.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mbookie.R
import com.example.mbookie.customer.ui.adapter.CustomerMovieListAdapter
import com.example.mbookie.customer.ui.adapter.ImageSliderAdapter
import com.example.mbookie.customer.ui.adapter.MovieCategoryAdapter
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.MovieListForCustomer
import com.example.mbookie.databinding.FragmentCustomerHomeBinding
import com.example.mbookie.util.AppSharedPreference
import com.example.mbookie.util.ImageSlider
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.CustomerViewModel
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

class CustomerHomeFragment : Fragment(), ImageSliderAdapter.OnItemClickListener,
    MovieCategoryAdapter.OnItemClickListener,
    CustomerMovieListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentCustomerHomeBinding
    private lateinit var movieCategoryAdapter: MovieCategoryAdapter

    private val appSharedPreference: AppSharedPreference by lazy {
        AppSharedPreference(requireContext())
    }

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var imageSliderHandle: Handler

    private val customerViewModel: CustomerViewModel by activityViewModels()

    private var posterList: ArrayList<ImageSlider> = arrayListOf()
    private var movieListForCustomer: ArrayList<MovieListForCustomer> = arrayListOf()

    private var i = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = true

        onClickEvent()

        imageSliderHandle = Handler(Looper.myLooper()!!)

        val userName = appSharedPreference.getValueString("userName")

        binding.tvHiUser.text = requireContext().resources.getString(R.string.hi_user, userName)

        getMovieList()

    }

    private fun onClickEvent() {
        binding.tvSeeAllNowPlaying.setOnClickListener{
            customerViewModel.mCategory = "Now Playing"
            findNavController().navigate(R.id.action_customerHomeFragment_to_seeAllMovieFragment)
        }
    }

    private fun getMovieList() {
        lifecycleScope.launchWhenStarted {
            customerViewModel.getMovieListForCustomer().collectLatest { result ->
                when (result) {
                    is UiState.Failure -> {
                        loadingDialog.hideDialog()
                        Toast.makeText(
                            requireContext(),
                            result.error.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    UiState.Loading -> {
                        loadingDialog.showDialog()
                    }

                    is UiState.Success -> {
                        loadingDialog.hideDialog()

                        val movieDetailList: ArrayList<MovieDetail> =
                            result.data as ArrayList<MovieDetail>
                        val groupedMovies = movieDetailList.groupBy { it.mCategoryId }

                        movieListForCustomer = groupedMovies.map { (categoryId, movies) ->
                            MovieListForCustomer(
                                categoryId?.toString() ?: "",
                                categoryId,
                                ArrayList(movies)
                            )
                        } as ArrayList<MovieListForCustomer>

                        Log.d("KLKJHKH", "getMovieList: $movieListForCustomer")

                        if (movieListForCustomer.isNotEmpty()) {

                            for (movie in movieListForCustomer) {
                                if (movie.mCategory.toString().lowercase() == "now playing") {
                                    posterList.addAll(movie.movieList!!.map {
                                        ImageSlider(
                                            1,
                                            it.mId.toString(),
                                            it.mPosterImg.toString(),
                                            it.mTitle.toString()
                                        )
                                    })
                                }
                            }

                            forNowPlayingImageSlider()

                            val filterMovieList = movieListForCustomer.filter { category ->
                                category.mCategory.toString().lowercase() != "now playing"
                            }

                            movieCategoryAdapter = MovieCategoryAdapter(
                                filterMovieList as ArrayList<MovieListForCustomer>,
                                this@CustomerHomeFragment,
                                this@CustomerHomeFragment
                            )
                            binding.recMovieCategory.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                                adapter = movieCategoryAdapter
                            }

                        }
                    }
                }
            }
        }
    }

    private fun forNowPlayingImageSlider() {
        // for Now Playing Image Slider
        if (posterList.isNotEmpty()) {
            binding.llNowPlaying.isVisible = true
            binding.viewPagerImageSlider.isVisible = true
            binding.tvNowPlaying.text = "Now Playing"
            bindImageSlider()
        } else {
            binding.llNowPlaying.isVisible = false
            binding.viewPagerImageSlider.isVisible = false
        }
    }

//    private fun getCategoryName(categoryId: Int): String {
//        return when (categoryId) {
//            1 -> "Popular"
//            2 -> "Upcoming"
//            3 -> "Now Playing"
//            else -> "Other"
//        }
//    }

    private fun bindImageSlider() {
        imageSliderAdapter = ImageSliderAdapter(binding.viewPagerImageSlider, posterList, this)
        binding.viewPagerImageSlider.adapter = imageSliderAdapter
        binding.viewPagerImageSlider.clipToPadding = false
        binding.viewPagerImageSlider.clipChildren = false
        binding.viewPagerImageSlider.offscreenPageLimit = 3

        binding.viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val comPosPageTarns = CompositePageTransformer()
        comPosPageTarns.addTransformer(MarginPageTransformer(30))
        comPosPageTarns.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPagerImageSlider.setPageTransformer(comPosPageTarns)

        binding.viewPagerImageSlider.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    imageSliderHandle.removeCallbacks(imageSliderRun)
                    imageSliderHandle.postDelayed(imageSliderRun, 2000)

                }
            }
        )

    }

    private val imageSliderRun = Runnable {
        binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        imageSliderHandle.removeCallbacks(imageSliderRun)
    }

    override fun onResume() {
        super.onResume()
        imageSliderHandle.postDelayed(imageSliderRun, 2000)
    }

    override fun onMoviePosterClick(movieId: String) {
        customerViewModel.movieId = movieId
        findNavController().navigate(R.id.action_customerHomeFragment_to_movieDetailFragment)
        requireActivity().showToast(movieId)
    }

    override fun onMovieDetailClick(movie: MovieDetail) {
        customerViewModel.movieId = movie.mId.toString()
        findNavController().navigate(R.id.action_customerHomeFragment_to_movieDetailFragment)
    }

    override fun onSeeAllClick(movieCategory : String) {
        customerViewModel.mCategory = movieCategory
        findNavController().navigate(R.id.action_customerHomeFragment_to_seeAllMovieFragment)
    }


}