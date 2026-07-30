package com.lms.sch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lms.sch.BuildConfig
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.interfaces.AnimationCallBack
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.session.Constants
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


object UiUtils {

    @Suppress("DEPRECATION")
    fun convertHtml(text : String): String {
        return if(text.isNotEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val res = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                res.toString()
            } else {
                val res = Html.fromHtml(text)
                res.toString()
            }
        } else{
            text
        }
    }
    fun animation(context: Context,view: View,type: Int,visible: Boolean){
        val anim: Animation = AnimationUtils.loadAnimation(context,type)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if(visible){
                    view.alpha = 1F
                    view.visibility = View.VISIBLE
                }
                else{
                    // view.alpha = 1F
                    view.visibility = View.GONE
                }
            }
            override fun onAnimationRepeat(p0: Animation?) {

            }
        })
        view.startAnimation(anim)
    }
    fun textViewGradient(textView: TextView, startColor: String, endColor: String) {
        val startColorInt = Color.parseColor(startColor)
        val endColorInt = Color.parseColor(endColor)

        val textWidth = textView.paint.measureText(textView.text.toString())
        val shader = LinearGradient(
            0f, 0f, textWidth, 0f,
            intArrayOf(startColorInt, endColorInt),
            null,
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader
    }
    fun getColorFromEventName(eventName: String): Int {
        val colors = listOf(
            Color.parseColor("#0000FF"),  // Blue
            Color.parseColor("#008000"),  // Green
            Color.parseColor("#FF0000")   // Red
        )
        val index = abs(eventName.hashCode()) % colors.size
        return colors[index]
    }

    fun getOrdinalSuffix(number: Int): String {
        return when {
            number % 100 in 11..13 -> "${number}th Std"
            else -> when (number % 10) {
                1 -> "${number}st Std"
                2 -> "${number}nd Std"
                3 -> "${number}rd Std"
                else -> "${number}th Std"
            }
        }
    }
    fun animationWithCallback(context: Context, view: View, type: Int, callBack: AnimationCallBack){
        val anim: Animation = AnimationUtils.loadAnimation(context,type)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                callBack.onAnimationStart()
            }

            override fun onAnimationEnd(p0: Animation?) {
                callBack.onAnimationEnd()
            }
            override fun onAnimationRepeat(p0: Animation?) {
                callBack.onAnimationRepeat()
            }
        })
        view.startAnimation(anim)
    }
    fun showSnack(content: String, context: Context) {
        val view = (context as BaseActivity).view
        BaseUtils.hideForceKeyboard(view)
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show()
    }
    fun showSnack(content: String, view: View) {
        BaseUtils.hideForceKeyboard(view)
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show()
    }
    fun showSnack(view: View, content: String,onClickListener: OnClickListener){
        var isUndo = false
        Snackbar.make(view, content, Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                isUndo = true
                onClickListener.onClickItem(0)
            }
            .addCallback(object : Callback() {
                override fun onShown(sb: Snackbar?) {

                }

                override fun onDismissed(transientBottomBar: Snackbar?, @DismissEvent event: Int) {
                    if(!isUndo){
                        onClickListener.onClickItem(1)
                    }
                }
            }).show()

    }

    @SuppressLint("RestrictedApi")
    fun showSnack(content: String, view: View, isSuccess: Boolean) {
        BaseUtils.hideForceKeyboard(view)
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        val customView = LayoutInflater.from(view.context).inflate(R.layout.custom_snackbar, null)
        val snackbarIcon = customView.findViewById<ImageView>(R.id.icons)
        val snackbarText = customView.findViewById<TextView>(R.id.s_txt)
        var snackbarText1 = customView.findViewById<TextView>(R.id.s_status)
        if (content.isNotEmpty()){
            snackbarText.text = content
        } else {
            snackbarText.text = "Something went Wrong"
        }
        if (isSuccess) {
            snackbarText1.text = "Success"
            imageviewDrawable(snackbarIcon,R.drawable.success_snack)
            val linearLayoutCustomView = customView as LinearLayout
            linearLayoutBgTint(linearLayoutCustomView, null, R.color.green2)
        } else {
            snackbarText1.text = "Failed"
            imageviewDrawable(snackbarIcon,R.drawable.snack_error)
            val linearLayoutCustomView = customView as LinearLayout
            linearLayoutBgTint(linearLayoutCustomView, null, R.color.red2)
        }
        customView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.layoutParams = (snackbarLayout.layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(26, 0, 26, 50)
        }
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(customView, 0)
//        animation(view.context,snackbarLayout,R.anim.slide_in_from_bottom,true)
        snackbar.show()
    }

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        if (message.isNotEmpty()) {
            (context as BaseActivity).mToast?.cancel()
            context.mToast = Toast.makeText(context, message, duration)
            context.mToast!!.show()
        }
    }

    fun dismiss(context: Context) {
        (context as BaseActivity).mToast?.cancel()
    }
    fun showToast(context: Context,content: String) {
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show()
    }
    fun showLog(TAG: String, content: String) {
        Log.d(TAG, content)
    }
    fun log(TAG: String?, content: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, content!!)
        }
    }
    fun loadCustomImage(imageView: ImageView?, imageUrl: String?){
        if (imageUrl == null || imageView == null) {
            return
        }
        else{
            //imageView.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(imageView.context)
                .load(imageUrl)
                .apply(
                    RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.color.hint_color2)
                )
                .into(imageView)
        }
    }
    fun loadCustomImage(imageView: ImageView?, file: File?){
        if (imageView == null || file == null) {
            return
        }
        else{
            //imageView.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(imageView.context)
                .load(file)
                .apply(
                    RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.color.hint_color2)
                )
                .into(imageView)
        }
    }

    fun loadImage(imageView: ImageView?, imageUrl: String?) {
        if(BaseUtils.nullCheckerStr(imageUrl).isNotEmpty() && imageView != null && imageUrl!!.contains("http")){
            if(imageUrl.substring(imageUrl.lastIndexOf('.') + 1) == ".svg"){
                GlideToVectorYou.init()
                    .with(imageView.context)
                    .setPlaceHolder(R.color.hint_color2, R.mipmap.ic_launcher)
                    .load(Uri.parse(imageUrl), imageView)
            }
            else{
               /* Glide.with(imageView.context)
                    .load(imageUrl)
                    .apply(RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.color.hint_color2))
                    .into(imageView)*/
                Picasso.get().load(imageUrl)
                    .placeholder(R.color.hint_color2)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView)
            }
        }
        else if (imageView != null) {
            UiUtils.imageviewDrawable(imageView,R.color.hint_color2)
        }
        else{
            return
        }
    }
    fun loadImageWithCenterCrop(imageView: ImageView?, imageUrl: String?){
        if(BaseUtils.nullCheckerStr(imageUrl).isNotEmpty() && imageView != null && imageUrl!!.contains("http")){
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(imageView.context)
                .load(imageUrl)
                .apply(RequestOptions().error(R.mipmap.ic_launcher).placeholder(R.color.hint_color2))
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageViewBgColor(imageView,null,R.color.colorPrimary)
                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // imageView.scaleType = ImageView.ScaleType.FIT_XY
                        return false
                    }

                })
                .into(imageView)
        }
        else if (imageView != null) {
            UiUtils.imageviewDrawable(imageView,R.color.hint_color2)
        }
        else{
            return
        }

    }
    fun getNavIconColorStates(color: String): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(Color.parseColor("#ffffff"), Color.parseColor(color))
        )
    }
    fun getAngleDrawable(context: Context, solidColor: Int, _radius: FloatArray?, strokeWidth: Int, strokeColor: String): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.color = ColorStateList.valueOf(context.getColor(solidColor))
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.cornerRadii = _radius
        gradientDrawable.setStroke(strokeWidth, ColorStateList.valueOf(Color.parseColor(strokeColor)))
        return gradientDrawable
    }
    fun editTextColor(edittext: TextInputLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                edittext.boxStrokeColor = Color.parseColor(colorStr)
                edittext.hintTextColor = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                edittext.boxStrokeColor = edittext.context.getColor(colorInt)
                edittext.hintTextColor = ColorStateList.valueOf(edittext.context.getColor(colorInt))
            }
            else -> {
                edittext.boxStrokeColor = edittext.context.getColor(R.color.colorPrimary)
                edittext.hintTextColor = ColorStateList.valueOf(edittext.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun setTextViewDrawableColor(textView: TextView,colorStr: String?, colorInt: Int?) {
        for (drawable in textView.compoundDrawablesRelative) {
            if (drawable != null) {
                when {
                    colorStr != null -> {
                        // textView.compoundDrawables[0].setTint(Color.parseColor(colorStr))
                        drawable.colorFilter = PorterDuffColorFilter(Color.parseColor(colorStr), PorterDuff.Mode.SRC_IN)
                    }
                    colorInt != null -> {
                        // textView.compoundDrawables[0].setTint(textView.context.resources.getColor(colorInt))
                        drawable.colorFilter = PorterDuffColorFilter(textView.context.getColor(colorInt), PorterDuff.Mode.SRC_IN)
                    }
                    else -> {
                        // textView.compoundDrawables[0].setTint(textView.context.resources.getColor(R.color.black))
                        drawable.colorFilter = PorterDuffColorFilter(textView.context.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                    }
                }
            }
        }
    }
    fun radioButtonTint(radioButton: RadioButton, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                radioButton.buttonTintList = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                radioButton.buttonTintList = ColorStateList.valueOf(radioButton.context.getColor(colorInt))
            }
            else -> {
                radioButton.buttonTintList = ColorStateList.valueOf(radioButton.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun checkBoxTint(checkBox: CheckBox, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                checkBox.buttonTintList = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                checkBox.buttonTintList = ColorStateList.valueOf(checkBox.context.getColor(colorInt))
            }
            else -> {
                checkBox.buttonTintList = ColorStateList.valueOf(checkBox.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun imageViewTint(imageView: ImageView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                imageView.imageTintList = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                imageView.imageTintList = ColorStateList.valueOf(imageView.context.getColor(colorInt))
            }
            else -> {
                imageView.imageTintList = ColorStateList.valueOf(imageView.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun cardViewBgColor(cardView: CardView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                cardView.setCardBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                cardView.setCardBackgroundColor(cardView.context.getColor(colorInt))
            }
            else -> {
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun buttonBgColor(button: Button, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                button.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                button.setBackgroundColor(button.context.getColor(colorInt))
            }
            else -> {
                button.setBackgroundColor(button.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun relativeLayoutBgColor(relativeLayout: RelativeLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                relativeLayout.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(colorInt))
            }
            else -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun constraintLayoutBgColor(relativeLayout: ConstraintLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                relativeLayout.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(colorInt))
            }
            else -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun coordinateLayoutBgColor(relativeLayout: CoordinatorLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                relativeLayout.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(colorInt))
            }
            else -> {
                relativeLayout.setBackgroundColor(relativeLayout.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun linearLayoutBgColor(linearLayout: LinearLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                linearLayout.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                linearLayout.setBackgroundColor(linearLayout.context.getColor(colorInt))
            }
            else -> {
                linearLayout.setBackgroundColor(linearLayout.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun imageViewBgColor(imageView: ImageView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                imageView.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                imageView.setBackgroundColor(imageView.context.getColor(colorInt))
            }
            else -> {
                imageView.setBackgroundColor(imageView.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun viewBgColor(view: View, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                view.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                view.setBackgroundColor(view.context.getColor(colorInt))
            }
            else -> {
                view.setBackgroundColor(view.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun textViewBgColor(textView: TextView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                textView.setBackgroundColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                textView.setBackgroundColor(textView.context.getColor(colorInt))
            }
            else -> {
                textView.setBackgroundColor(textView.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun textViewBgTint(textView: TextView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                textView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colorStr), BlendModeCompat.SRC_ATOP)
            }
            colorInt != null -> {
                textView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(textView.context.getColor(colorInt), BlendModeCompat.SRC_ATOP)
            }
            else -> {
                textView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(textView.context.getColor(R.color.colorPrimary), BlendModeCompat.SRC_ATOP)
            }
        }
    }
    fun buttonBgTint(button: Button, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                button.backgroundTintList = ColorStateList.valueOf(button.context.getColor(colorInt))
            }
            else -> {
                button.backgroundTintList = ColorStateList.valueOf(button.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun cardViewBgTint(cardView: CardView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                cardView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colorStr), BlendModeCompat.SRC_ATOP)
            }
            colorInt != null -> {
                cardView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(cardView.context.getColor(colorInt), BlendModeCompat.SRC_ATOP)
            }
            else -> {
                cardView.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(cardView.context.getColor(R.color.colorPrimary), BlendModeCompat.SRC_ATOP)
            }
        }
    }
    fun linearLayoutBgTint(linearLayout: LinearLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colorStr), BlendModeCompat.SRC_ATOP)
            }
            colorInt != null -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(linearLayout.context.getColor(colorInt), BlendModeCompat.SRC_ATOP)
            }
            else -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(linearLayout.context.getColor(R.color.colorPrimary), BlendModeCompat.SRC_ATOP)
            }
        }
    }
    fun constraintLayoutBgTint(linearLayout: ConstraintLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colorStr), BlendModeCompat.SRC_ATOP)
            }
            colorInt != null -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(linearLayout.context.getColor(colorInt), BlendModeCompat.SRC_ATOP)
            }
            else -> {
                linearLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(linearLayout.context.getColor(R.color.colorPrimary), BlendModeCompat.SRC_ATOP)
            }
        }
    }
    fun relativeLayoutBgTint(relativeLayout: RelativeLayout, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                relativeLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(colorStr), BlendModeCompat.SRC_ATOP)
            }
            colorInt != null -> {
                relativeLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(relativeLayout.context.getColor(colorInt), BlendModeCompat.SRC_ATOP)
            }
            else -> {
                relativeLayout.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(relativeLayout.context.getColor(R.color.colorPrimary), BlendModeCompat.SRC_ATOP)
            }
        }
    }
    fun editTextBgDrawable(editText: EditText,drawable: Int){
        editText.background = AppCompatResources.getDrawable(editText.context, drawable)
    }
    fun linearLayoutBgDrawable(linearLayout: LinearLayout,drawable: Int){
        linearLayout.background = AppCompatResources.getDrawable(linearLayout.context, drawable)
    }
    fun constraintLayoutBgDrawable(constraintLayout: ConstraintLayout,drawable: Int){
        constraintLayout.background = AppCompatResources.getDrawable(constraintLayout.context, drawable)
    }
    fun relativeLayoutBgDrawable(linearLayout: RelativeLayout,drawable: Int){
        linearLayout.background = AppCompatResources.getDrawable(linearLayout.context, drawable)
    }
    fun viewBgDrawable(constraintLayout: View,drawable: Int){
        constraintLayout.background = AppCompatResources.getDrawable(constraintLayout.context, drawable)
    }

    fun recyclerViewBgDrawable(recyclerView: RecyclerView,drawable: Int){
        recyclerView.background = AppCompatResources.getDrawable(recyclerView.context, drawable)
    }
    fun imageviewDrawable(imageView: ImageView, drawable: Int) {
        imageView.setImageDrawable(AppCompatResources.getDrawable(imageView.context, drawable))
    }
    fun textviewCustomDrawable(textView: TextView, drawable: Int){
        textView.background = AppCompatResources.getDrawable(textView.context,drawable)
    }
    fun textviewImgDrawable(textView: TextView, drawable: Int?, position:String){
        if(drawable != null){
            if(position == Constants.IntentKeys.START){
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(textView.context,drawable),
                    null,
                    null,
                    null)
            }
            else if(position == Constants.IntentKeys.END){
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(textView.context,drawable),
                    null)
            }
        }
        else{
            // textView.setCompoundDrawables(null,null,null,null)
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null)
        }
    }
    fun editTextImgDrawable(textView: EditText, drawable: Int?, position:String){
        if(drawable != null){
            if(position == Constants.IntentKeys.START){
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(textView.context,drawable),
                    null,
                    null,
                    null)
            }
            else if(position == Constants.IntentKeys.END){
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(textView.context,drawable),
                    null)
            }
        }
        else{
            // textView.setCompoundDrawables(null,null,null,null)
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null)
        }
    }
    fun notificationBar(activity: Activity, colorStr: String?, colorInt: Int?){
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        when {
            colorStr != null -> {
                window.statusBarColor = Color.parseColor(colorStr)
            }
            colorInt != null -> {
                window.statusBarColor = activity.getColor(colorInt)
            }
            else -> {
                window.statusBarColor = activity.getColor(R.color.colorPrimary)
            }
        }
    }
    fun notificationBarTrans(activity: Activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //   activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    fun notificationBarCollapsing(activity: Activity,rgb: String?,colorStr: String?, colorInt: Int?){
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        when {
            colorStr != null -> {
                activity.window.statusBarColor = Color.parseColor(colorStr)
            }
            colorInt != null -> {
                activity.window.statusBarColor = activity.getColor(colorInt)
            }
            rgb != null -> {
                activity.window.statusBarColor = rgb.toInt()
            }
        }
    }
    fun fullScreenMode(activity: Activity,value:Boolean){
        if(value){
            // Hide the status bar.
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else{
            // activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        }
    }
    fun plusMinus(button: MaterialButton){
        button.iconTint = ColorStateList.valueOf(button.context.getColor(R.color.colorPrimary))
        button.strokeColor = ColorStateList.valueOf(button.context.getColor(R.color.colorPrimary))
    }
    fun textViewTextColor(textView: TextView, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                textView.setTextColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                textView.setTextColor(textView.context.getColor(colorInt))
            }
            else -> {
                textView.setTextColor(textView.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun btnTextColor(button: Button, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                button.setTextColor(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                button.setTextColor(button.context.getColor(colorInt))
            }
            else -> {
                button.setTextColor(button.context.getColor(R.color.colorPrimary))
            }
        }
    }
    fun btnStrokeColor(button: MaterialButton, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                button.strokeColor = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                button.strokeColor = ColorStateList.valueOf(button.context.resources.getColor(colorInt,null))
            }
            else -> {
                button.strokeColor = ColorStateList.valueOf(button.context.resources.getColor(R.color.colorPrimary,null))
            }
        }
    }
    fun viewBgTint(view: View, colorStr: String?, colorInt: Int?){
        when {
            colorStr != null -> {
                view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorStr))
            }
            colorInt != null -> {
                view.backgroundTintList = ColorStateList.valueOf(view.context.resources.getColor(colorInt,null))
            }
            else -> {
                view.backgroundTintList = ColorStateList.valueOf(view.context.resources.getColor(R.color.hint_color2,null))
            }
        }
    }
    fun formattedValues(value: String?): String {
        return if (value != null && !value.equals("", ignoreCase = true) && !BaseUtils.isContainsChar(value)) java.lang.String.format(
            Locale.ENGLISH,
            "%.2f",
            value.toDouble()
        ) else Constants.IntentKeys.AMOUNT_DUMMY
    }
    fun stringCon(arrayOf: Array<String>): String{
        when (arrayOf.size) {
            1 -> {
                return String.format(Locale.ENGLISH,"%s",arrayOf[0])
            }
            2 -> {
                return String.format(Locale.ENGLISH,"%s %s", arrayOf[0], arrayOf[1])
            }
            3 -> {
                return String.format(Locale.ENGLISH,"%s %s %s", arrayOf[0], arrayOf[1],arrayOf[2])
            }
            4 -> {
                return String.format(Locale.ENGLISH,"%s %s %s %s", arrayOf[0], arrayOf[1],arrayOf[2],arrayOf[3])
            }
            5 -> {
                return String.format(Locale.ENGLISH,"%s %s %s %s %s", arrayOf[0], arrayOf[1],arrayOf[2],arrayOf[3],arrayOf[4])
            }
            else -> {
                return ""
            }
        }
    }
    fun removeSpace(string: String):String{
        return string.replace(" ","").trim()
    }
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        //  return dp * (context.getResources().getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)
    }
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
    /* fun loadImageWithFitXY(imageView: ImageView?, imageUrl: String?){
         if (imageUrl == null || imageView == null || !imageUrl.contains("http")) {
             return
         }

         imageView.scaleType = ImageView.ScaleType.CENTER
         Glide.with(imageView.context)
             .load(imageUrl)
             .apply(RequestOptions().placeholder(R.drawable.banner_loader1).error(R.drawable.no_image))
             .listener(object: RequestListener<Drawable> {
                 override fun onLoadFailed(
                     e: GlideException?,
                     model: Any?,
                     target: Target<Drawable>?,
                     isFirstResource: Boolean
                 ): Boolean {
                     imageView.scaleType = ImageView.ScaleType.FIT_XY
                     return false
                 }

                 override fun onResourceReady(
                     resource: Drawable?,
                     model: Any?,
                     target: Target<Drawable>?,
                     dataSource: DataSource?,
                     isFirstResource: Boolean
                 ): Boolean {
                     imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                     return false
                 }

             })
             .into(imageView)

     }
     fun getSelectorDrawable(color: String): StateListDrawable {
         val colorList = ColorStateList.valueOf(Color.parseColor(color))
         val out = StateListDrawable()
         out.addState(intArrayOf(android.R.attr.state_pressed), createNormalDrawable(colorList))
         out.addState(intArrayOf(), createStrokeDrawable(colorList))
         out.addState(StateSet.WILD_CARD, createStrokeDrawable(colorList))
         return out
     }
     private fun createNormalDrawable(color: ColorStateList): GradientDrawable {
         val out = GradientDrawable()
         out.color = color
         return out
     }
     private fun createStrokeDrawable(color: ColorStateList): GradientDrawable {
         val out = GradientDrawable()
         out.setStroke(1, color)
         return out
     }
     fun convertStringStrike(text : String): String {
         val string: Spannable = SpannableString(text)
         string.setSpan(StrikethroughSpan(), 0, string.length, 0)
         string.setSpan(ForegroundColorSpan(Color.RED), 0, string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
         return string.toString()
     }
     fun formattedValues(value: Int): String? {
         return java.lang.String.format(Locale.ENGLISH, "%02d", value)
     }
     fun formattedValues(value: Double?): String? {
         return java.lang.String.format(Locale.ENGLISH, "%.2f", value)
     }
     fun getColoredAndBoldString(context: Context?, content: String): SpannableStringBuilder {
         val stringBuilder = SpannableStringBuilder()
         val spannableString = SpannableString(content)
         //val styleSpan = StyleSpan(Typeface.BOLD)
         val colorSpan = ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.red))
         //spannableString.setSpan(styleSpan, 0, content.length, 0)
         spannableString.setSpan(colorSpan, 0, content.length, 0)
         spannableString.setSpan(StrikethroughSpan(), 0, content.length, 0)
         stringBuilder.append(spannableString)
         return stringBuilder
     }
    */
}
