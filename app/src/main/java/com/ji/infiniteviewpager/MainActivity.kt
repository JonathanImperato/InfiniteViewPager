package com.ji.infiniteviewpager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf("Item 1", "Item 2", "Item 3")
        val vp = findViewById<InfiniteViewPager>(R.id.infiniteViewPager).apply {
            set(R.layout.viewpager_item, list, { item, view ->
                view.findViewById<TextView>(R.id.text).text = item
            }, { f, s ->
                return@set f == s
            })
        }
        
        Handler(Looper.getMainLooper()).postDelayed(6000) {
            vp.update(list.apply {
                add("Item 4")
                add("Item 5")
            })
        }
    }
}