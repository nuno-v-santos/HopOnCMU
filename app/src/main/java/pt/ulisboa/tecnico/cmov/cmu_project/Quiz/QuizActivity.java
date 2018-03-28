package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class QuizActivity extends AppCompatActivity {

    /* atributos relacionados com a passagem de informação */
    public final static String QUIZ_QUESTIONS = "QUIZ_QUESTIONS";
    public final static String MONUMENT_IMG = "MONUMENT_IMG";


    private int imgID = 0; // resource ID para a imagem do monumento
    private int questionNumber = 0; // numero da questão a apresentar ao utilizador


    private ArrayList<QuizQuestion> quizQuestions; // lista com as questões a apresentar ao utilizador
    private QuizListAdapter itemsAdapter; // list adapter que extende o ArrayAdapter<String>
    private ArrayList<String> adapterItens = new ArrayList<>(); // lista de strings que ira conter as respostas possiveis
    private String[] alphabet = new String[4]; //array de strings contendo A,B,C,D para efeitos gráficos
    private ListView listView; // atributo list view

    private QuizQuestion currentQuestion; // questão que o utilizador se encontra a responder
    private boolean questionsAnswered = false; // atriduto que verifica se o utilizador já respondeu à questão


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_quiz);


        int j = 0;
        for (char c = 'A'; c <= 'D'; c++) {
            this.alphabet[j] = "" + c;
            j++;
        }


        /*Obter dados da actividade anterior*/
        Intent intent = getIntent();
        this.imgID = intent.getIntExtra(QuizActivity.MONUMENT_IMG, 0);
        this.quizQuestions = (ArrayList<QuizQuestion>) intent.getSerializableExtra(QuizActivity.QUIZ_QUESTIONS);

        this.setInitialState();// carregar a primeira questão do quiz para a interface

    }

    /**
     * Função que coloca a imagem do monumento e carrega a questão que é dada por a actividade anterior
     * para a interface gráfica
     */
    private void setInitialState() {

        //colocar a imagem como fundo
        if (imgID != 0) {

            RelativeLayout relativeLayout = findViewById(R.id.relativeLayoutQuiz);
            relativeLayout.setBackground(getResources().getDrawable(this.imgID));
        }

        // verficar se lista de questões não é null
        if (this.quizQuestions != null) {

            this.adapterItens = new ArrayList<>();
            this.currentQuestion = this.quizQuestions.get(this.questionNumber);
            this.itemsAdapter = new QuizListAdapter(this, this.adapterItens, this.alphabet);

            this.listView = findViewById(R.id.lstViewQuiz);
            this.updateItemsAdapterView();

            /*criar handler de ventos */
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (QuizActivity.this.questionNumber <= QuizActivity.this.quizQuestions.size()) {
                        listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        for (int i = 0; i < QuizActivity.this.adapterItens.size(); i++) {

                            if (i != position) {

                                listView.getChildAt(i).setBackgroundColor(Color.RED);
                            }

                            if (QuizActivity.this.selectedRightOption(listView.getItemAtPosition(i).toString())) {

                                listView.getChildAt(i).setBackgroundColor(Color.GREEN);
                            }
                        }
                        QuizActivity.this.listView.setEnabled(false);
                        QuizActivity.this.questionsAnswered = true;
                    }
                }
            });

        }

    }

    /**
     * Função que actualiza os elementos na list view
     */
    private void updateItemsAdapterView() {

        this.adapterItens.clear();
        this.listView.setAdapter(null);
        this.listView.setEnabled(true);


        String question = this.currentQuestion.getQuestion();
        TextView txtViewQuestion = findViewById(R.id.txtViewQuestion);
        txtViewQuestion.setText(question);

        String correctAnswer = this.currentQuestion.getCorrectAnswer();
        String answer1 = this.currentQuestion.getInvalidAnswer1();
        String answer2 = this.currentQuestion.getInvalidAnswer2();
        String answer3 = this.currentQuestion.getInvalidAnswer3();

        ArrayList<String> tempStrLst = new ArrayList<>();
        tempStrLst.add(correctAnswer);
        tempStrLst.add(answer1);
        tempStrLst.add(answer2);
        tempStrLst.add(answer3);
        Collections.shuffle(tempStrLst);

        this.adapterItens.addAll(tempStrLst);
        this.listView.setAdapter(this.itemsAdapter);

    }

    /**
     * Função que avalia
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
     * @param view
     */
    public void btnNextQuestionOnClick(View view) {

        this.questionNumber++;
        if (this.questionsAnswered && this.questionNumber < this.quizQuestions.size()) {

            this.currentQuestion = this.quizQuestions.get(QuizActivity.this.questionNumber++);
            this.updateItemsAdapterView();
            QuizActivity.this.questionsAnswered = false;


        } else {

            Toast.makeText(this, R.string.answer_question, Toast.LENGTH_SHORT).show();
        }
    }
}
