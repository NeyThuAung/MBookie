package com.example.mbookie.admin.ui.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.GenreAdapter
import com.example.mbookie.admin.ui.adapter.MovieCategoryAdapter
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.FragmentAddMovieBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.MovieCategoryData
import com.example.mbookie.util.UiState
import com.example.mbookie.util.movieCategoryList
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class AddMovieFragment : Fragment() {

    private lateinit var binding: FragmentAddMovieBinding
    private lateinit var movieCoverUri: Uri

    private val movieViewModel: MovieViewModel by activityViewModels()
    private var selectedPos = 1
    private var selectedCategoryId = -1
    private var selectedGenreIdList: ArrayList<String> = arrayListOf()

    val storageRef = FirebaseStorage.getInstance()
    private var moviePosterImg = ""

    private var editMoviePoster = 0 // 0 is Normal // 1 is edit

    private var oldMovieName = ""
    private var oldMovieCategoryId = -1
    private var oldSelectedGenre = ""
    private var oldSelectedGenreIdList = arrayListOf<String>()
    private var oldLanguage = ""
    private var oldDuration = ""
    private var oldReleaseDate = ""
    private var oldCensorShip = ""
    private var oldTrailerLint = ""
    private var oldDescription = ""

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    override fun onResume() {
        super.onResume()

        if (movieViewModel.selectedGenreList.isNotEmpty()) {
            Log.d("JHKJH", "onResume: ${movieViewModel.selectedGenreList}")
            val selectedGenre = StringBuilder()
            selectedGenreIdList.clear()
            for ((index, genre) in movieViewModel.selectedGenreList.withIndex()) {
                selectedGenreIdList.add(genre.id.toString())
                selectedGenre.append(genre.genre)
                if (index < movieViewModel.selectedGenreList.size - 1) {
                    selectedGenre.append(", ")
                }

            }
            binding.etSelectGenre.setText(selectedGenre)
            binding.tvSave.checkRequirement()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        if (movieViewModel.movieId.isNotEmpty()) {
            setMovieEditLayout()
        } else {
            binding.tvAddNewMovieTitle.text = "Create Movie"
        }

        onBackPressed()
        onClickEvents()
        bindMovieCategoryDropDown(movieCategoryList)
        clearText()

        binding.tvMovieTitleCount.text = "0/200"
        binding.etMovieTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    binding.tvMovieTitleCount.text = "0/200"

                } else {
                    binding.tvMovieTitleCount.text = p0.toString().length.toString() + "/200"
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.tvSave.checkRequirement()
            }
        })

        binding.etDuration.doAfterTextChanged {
            binding.tvSave.checkRequirement()
        }

        binding.etLanguage.doAfterTextChanged {
            binding.tvSave.checkRequirement()
        }

    }

    private fun setMovieEditLayout() {
        binding.tvAddNewMovieTitle.text = "Edit Movie"

        if (movieViewModel.editMovieDetail.mPosterImg.toString().isNotEmpty()) {
            binding.llCamera.isVisible = false
            binding.ivEditMoviePhoto.isVisible = true

            moviePosterImg = movieViewModel.editMovieDetail.mPosterImg!!

            Glide.with(binding.root)
                .load(movieViewModel.editMovieDetail.mPosterImg)
                .into(binding.ivMovieCoverImage)

        } else {
            binding.llCamera.isVisible = true
            binding.ivEditMoviePhoto.isVisible = false
        }

        binding.etMovieTitle.setText(movieViewModel.editMovieDetail.mTitle)

        selectedCategoryId = movieViewModel.editMovieDetail.mCategoryId!!
        binding.actvMovieCategory.setText(movieCategoryList[selectedCategoryId].movieCategoryName)

        selectedGenreIdList.clear()
        selectedGenreIdList.addAll(movieViewModel.editMovieDetail.mGenreIdList!!)
//        getGenreListWithSelectedIdList()
        binding.etSelectGenre.setText(movieViewModel.editMovieDetail.mGenre)

        binding.etLanguage.setText(movieViewModel.editMovieDetail.mLanguage)
        binding.etDuration.setText(movieViewModel.editMovieDetail.mDuration)
        binding.etReleaseDate.setText(movieViewModel.editMovieDetail.mReleaseDate)

        val censorship = movieViewModel.editMovieDetail.mCensorship
        if (!censorship.isNullOrEmpty()) {
            binding.etCensorship.setText(censorship)
            oldCensorShip = movieViewModel.editMovieDetail.mCensorship.toString()
        }

        val trailer = movieViewModel.editMovieDetail.mTrailerLink
        if (!trailer.isNullOrEmpty()) {
            binding.etTrailerLink.setText(trailer)
            oldTrailerLint = movieViewModel.editMovieDetail.mTrailerLink.toString()
        }

        val desc = movieViewModel.editMovieDetail.mDescription
        if (!desc.isNullOrEmpty()) {
            binding.etDescription.setText(desc)
            oldDescription = movieViewModel.editMovieDetail.mDescription.toString()
        }

        oldMovieName = movieViewModel.editMovieDetail.mTitle.toString()
        oldMovieCategoryId = movieViewModel.editMovieDetail.mCategoryId!!
        oldSelectedGenreIdList = movieViewModel.editMovieDetail.mGenreIdList!!
        oldSelectedGenre = movieViewModel.editMovieDetail.mGenre.toString()
        oldLanguage = movieViewModel.editMovieDetail.mLanguage.toString()
        oldDuration = movieViewModel.editMovieDetail.mDuration.toString()
        oldReleaseDate = movieViewModel.editMovieDetail.mReleaseDate.toString()


    }

    private fun getGenreListWithSelectedIdList() {
        movieViewModel.getGenreListWithIdLst(movieViewModel.editMovieDetail.mGenreIdList!!)
        movieViewModel.genreListWithIdLst.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    Toast.makeText(requireContext(), state.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    loadingDialog.hideDialog()

                    movieViewModel.selectedGenreList.clear()
                    movieViewModel.selectedGenreList.addAll(state.data as ArrayList<Genre>)
                    Log.d("KJHKH", "getGenreListWithSelectedIdList: ${movieViewModel.selectedGenreList}")

                }
            }
        }
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                movieViewModel.selectedGenreList.clear()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun clearText() {
        binding.etReleaseDate.doAfterTextChanged {
            if (!it.toString().isNullOrEmpty()) {

                binding.tilReleaseDate.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_text)

                binding.tilReleaseDate.setEndIconOnClickListener {
                    binding.etReleaseDate.text?.clear()
                }

            } else {

                binding.tilReleaseDate.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar)

                binding.tilReleaseDate.setEndIconOnClickListener {
                    openDatePicker()
                }

            }

            binding.tvSave.checkRequirement()
        }
    }

    private fun bindMovieCategoryDropDown(movieCategoryList: ArrayList<MovieCategoryData>) {
        binding.actvMovieCategory.setDropDownBackgroundResource(R.color.white)
        var mAdapter =
            MovieCategoryAdapter(
                requireContext(),
                R.layout.movie_category_item_card,
                selectedPos,
                movieCategoryList
            )
        binding.actvMovieCategory.setAdapter(mAdapter)

        binding.actvMovieCategory.setOnItemClickListener { _, _, i, _ ->
            selectedPos = i
            if (movieCategoryList[i].id != -1) {
//                productDetailViewModel.productTypeId = lst[i].id
                selectedCategoryId = movieCategoryList[i].id
            } else {
//                productDetailViewModel.productTypeId = 0
            }

            binding.actvMovieCategory.setText(movieCategoryList[i].movieCategoryName)
            binding.tvSave.checkRequirement()
        }
    }

    private fun onClickEvents() {
        binding.ivBack.setOnClickListener {
            movieViewModel.selectedGenreList.clear()
            findNavController().popBackStack()
        }

        binding.llCamera.setOnClickListener {
            pickProfile()
        }

        binding.ivEditMoviePhoto.setOnClickListener {
            editMoviePoster = 1
            pickProfile()
        }

        binding.tilSelectGenre.setOnClickListener {
            findNavController().navigate(R.id.action_addMovieFragment_to_selectGenreFragment)
        }

        binding.etSelectGenre.setOnClickListener {
            findNavController().navigate(R.id.action_addMovieFragment_to_selectGenreFragment)
        }

        binding.etReleaseDate.setOnClickListener {
            openDatePicker()
        }

        binding.tvSave.setOnClickListener {

            if (movieViewModel.movieId.isNotEmpty()){
                if (editMoviePoster == 1){
                    storageRef.getReference("Images")
                        .child(System.currentTimeMillis().toString())
                        .putFile(movieCoverUri)
                        .addOnSuccessListener { task ->

                            task.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener {
                                    Log.d("JHKHKJ", "onClickEvents: ${it.toString()}")
                                    movieViewModel.editMovieDetail.mPosterImg = it.toString()

                                    updateMovie()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        requireContext(),
                                        it.localizedMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                it.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }else{
                    updateMovie()
                }
            }else{
                storageRef.getReference("Images")
                    .child(System.currentTimeMillis().toString())
                    .putFile(movieCoverUri)
                    .addOnSuccessListener { task ->

                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                moviePosterImg = it.toString()

                                saveMovieDetails()

                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    it.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            it.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }



        }

    }

    private fun saveMovieDetails() {
        movieViewModel.saveMovie(
            MovieDetail(
                mId = "",
                mPosterImg = moviePosterImg,
                mTitle = binding.etMovieTitle.text.toString(),
                mCategoryId = selectedCategoryId,
                mGenreIdList = selectedGenreIdList,
                mGenre = binding.etSelectGenre.text.toString(),
                mDuration = binding.etDuration.text.toString(),
                mReleaseDate = binding.etReleaseDate.text.toString(),
                mCensorship = binding.etCensorship.text.toString(),
                mTrailerLink = binding.etTrailerLink.text.toString(),
                mDescription = binding.etDescription.text.toString(),
                mLanguage = binding.etLanguage.text.toString()
            )
        )

        movieViewModel.saveMovie.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast("Movie has been successfully created.")
                    movieViewModel.selectedGenreList.clear()
                    movieViewModel.movieId = state.data
                    findNavController().navigate(R.id.action_addMovieFragment_to_availableCinemaFragment)
                }
            }
        }
    }

    private fun updateMovie() {
        movieViewModel.updateMovie(
            MovieDetail(
                mId = movieViewModel.editMovieDetail.mId,
                mPosterImg = movieViewModel.editMovieDetail.mPosterImg,
                mTitle = binding.etMovieTitle.text.toString(),
                mCategoryId = selectedCategoryId,
                mGenreIdList = selectedGenreIdList,
                mGenre = binding.etSelectGenre.text.toString(),
                mDuration = binding.etDuration.text.toString(),
                mReleaseDate = binding.etReleaseDate.text.toString(),
                mCensorship = binding.etCensorship.text.toString(),
                mTrailerLink = binding.etTrailerLink.text.toString(),
                mDescription = binding.etDescription.text.toString(),
                mLanguage = binding.etLanguage.text.toString()
            )
        )

        movieViewModel.updateMovie.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.data)

                    movieViewModel.movieId = movieViewModel.editMovieDetail.mId.toString()
                    findNavController().navigate(R.id.action_addMovieFragment_to_availableCinemaFragment)
                }
            }
        }

    }

    private fun openDatePicker() {
        val c = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val datePickerStart =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Start Date")
                .setSelection(c.timeInMillis)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePickerStart.show(childFragmentManager, "Tag")

        //to bind date to textView after date select
        datePickerStart.addOnPositiveButtonClickListener {
            val dateFormatter = "dd MMM yyyy"
            val sdf = SimpleDateFormat(dateFormatter, Locale.US)
            val UIStartDate = sdf.format(Date(it))

            binding.etReleaseDate.setText(UIStartDate)
            binding.tvSave.checkRequirement()

        }
    }

    private fun pickProfile() {

        ImagePicker.with(this)
            .crop(
                3f,
                4f
            )                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

    }

    private fun TextView.checkRequirement() {
        isEnabled = if (movieViewModel.movieId.isNotEmpty()) {
            binding.etMovieTitle.text.toString().trim().isNotEmpty() && selectedCategoryId != -1 &&
                    binding.etSelectGenre.text.toString().trim()
                        .isNotEmpty() && binding.etDuration.text.toString().trim().isNotEmpty() &&
                    binding.etReleaseDate.text.toString().trim()
                        .isNotEmpty() && binding.etLanguage.text.toString().trim().isNotEmpty() &&
                    ((binding.etMovieTitle.text.toString() != oldMovieName) || (selectedCategoryId != oldMovieCategoryId)
                            || (binding.etSelectGenre.text.toString() != oldSelectedGenre) || (binding.etLanguage.text.toString() != oldLanguage) ||
                            (binding.etDuration.text.toString() != oldDuration) || (binding.etReleaseDate.text.toString() != oldReleaseDate) ||
                            (binding.etCensorship.text.toString() != oldCensorShip) || (binding.etTrailerLink.text.toString() != oldTrailerLint) ||
                            (binding.etDescription.text.toString() != oldDescription))
        } else {
            binding.etMovieTitle.text.toString().trim().isNotEmpty() && selectedCategoryId != -1 &&
                    binding.etSelectGenre.text.toString().trim()
                        .isNotEmpty() && binding.etDuration.text.toString().trim().isNotEmpty() &&
                    binding.etReleaseDate.text.toString().trim()
                        .isNotEmpty() && binding.etLanguage.text.toString().trim().isNotEmpty()
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    movieCoverUri = data?.data!!
                    if (movieCoverUri.toString().isNotEmpty()) {
                        binding.llCamera.isVisible = false
                        binding.ivMovieCoverImage.setImageURI(movieCoverUri)
                        binding.ivEditMoviePhoto.isVisible = true
                    } else {
                        binding.llCamera.isVisible = true
                        binding.ivEditMoviePhoto.isVisible = false
                    }


                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}