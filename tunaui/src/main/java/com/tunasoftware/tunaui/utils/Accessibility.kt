package com.tunasoftware.tunaui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.children
import com.tunasoftware.tunaui.R


fun View.describeAsButton() {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.roleDescription = context.getString(R.string.tuna_accessibility_button)
        }
    })
}

fun ViewGroup.disableForAccessibility() {
    this.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
    this.children.forEach {
        if (it is ViewGroup) {
            it.disableForAccessibility()
        } else {
            it.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }
    }
}

fun Activity.announceForAccessibility(text: String) {
    window?.decorView?.announceForAccessibility(text)
}

fun View.setAsHeading() {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.isHeading = true
        }
    })
}

fun View.setAsReaderFocusable() {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.isScreenReaderFocusable = true
        }
    })
}

fun Context.announceForAccessibility(text: String) {
    val manager = this.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    if (manager.isEnabled) {
        val event = AccessibilityEvent.obtain()
        event.eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
        event.className = javaClass.name
        event.packageName = this.packageName
        event.text.add(text)
        manager.sendAccessibilityEvent(event)
    }
}