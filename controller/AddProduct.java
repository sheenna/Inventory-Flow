package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This is the AddProduct.java controller class. This class controls the Add Product page.
 */
public class AddProduct implements Initializable {

    private static Product newProduct;

    /**
     * A "temporary basket" for holding all associated parts until the product is ready to be added to the inventory.
     * This ensures that before the save button is clicked and the user confirms the changes, that no real changes
     * will be made.
     */
    ObservableList<Part> tempPartBasket = FXCollections.observableArrayList();

    @FXML
    private TextField addProductSearchTextField;
    @FXML
    private TableColumn addProductPricePerUnitColumnB;
    @FXML
    private TableColumn addProductInventoryLevelColumnB;
    @FXML
    private TableColumn addProductNameColumnB;
    @FXML
    private TableColumn addProductIDColumnB;
    @FXML
    private TableView addProductTableViewB;
    @FXML
    private TableColumn addProductPricePerUnitColumnA;
    @FXML
    private TableColumn addProductInventoryLevelColumnA;
    @FXML
    private TableColumn addProductNameColumnA;
    @FXML
    private TableColumn addProductIDColumnA;
    @FXML
    private TableView addProductTableViewA;
    @FXML
    private TextField addProductMinTextField;
    @FXML
    private TextField addProductMaxTextField;
    @FXML
    private TextField addProductPriceTextField;
    @FXML
    private TextField addProductInvTextField;
    @FXML
    private TextField addProductNameTextField;
    @FXML
    private TextField addProductIDTextField;
    @FXML
    private Button addProductSaveButton;



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
     * @param event onAction event for clicking the Search button
     */
    @FXML
    private void addProductSearch(ActionEvent event) {
        try {
            String s = addProductSearchTextField.getText();
            ObservableList<Part> result = Inventory.lookupPart(s); //checks against string names and puts in "result".

            if (s.isEmpty()) {
                addProductTableViewA.setItems(Inventory.getAllParts());
            } else if (result.size() == 0) { //if there was no match in the string name.
                try {
                    int i = Integer.parseInt(s);
                    Part p = Inventory.lookupPartID(i);
                    if (p != null) {
                        result.add(p);
                    } else {
                        MainScreen.popUpError("PART NOT FOUND", "Displaying all parts.");
                        addProductTableViewA.setItems(Inventory.getAllParts());
                        return;
                    }
                } catch (NumberFormatException e) {
                    MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
                    addProductTableViewA.setItems(Inventory.getAllParts());
                    return;
                }
                addProductTableViewA.setItems(Inventory.getAllParts());
            } else {//if there was a match in the STRING
                addProductTableViewA.setItems(result);
            }
            addProductTableViewA.setItems(result);
        } finally {
            addProductSearchTextField.setText("");
        }
    }

