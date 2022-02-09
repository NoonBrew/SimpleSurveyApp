package com.example.simplesurveyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_SURVEY_STATUS = "com.example.simplesurveyapp.survey_status"

class SurveyResultsActivity : AppCompatActivity() {

    private lateinit var yesTextView: TextView
    private lateinit var noTextView: TextView
    private lateinit var continueButton: Button
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_results)

        yesTextView = findViewById(R.id.yes_textview)
        noTextView = findViewById(R.id.no_textview)
        continueButton = findViewById(R.id.continue_button)
        resetButton = findViewById(R.id.reset_button)

        var yesAnswers = intent.getIntExtra(EXTRA_YES_ANSWERS, 0)
        var noAnswers = intent.getIntExtra(EXTRA_NO_ANSWERS,  0)

        updateCount(yesAnswers, noAnswers)


        resetButton.setOnClickListener {
            yesAnswers = 0
            noAnswers = 0
            updateCount(yesAnswers, noAnswers)
        }

        continueButton.setOnClickListener {
            val resultsReset = when {
                (yesAnswers > 0 && noAnswers > 0) -> true
                else -> false
            }
            surveyResultIntentHandler(resultsReset)
            // TODO add status check in Main, if True change int values in SurveyViewModel
            // TODO If false do nothing.

        }
    }

    // TODO Add saveOnInstance to store if reset was hit or not so rotation does not destroy
    // TODO the data flow.

    private fun surveyResultIntentHandler(resultsReset: Boolean) {
        Intent().apply {
            putExtra(EXTRA_SURVEY_STATUS, resultsReset)
            setResult(RESULT_OK, this)
            finish()
        }
    }

    private fun updateCount(yesAnswers: Int, noAnswers: Int) {
        yesTextView.text = yesAnswers.toString()
        noTextView.text = noAnswers.toString()
    }


}