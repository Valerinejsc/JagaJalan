package com.example.jagajalan.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jagajalan.R
import com.example.jagajalan.adapters.ReportAdapter
import com.example.jagajalan.databinding.ActivityHistoryBinding
import com.example.jagajalan.models.AppDatabase
import com.example.jagajalan.models.Report
import com.example.jagajalan.utils.BottomNavHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Intent
import com.example.jagajalan.activities.DetailReportActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var reportAdapter: ReportAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val reportList = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadUserReports()

        BottomNavHelper.setup(this, binding.bottomNavigation, R.id.nav_history)
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter(
            reports = reportList,
            onItemClick = { report ->
                val intent = Intent(this, DetailReportActivity::class.java).apply {
                    putExtra(DetailReportActivity.EXTRA_TITLE, report.title)
                    putExtra(DetailReportActivity.EXTRA_DESCRIPTION, report.description)
                    putExtra(DetailReportActivity.EXTRA_IMAGE_URL, report.imageUrl)
                    putExtra(DetailReportActivity.EXTRA_STATUS, report.status)
                    putExtra(DetailReportActivity.EXTRA_USERNAME, report.userName)
                    putExtra(DetailReportActivity.EXTRA_TIMESTAMP, report.timestamp)
                    putExtra(DetailReportActivity.EXTRA_LATITUDE, report.latitude)
                    putExtra(DetailReportActivity.EXTRA_LONGITUDE, report.longitude)
                }
                startActivity(intent)
            },
            onDeleteClick = { report ->
                showDeleteDialog(report)
            },
            showDeleteButton = true
        )
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = reportAdapter
        }
    }

    private fun loadUserReports() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("laporan")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    reportList.clear()
                    for (doc in snapshot.documents) {
                        val report = doc.toObject(Report::class.java)
                        if (report != null) reportList.add(report)
                    }
                    reportAdapter.updateData(reportList)
                    binding.tvEmpty.visibility =
                        if (reportList.isEmpty()) View.VISIBLE else View.GONE
                }
            }
    }

    private fun showDeleteDialog(report: Report) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Laporan")
            .setMessage("Apakah kamu yakin ingin menghapus laporan \"${report.title}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteReport(report)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteReport(report: Report) {
        // Hapus dari Firestore
        db.collection("laporan")
            .document(report.id)
            .delete()
            .addOnSuccessListener {
                // Hapus dari Room
                CoroutineScope(Dispatchers.IO).launch {
                    val database = AppDatabase.getDatabase(applicationContext)
                    database.reportDao().deleteAllReports()
                }
                Toast.makeText(this, "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menghapus laporan", Toast.LENGTH_SHORT).show()
            }
    }
}