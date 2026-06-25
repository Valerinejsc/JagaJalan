package com.example.jagajalan

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jagajalan.activities.LoginActivity
import com.example.jagajalan.adapters.ReportAdapter
import com.example.jagajalan.models.Report
import com.example.jagajalan.utils.LocationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.jagajalan.activities.DetailReportActivity
import com.example.jagajalan.utils.BottomNavHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: com.example.jagajalan.databinding.ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var locationHelper: LocationHelper
    private lateinit var reportAdapter: ReportAdapter
    private val db = FirebaseFirestore.getInstance()
    private val reportList = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = com.example.jagajalan.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHelper = LocationHelper(this)

        setupRecyclerView()

        loadReports()

        // Setup pencarian
        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterReports(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        BottomNavHelper.setup(this, binding.bottomNavigation, R.id.nav_home)
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
            onDeleteClick = null,       // ← tidak perlu hapus di halaman Beranda
            showDeleteButton = false    // ← sembunyikan tombol hapus di halaman Beranda
        )
        binding.rvReports.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = reportAdapter
        }
    }

    private fun loadReports() {
        db.collection("laporan")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Gagal memuat laporan", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    reportList.clear()
                    for (doc in snapshot.documents) {
                        val report = doc.toObject(Report::class.java)
                        if (report != null) reportList.add(report)
                    }
                    reportAdapter.updateData(reportList)

                    // Tampilkan pesan jika kosong
                    binding.tvEmpty.visibility =
                        if (reportList.isEmpty()) View.VISIBLE else View.GONE
                }
            }
    }

    private fun filterReports(query: String) {
        val filtered = if (query.isEmpty()) {
            reportList
        } else {
            reportList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }
        reportAdapter.updateData(filtered)
        binding.tvEmpty.visibility =
            if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationHelper.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GPS diberikan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}