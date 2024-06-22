package com.example.trainhub.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainhub.BuildConfig
import com.example.trainhub.R
import com.example.trainhub.adapters.ExerciseAdapter
import com.example.trainhub.api.ExerciseService
import com.example.trainhub.api.RetrofitClient
import com.example.trainhub.models.entities.Exercise
import retrofit2.Response
import android.widget.ProgressBar
class DiscoverFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var exerciseList: List<Exercise> = listOf()
    private var pb: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)
        pb = view.findViewById(R.id.pbDiscover)
        recyclerView = view.findViewById(R.id.rvExercises)
        recyclerView.layoutManager = LinearLayoutManager(context)
        exerciseAdapter = ExerciseAdapter(exerciseList)
        recyclerView.adapter = exerciseAdapter
        pb?.visibility = View.VISIBLE
        fetchExercises()

        return view
    }

    private fun fetchExercises() {
        val exercisesApiKey = BuildConfig.EXERCISES_API_KEY
        val retrofitService = RetrofitClient.getRetrofitInstance(exercisesApiKey).create(ExerciseService::class.java)

        val responseLiveData: LiveData<Response<List<Exercise>>> = liveData {
            try {
                val response = retrofitService.getExercises(10, 0)
                emit(response)
            } catch (e: Exception) {
                Log.e("API Error", "Exception occurred: ${e.message}")
                // Show toast message if there's an error
                showToast("Failed to fetch exercises. Please try again later.")
            }
        }

        responseLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                pb?.visibility = View.GONE
                exerciseList = response.body() ?: listOf()
                Log.d("API Response", "Fetched exercises: $exerciseList")

                // Update the adapter
                exerciseAdapter = ExerciseAdapter(exerciseList)
                recyclerView.adapter = exerciseAdapter
                exerciseAdapter.notifyDataSetChanged()
            } else {
                Log.e("API Error", "Failed to fetch exercises: ${response.code()}, ${response.message()}")
                Log.e("API Error", "Response Body: ${response.errorBody()?.string()}")
                Log.e("API Error", "Response Headers: ${response.headers()}")
                // Show toast message if response is not successful
                showToast("Failed to fetch exercises. Please try again later.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}