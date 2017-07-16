package app;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;
import structure.Mode;
import structure.Tense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Created by bachp on 7/1/2017.
 */
public class Controller {
    private final ObservableList<Mode> modes = FXCollections.observableArrayList(Mode.values());
    private final ObservableList<Tense> tenses = FXCollections.observableArrayList(Tense.values());

    private String textField = "";
    private static final StringBuilder sb = new StringBuilder();

    private Mode m;
    private Tense t;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ComboBox modeComboBox;

    @FXML
    private Button roundButton;
    private PopOver popOver;
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
        new Thread(() -> Platform.runLater(() -> new Program())).start();
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
        //TextFields.bindAutoCompletion(inputTextField.getText(), Verb.getVerbsListString());
        SplitPane.setResizableWithParent(leftPane, Boolean.FALSE);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        conjugate.setDefaultButton(true);
    }

    /**
     * show accents pop over when click on button
     */
    public void showAccents(){
        try {
            GridPane gridPane = new GridPane();
            final String[] accents = {"ç","â","à","ê","é","è","ë","î","ï","ô","û" ,"ù", "ü"};
            int i = 0;
            for(int r = 0; r < 3; ++r){
                for(int c = 0; c < 5; ++c){
                    if(i == accents.length) break;
                    String accent = accents[i];
                    Button btn = new Button(accent);
                    btn.getStyleClass().add("customButton");
                    btn.setOnAction(e -> {
                        //when clicked will append special accent to the text field
                        inputTextField.appendText(accent);
                    });
                    gridPane.add(btn,c,r);
                    i++;
                }
            }

            popOver = new PopOver(gridPane);
            popOver.setDetachable(false);
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
            popOver.setOnCloseRequest(e -> popOver.hide());
            popOver.show(roundButton);

        } catch (Exception e) {}
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

    /**
     * check if tense is in s specific mode
     * this method is delegate to isTenseInMode in Mode class
     * @param tense
     * @param mode
     * @return
     */
    private boolean isTenseInMode(Tense tense, Mode mode) {
        return mode != null && mode.isTenseInMode(tense);
    }

    public void conjugateButtonClicked(){
        final String notFoundMessage =
                "<p style=\"color:#762817;\"><em>verb not found!</em></p>";
        final String modeComboBoxMessage =
                "<p style=\"color:#762817;\"><em>please specify mode in combo box!</em></p>";
        final String tenseComboBoxMessage =
                "<p style=\"color:#762817;\"><em>please specify tense in combo box!</em></p>";
        final String modeTenseComboBoxMessage =
                "<p style=\"color:#762817;\"><em>please specify mode and tense in combo boxes!</em></p>";

        final WebEngine engine = wv.getEngine();

        String verb = inputTextField.getText();
        Mode mode = (Mode) modeComboBox.getSelectionModel().getSelectedItem();
        Tense tense = (Tense) tenseComboBox.getSelectionModel().getSelectedItem();

        if(verb.equals("")) return;

        if(mode == null && tense == null){
            engine.loadContent(modeTenseComboBoxMessage);
            return;
        }
        else if(mode == null) {
            engine.loadContent(modeComboBoxMessage);
            return;
        }
        else if(tense == null){
            engine.loadContent(tenseComboBoxMessage);
            return;
        }
        try {
            if(!textField.equals(verb.toLowerCase()) || mode != m || tense != t ) {
                textField = verb.toLowerCase();
                m = mode; t = tense;
                OutputWriter[] ows = new OutputWriter[0];
                try {
                    ows = Program.conjugateVerb(verb, mode, tense);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if(ows == null)
                    engine.loadContent(notFoundMessage);
                else{
                    for(OutputWriter ow : ows){
                        sb.append(ow.toHTMLFormat());
                    }
                    engine.loadContent(sb.toString());
                    sb.setLength(0);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
