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
    ObservableList<Mode> modes = FXCollections.observableArrayList(Mode.values());
    ObservableList<Tense> tenses = FXCollections.observableArrayList(Tense.values());
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
    }
    public void conjugateButtonClicked(){
        String verb = inputTextField.getText();
        Mode mode = (Mode) modeComboBox.getSelectionModel().getSelectedItem();
        Tense tense = (Tense) tenseComboBox.getSelectionModel().getSelectedItem();
        OutputWriter[] ow = Program.conjugateVerb(verb, mode, tense);
    }
    private void appendTextArea(OutputWriter ow){

    }
}
