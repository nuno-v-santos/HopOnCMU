package sample;

import M2M.*;
import M2T.Java;
import M2T.M2TClass;
import M2TBd.*;
import Web.PureHTML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import metamodels.*;
import metamodels.Class;
import utils.Build;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Model model;

    @FXML
    private TextField superName;

    @FXML
    void addSuper(ActionEvent event) {

        Class c = listViewClasses.getSelectionModel().getSelectedItem();

        System.out.println("o");
        if (c != null) {
            c = model.getClassbyName(c.getName());
            String supeN = superName.getText();
            System.out.println(supeN);
            System.out.println(supeN.isEmpty());
            System.out.println(c.getExtend());
            if (supeN.isEmpty()) {

                Class a = c.getExtend();

                if (a != null) {
                    a.setSuperClass(false);
                }

                c.setExtend(null);
                System.out.println(c.getExtend());
                updateClassList();
            } else {
                Class a = model.getClassbyName(supeN);
                if (a != null) {
                    c.setExtend(a);
                    a.setSuperClass(true);
                }
            }

        }
        model.getClasses().remove(c);
        updateClassList();
        model.getClasses().add(c);
        updateClassList();
    }

    @FXML
    private BorderPane ap;

    @FXML
    private ComboBox<RelationType> rType;

    @FXML
    private ListView<Relation> relationList;

    @FXML
    private TextField rClass1;

    @FXML
    private TextField rClass2;

    @FXML
    private ComboBox<M2TBd> basedados;

    @FXML
    private ComboBox<M2TClass> linguagem;

    @FXML
    private TextField aMinLength;

    @FXML
    private TextField aMaxLength;

    @FXML
    private TextField atributoTipo;

    @FXML
    private TextField aContains;

    @FXML
    private TextField atributoNome;

    @FXML
    private CheckBox aRequired;

    @FXML
    private CheckBox aEmpty;

    @FXML
    private TextField aMin;

    @FXML
    private TextField aMax;

    @FXML
    private TextField classAdName;

    @FXML
    private ListView<Attribute> listViewAtributos;

    @FXML
    private ListView<Class> listViewClasses;

    @FXML
    void adicionarClass(ActionEvent event) {

        if (!classAdName.getText().isEmpty()) {
            model.addClass(new Class(classAdName.getText()));
            ObservableList<Class> items = FXCollections.observableArrayList(model.getClasses());
            listViewClasses.setItems(items);
        }

    }

    @FXML
    void removerClass(ActionEvent event) {

        Class c = listViewClasses.getSelectionModel().getSelectedItem();

        if (c != null) {
            model.getClasses().remove(c);
            ObservableList<Class> items = FXCollections.observableArrayList(model.getClasses());
            listViewClasses.setItems(items);
            listViewAtributos.setItems(null);
        }

    }

    @FXML
    void adicionarAtributo(ActionEvent event) {

        Class c = listViewClasses.getSelectionModel().getSelectedItem();

        if (c != null) {
            c = model.getClassbyName(c.getName());
            String nome = atributoNome.getText();
            String tipo = atributoTipo.getText();

            if (!nome.isEmpty() && !tipo.isEmpty()) {
                c.addAttribute(new Attribute(nome, tipo));
            }

            updateAttributeList();

        }

    }

    @FXML
    void removerAtributo(ActionEvent event) {
        Class c = listViewClasses.getSelectionModel().getSelectedItem();

        if (c != null) {
            c = model.getClassbyName(c.getName());
            Attribute a = listViewAtributos.getSelectionModel().getSelectedItem();
            if (a != null) {
                c.getAttributes().remove(a);
                updateAttributeList();
            }
        }
    }

    @FXML
    void updateAtribute(ActionEvent event) {

        Attribute a = listViewAtributos.getSelectionModel().getSelectedItem();
        if (a != null) {

            a.setRequired(aRequired.isSelected());

            if (a.getType().equals("String")) {
                a.setNotEmpty(aEmpty.isSelected());

                if (!aContains.getText().isEmpty()) {
                    a.setContains(aContains.getText());
                }

                if (!aMaxLength.getText().isEmpty()) {
                    a.setMaxLength(new Integer(aMaxLength.getText()));
                }
                if (!aMinLength.getText().isEmpty()) {
                    a.setMinLength(new Integer(aMinLength.getText()));
                }
            } else {
                if (!aMax.getText().isEmpty()) {
                    a.setMax(new Integer(aMax.getText()));
                }
                if (!aMin.getText().isEmpty()) {
                    a.setMin(new Integer(aMin.getText()));
                }
            }

            updateAttributeList();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        XMLTOModel xm = new XMLTOModel("src/model/person.xml");
        System.out.println("HELO");
        model = xm.getModel();

        linguagem.setItems(FXCollections.observableArrayList(new Java()));
        linguagem.getSelectionModel().selectFirst();
        basedados.setItems(FXCollections.observableArrayList(new Sqlite3(), new Mysql()));
        basedados.getSelectionModel().selectFirst();
        rType.setItems(FXCollections.observableArrayList(RelationType.OneToOne, RelationType.OneToMany, RelationType.ManyToOne, RelationType.ManyToMany));
        rType.getSelectionModel().selectFirst();
        updateClassList();
        updateRelationList();

        listViewClasses.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Class superC = listViewClasses.getSelectionModel().getSelectedItem().getExtend();

                if (superC != null) {
                    superName.setText(superC.getName());
                } else {
                    superName.setText("");
                }

                updateAttributeList();
            }
        });

        listViewAtributos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateAttributeList();
            }
        });

    }

    public void updateClassList() {

        System.out.println("ola");
        ObservableList<Class> items = FXCollections.observableArrayList(model.getClasses());
        listViewClasses.setItems(items);

        Class superC = listViewClasses.getSelectionModel().getSelectedItem() != null ? listViewClasses.getSelectionModel().getSelectedItem().getExtend() : null;

        if (superC != null) {
            superName.setText(superC.getName());
        } else {
            superName.setText("");
        }


    }


    public void updateRelationList() {

        ObservableList<Relation> items = FXCollections.observableArrayList(model.getRelations());
        relationList.setItems(items);
    }

    public void updateAttributeList() {
        try {
            ObservableList<Attribute> itemsAt = FXCollections.observableArrayList(listViewClasses.getSelectionModel().getSelectedItem().getAttributes());
            listViewAtributos.setItems(itemsAt);

            Attribute a = listViewAtributos.getSelectionModel().getSelectedItem();
            if (a != null) {
                aRequired.setSelected(a.isRequired());
                aEmpty.setSelected(a.getNotEmpty());
                aMinLength.setText(a.getMinLength() == null ? "" : a.getMinLength() + "");
                aMaxLength.setText(a.getMaxLength() == null ? "" : a.getMaxLength() + "");
                aMin.setText(a.getMin() == null ? "" : a.getMin() + "");
                aMax.setText(a.getMax() == null ? "" : a.getMax() + "");
                aContains.setText(a.getContains());
            }

        } catch (Exception e) {
            listViewAtributos.setItems(null);
        }
    }

    @FXML
    void build(ActionEvent event) {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("src/proj/");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(ap.getScene().getWindow());

        Build b = new Build(linguagem.getSelectionModel().getSelectedItem(), null, basedados.getSelectionModel().getSelectedItem(), new PureHTML());
        if (selectedDirectory != null) {
            b.build(model, selectedDirectory);
        }else{
            b.build(model);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Build");
        alert.setHeaderText("Build");
        alert.setContentText("Success");
        alert.showAndWait();

    }


    @FXML
    void addRelation(ActionEvent event) {


        String c1 = rClass1.getText();
        String c2 = rClass2.getText();
        RelationType relationType = rType.getValue();
        Class cc1 = model.getClassbyName(c1);
        Class cc2 = model.getClassbyName(c2);

        if (cc1 != null && cc2 != null) {
            model.addRelation(new Relation(cc1, cc2, relationType));
        }

        updateRelationList();


    }

    @FXML
    void removeRelation(ActionEvent event) {
        Relation r = relationList.getSelectionModel().getSelectedItem();

        if (r != null) {
            model.getRelations().remove(r);
            updateRelationList();
        }

    }


    @FXML
    void loadXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedDirectory = fileChooser.showOpenDialog(ap.getScene().getWindow());

        System.out.println(selectedDirectory);

        XMLTOModel xmltoModel = new XMLTOModel(selectedDirectory.toString());

        try {
            model = xmltoModel.getModel();
            updateRelationList();
            updateClassList();
            updateAttributeList();
        } catch (Exception ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Load");
            alert.setHeaderText("Load");
            alert.setContentText("Failed to load xml");
            alert.showAndWait();
        }

    }

    @FXML
    void loadXMI(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedDirectory = fileChooser.showOpenDialog(ap.getScene().getWindow());

        System.out.println(selectedDirectory);

        XMITOModel xmitoModel = new XMITOModel(selectedDirectory.toString());

        try {
            model = xmitoModel.getModel();
            updateRelationList();
            updateClassList();
            updateAttributeList();
        } catch (Exception ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Load");
            alert.setHeaderText("Load");
            alert.setContentText("Failed to load xml");
            alert.showAndWait();
        }
    }

}
