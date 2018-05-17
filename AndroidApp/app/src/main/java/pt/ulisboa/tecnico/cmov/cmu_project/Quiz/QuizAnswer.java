package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

/**
 * Created by tiago on 17-May-18.
 */

public class QuizAnswer {
    private int questionId;
    private int answerId;
    private boolean isCorrect;
    private long time;

    public QuizAnswer(int questionId, int answerId, boolean isCorrect, long time) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.isCorrect = isCorrect;
        this.time = time;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
