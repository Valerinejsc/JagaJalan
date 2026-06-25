package com.example.jagajalan.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jagajalan.R
import com.example.jagajalan.databinding.ItemReportBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.view.View

class ReportAdapter(
    private var reports: List<Report>,
    private val onItemClick: (Report) -> Unit,
    private val onItemLongClick: ((Report) -> Unit)? = null,
    private val onDeleteClick: ((Report) -> Unit)? = null,  // ← tambahkan
    private val showDeleteButton: Boolean = false
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(report: Report) {
            binding.tvTitle.text = report.title
            binding.tvDescription.text = report.description
            binding.tvLocation.text = "Lat: ${report.latitude}, Lng: ${report.longitude}"

            // Format timestamp
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
            binding.tvTime.text = sdf.format(Date(report.timestamp))

            // Status badge dengan warna berbeda
            binding.tvStatus.text = report.status
            when (report.status) {
                "Pending" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))
                "Proses"  -> binding.tvStatus.setBackgroundColor(Color.parseColor("#2196F3"))
                "Selesai" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"))
                else      -> binding.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))
            }

            // Load foto dengan Glide
            if (report.imageUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(report.imageUrl)
                    .placeholder(R.drawable.ic_camera_placeholder)
                    .into(binding.ivReportPhoto)
            } else {
                binding.ivReportPhoto.setImageResource(R.drawable.ic_camera_placeholder)
            }

            if (showDeleteButton) {
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    onDeleteClick?.invoke(report)
                }
            } else {
                binding.btnDelete.visibility = View.GONE
            }

            // Klik item
            binding.root.setOnClickListener {
                onItemClick(report)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(report)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    fun updateData(newReports: List<Report>) {
        reports = newReports
        notifyDataSetChanged()
    }
}