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
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.MovieCategoryAdapter
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
    private var moviePosterImg =  ""


    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    override fun onResume() {
        super.onResume()

        if (movieViewModel.selectedGenreList.isNotEmpty()) {

            val selectedGenre = StringBuilder()

            for ((index, genre) in movieViewModel.selectedGenreList.withIndex()) {
                selectedGenreIdList.clear()
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

    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                movieViewModel.selectedGenreList.clear()
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

        binding.cvBackMovie.setOnClickListener {
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

    private fun saveMovieDetails() {
        movieViewModel.saveMovie(
            MovieDetail(
                mId = "",
                mPosterImg = moviePosterImg,
                mTitle = binding.etMovieTitle.text.toString(),
                mCategoryId = selectedCategoryId,
                mGenreIdList = selectedGenreIdList,
                mDuration = binding.etDuration.text.toString(),
                mReleaseDate = binding.etReleaseDate.text.toString(),
                mCensorship = binding.etCensorship.text.toString(),
                mTrailerLink = binding.etTrailerLink.text.toString(),
                mDescription = binding.etDescription.text.toString()
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
                    requireActivity().showToast(state.data)
                    movieViewModel.selectedGenreList.clear()
                    findNavController().navigate(R.id.action_addMovieFragment_to_selectCinemaFragment)
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
        isEnabled =
            binding.etMovieTitle.text.toString().trim().isNotEmpty() && selectedCategoryId != -1 &&
                    binding.etSelectGenre.text.toString().trim()
                        .isNotEmpty() && binding.etDuration.text.toString().trim().isNotEmpty() &&
                    binding.etReleaseDate.text.toString().trim().isNotEmpty()
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