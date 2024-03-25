package com.example.mbookie.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.R
import com.example.mbookie.customer.ui.adapter.MovieCategoryAdapter
import com.example.mbookie.customer.ui.adapter.SeeAllMovieAdapter
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.MovieListForCustomer
import com.example.mbookie.databinding.FragmentSeeAllMovieBinding
import com.example.mbookie.util.AppSharedPreference
import com.example.mbookie.util.ImageSlider
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.CustomerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest

class SeeAllMovieFragment : Fragment(), SeeAllMovieAdapter.OnOrderClickListener {

    private lateinit var binding: FragmentSeeAllMovieBinding

    private val appSharedPreference: AppSharedPreference by lazy {
        AppSharedPreference(requireContext())
    }

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }
    private val customerViewModel: CustomerViewModel by activityViewModels()
    private lateinit var seeAllMovieAdapter: SeeAllMovieAdapter

    private lateinit var lm: LinearLayoutManager
    private var checkingList = ArrayList<MovieDetail>()
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0
    private var isScrolling: Boolean = false
    private var isLastItemReached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSeeAllMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        binding.tvAddNewMovieTitle.text = customerViewModel.mCategory
        initMovieListRecycler()

        if (customerViewModel.movieList.size == 0) {
            getMovieList()
        } else {
            checkingList.addAll(customerViewModel.movieList)
            lifecycleScope.launchWhenStarted {
                seeAllMovieAdapter.submitList(customerViewModel.movieList)
            }
        }

        // get more order list when scroll recycler view
        binding.recMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = lm.childCount
                totalItems = lm.itemCount
                scrollOutItems = lm.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems) && !isLastItemReached) {
                    isScrolling = false
                    checkingList.clear()
                    getMovieListWithPagination()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }
        })

        onBackPressed()
        onClickEvent()

    }

    private fun onClickEvent() {

        binding.ivBack.setOnClickListener {
            customerViewModel.movieList.clear()
            findNavController().popBackStack()
        }
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customerViewModel.movieList.clear()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun initMovieListRecycler() {
        seeAllMovieAdapter = SeeAllMovieAdapter(this)
        lm = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        binding.recMovieList.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = lm
            adapter = seeAllMovieAdapter
        }
    }

    private fun getMovieList() {
        lifecycleScope.launchWhenStarted {
            customerViewModel.getFirstMovieList(customerViewModel.mCategory).collectLatest { result ->
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

                        checkingList =
                            result.data as ArrayList<MovieDetail>

                        customerViewModel.movieList.addAll(checkingList)
                        seeAllMovieAdapter.submitList(
                            customerViewModel.movieList
                        )

                    }
                }
            }
        }
    }

    private fun getMovieListWithPagination() {
        lifecycleScope.launchWhenStarted {
            customerViewModel.getMovieListWithPagination(appSharedPreference,customerViewModel.mCategory)
                .collectLatest { result ->
                    when (result) {
                        is UiState.Failure -> {
                            binding.progressLoad.isVisible = false
                            loadingDialog.hideDialog()
                            Toast.makeText(
                                requireContext(),
                                result.error.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        UiState.Loading -> {
                            binding.progressLoad.isVisible = true
                            loadingDialog.hideDialog()
                        }

                        is UiState.Success -> {
                            binding.progressLoad.isVisible = false
                            loadingDialog.hideDialog()

                            checkingList =
                                result.data as ArrayList<MovieDetail>
                            customerViewModel.movieList.addAll(checkingList)
                            seeAllMovieAdapter.submitList(
                                customerViewModel.movieList
                            )

                            isLastItemReached =
                                appSharedPreference.getValueBoolean("isLastItemReached", false)

                            if (isLastItemReached) {
                                binding.tvNoData.isVisible = true
                                binding.progressLoad.isVisible = false
                            }
                        }
                    }
                }
        }
    }

    override fun onRootClick(movieId: String) {

    }


}