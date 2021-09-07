package com.ji.infiniteviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import com.ji.infiniteviewpager.adapter.InfiniteAdapter

typealias onBind<T> = (T, View) -> Unit
typealias onDiff<T> = (T, T) -> Boolean

class InfiniteViewPager : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    
    private var goBackAnim = false
    private var adapter: InfiniteAdapter<*>? = null
    private var viewPager: ViewPager2? = null
    
    fun <T> set(@LayoutRes id: Int, data: List<T>, onBind: onBind<T>, onDiff: onDiff<T>, goBackAnim: Boolean = false) {
        this.goBackAnim = goBackAnim
        adapter = InfiniteAdapter(id, onBind, onDiff).apply { submitList(data.toMutableList()) }
        viewPager?.run {
            adapter = this@InfiniteViewPager.adapter
            currentItem = 1
        }
    }
    
    fun <T> update(list: List<T>){
        (adapter as? InfiniteAdapter<T>)?.submitList(list.toMutableList())
    }
    
    init {
        val root = inflate(context, R.layout.viewpager_view, null)
        viewPager = root.findViewById(R.id.viewPager)
        viewPager?.let {
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
    
                    val itemCount = adapter?.itemCount ?: 0
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        when (it.currentItem) {
                            itemCount - 1 -> it.setCurrentItem(1, goBackAnim)
                            0 -> it.setCurrentItem(itemCount - 2, goBackAnim)
                        }
                    }
                    
                }
     
            })
        }
        addView(root)
    }
}