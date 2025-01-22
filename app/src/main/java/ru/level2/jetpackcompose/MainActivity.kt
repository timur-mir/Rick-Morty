package ru.level2.jetpackcompose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import ru.level2.jetpackcompose.databinding.ActivityMainBinding
import ru.level2.jetpackcompose.presentation.CharacterFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportFragmentManager.commit {
            replace(R.id.container, CharacterFragment())
        }
    }
}