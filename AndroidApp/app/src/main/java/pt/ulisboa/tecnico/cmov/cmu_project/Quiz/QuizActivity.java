package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import pt.ulisboa.tecnico.cmov.cmu_project.DatabaseHelper;
import pt.ulisboa.tecnico.cmov.cmu_project.Monument.MonumentData;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class QuizActivity extends AppCompatActivity {


    private static final String QUESTION_ID = "questionID";
    private static final String ANSWER_ID = "answerID";


    /* information passing tags */
    public static final String QUIZ_QUESTIONS = "QUIZ_QUESTIONS";
    public static final String MON_ID = "MON_ID";


    private int imgID = 0; // resource ID monument image
    private int questionNumber = 0; // question number to present to user
    private DatabaseHelper db;
    private long startedTime;
    CountDownTimer timer;

    private ArrayList<QuizAnswer> quizAnswers;
    private ArrayList<QuizQuestion> quizQuestions; // question list
    private QuizListAdapter itemsAdapter; // list adapter that extends o ArrayAdapter<String>
    private ArrayList<String> adapterItens = new ArrayList<>(); // strings list of possible answers
    private ListView listView;

    private QuizQuestion currentQuestion; // question that the user is answering
    private boolean questionsAnswered = false; // check if user as answered the question
    private boolean prev_screen = false;
    private int monID; // monument ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_quiz);
        db = DatabaseHelper.getInstance(getApplicationContext());

        /*get data from previous activity*/
        Intent intent = getIntent();
        this.quizQuestions = (ArrayList<QuizQuestion>) intent.getSerializableExtra(QuizActivity.QUIZ_QUESTIONS);
        this.monID = intent.getIntExtra(QuizActivity.MON_ID, 0);
        db.updateMonumentStatus(monID, MonumentData.VISITED);
        db.updateMonumentQuizStatus(monID, MonumentData.STARTED);
        this.quizAnswers = new ArrayList<>();


        this.setInitialState();
        this.startedTime = System.currentTimeMillis();
        initTimer();

    }


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    /**
     * Function that puts monument image and loads questions
     */
    private void setInitialState() {

        //colocar a imagem como fundo
        if (this.imgID != 0) {

            RelativeLayout relativeLayout = findViewById(R.id.relativeLayoutQuiz);
            relativeLayout.setBackground(getResources().getDrawable(this.imgID));
        }

        if (this.quizQuestions != null) {

            this.adapterItens = new ArrayList<>();
            this.currentQuestion = this.quizQuestions.get(this.questionNumber);
            this.itemsAdapter = new QuizListAdapter(this, this.adapterItens);
            this.listView = findViewById(R.id.lstViewQuiz);
            this.updateItemsAdapterView();

            /*Event handler when answering questions*/
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DatabaseHelper.getInstance(getBaseContext()).updateQuestionAnswered(currentQuestion.getQuestionID());

                    if (QuizActivity.this.questionNumber <= QuizActivity.this.quizQuestions.size()) {

                        //marcar se errou ou acertou
                        if (QuizActivity.this.selectedRightOption(listView.getItemAtPosition(position).toString()))
                            listView.getChildAt(position).setBackgroundColor(Color.GREEN);
                        else
                            listView.getChildAt(position).setBackgroundColor(Color.RED);


                        quizAnswers.add(new QuizAnswer(currentQuestion.getQuestionID(), 0, QuizActivity.this.selectedRightOption(listView.getItemAtPosition(position).toString()), System.currentTimeMillis() - startedTime));

                        //todo: add answer to cache

                        QuizActivity.this.listView.setEnabled(false);
                        QuizActivity.this.questionsAnswered = true;
                        if (questionNumber + 1 == quizQuestions.size()) {
                            // QuizActivity.this.timeCounter.interrupt();
                            timer.cancel();
                            db.updateMonumentQuizStatus(monID, MonumentData.ANSWERED);
                            Button button = findViewById(R.id.btnNextQuestion);
                            button.setText(R.string.go_back);
                            prev_screen = true;
                            db.insertAnswersPool(quizAnswers);
                        } else {
                            timer.cancel();
                            questionsAnswered = true;
                        }

                    }
                }
            });
        }
    }

    private void initTimer() {

        final TextView textTimer = (TextView) findViewById(R.id.quizTimeElapsed);

        timer = new CountDownTimer(30000, 1000) {

            int time = 30;

            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                textTimer.setText("timeup");
            }

        }.start();
    }

    /**
     * Function that updates items in list view
     */
    private void updateItemsAdapterView() {

        this.adapterItens.clear();
        this.listView.setAdapter(null);
        this.listView.setEnabled(true);

        String question = this.currentQuestion.getQuestion();
        TextView txtViewQuestion = findViewById(R.id.txtViewQuestion);
        txtViewQuestion.setText(question);

        ArrayList<String> tempStrLst = new ArrayList<>();
        tempStrLst.addAll(this.currentQuestion.getAnswersList());
        Collections.shuffle(tempStrLst);
        this.adapterItens.addAll(tempStrLst);
        this.listView.setAdapter(this.itemsAdapter);
    }

    /**
     * Function that verifies if question as been answered correctly
     *
     * @param selectedOption
     * @return
     */
    private Boolean selectedRightOption(String selectedOption) {
        if (selectedOption.equals(this.currentQuestion.getCorrectAnswer()))
            return true;
        else
            return false;
    }

    /**
     * Função que responde ao evento onClick quando o utilizador pressiona o botão Next para carregar
     * a proxima questão do quiz
     *
     * @param view
     */
    public void btnNextQuestionOnClick(View view) {

        if (this.prev_screen) {
            finish();
            return;
        }

        if (this.questionsAnswered && this.questionNumber < this.quizQuestions.size()) {

            questionNumber++;
            this.currentQuestion = this.quizQuestions.get(QuizActivity.this.questionNumber);
            this.updateItemsAdapterView();
            QuizActivity.this.questionsAnswered = false;
            initTimer();
            startedTime = System.currentTimeMillis();

        } else {

            Toast.makeText(this, R.string.answer_question, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onDestroy() {
        // DatabaseHelper.getInstance(getBaseContext()).updateQuestionAnswered(currentQuestion.getQuestionID());
        // enviar para o servidor todas as respostas erradas talvez e tempos absurdos ???
        db.updateMonumentQuizStatus(monID, MonumentData.INTERRUPTED);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        return;
    }


}
