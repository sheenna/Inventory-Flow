package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Inventory model class. Acts as a go-between for parts and products.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Adds part
     * @param newPart part
     */
    public static void addPart(Part newPart) { //might have to be newPart not part
        allParts.add(newPart);
    }

    /**
     * Adds product
     * @param newProduct
     */
    public static void addProduct(Product newProduct) { //might have to be newProduct not newProduct
        allProducts.add(newProduct);
    }

    /**
     * Looks up part ID by int. Cycles through and looks for a match.
     * @param partID int
     * @return part
     */
    public static Part lookupPartID(int partID) {
        Part p = null;
        for (Part part : getAllParts()) {
            if (part.getId() == partID) {
                p = part;
            }
        }
        return p;
    }

    /**
     * Looks up product ID by int. Cycles through and looks for a match.
     * @param productID int
     * @return product
     */
    public static Product lookupProductID(int productID) {
        Product p = null;
        for (Product product : getAllProducts()) {
            if (product.getId() == productID) {
                p = product;
            }
        }
        return p;
    }

    /**
     * Looks up part by String. Changes all cases to lowercase, cycles through, and looks for a match.
     * @param partName string
     * @return Observable List of part-type
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        for (Part part : getAllParts()) {
            if (part.getName().toLowerCase().startsWith(partName.toLowerCase())) {
                partSearchResults.add(part);
            }
        }
        return partSearchResults;
    }

    /**
     * Looks up product by String. Changes all cases to lowercase, cycles through, and looks for a match.
     * @param productName string
     * @return Observable List of part-type
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productSearchResults = FXCollections.observableArrayList();
        for (Product product : getAllProducts()) {
            if (product.getName().toLowerCase().startsWith(productName.toLowerCase())) {
                productSearchResults.add(product);
            }
        }
        return productSearchResults;
    }

    /**
     * Updates the exact part being sent in
     * @param index int
     * @param selectedPart part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the exact product being sent in
     * @param index int
     * @param selectedProduct product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes part
     * @param selectedPart part
     * @return boolean
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes product
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Gets all current Parts in Inventory
     * @return Observable List of part-type
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets all current Products in Inventory
     * @return Observable List of product-type
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    //Methods I wrote outside of the UML diagram.

    /**
     * In order to auto-generate new Part IDs, I take the part ID of the last part in the array and increment by one.
     * Incrementing is done in the controller classes, as this method is solely for getting the last part ID.
     * @return int
     */
    public static int getLastPartID() {
        int i = 0;
        Part p = null;
        i = ((getAllParts().size()) - 1);
        p = allParts.get(i);
        i = p.getId();
        return i;
    }

    /**
     * In order to auto-generate new Product IDs, I take the part ID of the last part in the array and increment by one.
     * Incrementing is done in the controller classes, as this method is solely for getting the last product ID.
     * @return int
     */
    public static int getLastProductID() {
        int i = 0;
        Product p = null;
        i = ((getAllProducts().size()) - 1);
        p = allProducts.get(i);
        i = p.getId();
        return i;
    }

    /**
     * Test data made for testing purposes.
     */
    public static void addTestData() {


        //test data
        Part partTest1 = new Outsourced(1, "A", 4.99, 44, 20, 100, "ABC Company");
        Part partTest2 = new Outsourced(2, "B", 10.99, 76, 10, 100, "BusinessCorp");
        Part partTest3 = new InHouse(3, "C", 2.99, 32, 20, 100, 123);
        Part partTest4 = new Outsourced(4, "D", 14.99, 22, 20, 100, "ThingsCo");
        Part partTest5 = new Outsourced(5, "H", 4.99, 12, 10, 100, "Test Inc.");
        Part partTest6 = new InHouse(6, "I", 12.99, 84, 20, 100, 124);
        Part partTest7 = new Outsourced(7, "N", 4.99, 40, 20, 100, "Money Co.");
        Part partTest8 = new Outsourced(8, "Q", 8.99, 55, 10, 100, "Top Company");
        Part partTest9 = new InHouse(9, "S", 2.99, 70, 20, 100, 123);
        Part partTest10 = new InHouse(10, "Z", 12.99, 100, 20, 100, 124);

        Inventory.addPart(partTest1);
        Inventory.addPart(partTest2);
        Inventory.addPart(partTest3);
        Inventory.addPart(partTest4);
        Inventory.addPart(partTest5);
        Inventory.addPart(partTest6);
        Inventory.addPart(partTest7);
        Inventory.addPart(partTest8);
        Inventory.addPart(partTest9);
        Inventory.addPart(partTest10);

        Product productTest1 = new Product(1, "ABC", 21.99, 21, 15, 100);
        Product productTest2 = new Product(2, "DEF", 45.99, 98, 15, 100);
        Product productTest3 = new Product(3, "GHI", 9.99, 62, 15, 100);
        Product productTest4 = new Product(4, "JKL", 67.99, 15, 15, 100);
        Product productTest5 = new Product(5, "MNO", 12.99, 48, 15, 100);
        Product productTest6 = new Product(6, "PQR", 125.99, 24, 15, 100);
        Product productTest7 = new Product(7, "STU", 20.99, 16, 15, 100);
        Product productTest8 = new Product(8, "VWX", 15.99, 72, 15, 100);
        Product productTest9 = new Product(9, "YZ0", 22.99, 90, 15, 100);
        Product productTest10 = new Product(10, "123", 215.99, 18, 15, 100);

        Inventory.addProduct(productTest1);
        Inventory.addProduct(productTest2);
        Inventory.addProduct(productTest3);
        Inventory.addProduct(productTest4);
        Inventory.addProduct(productTest5);
        Inventory.addProduct(productTest6);
        Inventory.addProduct(productTest7);
        Inventory.addProduct(productTest8);
        Inventory.addProduct(productTest9);
        Inventory.addProduct(productTest10);
    }

}
