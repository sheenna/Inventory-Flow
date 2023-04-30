package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This is the AddPart.java controller class. This class controls the Add Part page.
 */
public class AddPart implements Initializable {

    boolean inHouse = true;

    @FXML
    private Label addPartTypeLabel;
    @FXML
    private TextField addPartInvTextField;
    @FXML
    private TextField addPartNameTextField;
    @FXML
    private TextField addPartIDTextField;
    @FXML
    private TextField addPartMinTextField;
    @FXML
    private TextField addPartMaxTextField;
    @FXML
    private TextField addPartPriceCostTextField;
    @FXML
    private TextField addPartTypeTextField;
    @FXML
    private RadioButton addPartOutsourcedRadioButton;
    @FXML
    private RadioButton addPartInHouseRadioButton;
    @FXML
    private Button addPartSaveButton;

    /**
     * This method makes sure that when the outsourced radio button is checked, that three things happen.
     * First, the "Company Name" label shows. Second, the inHouse boolean value is set to "false". And third,
     * the in-house radio button is not selected. The inHouse value is solidly tied to the relevant button and
     * both were not able to be selected simultaneously.
     * @param event onAction event for clicking the in-house radio button
     */
    @FXML
    private void inHouseType(ActionEvent event) {
        addPartTypeLabel.setText("Machine ID");
        inHouse = true;
        addPartOutsourcedRadioButton.setSelected(false);
    }

    /**
     * This method makes sure that when the outsourced radio button is checked, that three things happen.
     * First, the "Company Name" label shows. Second, the inHouse boolean value is set to "false". And third,
     * the in-house radio button is not selected. The inHouse value is solidly tied to the relevant button and
     * both were not able to be selected simultaneously.
     * @param event onAction event for clicking the outsourced radio button
     */
    @FXML
    private void outsourcedType(ActionEvent event) {
        addPartTypeLabel.setText("Company Name");
        inHouse = false;
        addPartInHouseRadioButton.setSelected(false);
    }

    /**
     * This is the "Save" onAction for the AddPart.java controller class.
     * It grabs all the necessary variables (making sure to throw any NumberFormatExceptions below) and also
     * implements all the necessary validation checks (Inventory level, Min/Max checks, etc.). Then after any
     * potential errors are out of the way, the data makes its way into being sent into Inventory as a new
     * Part. Added in checks for making sure that negative numbers would come up with a pop-up error and
     * returns them.
     * @param event onAction event for clicking the Save button
     */
    @FXML
    private void addPartSave(ActionEvent event) {
        Part p = null;
        try {

            int id = ((Inventory.getLastPartID() + 1));
            String name = addPartNameTextField.getText();
            int stock = Integer.parseInt(addPartInvTextField.getText());
            double price = Double.parseDouble(addPartPriceCostTextField.getText());
            int max = Integer.parseInt(addPartMaxTextField.getText());
            int min = Integer.parseInt(addPartMinTextField.getText());

            if (max <= min) {
                MainScreen.popUpError("ERROR", "Maximum inventory level must be greater than minimum inventory level. " +
                        "Please re-enter your values.");
                return;
            } else if (stock > max || stock < min) {
                MainScreen.popUpError("ERROR", "Inventory level must be in between maximum and minimum inventory level. " +
                        "Please re-enter your values.");
                return;
            } else if ((stock < 0) || (min < 0) || (max <= 0)) {
                MainScreen.popUpError("ERROR", "Values cannot be less than zero. Please re-enter your values. ");
                return;
            } else {
                if (inHouse) {
                    try {
                        int machineID = Integer.parseInt(addPartTypeTextField.getText());
                        if (machineID <= 0) {
                            MainScreen.popUpError("ERROR", "Values cannot be less than zero. Please re-enter your values.");
                            return;
                        } else {
                            p = new InHouse(id, name, price, stock, min, max, machineID);
                            Inventory.addPart(p);
                            MainScreen.saveButton(event);
                        }
                    } catch (NumberFormatException e) {
                        MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
                        return;
                    }
                } else {
                    String companyName = addPartTypeTextField.getText();
                    p = new Outsourced(id, name, price, stock, max, min, companyName);
                    Inventory.addPart(p);
                    MainScreen.saveButton(event);
                }
            }
        } catch (NumberFormatException | IOException e) {
            MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
        }
    }


    /**
     * Calls a method from the MainScreen.java controller to take the user back to the main page.
     * @param event onAction event for the cancel button
     */
    @FXML
    private void addPartReturn(ActionEvent event) throws IOException {
        MainScreen.cancelButton(event);
    }




    /**
     * This is the @Override initialize page for the ModifyPart.java controller.
     * @param url url parameter for initialize
     * @param resourceBundle resource bundle for initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * This is the @Override initialize method for the AddPart.java controller class.
         * Upon initializing the page, the type label is set to machine ID, the outsourced radio button is set to false,
         * the in-house radio button is set to true. Helps ensure that there are no errors between a part type.
         */
        addPartTypeLabel.setText("Machine ID");
        addPartOutsourcedRadioButton.setSelected(false);
        addPartInHouseRadioButton.setSelected(true);

        /**
         * Disables ID textField and sets the text to "AUTO-GEN" to tell the user this value is auto-generated.
         */
        addPartIDTextField.setText("AUTO-GEN");
        addPartIDTextField.setDisable(true);

        /**
         *  This section disables the Save button if it is found that any of the text fields are empty,
         *  in order to ensure that no blank fields are passed into a part.
         */
        addPartSaveButton.disableProperty().bind(addPartNameTextField.textProperty().isEmpty()
                .or(addPartPriceCostTextField.textProperty().isEmpty()
                        .or(addPartInvTextField.textProperty().isEmpty()
                                .or(addPartMaxTextField.textProperty().isEmpty()
                                        .or(addPartTypeTextField.textProperty().isEmpty())))));
    }
}
