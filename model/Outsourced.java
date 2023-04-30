package model;

/**
 * This is the Outsourced class, extends the Part class. Outsourced is a type of part. Has additional variable "Company Name"
 */
public class Outsourced extends Part {

    private String companyName;

    /**
     * Defines the outsourced part type.
     * @param id part id
     * @param name part name
     * @param price price of part
     * @param stock amount of parts currently in stock
     * @param min minimum level of parts that should stay in stock
     * @param max maximum level of parts that should be in stock
     * @param companyName company name of the company that produced the part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * company name setter
     * @param companyName company name of the company that produced the part
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * company name getter
     * @return company name of the company that produced the part
     */
    public String getCompanyName() {
        return companyName;
    }

}


