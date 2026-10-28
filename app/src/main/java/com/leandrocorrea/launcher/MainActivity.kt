package com.leandrocorrea.launcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leandrocorrea.launcher.data.ThemeManager
import com.leandrocorrea.launcher.ui.CreatorDialog
import com.leandrocorrea.launcher.ui.ThemeStoreDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var themeManager: ThemeManager
    private val clockHandler = Handler(Looper.getMainLooper())
    private lateinit var tvClock: TextView
    private lateinit var tvDate: TextView
    private lateinit var rootLayout: View

    private val clockRunnable = object : Runnable {
        override fun run() {
            updateClock()
            clockHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(android.R.id.content)
        tvClock = findViewById(R.id.tvHeaderClock)
        tvDate = findViewById(R.id.tvHeaderDate)

        // Inicializa o gerenciador de temas e aplica a cor salva
        themeManager = ThemeManager(this)
        applyCurrentTheme()

        // 1. Crédito do Criador Leandro Corrêa
        findViewById<View>(R.id.containerCreatorBadge).setOnClickListener {
            CreatorDialog(this).show()
        }

        // 2. Botão Ajustes (Abre a Loja de Temas em 1 Clique)
        findViewById<View>(R.id.menuConfig).setOnClickListener {
            ThemeStoreDialog(this, themeManager) {
                applyCurrentTheme()
            }.show()
        }

        // 3. Botões do Topo
        findViewById<View>(R.id.btnMute).setOnClickListener {
            Toast.makeText(this, "Modo Silencioso Alternado", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnTvMode).setOnClickListener {
            Toast.makeText(this, "Modo Controle TV Ativo", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnMobileMode).setOnClickListener {
            Toast.makeText(this, "Modo Celular (Touch) Ativo", Toast.LENGTH_SHORT).show()
        }

        // 4. Lançadores de Aplicativos
        setupAppLauncher(R.id.cardYouTube, "com.google.android.youtube.tv", "YouTube")
        setupAppLauncher(R.id.cardFlixCine, "com.netflix.ninja", "FlixCine")
        setupAppLauncher(R.id.cardTclChannel, "com.tcl.channel", "TCL Channel")

        // 5. Entradas Rápidas HDMI
        setupHdmi(R.id.btnHdmi1, "HDMI 1")
        setupHdmi(R.id.btnHdmi2, "HDMI 2 (eARC)")
        setupHdmi(R.id.btnHdmi3, "HDMI 3")

        startClock()
    }

    private fun applyCurrentTheme() {
        val theme = themeManager.getCurrentTheme()
        rootLayout.setBackgroundColor(theme.primaryBg)
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
