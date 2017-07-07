package App;

import DataStructure.Mode;
import DataStructure.Tense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bachp on 7/1/2017.
 */
public class Controller {
    private ObservableList<Mode> modes = FXCollections.observableArrayList(Mode.values());
    private ObservableList<Tense> tenses = FXCollections.observableArrayList(Tense.values());
    private Set<String> textField = new HashSet <>();
    private Mode m;
    private Tense t;
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
        tenseComboBox.setCellFactory(lv -> new ListCell<Tense>(){
            @Override
            public void updateItem(Tense item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                } else{
                    setText(item.toString());
                    setDisable(true);
                }
            }
        });
        SplitPane.setResizableWithParent(leftPane, Boolean.FALSE);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        displayArea.setWrapText(true);
    }
    public void onPickedMode(){
        tenseComboBox.setCellFactory(lv -> new ListCell<Tense>(){
            @Override
            public void updateItem(Tense item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                } else{
                    setText(item.toString());
                    setDisable(!isTenseInMode(
                            item,
                            (Mode) modeComboBox.getSelectionModel().getSelectedItem()
                    ));
                }
            }
        });
        tenseComboBox.valueProperty().set(null);
    }
    private boolean isTenseInMode(Tense tense, Mode mode){
        if(mode == null) return false;
        return mode.isTenseInMode(tense);
    }
    public void conjugateButtonClicked(){
        String verb = inputTextField.getText();
        Mode mode = (Mode) modeComboBox.getSelectionModel().getSelectedItem();
        Tense tense = (Tense) tenseComboBox.getSelectionModel().getSelectedItem();
        if( (!verb.equals("") && !textField.contains(verb.toLowerCase())) || mode != m || tense != t ) {
            textField.add(verb.toLowerCase());
            m = mode; t = tense;
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
