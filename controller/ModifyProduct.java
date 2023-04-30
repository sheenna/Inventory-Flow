package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
 * This is the ModifyProduct.java controller class. This class controls the Modify Product page.
 */
public class ModifyProduct implements Initializable {

    private static Product product;
    private static int productIndex;

    /**
     * A "temporary basket" for holding all associated parts until the product is ready to be added to the inventory.
     * This ensures that before the save button is clicked and the user confirms the changes, that no real changes
     * will be made.
     */
    ObservableList<Part> tempPartBasket = FXCollections.observableArrayList();

    @FXML
    private TableView modifyProductTableViewB;
    @FXML
    private TableColumn modifyProductIDColumnB;
    @FXML
    private TableColumn modifyProductNameColumnB;
    @FXML
    private TableColumn modifyProductInventoryLevelColumnB;
    @FXML
    private TableColumn modifyProductPricePerUnitColumnB;
    @FXML
    private Button modifyProductSaveButton;
    @FXML
    private TextField modifyProductSearchTextField;
    @FXML
    private TableView modifyProductTableViewA;
    @FXML
    private TableColumn modifyProductIDColumnA;
    @FXML
    private TableColumn modifyProductNameColumnA;
    @FXML
    private TableColumn modifyProductInventoryLevelColumnA;
    @FXML
    private TableColumn modifyProductPricePerUnitColumnA;
    @FXML
    private TextField modifyProductMinTextField;
    @FXML
    private TextField modifyProductPriceTextField;
    @FXML
    private TextField modifyProductMaxTextField;
    @FXML
    private TextField modifyProductInvTextField;
    @FXML
    private TextField modifyProductNameTextField;
    @FXML
    private TextField modifyProductIDTextField;

    /**
     * Takes the selected product from the MainScreen.java controller and puts it in two local variables.
     * @param selectedProduct the product being received
     * @param selectedProductIndex the index value of product being received
     */
    public static void getProduct(Product selectedProduct, int selectedProductIndex) {
        product = selectedProduct;
        productIndex = selectedProductIndex;
    }

    /**
     * Makes sure that the user must select a part to be added. If there is no part to be added, displays an
     * error pop-up. Also contains a check to make sure that the same part cannot be added to a product
     * multiple times.
     * @param event onAction event for clicking the add button
     */
    @FXML
    private void modifyProductAdd(ActionEvent event) {
        Part selectedPart = (Part) modifyProductTableViewA.getSelectionModel().getSelectedItem();
        if (modifyProductTableViewA.getSelectionModel().isEmpty()) {
            MainScreen.popUpError("ERROR", "Please select a part.");
        } else if (tempPartBasket.contains(selectedPart)) {
            MainScreen.popUpError("ERROR", "You can't add the same part twice!");
        } else {
            tempPartBasket.add(selectedPart);
            modifyProductTableViewB.setItems(tempPartBasket);
        }
    }

    /**
     * Makes sure that the user must select a part to be deleted. If there is no part to be deleted, displays an
     * error pop-up.
     * @param event onAction event for clicking the delete button
     */
    @FXML
    private void modifyProductDelete(ActionEvent event) {
        Part selectedPart = (Part) modifyProductTableViewB.getSelectionModel().getSelectedItem();
        if (modifyProductTableViewB.getSelectionModel().isEmpty()) {
            MainScreen.popUpError("ERROR", "Please select a part.");
        } else {
            tempPartBasket.remove(selectedPart);
            modifyProductTableViewB.setItems(tempPartBasket);
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
     * @param event onAction event for clicking the search button
     */
    @FXML
    private void modifyProductSearch(ActionEvent event) {
        try {
            String s = modifyProductSearchTextField.getText();
            ObservableList<Part> result = Inventory.lookupPart(s); //checks against string names and puts in "result".

            if (s.isEmpty()) {
                modifyProductTableViewA.setItems(Inventory.getAllParts());
            } else if (result.size() == 0) { //if there was no match in the string name.
                try {
                    int i = Integer.parseInt(s);
                    Part p = Inventory.lookupPartID(i);
                    if (p != null) {
                        result.add(p);
                    } else {
                        MainScreen.popUpError("PART NOT FOUND", "Displaying all parts.");
                        modifyProductTableViewA.setItems(Inventory.getAllParts());
                        return;
                    }
                } catch (NumberFormatException e) {
                    MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
                    modifyProductTableViewA.setItems(Inventory.getAllParts());
                    return;
                }
                modifyProductTableViewA.setItems(Inventory.getAllParts());
            } else {//if there was a match in the STRING
                modifyProductTableViewA.setItems(result);
            } // basically, there's no point to the else statement as is bc of the below line.
            modifyProductTableViewA.setItems(result);
        } finally {
            modifyProductSearchTextField.setText("");
        }
    }

    /**
     * This method allows the user to hit the "enter" key when searching rather than having to click the Search button.
     * @param event KeyEvent for hitting the enter key at the search bar
     */
    @FXML
    private void modifyProductSearchBar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent();
            modifyProductSearch(actionEvent);
        }
    }

