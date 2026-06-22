package com.example.jagajalan.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jagajalan.R
import com.example.jagajalan.adapters.AdminReportAdapter
import com.example.jagajalan.models.Report
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ManageReportsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminReportAdapter

    private val db = FirebaseFirestore.getInstance()
    private val reportList = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_manage_reports)

        recyclerView =
            findViewById(R.id.recyclerReports)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter = AdminReportAdapter(

            reportList,

            onUpdateClick = { report, status ->

                db.collection("laporan")
                    .document(report.id)
                    .update("status", status)
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Status berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },

            onDeleteClick = { report ->

                db.collection("laporan")
                    .document(report.id)
                    .delete()
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Laporan berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },

            onItemClick = { report ->

                val intent = Intent(
                    this,
                    AdminDetailReportActivity::class.java
                )

                intent.putExtra(
                    "title",
                    report.title
                )

                intent.putExtra(
                    "description",
                    report.description
                )

                intent.putExtra(
                    "imageUrl",
                    report.imageUrl
                )

                intent.putExtra(
                    "status",
                    report.status
                )

                intent.putExtra(
                    "user",
                    report.userName
                )

                intent.putExtra(
                    "lat",
                    report.latitude
                )

                intent.putExtra(
                    "lng",
                    report.longitude
                )

                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter

        loadReports()
    }

    private fun loadReports() {

        db.collection("laporan")
            .orderBy(
                "timestamp",
                Query.Direction.DESCENDING
            )
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

                reportList.clear()

                snapshot?.documents?.forEach { doc ->

                    val report =
                        doc.toObject(Report::class.java)

                    if (report != null) {

                        reportList.add(
                            report.copy(
                                id = doc.id
                            )
                        )
                    }
                }

                adapter.updateData(reportList)
            }
    }

}
