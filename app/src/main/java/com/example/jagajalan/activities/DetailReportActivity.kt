package com.example.jagajalan.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jagajalan.R
import com.example.jagajalan.databinding.ActivityDetailReportBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailReportActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailReportBinding
    private lateinit var googleMap: GoogleMap

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var reportTitle: String = ""

    companion object {
        const val EXTRA_REPORT_ID = "extra_report_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_STATUS = "extra_status"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_TIMESTAMP = "extra_timestamp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Ambil data dari Intent
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        val status = intent.getStringExtra(EXTRA_STATUS) ?: "Pending"
        val userName = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, 0L)
        latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        reportTitle = title

        // Isi data ke UI
        binding.tvTitle.text = title
        binding.tvDescription.text = description
        val sensoredEmail = if (userName.contains("@")) {
            val local = userName.substringBefore("@")
            val domain = userName.substringAfter("@")
            val censored = local.first() + "***" + local.last()
            "$censored@$domain"
        } else userName
        binding.tvUserName.text = sensoredEmail
        binding.tvLocation.text = "Lat: $latitude, Lng: $longitude"

        // Format waktu
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        binding.tvTime.text = sdf.format(Date(timestamp))

        // Status badge
        binding.tvStatus.text = status
        when (status) {
            "Pending" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))
            "Proses"  -> binding.tvStatus.setBackgroundColor(Color.parseColor("#2196F3"))
            "Selesai" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"))
            else      -> binding.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))
        }

        // Load foto
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_camera_placeholder)
                .into(binding.ivPhoto)
        }

        // Setup Google Maps
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(reportTitle)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}