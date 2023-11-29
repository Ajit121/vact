package com.score.vact.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.button.MaterialButton
import com.score.vact.R
import com.score.vact.model.QuestionData
import com.score.vact.vo.Answer
import kotlinx.coroutines.Job

class SurveyAdapter(
    private val mContext: Context,
    private val questions: List<QuestionData>,
    private val onAnswer: ((questionId: Int, answer: Answer) -> Job)
) :
    PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val question = questions[position]
        val layout =
            LayoutInflater.from(mContext).inflate(R.layout.question_layout, container, false)
        val tv = layout.findViewById<TextView>(R.id.tvQuestion)
        val btnNo = layout.findViewById<MaterialButton>(R.id.btnNo)
        btnNo.tag = position
        val btnYes = layout.findViewById<MaterialButton>(R.id.btnYes)
        btnYes.tag = position

        val btnNotSure = layout.findViewById<MaterialButton>(R.id.btnNotSure)
        btnNotSure.tag = position
        tv.text = question.question

        if (question.answer == Answer.Yes) {
            btnYes.backgroundTintList =
                ContextCompat.getColorStateList(mContext, R.color.colorPrimary);
            btnYes.setTextColor(ContextCompat.getColor(mContext, R.color.ghostWhite))

            btnNo.backgroundTintList =
                ContextCompat.getColorStateList(mContext, android.R.color.white);
            btnNo.setTextColor(ContextCompat.getColor(mContext, R.color.divider))

            btnNotSure.setTextColor(ContextCompat.getColor(mContext, R.color.divider))
        } else if (question.answer == Answer.No) {
            btnNo.backgroundTintList =
                ContextCompat.getColorStateList(mContext, R.color.colorPrimary);
            btnNo.setTextColor(ContextCompat.getColor(mContext, R.color.ghostWhite))

            btnYes.backgroundTintList =
                ContextCompat.getColorStateList(mContext, android.R.color.white);
            btnYes.setTextColor(ContextCompat.getColor(mContext, R.color.divider))

            btnNotSure.setTextColor(ContextCompat.getColor(mContext, R.color.divider))
        } else if (question.answer == Answer.NOT_AWARE) {
            btnYes.backgroundTintList =
                ContextCompat.getColorStateList(mContext, android.R.color.white);
            btnYes.setTextColor(ContextCompat.getColor(mContext, R.color.divider))

            btnNo.backgroundTintList =
                ContextCompat.getColorStateList(mContext, android.R.color.white);
            btnNo.setTextColor(ContextCompat.getColor(mContext, R.color.divider))

            btnNotSure.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        }


        btnYes.setOnClickListener {
            onAnswer.invoke(question.id, Answer.Yes)
        }
        btnNo.setOnClickListener {
            onAnswer.invoke(question.id, Answer.No)
        }
        btnNotSure.setOnClickListener {
            onAnswer.invoke(question.id, Answer.NOT_AWARE)
        }
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return questions.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    interface AnswerListener {
        fun onAnswer(position: Int, answer: Boolean)
    }
}



