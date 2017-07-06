package App;

import DataStructure.Mode;
import DataStructure.Tense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Created by bachp on 7/1/2017.
 */
public class Controller {
    private ObservableList<Mode> modes = FXCollections.observableArrayList(Mode.values());
    private ObservableList<Tense> tenses = FXCollections.observableArrayList(Tense.values());
    private String textField = "";

    @FXML
    private SplitPane splitPane;
    @FXML
    private ComboBox modeComboBox;
    @FXML
    private ComboBox tenseComboBox;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextArea displayArea;
    @FXML
    private HBox buttons;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private void initialize(){
        modeComboBox.setItems(modes);
        tenseComboBox.setItems(tenses);
        SplitPane.setResizableWithParent(leftPane, Boolean.FALSE);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        displayArea.setWrapText(true);
    }
    public void conjugateButtonClicked(){
        String verb = inputTextField.getText();
        if(!verb.equals("") && !verb.equals(textField)){
            textField = verb;
            Mode mode = (Mode) modeComboBox.getSelectionModel().getSelectedItem();
            Tense tense = (Tense) tenseComboBox.getSelectionModel().getSelectedItem();
            OutputWriter[] ows = Program.conjugateVerb(verb, mode, tense);
            for(OutputWriter ow : ows){
                appendTextArea(ow);
            }
        }
    }
    private void appendTextArea(OutputWriter ow){
        displayArea.appendText(ow.toString());
        displayArea.appendText("\n");
    }
}
