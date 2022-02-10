package com.example.simplesurveyapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_SURVEY_STATUS_YES = "com.example.simplesurveyapp.survey_status_yes"
const val EXTRA_SURVEY_STATUS_NO = "com.example.simplesurveyapp.survey_status_no"

private const val RESULT_STATUS_KEY = "result-status-key"


class SurveyResultsActivity : AppCompatActivity() {

    private lateinit var yesTextView: TextView
    private lateinit var noTextView: TextView
    private lateinit var continueButton: Button
    private lateinit var resetButton: Button

    private var surveyResetCheck = false
    // Initialized the Yes No answers here so I could use them for functions
    private var yesAnswers = 0
    private var noAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_results)

        yesTextView = findViewById(R.id.yes_textview)
        noTextView = findViewById(R.id.no_textview)
        continueButton = findViewById(R.id.continue_button)
        resetButton = findViewById(R.id.reset_button)

        // Reads the values of our yes and no answers that are passed from the main activity if
        // nothing is passed we set the default to 0
        yesAnswers = intent.getIntExtra(EXTRA_YES_ANSWERS, 0)
        noAnswers = intent.getIntExtra(EXTRA_NO_ANSWERS,  0)
        // Checks are saved instance state Boolean to see if the reset button has been hit
        // If it has we reset the answers to 0 rather than using the data passed from the main
        val status = savedInstanceState?.getBoolean(RESULT_STATUS_KEY, false) ?: false
        if (status) {
            // Simple method that resets our yes and no answers to 0
            setAnswersZero()
        }
        // Takes are yes and no answers and updates the display.
        updateCount(yesAnswers, noAnswers)


        resetButton.setOnClickListener {
            // Sets yes and no back to 0
            setAnswersZero()
            // and sets are rest status to true
            surveyResetCheck = true
            // updates display.
            updateCount(yesAnswers, noAnswers)
        }

        continueButton.setOnClickListener {
            // Passes our yes and no answers to the method to send them back to the main activity.
            surveyResultIntentHandler(yesAnswers, noAnswers)


        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Stores a key value pair that is true if the user has hit the reset button.
        outState.putBoolean(RESULT_STATUS_KEY, surveyResetCheck)
    }

    private fun surveyResultIntentHandler(resultsStatusYes: Int, resultStatusNo: Int) {
        Intent().apply {
            // Passes data back to the main activity as two extras one for the yes and no
            // int variable.
            putExtra(EXTRA_SURVEY_STATUS_YES, resultsStatusYes)
            putExtra(EXTRA_SURVEY_STATUS_NO, resultStatusNo)
            // Sets the status as ok meaning the activity was completed successfully.
            setResult(Activity.RESULT_OK, this)
            // Ends the activity
            finish()
        }

    }

    private fun updateCount(yesAnswers: Int, noAnswers: Int) {
        // Changes the display to match the current yes and now values.
        yesTextView.text = yesAnswers.toString()
        noTextView.text = noAnswers.toString()
    }

    private fun setAnswersZero(){
        // resets the yes and no values.
        yesAnswers = 0
        noAnswers = 0
    }


}