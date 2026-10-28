package com.leandrocorrea.launcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leandrocorrea.launcher.ui.CreatorDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val clockHandler = Handler(Looper.getMainLooper())
    private lateinit var tvClock: TextView
    private lateinit var tvDate: TextView

    private val clockRunnable = object : Runnable {
        override fun run() {
            updateClock()
            clockHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvClock = findViewById(R.id.tvHeaderClock)
        tvDate = findViewById(R.id.tvHeaderDate)

        // Crédito do Criador Leandro Corrêa
        findViewById<View>(R.id.containerCreatorBadge).setOnClickListener {
            CreatorDialog(this).show()
        }

        // Modos
        findViewById<View>(R.id.btnMute).setOnClickListener {
            Toast.makeText(this, "Modo Silencioso Alternado", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnTvMode).setOnClickListener {
            Toast.makeText(this, "Modo Controle TV Ativo", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnMobileMode).setOnClickListener {
            Toast.makeText(this, "Modo Celular (Touch) Ativo", Toast.LENGTH_SHORT).show()
        }

        // Lançador de Apps
        setupAppLauncher(R.id.cardYouTube, "com.google.android.youtube.tv", "YouTube")
        setupAppLauncher(R.id.cardFlixCine, "com.netflix.ninja", "FlixCine")
        setupAppLauncher(R.id.cardTclChannel, "com.tcl.channel", "TCL Channel")

        // Botões HDMI
        setupHdmi(R.id.btnHdmi1, "HDMI 1")
        setupHdmi(R.id.btnHdmi2, "HDMI 2 (eARC)")
        setupHdmi(R.id.btnHdmi3, "HDMI 3")

        startClock()
    }

    private fun setupAppLauncher(viewId: Int, pkg: String, name: String) {
        findViewById<View>(viewId)?.setOnClickListener {
            val intent = packageManager.getLeanbackLaunchIntentForPackage(pkg)
                ?: packageManager.getLaunchIntentForPackage(pkg)
            if (intent != null) startActivity(intent)
            else Toast.makeText(this, "$name não instalado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHdmi(viewId: Int, label: String) {
        findViewById<View>(viewId)?.setOnClickListener {
            Toast.makeText(this, "Alternando para $label...", Toast.LENGTH_SHORT).show()
            try { startActivity(Intent("android.intent.action.VIEW_INPUTS")) } catch (_: Exception) {}
        }
    }

    private fun updateClock() {
        tvClock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val day = SimpleDateFormat("EEEE", Locale("pt", "BR")).format(Date()).replaceFirstChar { it.uppercase() }
        val date = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")).format(Date())
        tvDate.text = "$day\n$date"
    }

    private fun startClock() {
        clockHandler.post(clockRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        clockHandler.removeCallbacks(clockRunnable)
    }
}
