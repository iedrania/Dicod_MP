package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.Review
import com.dicoding.mentoring.databinding.ItemIndividualFeedbackBinding

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemIndividualFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReview = reviews[position]

        holder.binding.tvMenteeName.text = currentReview.menteeName
        holder.binding.tvRatingValue.text = currentReview.rating.toString()
        holder.binding.tvFeedbackMentee.text = currentReview.feedback
    }

    override fun getItemCount(): Int = reviews.size

    class ViewHolder(var binding: ItemIndividualFeedbackBinding) : RecyclerView.ViewHolder(binding.root)


}