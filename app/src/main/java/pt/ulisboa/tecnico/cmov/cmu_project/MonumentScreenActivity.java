package pt.ulisboa.tecnico.cmov.cmu_project;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MonumentScreenActivity extends AppCompatActivity {

    private MonumentData monData;
    private final String MONUMENT_DATA="MONUMNET_DATA";
    private int nClick = 0;
    private boolean validSSID = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monument_screen);

        this.updateUIWithMonumentInfo();
        Intent intent = getIntent();
        this.monData = (MonumentData) intent.getSerializableExtra(MONUMENT_DATA);

    }

    /**
     * Função que actualiza a interface de acordo com o objecto MonumentData
     */
    private void updateUIWithMonumentInfo(){
        ImageView imageView = findViewById(R.id.imgViewMonument);
        TextView monumentNameTxtView = findViewById(R.id.txtMonumentName);
        TextView monumentInfoTxtView = findViewById(R.id.txtMonumentInfo);

        imageView.setImageResource(this.monData.getImID());
        monumentNameTxtView.setText(this.monData.getMonumentName());
        monumentInfoTxtView.setText(this.monData.getMonumentDescription());
    }

    /**
     * Funcão que trata do evento onClick no botão que faz o download do Quiz
     * @param view
     */
    public void btnDownloadQuizOnClick(View view) {

        /* Validar o ssd beacon */

        if (this.validSSID && this.nClick>=2){
            /*
            * Lançar actividade do quiz
            *
            * */
        }


        this.nClick++;

    }
}
