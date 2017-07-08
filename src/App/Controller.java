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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bachp on 7/1/2017.
 */
public class Controller {
    private ObservableList<Mode> modes = FXCollections.observableArrayList(Mode.values());
    private ObservableList<Tense> tenses = FXCollections.observableArrayList(Tense.values());
    private String textField = "";
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
    private WebView wv;
    @FXML
    private HBox buttons;
    @FXML
    private Button conjugate;
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
        buttons.setAlignment(Pos.CENTER_RIGHT);
        conjugate.setDefaultButton(true);
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
        final String notFoundMessage = "<p style=\"color:#762817;\"><em>verb not found!</em></p>";
        final String modeTenseSpecificationMessage = "<p style=\"color:#762817;\"><em>please specify mode and tense for conjugation!</em></p>";
        final WebEngine engine = wv.getEngine();
        String verb = inputTextField.getText();
        Mode mode = (Mode) modeComboBox.getSelectionModel().getSelectedItem();
        Tense tense = (Tense) tenseComboBox.getSelectionModel().getSelectedItem();
        try {
            if( (!verb.equals("") && !textField.equals(verb.toLowerCase())) || mode != m || tense != t ) {
                textField = verb.toLowerCase();
                m = mode; t = tense;
                OutputWriter[] ows = Program.conjugateVerb(verb, mode, tense);
                if(ows == null)
                    engine.loadContent(notFoundMessage);
                else{
                    for(OutputWriter ow : ows){
                        engine.loadContent(ow.toHTMLFormat());
                    }
                }
            }
        } catch (NullPointerException e) {
            engine.loadContent(modeTenseSpecificationMessage);
        }
    }
}
