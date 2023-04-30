package model;

/**
 * This is the InHouse class, extends the Part class. InHouse is a type of part. Has additional variable "Machine ID"
 */
public class InHouse extends Part {

    private int machineID;

    /**
     * Defines the in-house part type.
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock number of part currently in stock
     * @param min minimum level of parts that should stay in stock
     * @param max maximum level of parts that should stay in stock
     * @param machineID machine ID of the machine that made the part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * machineID setter
     * @param machineID machine ID of the machine that made the part
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * machineID getter
     * @return returns the machineID
     */
    public int getMachineID() {
        return machineID;
    }

}
