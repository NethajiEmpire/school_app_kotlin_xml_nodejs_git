package com.lms.sch.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.lms.sch.R

class PdfViewerAdapter(private val pdfPages: List<Bitmap>) : PagerAdapter() {

    override fun getCount(): Int {
        return pdfPages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val imageView = LayoutInflater.from(context).inflate(R.layout.item_pdf_view, container, false) as ImageView
        imageView.setImageBitmap(pdfPages[position])
        container.addView(imageView)

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}