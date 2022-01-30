package com.example.simplesurveyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

const val TAG = "SURVEY_ACTIVITY"
const val LAST_SURVEY_QUESTION_KEY = "last-survey-question-bundle-key"

class MainActivity : AppCompatActivity() {
    // Allows us to initialize our Survey class when it is used.
    private val surveyViewModel: Survey by lazy {
        ViewModelProvider(this).get(Survey::class.java) }

    // Initializes the views we will be using in our app
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
        // Because of the amount of views that we have to link to resource ids I created a
        // function that will hold all of the setting of resource Ids.
        findByViewSetup()
        // First onclick listener.
        startSurveyButton.setOnClickListener {
            // could save a line of code by turning the question String in the survey class
            // into a public var rather than private.

            // Uses our ViewModel class to store our string, this will help us recreate the
            // Activity when the screen is rotated.
            surveyViewModel.storeQuestion(surveyQuestion.text.toString())
            // We call our ViewModel class to retrieve the stored question and store it in
            // a value that we will check.
            val questionStore = surveyViewModel.retrieveQuestion()
            // If a question was entered we will switch the app into survey mode.
            if (questionStore.isNotBlank()) {
                /**
                 * Survey mode method will turn the editText view and surveyButton view invisible
                 * Also makes our hidden buttons and views visable.
                 */
                surveyMode()
                // We will then pass our string to a textView view to be displayed on the screen
                questionDisplay.text = questionStore
                // This method will grab the stored answers from our ViewModel class and display
                // them.
                updateYesAndNo()
            } else {
                // Hint if user does not enter a survey question.
                Toast.makeText(this, R.string.no_survey_question, Toast.LENGTH_SHORT).show()
            }

        }
        // Yes button listener.
        yesButton.setOnClickListener {
            // This ViewModel method increments our yes value by 1.
            surveyViewModel.addYes()
            // call to update the values displayed.
            updateYesAndNo()
        }
        // No button listener.
        noButton.setOnClickListener {
            //This ViewModel method increments our no value by 1.
            surveyViewModel.addNo()
            // Call to update the values displayed.
            updateYesAndNo()
        }
        // New survey button listener.
        newSurveyButton.setOnClickListener {
            // This ViewModel method resets our stored question to blank
            // and resets our stored yes and no int values to 0 so that a new question can be
            // asked.
            surveyViewModel.resetSurvey()
            // This method hides yes, no buttons, and text views and shows the editText view
            // and the startSurvey button.
            questionMode()
        }
        // Checks to see if we have a saved instance state. Got this version from a youTube Video
        // https://www.youtube.com/watch?v=TcTgbVudLyQ (I think I could write this call like
        // you did for our GuestList app)
        if(savedInstanceState != null) {
            // Gets the stored string with our key and checks to see if it is equal to test.
            if(savedInstanceState.getString(LAST_SURVEY_QUESTION_KEY).equals("test")){
                /**
                 * If the saved string equals test that means a survey not currently being asked
                 * and we want to remain in question mode and reset the survey this is
                 * to make sure data was properly cleared.
                 */
                questionMode()
                surveyViewModel.resetSurvey()
            }else{
                // If there was a survey being conducted we will tell the activity to stay in
                // Survey mode and update the yes and no answers. The program also
                // sets the question display back to the saved string.
                questionDisplay.text = savedInstanceState.getString("question")
                surveyMode()
                updateYesAndNo()
            }

        }


    }
    // This function saves information about our Activity when the activity is rotated
    // and recreated.
    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        // First we check to make sure we have a stored string in our ViewModel
        if(surveyViewModel.retrieveQuestion().isNotBlank()){
            // If we do, we saved that stored string as a value to our key.
            outState.putString(LAST_SURVEY_QUESTION_KEY, surveyViewModel.retrieveQuestion())
        } else{
            // Otherwise we will set the value connected to the key to "test"
            outState.putString(LAST_SURVEY_QUESTION_KEY, "test")
        }

    }
    // Uses the ViewModel method getXAnswers() to grab a string version of our int
    // values and display them.
    private fun updateYesAndNo() {
        yesTextView.text = surveyViewModel.getYesAnswers()
        noTextView.text = surveyViewModel.getNoAnswers()
    }
    // This is the function setting up all our resource connections for our views.
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
    // This function hides our initial question and start survey button
    // and also clears the surveyQuestion EditText. I found that when this was not cleared
    // it sometimes resulted in issues with the saved instance state.
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
    // This hides all our relevant survey views like the yes and no button and the new survey
    // button and makes our survey question EditTextView and start survey button visible again.
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