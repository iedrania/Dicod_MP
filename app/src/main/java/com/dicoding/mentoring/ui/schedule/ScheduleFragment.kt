package com.dicoding.mentoring.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mentoring.adapter.ItemScheduleAdapter
import com.dicoding.mentoring.adapter.SubItemScheduleAdapter
import com.dicoding.mentoring.data.local.ItemSchedule
import com.dicoding.mentoring.data.local.Schedule
import com.dicoding.mentoring.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null

    private lateinit var itemAdapter : ItemScheduleAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            itemAdapter = ItemScheduleAdapter(getMentoringSession())
            binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getMentoringSession(): List<Schedule>{

        val mentoringSession = mutableListOf<Schedule>()

        val mondayMentoringSessions = listOf(
            ItemSchedule("Kak Nia Keren","19:00 - 20:00"),
            ItemSchedule("Kak Abil Keren","19:00 - 20:00"),
            ItemSchedule("Dapex Gaming","19:00 - 20:00")
        )

        val mondayMentoringSession = Schedule("Monday, 17 March 2023",mondayMentoringSessions)
        mentoringSession.add(mondayMentoringSession)

        return mentoringSession
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}