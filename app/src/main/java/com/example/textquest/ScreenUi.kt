package com.example.textquest

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

class ScreenUi (
    fullText: String,
    actions: List<ActionUi>
) {

    private val spannableString = SpannableString(fullText)

    init {
        for (action in actions) {
            action.setSpan(spannableString, fullText)
        }
    }

    fun show(textView: TextView) = textView.run {
        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
}

class ActionUi(
    private val actionCallback: ActionCallback,
    private val id: String,
    private val text: String
) {
    private val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) = actionCallback.moveToScreen(id)

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.color = Color.parseColor("#FF0000")
        }
    }

    fun setSpan(spannableString: SpannableString, fullText: String) {
        val indexOf = fullText.indexOf(text)
        spannableString.setSpan(
            clickableSpan,
            indexOf,
            indexOf + text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

interface ActionCallback {

    fun moveToScreen(id: String)
}