package ru.pdc.app

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectFiles: MaterialButton
    private lateinit var btnStart: MaterialButton
    private lateinit var tvSelectedCount: TextView
    private lateinit var rvFiles: RecyclerView

    private val selectedUris = mutableListOf<Uri>()
    private lateinit var adapter: FilesAdapter

    // Выбор нескольких файлов
    private val openDocuments = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedUris.clear()
            selectedUris.addAll(uris)
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим элементы
        btnSelectFiles = findViewById(R.id.btnSelectFiles)
        btnStart = findViewById(R.id.btnStart)
        tvSelectedCount = findViewById(R.id.tvSelectedCount)
        rvFiles = findViewById(R.id.rvFiles)

        // Настраиваем список
        adapter = FilesAdapter(selectedUris)
        rvFiles.layoutManager = LinearLayoutManager(this)
        rvFiles.adapter = adapter

        // Кнопка выбора файлов
        btnSelectFiles.setOnClickListener {
            openDocuments.launch(
                arrayOf(
                    "application/msword", // .doc
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // .docx
                )
            )
        }

        // Кнопка "Начать обработку" (пока просто заглушка)
        btnStart.setOnClickListener {
            // TODO: здесь позже будет запуск обработки
            // startProcessing(selectedUris)
        }
    }

    private fun updateUI() {
        if (selectedUris.isEmpty()) {
            tvSelectedCount.text = "Файлы ещё не выбраны"
            btnStart.isEnabled = false
        } else {
            tvSelectedCount.text = "Выбрано файлов: ${selectedUris.size}"
            btnStart.isEnabled = true
        }
        adapter.notifyDataSetChanged()
    }
}