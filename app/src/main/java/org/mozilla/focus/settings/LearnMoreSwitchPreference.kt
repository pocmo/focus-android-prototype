package org.mozilla.focus.settings

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import mozilla.components.browser.state.state.SessionState
import org.mozilla.focus.R
import org.mozilla.focus.ext.components

abstract class LearnMoreSwitchPreference(context: Context?, attrs: AttributeSet?) :
    SwitchPreferenceCompat(context, attrs) {

    init {
        layoutResource = R.layout.preference_switch_learn_more
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        getDescription()?.let {
            val summaryView = holder!!.findViewById(android.R.id.summary) as TextView
            summaryView.text = it
            summaryView.visibility = View.VISIBLE
        }

        val learnMoreLink = holder!!.findViewById(R.id.link) as TextView
        learnMoreLink.paintFlags = learnMoreLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        learnMoreLink.setTextColor(ContextCompat.getColor(context, R.color.colorAction))
        learnMoreLink.setOnClickListener {
            context.components.tabsUseCases.addPrivateTab(
                getLearnMoreUrl(),
                source = SessionState.Source.MENU,
                selectTab = true
            )

            if (context is ContextThemeWrapper) {
                if ((context as ContextThemeWrapper).baseContext is Activity) {
                    ((context as ContextThemeWrapper).baseContext as Activity).finish()
                }
            } else {
                (context as? Activity)?.finish()
            }
        }

        val backgroundDrawableArray =
            context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        val backgroundDrawable = backgroundDrawableArray.getDrawable(0)
        backgroundDrawableArray.recycle()
        learnMoreLink.background = backgroundDrawable
    }

    open fun getDescription(): String? = null

    abstract fun getLearnMoreUrl(): String
}
