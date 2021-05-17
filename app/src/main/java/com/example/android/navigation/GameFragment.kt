package com.example.android.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numberOfQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater,
            R.layout.fragment_game,
            container,
            false
        )

        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (checkedId != -1) {
                var answerIndex = 0
                when (checkedId) {
                    binding.secondAnswerRadioButton.id -> answerIndex = 1
                    binding.thirdAnswerRadioButton.id -> answerIndex = 2
                    binding.fourthAnswerRadioButton.id -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers!![0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numberOfQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(
                                numberOfQuestions,
                                questionIndex))
                        // view.findNavController()
                        // navigate(R.id.action_gameFragment_to_gameWonFragment)

                        // We've won!  Navigate to the gameWonFragment.
                        // TODO (05) Find the navController from the view and navigate to the gameWonFragment
                        // call view.findNavController
                        // navigate to R.id.action_gameFragment_to_gameWonFragment
                    }

                } else {
                    view.findNavController()
                        .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                    // view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    // TODO (06) Find the navController from the view and navigate to the gameOverFragment
                    // call view.findNavController
                    // navigate to R.id.action_gameFragment_to_gameOverFragment
                }
            }
        }

        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers!!.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_android_trivia_question, +questionIndex + 1, numberOfQuestions)

    }

}