    /**
     * Calls a method from the MainScreen.java controller to take the user back to the main page.
     * @param event onAction event for clicking the cancel button
     */
    @FXML
    private void modifyProductReturn(ActionEvent event) throws IOException {
        MainScreen.cancelButton(event);
    }

    /**
     * This is the "Save" onAction for the ModifyProduct.java controller class.
     * It grabs all the necessary variables (making sure to throw any NumberFormatExceptions below) and also
     * implements all the necessary validation checks (Inventory level, Min/Max checks, etc.). Then after any
     * potential errors are out of the way, the data makes its way into being sent into Inventory as a new
     * Part. Added in checks for making sure that negative numbers would come up with a pop-up error and
     * returns them.
     * @param event onAction event for clicking the save button
     */
    @FXML
    private void modifyProductSave(ActionEvent event) {
        Product updatedProduct = null;

        try {
            int id = Integer.parseInt(modifyProductIDTextField.getText());
            String name = modifyProductNameTextField.getText();
            int stock = Integer.parseInt(modifyProductInvTextField.getText());
            double price = Double.parseDouble(modifyProductPriceTextField.getText());
            int max = Integer.parseInt(modifyProductMaxTextField.getText());
            int min = Integer.parseInt(modifyProductMinTextField.getText());

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
                updatedProduct = new Product(id, name, price, stock, min, max);
                for (Part p : tempPartBasket) {
                    updatedProduct.deleteAssociatedPart(p);
                    updatedProduct.addAssociatedPart(p);
                }
                Inventory.updateProduct(productIndex, updatedProduct);
            }
            MainScreen.saveButton(event);
        } catch (NumberFormatException | IOException e) {
            MainScreen.popUpError("ERROR", "Invalid input. Please try again.");
        }
    }


    /**
     * This is the @Override initializer for the ModifyProduct.java controller
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         *  Pulls the selected Product data and sets the Text Field values as such.
         */
        modifyProductIDTextField.setText(Integer.toString(product.getId()));
        modifyProductNameTextField.setText(product.getName());
        modifyProductInvTextField.setText(Integer.toString(product.getStock()));
        modifyProductPriceTextField.setText(Double.toString(product.getPrice()));
        modifyProductMaxTextField.setText(Integer.toString(product.getMax()));
        modifyProductMinTextField.setText(Integer.toString(product.getMin()));


        /**
         *  This section disables the Save button if it is found that any of the text fields are empty,
         *  in order to ensure that no blank fields are passed into a part.
         */
        modifyProductSaveButton.disableProperty().bind(modifyProductNameTextField.textProperty().isEmpty()
                .or(modifyProductInvTextField.textProperty().isEmpty().or(modifyProductPriceTextField.textProperty().isEmpty()
                        .or(modifyProductMaxTextField.textProperty().isEmpty().or(modifyProductMinTextField.textProperty().isEmpty())))));

        /**
         * Disables ID textField and sets the text to "AUTO-GEN" to tell the user this value is auto-generated.
         */
        modifyProductIDTextField.setDisable(true);



        /**
         * Sets the top and bottom Tableview.
         */
        modifyProductIDColumnA.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductNameColumnA.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductInventoryLevelColumnA.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPricePerUnitColumnA.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductTableViewA.setItems(Inventory.getAllParts());

        //"B" means the bottom table - the "associated parts" table.
        modifyProductIDColumnB.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductNameColumnB.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductInventoryLevelColumnB.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPricePerUnitColumnB.setCellValueFactory(new PropertyValueFactory<>("price"));



        tempPartBasket = product.getAllAssociatedParts();

        /** RUNTIME ERROR
         * I was struggling with having the associated parts not staying saved to the product. I changed the way I
         * approached the issue, and considered it like a temporary shopping basket at the grocery store where you're
         * putting everything you want in it, but you still haven't decided whether you want everything just yet.
         * (In this analogy, the Save button is the check-out counter).
         */
        modifyProductTableViewB.setItems(tempPartBasket);


    }
}

