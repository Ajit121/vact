package com.score.vact.binding

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.score.vact.R
import com.score.vact.vo.Status


/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(
        value = ["app:imageUrl", "app:placeHolder", "app:error", "circular"],
        requireAll = false
    )
    fun loadImage(
        imageView: ImageView,
        url: String?,
        holderDrawable: Drawable,
        errorDrawable: Drawable,
        circular: String?
    ) {

        if (url.isNullOrEmpty()) {
            imageView.setImageDrawable(holderDrawable)
        } else {
            var requestOptions = RequestOptions()
            if (circular != null) {
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(200))
            }

            Glide.with(imageView.context)
                .load(url)
                .centerCrop()
                .placeholder(holderDrawable)
                .error(errorDrawable)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["validateField", "errorMessage"], requireAll = false)
    fun checkEmpty(view: TextInputLayout, text: String, errorMessage: String?) {
        view.error =
            if (text.isEmpty()) if (errorMessage.isNullOrEmpty()) "Field cannot be empty" else errorMessage else ""
        view.isErrorEnabled = text.isEmpty()
    }

    @JvmStatic
    @BindingAdapter("validateEmail")
    fun checkValidEmail(view: TextInputLayout, enteredEmail: String) {
        var errorMessage = ""
        var hasError = false
        if (enteredEmail.isEmpty()) {
            errorMessage = "Please enter email Id"
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            errorMessage = "Invalid Email Id"
            hasError = true
        }
        view.error = errorMessage
        view.isErrorEnabled = hasError
    }

    @JvmStatic
    @BindingAdapter("showVehicleInfo")
    fun showVehicleInfo(view: View, selectedVehicleType: Int) {
        val selectedType = selectedVehicleType == R.id.rdPersonal
        if (selectedType) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("isEnabled")
    fun isEnabled(view: View, isEnabled: Boolean) {
        view.isClickable = isEnabled
        if (isEnabled) {
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.colorPrimary))
        }else{
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.divider))
        }
    }

    @JvmStatic
    @BindingAdapter("arrayFormattedText")
    fun arrayFormattedText(view:TextView,text:List<String>?){
        if(!text.isNullOrEmpty()) {
            val formatted1 = text.toString().replace("[", "")
            val formattedText = formatted1.replace("]", "")
            view.text = formattedText
        }else{
            view.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["status","errorDrawable","successDrawable"])
    fun applyStatusOnImage(view:ImageView,status:Status?,errorDrawable: Drawable,successDrawable: Drawable){
        status?.let {
            if(status ==Status.LOADING){
                view.visibility = View.INVISIBLE
            }else{
                view.visibility = View.VISIBLE
            }
            if(status== Status.SUCCESS){
                view.setImageDrawable(successDrawable)
            }else if(status == Status.ERROR){
                view.setImageDrawable(errorDrawable)
            }
        }
    }
}
