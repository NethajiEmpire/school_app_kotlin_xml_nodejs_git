package com.lms.sch.activity

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.lms.sch.adapter.PdfViewerAdapter
import com.lms.sch.databinding.ActivityPdfViewerBinding
import com.lms.sch.session.Constants
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.*
import java.io.File
import java.io.IOException

class PdfViewerActivity : AppCompatActivity() {
    lateinit var binding: ActivityPdfViewerBinding
    lateinit var pdfRenderer: PdfRenderer
    lateinit var fileDescriptor: ParcelFileDescriptor
    lateinit var pagerAdapter: PdfViewerAdapter
    var pdfPages = mutableListOf<Bitmap>()
    var docUrl :String ?= ""
    private lateinit var pageCounter: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        docUrl = intent.getStringExtra(Constants.IntentKeys.KEY)
        UiUtils.log("sdhygfdc",docUrl)
        DialogUtils.showLoader(this)
        downloadAndSavePdf(docUrl!!)
        pageCounter = binding.pageCount

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                updatePageCounter(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun downloadAndSavePdf(docUrl: String) {
        val file = File(filesDir, "downloaded_pdf.pdf")

        if (file.exists()) {
            file.delete()
        }
        val client = OkHttpClient()
        val request = Request.Builder().url(docUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    UiUtils.showSnack("Failed to download PDF", binding.root,false)
                }
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val outputStream = file.outputStream()
                        inputStream.copyTo(outputStream)
                        outputStream.close()

                        runOnUiThread {
                            openPdf(file)
                        }
                    }
                } else {
                    runOnUiThread {
                        UiUtils.showSnack("Failed to download PDF", binding.root,false)
                    }
                }
            }
        })
    }
    private fun openPdf(file: File) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
            loadPdfPages()
            runOnUiThread {
                pagerAdapter = PdfViewerAdapter(pdfPages)
                binding.viewPager.adapter = pagerAdapter
                updatePageCounter(0)
                DialogUtils.dismissLoader()
            }
        } catch (e: Exception) {
            DialogUtils.dismissLoader()
            Log.e("PDFViewer", "Error opening PDF", e)
            UiUtils.showSnack("Error opening PDF", binding.root,false)
        }
    }
    private fun loadPdfPages() {
        for (i in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(i)

            val scale = 2.0f
            val width = (page.width * scale).toInt()
            val height = (page.height * scale).toInt()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val renderRect = Rect(0, 0, width, height)

            page.render(bitmap, renderRect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfPages.add(bitmap)
            page.close()
        }
    }

    private fun updatePageCounter(position: Int) {
        pageCounter.text = "${position + 1} / ${pdfPages.size}"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::pdfRenderer.isInitialized) {
            pdfRenderer.close()
        }
        if (::fileDescriptor.isInitialized) {
            fileDescriptor.close()
        }
        for (bitmap in pdfPages) {
            bitmap.recycle()
        }
    }

}