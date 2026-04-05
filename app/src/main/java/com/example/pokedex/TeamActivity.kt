package com.example.pokedex

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class TeamActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var  txtTeamMessage: TextView
    private lateinit var listTeam: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        toolbar = findViewById(R.id.toolbar)
        txtTeamMessage = findViewById(R.id.txtTeamMessage)
        listTeam = findViewById(R.id.listTeam)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}