package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the MainScreen.java controller class. This class controls the main page.
 */
public class MainScreen implements Initializable {

    @FXML
    private TextField mainPartsSearchTextField;
    @FXML
    private TableView<Part> mainPartsTableView;
    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;
    @FXML
    private TableColumn<Part, String> mainPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> mainPartsInventoryLevelColumn;
    @FXML
    private TableColumn<Part, Double> mainPartsPricePerUnitColumn;

    @FXML
    private TextField mainProductsSearchTextField;
    @FXML
    private TableView<Product> mainProductsTableView;
    @FXML
    private TableColumn<Product, Integer> mainProductsIDColumn;
    @FXML
    private TableColumn<Product, String> mainProductsNameColumn;
    @FXML
    private TableColumn<Product, Integer> mainProductsInventoryLevelColumn;
    @FXML
    private TableColumn<Product, Double> mainProductsPricePerUnitColumn;

    /**
     * A method to change the screen from the main screen outwards. For use when other conditions are required before
     * changing the scene, like having a part or product selected for modification.
     * @param event onAction for changing scenes
     * @param screen the file location for the relevant file
     */
    @FXML
    public void changeScreen(ActionEvent event, String screen) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(screen));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * A pop-up error alert with strings as parameters for ease of use throughout the entire application.
     * To be used for any potential errors.
     @param h The header string.
     @param c The content string.
     */
    public static void popUpError(String h, String c) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setResizable(true);
        error.getDialogPane().setPrefSize(350, 190);
        error.setTitle("  ");
        error.setHeaderText(h);
        error.setContentText(c);
        error.showAndWait();
    }


    /**
     * A pop-up success alert with strings as parameters for ease of use throughout the entire application.
     * To be used for any potential successes.
     @param h The header string.
     @param c The content string.
     */
    public static void popUpSuccess(String h, String c) {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setResizable(true);
        success.getDialogPane().setPrefSize(350, 190);
        success.setTitle("  ");
        success.setHeaderText(h);
        success.setContentText(c);
        success.showAndWait();
    }

    /**
     * This pop-up also doubles as a decision maker. If either the part or product delete button is pressed,
     * this will make sure that an item is selected, confirms that the user wants to delete, and deletes.
     * @param isProduct A Boolean to help decide whether it is a part or a product.
     * @param h The header string.
     * @param c The content string.
     */
    public void confirmDeletePopUp(Boolean isProduct, String h, String c) {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setResizable(true);
        warning.getDialogPane().setPrefSize(350, 190);
        warning.setTitle("  ");
        warning.setHeaderText(h);
        warning.setContentText(c);
        Optional<ButtonType> result = warning.showAndWait();
        if (!isProduct) {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part selectedPart;
                selectedPart = mainPartsTableView.getSelectionModel().getSelectedItem();
                Inventory.deletePart(Inventory.lookupPartID(selectedPart.getId()));
                popUpSuccess("SUCCESS", "The part has successfully been deleted.");
                mainPartsTableView.setItems(Inventory.getAllParts());
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                warning.close();
            }
        } else {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product selectedProduct;
                selectedProduct = mainProductsTableView.getSelectionModel().getSelectedItem();
                Inventory.deleteProduct(Inventory.lookupProductID(selectedProduct.getId()));
                popUpSuccess("SUCCESS", "The product has successfully been deleted.");
                mainProductsTableView.setItems(Inventory.getAllProducts());
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                warning.close();
            }
        }
    }

    /**
     * This cancel button is referenced by the other four controllers to come back to this page in the event that
     * the user selects "Cancel" and warns them that any changes they may have made will not be saved. The user
     * must press the "Save" button to save any made changes.
     * @param event not associated with any button on this MainScreen controller
     */
    @FXML
    public static void cancelButton(ActionEvent event) throws IOException {

        Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);
        cancel.setResizable(true);
        cancel.getDialogPane().setPrefSize(350, 190);
        cancel.setTitle("  ");
        cancel.setHeaderText("Are you sure you want to go back?");
        cancel.setContentText("If you made any changes, they will not be saved.");

        Optional<ButtonType> option = cancel.showAndWait();

        if (option.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(MainScreen.class.getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setTitle("Back to Main");
            stage.setScene(scene);
            stage.show();
        } else {
            cancel.close();
        }
    }

    /**
     * Changes the scene back to the main screen. Only used by other controllers.
     * @param event not associated with any button on the MainScreen controller
     */
    @FXML
    public static void saveButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainScreen.class.getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Back to Main");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Calls the exitProgram() method when the EXIT button is clicked.
     * @param event onAction for clicking the EXIT button
     */
    @FXML
    private void exitProgram(ActionEvent event) {
        exitProgram();
    }

    /**
     * Confirms with the user that they want to exit the program. OK exits, Cancel closes the alert.
     */
    public void exitProgram() {

        Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
        exit.setTitle(" ");
        exit.setHeaderText("Are you sure want to exit this program?");

        Optional<ButtonType> option = exit.showAndWait();

        if (option.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            exit.close();
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
     * @param event onAction event for clicking the part side's search button
     */
    @FXML
    void partSearch(ActionEvent event) {
        try {
            String s = mainPartsSearchTextField.getText();
            ObservableList<Part> result = Inventory.lookupPart(s); //checks against string names and puts in "result".

            if (s.isEmpty()) {
                mainPartsTableView.setItems(Inventory.getAllParts());
            } else if (result.size() == 0) { //if there was no match in the string name.
                try {
                    int i = Integer.parseInt(s);
                    Part p = Inventory.lookupPartID(i);
                    if (p != null) {
                        result.add(p);
                    } else {
                        popUpError("PART NOT FOUND", "Displaying all parts.");
                        mainPartsTableView.setItems(Inventory.getAllParts());
                        return;
                    }
                } catch (NumberFormatException e) {
                    popUpError("ERROR", "Invalid input. Please try again.");
                    mainPartsTableView.setItems(Inventory.getAllParts());
                    return;
                }
                mainPartsTableView.setItems(Inventory.getAllParts());
            } else {//if there was a match in the STRING
                mainPartsTableView.setItems(result);
            } // basically, there's no point to the else statement as is bc of the below line.
            mainPartsTableView.setItems(result);
        } finally {
            mainPartsSearchTextField.setText("");
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
     * @param event onAction event for clicking the product side's search button
     */
    @FXML
    void productSearch(ActionEvent event) {
        try {
            String s = mainProductsSearchTextField.getText();
            ObservableList<Product> result = Inventory.lookupProduct(s); //checks against string names and puts in "result".

            if (s.isEmpty()) {
                mainPartsTableView.setItems(Inventory.getAllParts());
            } else if (result.size() == 0) { //if there was no match in the string name.
                try {
                    int i = Integer.parseInt(s);
                    Product p = Inventory.lookupProductID(i);
                    if (p != null) {
                        result.add(p);
                    } else {
                        popUpError("NOT FOUND", "It's not a product name or ID... Displaying all products..");
                        mainProductsTableView.setItems(Inventory.getAllProducts());
                        return;
                    }
                } catch (NumberFormatException e) {
                    popUpError("ERROR", "Invalid input. Please try again.");
                    mainProductsTableView.setItems(Inventory.getAllProducts());
                    return;
                }
                mainProductsTableView.setItems(Inventory.getAllProducts());
            } else { //if there was a match in the STRING
                mainProductsTableView.setItems(result);
            } // basically, there's no point to the else statement as is bc of the below line.
            mainProductsTableView.setItems(result);
        } finally {
            mainProductsSearchTextField.setText("");
        }
    }

    /**
     * This method allows the user to hit the "enter" key when searching rather than having to click the Search button.
     * @param event KeyEvent for hitting the enter key at the search bar
     */
    @FXML
    void enterPartSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent();
            partSearch(actionEvent);
        }
    }

    /**
     * This method allows the user to hit the "enter" key when searching rather than having to click the Search button.
     * @param event KeyEvent for hitting the enter key at the search bar
     */
    @FXML
    void enterProductSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent actionEvent = new ActionEvent();
            productSearch(actionEvent);
        }
    }

    /**
     * Opens Add Part scene
     * @param event onAction event for clicking the part side's add button
     */
    @FXML
    public void openAddPartScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens Modify Part Scene. Requires that a part be selected first or else a pop-up comes up.
     * Also sends the part data over to the Modify Part page.
     * @param event onAction event for clicking the part side's modify button
     */
    @FXML
    public void openModifyPartScreen(ActionEvent event) throws IOException {
        if (mainPartsTableView.getSelectionModel().isEmpty()) {
            popUpError("ERROR", "Please select a part to be modified.");
        } else {
            Part selectedPart = mainPartsTableView.getSelectionModel().getSelectedItem();
            int selectedPartIndex = mainPartsTableView.getSelectionModel().getSelectedIndex();
            ModifyPart.getPart(selectedPart, selectedPartIndex);
            changeScreen(event, "/view/ModifyPart.fxml");
        }
    }

    /**
     * Opens Add Product scene
     * @param event onAction event for clicking the product side's add button
     */
    @FXML
    public void openAddProductScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens Modify Product Scene. Requires that a part be selected first or else a pop-up comes up.
     * Also sends the part data over to the Modify Product page.
     * @param event onAction event for clicking the product side's modify button
     */
    @FXML
    public void openModifyProductScreen(ActionEvent event) throws IOException {
        if (mainProductsTableView.getSelectionModel().isEmpty()) {
            popUpError("ERROR", "Please select a product to be modified.");
        } else {
            Product selectedProduct = mainProductsTableView.getSelectionModel().getSelectedItem();
            int selectedProductIndex = mainProductsTableView.getSelectionModel().getSelectedIndex();
            ModifyProduct.getProduct(selectedProduct, selectedProductIndex);
            changeScreen(event, "/view/ModifyProduct.fxml");
        }
    }

    /**
     * Makes sure that a part is selected for deletion, confirms with the user.
     * @param event onAction event for clicking the part side's delete button
     */
    @FXML
    void partDelete(ActionEvent event) {
        if (!mainPartsTableView.getSelectionModel().isEmpty()) {
            confirmDeletePopUp(false, "WAIT", "Are you sure you want to delete this part?");
        } else {
            popUpError("ERROR", "Please make a selection.");
        }
    }

    /**
     * Makes sure that a product is selected for deletion. Checks for associated parts, and confirms with the user.
     * @param event onAction event for clicking the product side's Delete button
     */
    @FXML
    void productDelete(ActionEvent event) {
        if (!mainProductsTableView.getSelectionModel().isEmpty()) {
            Product p = mainProductsTableView.getSelectionModel().getSelectedItem();
            if (!Product.hasAssociatedParts(p)) {
                popUpError("ERROR", "This product has associated parts. You may not delete a product with associated parts.");
            } else {
                confirmDeletePopUp(true, "WAIT", "Are you sure you want to delete this product?");
            }
        } else {
            popUpError("ERROR", "Please make a selection.");
        }
    }

    /**
     * This is the @Override initialize page for the MainScreen.java controller.
     * Loads both the Parts and Products tables.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");

        mainPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartsInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartsPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductsInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductsPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainPartsTableView.setItems(Inventory.getAllParts());
        mainProductsTableView.setItems(Inventory.getAllProducts());
    }
}
