package com.example.mbookie.admin.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.SelectedShowDateAdapter
import com.example.mbookie.admin.ui.adapter.SelectedShowTimeAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.data.model.ShowTime
import com.example.mbookie.databinding.FragmentSetUpShowTimeBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class SetUpShowTimeFragment : Fragment(),SelectedShowDateAdapter.OnItemClickListener,SelectedShowTimeAdapter.OnItemClickListener {

    private lateinit var binding : FragmentSetUpShowTimeBinding

    private var showDateList : ArrayList<ShowDate> = arrayListOf()
    private lateinit var selectedShowDateAdapter: SelectedShowDateAdapter
    private var dateId = 1
    private var timeId = 1
    private var addShowTimeClickPos = -1

    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSetUpShowTimeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        initRecycler()
        onClickListener()

    }

    private fun onClickListener() {

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvAddShowDate.setOnClickListener {
            openDatePicker()
        }

        binding.tvSave.setOnClickListener {
            movieViewModel.saveShowtime(
                showDateList,
                movieViewModel.movieId,
                movieViewModel.selectedCinema.id.toString()
            )

            movieViewModel.saveShowtime.observe(viewLifecycleOwner) { state ->
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
                        findNavController().navigate(R.id.action_setUpShowTimeFragment_to_cinemaShowTimesFragment)
                    }
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
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
            val dateFormatter = "MMM dd"
            val sdf = SimpleDateFormat(dateFormatter, Locale.US)
            val uiStartDate = sdf.format(Date(it))

            showDateList.add(
                ShowDate(
                    id=dateId,
                    date = uiStartDate,
                    showTimeList = arrayListOf()
                )
            )
            selectedShowDateAdapter.notifyDataSetChanged()
            dateId++
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openStartTimePicker() {
        // instance of MDC time picker
        val materialTimePickerStart: MaterialTimePicker = MaterialTimePicker.Builder()
            // set the title for the alert dialog
            .setTitleText("SELECT SHOW TIME")
            // set the default hour for the
            // dialog when the dialog opens
            .setHour(12)
            // set the default minute for the
            // dialog when the dialog opens
            .setMinute(0)
            // set the time format
            // according to the region
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()

        materialTimePickerStart.show(childFragmentManager, "SetUpShowTimeFragment")

        // on clicking the positive button of the time picker
        // dialog update the TextView accordingly
        materialTimePickerStart.addOnPositiveButtonClickListener {

            val pickedHour: Int = materialTimePickerStart.hour
            val pickedMinute: Int = materialTimePickerStart.minute



            // check for single digit hour hour and minute
            // and update TextView accordingly
            val formattedTime: String = when {
                pickedHour > 12 -> {
                    val hour = if (pickedHour - 12 < 10) "0${pickedHour - 12}" else "${pickedHour - 12}"
                    val minute = if (pickedMinute < 10) "0$pickedMinute" else "$pickedMinute"
                    "$hour:$minute PM"
                }
                pickedHour == 12 -> {
                    val minute = if (pickedMinute < 10) "0$pickedMinute" else "$pickedMinute"
                    "${pickedHour}:${minute} PM"
                }
                pickedHour == 0 -> {
                    val hour = if (pickedHour + 12 < 10) "0${pickedHour + 12}" else "${pickedHour + 12}"
                    val minute = if (pickedMinute < 10) "0$pickedMinute" else "$pickedMinute"
                    "$hour:$minute AM"
                }
                else -> {
                    val hour = if (pickedHour < 10) "0$pickedHour" else "$pickedHour"
                    val minute = if (pickedMinute < 10) "0$pickedMinute" else "$pickedMinute"
                    "$hour:$minute AM"
                }
            }

            showDateList[addShowTimeClickPos].showTimeList.add(
                ShowTime(
                    id = timeId,
                    time = formattedTime
                )
            )
            selectedShowDateAdapter.notifyDataSetChanged()
            timeId++
        }
    }

    private fun initRecycler(){
        selectedShowDateAdapter = SelectedShowDateAdapter(showDateList,this,this)
        binding.recSelectedShowDate.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = selectedShowDateAdapter
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRemoveSelectedDateClick(pos: Int) {
        showDateList.removeAt(pos)
        selectedShowDateAdapter.notifyDataSetChanged()
    }

    override fun onAddShowTimeClick(pos: Int) {
        addShowTimeClickPos = pos
        openStartTimePicker()
        binding.tvSave.checkRequirement()
    }

    override fun onRemoveShowTimeClick(showDatePos: Int, showTimePos: Int) {
        showDateList[showDatePos].showTimeList.removeAt(showTimePos)
        selectedShowDateAdapter.notifyItemChanged(showDatePos)
        binding.tvSave.checkRequirement()
    }

    private fun TextView.checkRequirement(){
        isEnabled = !showDateList[0].showTimeList.isNullOrEmpty()
    }

}