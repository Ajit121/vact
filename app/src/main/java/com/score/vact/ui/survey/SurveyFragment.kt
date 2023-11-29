package com.score.vact.ui.survey

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager

import com.score.vact.R
import com.score.vact.adapter.SurveyAdapter
import com.score.vact.databinding.SurveyFragmentBinding
import com.score.vact.di.Injectable
import com.score.vact.model.QuestionData
import com.score.vact.vo.Answer
import kotlinx.android.synthetic.main.survey_fragment.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SurveyFragment : Fragment(), Injectable, CoroutineScope, ViewPager.OnPageChangeListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var job: Job
    private lateinit var viewModel: SurveyViewModel
    private lateinit var questions: MutableList<QuestionData>
    private lateinit var adapter: SurveyAdapter
    private val TAG = javaClass.simpleName
    private val args by navArgs<SurveyFragmentArgs>()
    private lateinit var binding: SurveyFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SurveyFragmentBinding>(
            inflater,
            R.layout.survey_fragment,
            container,
            false
        )
        this.binding = binding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeSurveyQuestions()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // viewModel.getSurveyQuestions()
        questions = mutableListOf()

        viewPager.addOnPageChangeListener(this)
        adapter = SurveyAdapter(this.requireContext(), questions) { questionId, answer ->

            showNextQuestion(questionId,answer)
        }
        viewPager.adapter = adapter
        viewPager.clipToPadding = false
        viewPager.pageMargin = 20



        btnPrevious.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        btnDone.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_surveyFragment_to_visitorDetailsFragment)
        }


    }



    private fun observeSurveyQuestions() = launch {


        viewModel.question.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            this@SurveyFragment.questions.clear()
            this@SurveyFragment.questions.addAll(it)
            this@SurveyFragment.adapter.notifyDataSetChanged()
            updateProgress(it.size)
        })
    }

    private fun showNextQuestion(questionId: Int, answer: Answer) = launch{

        viewModel.submitAnswer(questionId, answer)
        //Keeping some delay to make the page answer update and showing
        //next page with smooth animation
        Handler().postDelayed({
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            try{
                if (viewPager.currentItem == viewModel.question.value!!.size-1 &&
                    viewModel.question.value!![ viewModel.question.value!!.size-1].answer!=Answer.NA) {
                    binding.btnDone.visibility = View.VISIBLE
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        },500)

    }

    private fun updateProgress(size: Int) {
        tvQuestionIndex.text = "${viewPager.currentItem + 1}/${size}"
        val progress = ((viewPager.currentItem + 1).toDouble() / size.toDouble()) * 100
        progress_bar_1.progress = progress.toInt()
    }



    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        Log.d(TAG,"viewPager position $position")
        if(position>0){
            binding.btnPrevious.visibility = View.VISIBLE
        }else{
            binding.btnPrevious.visibility = View.INVISIBLE
        }
        updateProgress(questions.size)

    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}
