package edu.escuelaing.arep.taller6.services.interfaces;

import java.util.List;
import java.util.Map;

import edu.escuelaing.arep.taller6.exception.PropertyListingException;
import edu.escuelaing.arep.taller6.model.Property;

public interface PropertyListingServices {

    List<Property> getProperties();

    List<Property> getPropertiesByPriceRange(double min, double max) throws PropertyListingException;

    List<Property> getPropertiesBySizeRange(double min, double max) throws PropertyListingException;

    Property getPropertyById(Long id) throws PropertyListingException;

    Property updateProperty(Long id, Property property) throws PropertyListingException;

    Property deleteProperty(Long id) throws PropertyListingException;

    Property createProperty(Property property) throws PropertyListingException;

    
}