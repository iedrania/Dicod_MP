package com.dicoding.mentoring.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.adapter.DaysAdapter
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.FragmentHomeBinding
import com.dicoding.mentoring.ui.profile.ListInterestActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DaysAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.chipChooseDays.setOnClickListener {
            val intent = Intent(activity, ListDayActivity::class.java)
            activity?.startActivity(intent)
        }

        return binding.root


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}