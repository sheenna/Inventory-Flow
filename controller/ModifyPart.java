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
 * This is the ModifyPart.java controller class. This class controls the Modify Part page.
 */
public class ModifyPart implements Initializable {

    private static Part part = null;
    private static int partIndex;
    boolean inHouse = true;

    @FXML
    private Label modifyPartTypeLabel;
    @FXML
    private TextField modifyPartMaxTextField;
    @FXML
    private TextField modifyPartMinTextField;
    @FXML
    private TextField modifyPartIDTextField;
    @FXML
    private TextField modifyPartInvTextField;
    @FXML
    private TextField modifyPartNameTextField;
    @FXML
    private TextField modifyPartPriceCostTextField;
    @FXML
    private RadioButton modifyPartInHouseRadioButton;
    @FXML
    private RadioButton modifyPartOutsourcedRadioButton;
    @FXML
    private TextField modifyPartTypeTextField;
    @FXML
    private Button modifyPartSaveButton;

    /**
     * Takes the selected part from the MainScreen.java controller and puts it in two local variables.
     * @param selectedPart the part being received
     * @param selectedPartIndex the index value of part being received
     */
    public static void getPart(Part selectedPart, int selectedPartIndex) {
        part = selectedPart;
        partIndex = selectedPartIndex;
    }

    /**
     * This method makes sure that when the outsourced radio button is checked, that three things happen.
     * First, the "Company Name" label shows. Second, the inHouse boolean value is set to "false". And third,
     * the in-house radio button is not selected. The inHouse value is solidly tied to the relevant button and
     * both were not able to be selected simultaneously.
     * @param event onAction event for clicking the outsourced radio button
     */
    @FXML
    private void outsourcedRadioButton(ActionEvent event) {
        modifyPartTypeLabel.setText("Company Name");
        inHouse = false;
        modifyPartInHouseRadioButton.setSelected(false);
        if (part instanceof Outsourced) {
            modifyPartTypeTextField.setText(((Outsourced) part).getCompanyName());
        } else {
            modifyPartTypeTextField.setText("");
        }
    }

    /**
     * This method makes sure that when the outsourced radio button is checked, that three things happen.
     * First, the "Company Name" label shows. Second, the inHouse boolean value is set to "false". And third,
     * the in-house radio button is not selected. The inHouse value is solidly tied to the relevant button and
     * both were not able to be selected simultaneously.
     * @param event onAction event for clicking the in-house radio button
     */
    @FXML
    private void inHouseRadioButton(ActionEvent event) {
        modifyPartTypeLabel.setText("Machine ID");
        inHouse = true;
        modifyPartOutsourcedRadioButton.setSelected(false);
        if (part instanceof InHouse) {
            modifyPartTypeTextField.setText(Integer.toString(((InHouse) part).getMachineID()));
        } else {
            modifyPartTypeTextField.setText("");
        }
    }

    /**
     * This search method takes the string from the text field and puts it into s.
     * 2. s is sent to the lookup in Inventory to be matched against the strings.
     * 3. If unsuccessful, the method checks to see if s was empty in the first place.
     * If so, then it immediately sets the tableview to show all.
     * 4. Then if the results of the string look-up were empty, it parses s for an ID
     * and again, sends it to the int lookup equivalent in Inventory.
     * 5. It then goes through some checks to "rule out" some  different errors.
     * Of note: Added the required invalid input validation check, as well as showing
     * when it wasn't found in the first place. At the end, it sets the search field
     * to be blank for convenience.
     *
     * @param event onAction event for clicking the save button
     */
    @FXML
    private void modifyPartSave(ActionEvent event) {
        Part updatedPart = null;

        try {

            int id = Integer.parseInt(modifyPartIDTextField.getText());
            String name = modifyPartNameTextField.getText();
            int stock = Integer.parseInt(modifyPartInvTextField.getText());
            double price = Double.parseDouble(modifyPartPriceCostTextField.getText());
            int max = Integer.parseInt(modifyPartMaxTextField.getText());
            int min = Integer.parseInt(modifyPartMinTextField.getText());
            String companyName = modifyPartTypeTextField.getText();

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
                        int machineID = Integer.parseInt(modifyPartTypeTextField.getText());
                        if (machineID <= 0) {
                            MainScreen.popUpError("ERROR", "Values cannot be less than zero. Please re-enter your values.");
                            return;
                        } else {
                            updatedPart = new InHouse(id, name, price, stock, min, max, machineID);
                            Inventory.updatePart(partIndex, updatedPart);
                            MainScreen.saveButton(event);
                        }
                    } catch (NumberFormatException e) {
                        MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
                        return;
                    }
                } else {
                    updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(partIndex, updatedPart);
                    MainScreen.saveButton(event);
                }
            }
        } catch (NumberFormatException | IOException e) {
            MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
            return;
        }
    }

    /**
     * Calls a method from the MainScreen.java controller to take the user back to the main page.
     * @param event onAction event for clicking the cancel button
     */
    @FXML
    private void modifyPartReturn(ActionEvent event) throws IOException {
        MainScreen.cancelButton(event);
    }

    /**
     * This is the @Override initialize page for the ModifyPart.java controller.
     * @param url url parameter
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        /**
         *  This section disables the Save button if it is found that any of the text fields are empty,
         *  in order to ensure that no blank fields are passed into a part.
         */
        modifyPartSaveButton.disableProperty().bind(modifyPartNameTextField.textProperty().isEmpty()
                .or(modifyPartPriceCostTextField.textProperty().isEmpty()
                        .or(modifyPartInvTextField.textProperty().isEmpty()
                                .or(modifyPartMaxTextField.textProperty().isEmpty()
                                        .or(modifyPartTypeTextField.textProperty().isEmpty())))));

        /**
         * Changes the in-house/outsourced differences based on which type the part is an instance of.
         */
        if (part instanceof InHouse) {
            modifyPartInHouseRadioButton.setSelected(true);
            modifyPartTypeLabel.setText("Machine ID");
            modifyPartTypeTextField.setText(Integer.toString(((InHouse) part).getMachineID()));
        } else if (part instanceof Outsourced) {
            modifyPartOutsourcedRadioButton.setSelected(true);
            modifyPartTypeLabel.setText("Company Name");
            modifyPartTypeTextField.setText(((Outsourced) part).getCompanyName());
        } else {
            MainScreen.popUpError("ERROR", "Something went wrong! Please try again.");
            System.exit(0);
        }

        /**
         * Sets all of the text fields to the part that is being brought in.
         */
        modifyPartIDTextField.setText(Integer.toString(part.getId()));
        modifyPartNameTextField.setText(part.getName());
        modifyPartPriceCostTextField.setText(Double.toString(part.getPrice()));
        modifyPartInvTextField.setText(Integer.toString(part.getStock()));
        modifyPartMaxTextField.setText(Integer.toString(part.getMax()));
        modifyPartMinTextField.setText(Integer.toString(part.getMin()));

        /**
         * Disables ID textField and sets the text to "AUTO-GEN" to tell the user this value is auto-generated.
         */
        modifyPartIDTextField.setDisable(true);

        /**
         * Sets the inHouse boolean value upon initializing.
         */
        if (modifyPartInHouseRadioButton.isSelected()) {
            inHouse = true;
        } else if (modifyPartOutsourcedRadioButton.isSelected()) {
            inHouse = false;
        } else {
            MainScreen.popUpError("ERROR", "Something went wrong! Please try again.");
            System.exit(0);
        }
    }


}
