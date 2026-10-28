package com.leandrocorrea.launcher.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val rvThemes = findViewById<RecyclerView>(R.id.rvThemesList)
        val btnClose = findViewById<Button>(R.id.btnCloseThemeStore)

        rvThemes?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvThemes?.adapter = ThemeAdapter(themeManager.builtInThemes) { selectedTheme ->
            themeManager.saveTheme(selectedTheme.id)
            onThemeApplied(selectedTheme)
            Toast.makeText(context, "Tema '${selectedTheme.name}' instalado com sucesso!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        btnClose?.setOnClickListener { dismiss() }
    }

    private class ThemeAdapter(
        private val list: List<ThemeModel>,
        private val onSelect: (ThemeModel) -> Unit
    ) : RecyclerView.Adapter<ThemeAdapter.VH>() {

        class VH(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.themeTitle)
            val preview: View = view.findViewById(R.id.themePreviewBox)
            val btnApply: TextView = view.findViewById(R.id.btnApplyTheme)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme_card, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val theme = list[position]
            holder.title.text = theme.name
            holder.preview.setBackgroundColor(theme.primaryBg)
            holder.btnApply.setOnClickListener { onSelect(theme) }
            holder.itemView.setOnClickListener { onSelect(theme) }
        }

        override fun getItemCount() = list.size
    }
}