    /**
     * This method allows the user to hit the "enter" key when searching rather than having to click the Search button.
     * @param event onAction event for hitting the enter key at the search bar
     */
    @FXML
    private void addProductSearchBar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent();
            addProductSearch(actionEvent);
        }
    }

    /**
     * Makes sure that the user must select a part to be added. If there is no part to be added, displays an
     * error pop-up. Also contains a check to make sure that the same part cannot be added to a product
     * multiple times.
     * @param event onAction event for clicking the Add button
     */
    @FXML
    private void addProductAdd(ActionEvent event) {
        Part selectedPart = (Part) addProductTableViewA.getSelectionModel().getSelectedItem();
        if (addProductTableViewA.getSelectionModel().isEmpty()) {
            MainScreen.popUpError("ERROR", "Please select a part.");
        } else if (tempPartBasket.contains(selectedPart)) {
            MainScreen.popUpError("ERROR", "You can't add the same part twice!");
        } else {
            tempPartBasket.add(selectedPart);
            addProductTableViewB.setItems(tempPartBasket);
        }
    }

    /**
     * Makes sure that the user must select a part to be deleted. If there is no part to be deleted, displays an
     * error pop-up.
     * @param event onAction event for clicking the Delete button
     */
    @FXML
    private void addProductDelete(ActionEvent event) {
        Part selectedPart = (Part) addProductTableViewB.getSelectionModel().getSelectedItem();
        if (addProductTableViewB.getSelectionModel().isEmpty()) {
            MainScreen.popUpError("ERROR", "Please select a part.");
        } else {
            tempPartBasket.remove(selectedPart);
            addProductTableViewB.setItems(tempPartBasket);
        }
    }

    /**
     * Calls a method from the MainScreen.java controller to take the user back to the main page.
     * @param event onAction event for clicking the Cancel button
     */
    @FXML
    private void addProductReturn(ActionEvent event) throws IOException {
        MainScreen.cancelButton(event);
    }

    /**
     * This is the "Save" onAction for the AddProduct.java controller class.
     * It grabs all the necessary variables (making sure to throw any NumberFormatExceptions below) and also
     * implements all the necessary validation checks (Inventory level, Min/Max checks, etc.). Then after any
     * potential errors are out of the way, the data makes its way into being sent into Inventory as a new
     * Part. Added in checks for making sure that negative numbers would come up with a pop-up error and
     * returns them.
     * @param event onAction event for clicking the Save button
     */
    @FXML
    private void addProductSave(ActionEvent event) {

        try {
            int id = ((Inventory.getLastProductID() + 1));
            String name = addProductNameTextField.getText();
            int stock = Integer.parseInt(addProductInvTextField.getText());
            double price = Double.parseDouble(addProductPriceTextField.getText());
            int max = Integer.parseInt(addProductMaxTextField.getText());
            int min = Integer.parseInt(addProductMinTextField.getText());

            if (max <= min) {
                MainScreen.popUpError("ERROR", "Maximum inventory level must be greater than minimum inventory level. " +
                        "Please re-enter your values.");
                return;
            } else if (stock > max || stock < min) {
                MainScreen.popUpError("ERROR", "Inventory level must be in between maximum and minimum inventory level. " +
                        "Please re-enter your values.");
                return;
            } else if ((stock < 0) || (min < 0) || (max <= 0)) {
                MainScreen.popUpError("ERROR", "Values cannot be less than zero. Please re-enter your values. " +
                        "Please re-enter your values.");
                return;
            } else {
                newProduct = new Product(id, name, price, stock, min, max);
                for (Part p : tempPartBasket) {
                    newProduct.deleteAssociatedPart(p);
                    newProduct.addAssociatedPart(p);
                }
                Inventory.addProduct(newProduct);
            }
            MainScreen.saveButton(event);
        } catch (NumberFormatException | IOException e) {
            MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
        }
    }




    /**
     * This is the @Override initialize page for the AddProduct.java controller.
     * @param url url parameter for initialize
     * @param resourceBundle resource bundle for initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Disables ID textField and sets the text to "AUTO-GEN" to tell the user this value is auto-generated.
         */
        addProductIDTextField.setText("AUTO-GEN");
        addProductIDTextField.setDisable(true);

        /**
         *  This section disables the Save button if it is found that any of the text fields are empty,
         *  in order to ensure that no blank fields are passed into a part.
         */
        addProductSaveButton.disableProperty().bind(addProductNameTextField.textProperty().isEmpty()
                .or(addProductInvTextField.textProperty().isEmpty().or(addProductPriceTextField.textProperty().isEmpty()
                        .or(addProductMaxTextField.textProperty().isEmpty().or(addProductMinTextField.textProperty().isEmpty())))));


        addProductIDColumnA.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductNameColumnA.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryLevelColumnA.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPricePerUnitColumnA.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductTableViewA.setItems(Inventory.getAllParts());

        addProductIDColumnB.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductNameColumnB.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryLevelColumnB.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPricePerUnitColumnB.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
}
