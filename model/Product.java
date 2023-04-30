package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * This is the controller for the Product.java controller.
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Defining Product type
     * @param id id
     * @param name name
     * @param price price
     * @param stock inventory level
     * @param min min
     * @param max max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * id setter
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * name setter
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * price setter
     * @param price price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * inventory level setter
     * @param stock stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * min setter
     * @param min max
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * max setter
     * @param max max
     */
    public void setMax(int max) {
        this.max = max;
    }


    /**
     * id getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * price getter
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * inventory level getter
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * min getter
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * max getter
     * @return max
     */
    public int getMax() {
        return max;
    }


    /**
     * Adds associated part to associated part list
     * @param part part being passed
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes associated part from associated part list
     * @param selectedPart part
     * @return boolean
     */
    public boolean deleteAssociatedPart(Part selectedPart) {
        if (associatedParts.contains(selectedPart)) {
            associatedParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns all parts associated with the product
     * @return observablelist of part type
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }

    //Methods I wrote outside of UML diagram

    /**
     * A quick way to tell if a product has any associated parts.
     * @param selectedProduct product being passed as a parameter
     * @return boolean
     */
    public static boolean hasAssociatedParts(Product selectedProduct) {
        return selectedProduct.getAllAssociatedParts().isEmpty();
    }

}
