package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.Feedback
import com.dicoding.mentoring.databinding.ItemIndividualFeedbackBinding

class ReviewAdapter(private val feedbacks: List<Feedback>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemIndividualFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReview = feedbacks[position]

        holder.binding.tvMenteeName.text = currentReview.menteeName
        holder.binding.ratingBarFeedback.rating = currentReview.rating
        holder.binding.tvRatingValue.text = currentReview.rating.toString()
        holder.binding.tvFeedbackMentee.text = currentReview.feedback

    }

    override fun getItemCount(): Int = feedbacks.size

    class ViewHolder(var binding: ItemIndividualFeedbackBinding) : RecyclerView.ViewHolder(binding.root)


}