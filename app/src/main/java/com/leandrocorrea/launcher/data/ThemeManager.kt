package com.leandrocorrea.launcher.data

import android.content.Context
import android.graphics.Color

data class ThemeModel(
    val id: String,
    val name: String,
    val primaryBg: Int,
    val accentColor: Int,
    val cardColor: Int,
    val isOledPureBlack: Boolean = true,
    val previewGradient: String = ""
)

class ThemeManager(context: Context) {
    private val prefs = context.getSharedPreferences("launcher_theme_prefs", Context.MODE_PRIVATE)

    val builtInThemes = listOf(
        ThemeModel("cyan", "Obsidian Cyan (Padrão)", Color.parseColor("#070B14"), Color.parseColor("#00E5FF"), Color.parseColor("#121927")),
        ThemeModel("crimson", "Cyberpunk Crimson", Color.parseColor("#0A050B"), Color.parseColor("#FF0055"), Color.parseColor("#1C0F18")),
        ThemeModel("gold", "Midnight Gold Lux", Color.parseColor("#0B0B0C"), Color.parseColor("#FFD700"), Color.parseColor("#1C1A16")),
        ThemeModel("emerald", "Emerald Matrix OLED", Color.parseColor("#000000"), Color.parseColor("#00E676"), Color.parseColor("#0B1710")),
        ThemeModel("nebula", "Deep Space Nebula", Color.parseColor("#060814"), Color.parseColor("#A855F7"), Color.parseColor("#13172E"))
    )

    fun getCurrentTheme(): ThemeModel {
        val savedId = prefs.getString("current_theme_id", "cyan") ?: "cyan"
        return builtInThemes.find { it.id == savedId } ?: builtInThemes[0]
    }

    fun saveTheme(themeId: String) {
        prefs.edit().putString("current_theme_id", themeId).apply()
    }
}
