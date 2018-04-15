package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by espada on 25-03-2018.
 */

public class QuizQuestion implements Serializable {

    private String question;
    private String correctAnswer;
    private String invalidAnswer1;
    private String invalidAnswer2;
    private String invalidAnswer3;
    private ArrayList<String> questions;

    public QuizQuestion(String question, String correctAnswer, String invalidAnswer1, String invalidAnswer2, String invalidAnswer3) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.invalidAnswer1 = invalidAnswer1;
        this.invalidAnswer2 = invalidAnswer2;
        this.invalidAnswer3 = invalidAnswer3;
    }

    public QuizQuestion(String question, String correctAnswer, ArrayList<String> questions){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.questions = questions;
    }


    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getInvalidAnswer1() {
        return invalidAnswer1;
    }

    public String getInvalidAnswer2() {
        return invalidAnswer2;
    }

    public String getInvalidAnswer3() {
        return invalidAnswer3;
    }

    @Override
    public String toString() {
        return this.question + "\n" + this.correctAnswer + "\n" + this.invalidAnswer1 +
                "\n" + this.invalidAnswer2 + this.invalidAnswer3;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizQuestion that = (QuizQuestion) o;

        if (question != null ? !question.equals(that.question) : that.question != null)
            return false;
        if (correctAnswer != null ? !correctAnswer.equals(that.correctAnswer) : that.correctAnswer != null)
            return false;
        if (invalidAnswer1 != null ? !invalidAnswer1.equals(that.invalidAnswer1) : that.invalidAnswer1 != null)
            return false;
        if (invalidAnswer2 != null ? !invalidAnswer2.equals(that.invalidAnswer2) : that.invalidAnswer2 != null)
            return false;
        return invalidAnswer3 != null ? invalidAnswer3.equals(that.invalidAnswer3) : that.invalidAnswer3 == null;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (correctAnswer != null ? correctAnswer.hashCode() : 0);
        result = 31 * result + (invalidAnswer1 != null ? invalidAnswer1.hashCode() : 0);
        result = 31 * result + (invalidAnswer2 != null ? invalidAnswer2.hashCode() : 0);
        result = 31 * result + (invalidAnswer3 != null ? invalidAnswer3.hashCode() : 0);
        return result;
    }


}
