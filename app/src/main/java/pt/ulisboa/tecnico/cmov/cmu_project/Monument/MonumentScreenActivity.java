package pt.ulisboa.tecnico.cmov.cmu_project.Monument;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizActivity;
import pt.ulisboa.tecnico.cmov.cmu_project.Quiz.QuizQuestion;
import pt.ulisboa.tecnico.cmov.cmu_project.R;

public class MonumentScreenActivity extends AppCompatActivity {

    private MonumentData monData;
    public static final String MONUMENT_DATA = "MONUMENT_DATA";
    private int nClick = 0;
    private boolean validSSID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monument_screen);


        Intent intent = getIntent();
        this.monData = (MonumentData) intent.getSerializableExtra(MONUMENT_DATA);
        this.updateUIWithMonumentInfo();

    }

    /**
     * Função que actualiza a interface de acordo com o objecto MonumentData
     */
    private void updateUIWithMonumentInfo() {
        ImageView imageView = findViewById(R.id.imgViewMonument);
        TextView monumentNameTxtView = findViewById(R.id.txtMonumentName);
        TextView monumentInfoTxtView = findViewById(R.id.txtMonumentInfo);

        imageView.setBackground(getResources().getDrawable(this.monData.getImID()));
        monumentNameTxtView.setText(this.monData.getMonumentName());
        monumentInfoTxtView.setText(this.monData.getMonumentDescription());
    }

    /**
     * Funcão que trata do evento onClick no botão que faz o download do Quiz
     *
     * @param view
     */
    public void btnDownloadQuizOnClick(View view) {

        Intent intent = new Intent(this, QuizActivity.class);
        QuizQuestion question = new QuizQuestion("Qual é o melhor clube","Benfica","Porto","Sporting","Braga");
        QuizQuestion question2 = new QuizQuestion("Melhor filme","Harry Potter","Pulp Fiction","Insidious","Dalila");
        ArrayList<QuizQuestion> quiz =new ArrayList<>();
        quiz.add(question);
        quiz.add(question2);
        intent.putExtra(QuizActivity.QUIZ_QUESTIONS,quiz);
        intent.putExtra(QuizActivity.MONUMENT_IMG,this.monData.getImID());
        startActivity(intent);


    }
}
