package com.example.simplesurveyapp

import androidx.lifecycle.ViewModel

class Survey(): ViewModel() {

    private var surveyQuestion = ""
    private var yesAnswers = 0
    private var noAnswers = 0

    fun getQuestion(question: String) {
        surveyQuestion = question
    }

    fun giveQuestion(): String {
        return surveyQuestion
    }

    fun addYes() {
        yesAnswers++
    }

    fun addNo() {
        noAnswers++
    }

    fun getYesAnswers(): String {
        return yesAnswers.toString()
    }

    fun getNoAnswers(): String {
        return noAnswers.toString()
    }

    fun resetSurvey () {
        yesAnswers = 0
        noAnswers = 0
        surveyQuestion = ""
    }


}