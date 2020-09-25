package com.example.android.guesstheword.screens.game

import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }
    private val timer: CountDownTimer
    val _timer =  MutableLiveData<Long>()
    val currentTimeString = Transformations.map(_timer, { time ->
        DateUtils.formatElapsedTime(time)
    })


    // The current word
    var word = MutableLiveData<String>()

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>get() = _score
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>get() = _eventGameFinish
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    init {
        _eventGameFinish.value = false
        Log.i("GameViewModel","GameViewModel created!")
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = millisUntilFinished/ONE_SECOND
                // TODO implement what should happen each tick of the timer
            }
            override fun onFinish() {
                _eventGameFinish.value = true
                Log.i("GameViewModel","finish")

                // TODO implement what should happen when the timer finishes
            }
        }
        timer.start()

       // DateUtils.formatElapsedTime(newTime)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel","GameViewModel destroed!")
        timer.cancel()
    }
    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }
    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        word.value = wordList.removeAt(0)


    }
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)

        nextWord()
    }
    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }
}