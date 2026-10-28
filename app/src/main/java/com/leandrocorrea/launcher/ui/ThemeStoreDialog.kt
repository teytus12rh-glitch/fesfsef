package com.leandrocorrea.launcher.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.leandrocorrea.launcher.R
import com.leandrocorrea.launcher.data.ThemeManager
import com.leandrocorrea.launcher.data.ThemeModel

class ThemeStoreDialog(
    context: Context,
    private val themeManager: ThemeManager,
    private val onThemeApplied: (ThemeModel) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_theme_store)

        // Liga os cliques nos 5 temas
        setupThemeButton(R.id.btnThemeCyan, "cyan")
        setupThemeButton(R.id.btnThemeCrimson, "crimson")
        setupThemeButton(R.id.btnThemeGold, "gold")
        setupThemeButton(R.id.btnThemeEmerald, "emerald")
        setupThemeButton(R.id.btnThemeNebula, "nebula")

        findViewById<Button>(R.id.btnCloseThemeStore)?.setOnClickListener { dismiss() }
    }

    private fun setupThemeButton(viewId: Int, themeId: String) {
        findViewById<View>(viewId)?.setOnClickListener {
            val theme = themeManager.builtInThemes.find { it.id == themeId } ?: return@setOnClickListener
            themeManager.saveTheme(theme.id)
            onThemeApplied(theme)
            Toast.makeText(context, "Tema '${theme.name}' instalado com sucesso!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
