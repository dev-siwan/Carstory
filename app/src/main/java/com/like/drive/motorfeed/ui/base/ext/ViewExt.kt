package com.like.drive.motorfeed.ui.base.ext

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.like.drive.motorfeed.ui.base.loading.LoadingProgressDialog
import java.lang.Exception
import java.util.*
import kotlin.reflect.KClass


fun Context.showShortToast(@StringRes resId: Int) = showToast(resId, Toast.LENGTH_SHORT)
fun Context.showLongToast(@StringRes resId: Int) = showToast(resId, Toast.LENGTH_LONG)

fun Context.showShortToast(text: String) {
    if (text.isNotBlank()) showToast(text, Toast.LENGTH_SHORT)
}
fun Context.showLongToast(text: String) = showToast(text, Toast.LENGTH_LONG)

fun Context.showToast(text: String, duration: Int) = Toast.makeText(this, text, duration).show()
fun Context.showToast(@StringRes resId: Int, duration: Int) =
    Toast.makeText(this, resId, duration).show()

fun Context.showLongSnackbar(view:View,text:String) = Snackbar.make(view,text,Snackbar.LENGTH_LONG).show()

fun Context.spToPixel(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun Context.dpToPixel(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

fun Context.valueToDp(value:Int):Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics).toInt()
}

fun Context.pixelToDp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.density
}

fun Context.hideKeyboard(view:View){
    val imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view:View){
    val imm =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}


fun Context.showListDialog(list: Array<String>, title: String?="", action: (Int) -> Unit) {
    val builder = AlertDialog.Builder(this)
    title?.let {
        builder.setTitle(it)
    }
    builder.setItems(list) { _, position ->
        action(position)
    }
    val dialog = builder.create()

    dialog?.run {
        show()
    }
}

fun Context.progressBar():Dialog{
    return LoadingProgressDialog(this)
}

fun Context.openWebBrowser(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        //showShortToast(getString(R.string.wrong_url))
    }
}


/**
 * datePicker 에 대한 설정이 변경될 가능성이 있어서, DatePicker 를 리턴.
 * 예) 날짜 선택 불가능/가능
 */
fun Context.getDatePicker(separator: String, action: (String) -> Unit): DatePickerDialog{
    val c = Calendar.getInstance()
    val calYear = c.get(Calendar.YEAR)
    val calMonth = c.get(Calendar.MONTH)
    val calDay = c.get(Calendar.DAY_OF_MONTH)

    return DatePickerDialog(this,
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            val displayMonth = if(month.plus(1)<10) "0${month.plus(1)}"
            else month.plus(1)

            val displayDay = if(dayOfMonth<10) "0${dayOfMonth}"
            else dayOfMonth

            action("$year$separator${displayMonth}$separator$displayDay")
        },calYear, calMonth, calDay
    )
}


fun NestedScrollView.setScrollPositionAndFocus(context: Context,view: View) {
    this.smoothScrollTo(0, view.y.toInt())
    view.isFocusableInTouchMode = true
    view.requestFocus()
    context.hideKeyboard(view)
}

fun ScrollView.setScrollPositionAndFocus(context: Context, view: View) {
    this.smoothScrollTo(0, view.y.toInt())
    view.isFocusableInTouchMode = true
    view.requestFocus()
    context.hideKeyboard(view)
}

fun Context.startAct(clazz: KClass<*>, bundle: Bundle? = null) {
    Intent(this, clazz.java).apply {
        bundle?.let {
            putExtras(it)
        }
    }.run {
        startActivity(this)
    }
}

fun Fragment.startToAct(clazz: KClass<*>, bundle: Bundle? = null, attachException:(()->Unit)?=null) {
    try {
        Intent(requireContext(), clazz.java).apply {
            bundle?.let {
                putExtras(it)
            }
        }.run {
            startActivity(this)
        }
    } catch (e: Exception) {
        attachException?.invoke()
    }
}

fun Activity.startActForResult(clazz: KClass<*>, requestCode: Int, bundle: Bundle? = null) {
    Intent(this, clazz.java).apply {
        bundle?.let {
            putExtras(it)
        }
    }.run {
        startActivityForResult(this, requestCode)
    }
}

fun FragmentManager.currentNavigationFragment() =
    primaryNavigationFragment?.childFragmentManager?.fragments?.first()

inline fun <T: View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}
