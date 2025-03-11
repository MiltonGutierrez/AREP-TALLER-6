package edu.escuelaing.arep.taller6.exception;

public class PropertyListingException extends Exception{

    public static final String PROPERTY_NOT_FOUND = "Property not found";
    public static final String PROPERTY_NOT_UPDATED = "Property could not be updated";
    public static final String MISSING_PROPERTY_PARAMETERS = "Missing property parameters";
    public static final String INVALID_PRICE_RANGE = "Invalid price range";
    public static final String INVALID_SIZE_RANGE = "Invalid size range";

    public PropertyListingException(String message){
        super(message);
    }
    
}
