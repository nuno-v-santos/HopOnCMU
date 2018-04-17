package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by espada on 25-03-2018.
 */

public class QuizQuestion implements Serializable {

    private String question;
    private String correctAnswer;
    private ArrayList<String> answersList;
    public ArrayList<String> getAnswersList() {
        return answersList;
    }

    public QuizQuestion(String question, String correctAnswer, ArrayList<String> questions) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answersList = questions;
    }


    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return this.question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizQuestion that = (QuizQuestion) o;
        return Objects.equals(question, that.question) &&
                Objects.equals(correctAnswer, that.correctAnswer) &&
                Objects.equals(answersList, that.answersList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(question, correctAnswer, answersList);
    }
}
