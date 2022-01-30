package com.example.simplesurveyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import java.lang.NullPointerException

const val TAG = "SURVEY_ACTIVITY"
const val LAST_SURVEY_QUESTION_KEY = "last-survey-question-bundle-key"

class MainActivity : AppCompatActivity() {

    private val surveyViewModel: Survey by lazy {
        ViewModelProvider(this).get(Survey::class.java) }

    private lateinit var startSurveyButton: Button
    private lateinit var surveyQuestion: EditText
    private lateinit var yesButton: Button
    private lateinit var noButton: Button
    private lateinit var yesTextView: TextView
    private lateinit var noTextView: TextView
    private lateinit var questionDisplay: TextView
    private lateinit var newSurveyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findByViewSetup()

        startSurveyButton.setOnClickListener {
            // could save a line of code by turning the question String in the survey class
            // into a public var rather than private.
            surveyViewModel.getQuestion(surveyQuestion.text.toString())
            val questionStore = surveyViewModel.giveQuestion()
            if (questionStore.isNotBlank()) {
                surveyMode()
                questionDisplay.text = questionStore
                updateYesAndNo()
            }
        }

        yesButton.setOnClickListener {
            surveyViewModel.addYes()
            updateYesAndNo()
        }

        noButton.setOnClickListener {
            surveyViewModel.addNo()
            updateYesAndNo()
        }

        newSurveyButton.setOnClickListener {
            surveyViewModel.resetSurvey()
            questionMode()
        }

        if(savedInstanceState != null) {
            if(savedInstanceState.getString("question").equals("test")){
                questionMode()
                surveyViewModel.resetSurvey()
            }else{
                questionDisplay.text = savedInstanceState.getString("question")
                surveyMode()
                updateYesAndNo()
            }

        }


    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        if(surveyViewModel.giveQuestion().isNotBlank()){
            outState.putString(LAST_SURVEY_QUESTION_KEY, surveyViewModel.giveQuestion())
        } else{
            outState.putString(LAST_SURVEY_QUESTION_KEY, "test")
        }

    }

    private fun updateYesAndNo() {
        yesTextView.text = surveyViewModel.getYesAnswers()
        noTextView.text = surveyViewModel.getNoAnswers()
    }

    private fun findByViewSetup() {
        startSurveyButton = findViewById(R.id.start_survey_button)
        surveyQuestion = findViewById(R.id.survey_edit_text_view)
        yesButton = findViewById(R.id.yes_button)
        noButton = findViewById(R.id.no_button)
        yesTextView = findViewById(R.id.yes_textview)
        noTextView = findViewById(R.id.no_textview)
        questionDisplay = findViewById(R.id.question_holder_textview)
        newSurveyButton = findViewById(R.id.new_survey_button)
    }

    private fun surveyMode() {
        surveyQuestion.text.clear()
        startSurveyButton.visibility = View.INVISIBLE
        surveyQuestion.visibility = View.INVISIBLE
        yesButton.visibility = View.VISIBLE
        noButton.visibility = View.VISIBLE
        yesTextView.visibility = View.VISIBLE
        noTextView.visibility = View.VISIBLE
        questionDisplay.visibility = View.VISIBLE
        newSurveyButton.visibility = View.VISIBLE
    }

    private fun questionMode() {
        startSurveyButton.visibility = View.VISIBLE
        surveyQuestion.visibility = View.VISIBLE
        yesButton.visibility = View.INVISIBLE
        noButton.visibility = View.INVISIBLE
        yesTextView.visibility = View.INVISIBLE
        noTextView.visibility = View.INVISIBLE
        questionDisplay.visibility = View.INVISIBLE
        newSurveyButton.visibility = View.INVISIBLE
    }
}