package ru.dw.astronomypictureoftheday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.dw.astronomypictureoftheday.R
import ru.dw.astronomypictureoftheday.ui.list.ListPhotosDayNasaFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListPhotosDayNasaFragment.newInstance()).commit()
        }
    }
}