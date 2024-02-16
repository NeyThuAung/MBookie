package com.example.mbookie.admin.ui.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentAddMovieBinding
import com.example.mbookie.viewmodel.MovieViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddMovieFragment : Fragment() {

    private lateinit var binding : FragmentAddMovieBinding
    private lateinit var movieCoverUri: Uri

    private val movieViewModel : MovieViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()

        if (movieViewModel.selectedGenreList.isNotEmpty()){

            val selectedGenre = StringBuilder()

            for ((index,genre) in movieViewModel.selectedGenreList.withIndex()){
                selectedGenre.append(genre.genre)
                if (index < movieViewModel.selectedGenreList.size - 1) {
                    selectedGenre.append(", ")
                }

            }
            binding.etSelectGenre.setText(selectedGenre)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddMovieBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Vetail Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        onClickEvents()

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
            }

        })
    }

    private fun onClickEvents() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cvBackMovie.setOnClickListener {
            pickProfile()
        }

        binding.tilSelectGenre.setOnClickListener{
            findNavController().navigate(R.id.action_addMovieFragment_to_selectGenreFragment)
        }

        binding.etSelectGenre.setOnClickListener{
            findNavController().navigate(R.id.action_addMovieFragment_to_selectGenreFragment)
        }
    }

    private fun pickProfile() {

        ImagePicker.with(this)
            .crop(3f,4f)                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    movieCoverUri = data?.data!!
                    if (movieCoverUri.toString().isNotEmpty()){
                        binding.llCamera.isVisible = false
                        binding.ivMovieCoverImage.setImageURI(movieCoverUri)
                    }else{
                        binding.llCamera.isVisible = true
                    }

//                    profileUri = uri.toString()
//                    if (uri.toString().isNotEmpty()) {
//                        val path = RealPathUtil.getRealPath(requireActivity(),uri).toString()
//                        val bitmap = BitmapFactory.decodeFile(path)
//                        viewModel.saveCategoryRequest.profileB64 = ProductImageUtil.encodeImageWithBitmap(bitmap).toString()
//                        showHideProfile(bitmap,true)
//                        binding.tvSave.checkRequirement()
//                    }
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}