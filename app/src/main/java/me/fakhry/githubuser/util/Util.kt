package me.fakhry.githubuser.util

import android.view.View
import android.widget.ProgressBar

//fun showLoading(isLoading: Boolean) {
//
//}

fun ProgressBar.showLoading(isLoading: Boolean) {
    visibility = if (isLoading) {
        View.VISIBLE
    } else {
        View.GONE
    }
}