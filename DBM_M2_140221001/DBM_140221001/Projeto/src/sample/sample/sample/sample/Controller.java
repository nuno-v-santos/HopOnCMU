package sample.sample.sample.sample;

import M2M.XMLTOModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import metamodels.Class;
import metamodels.Model;

public class Controller {

    @FXML
    private ListView<Class> listViewAtributos;

    @FXML
    private ListView<Class> listViewClasses;


    public Controller(){

       XMLTOModel xm = new XMLTOModel("src/model/person.xml");

       Model md = xm.getModel();


       for (Class c : md.getClasses()){

           listViewAtributos.getItems().add(c);
       }
    }


}
