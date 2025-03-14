package edu.escuelaing.arep.taller6.controller.interfaces;


import java.util.Map;

import org.springframework.http.ResponseEntity;

import edu.escuelaing.arep.taller6.model.Property;

public interface PropertyListingController {
    ResponseEntity<Object> getPropertyById(Long id);

    ResponseEntity<Object> getProperties();

    ResponseEntity<Object> updateProperty(Long id, Property property);

    ResponseEntity<Object> deleteProperty(Long id);

    ResponseEntity<Object> createProperty(Property property);

    ResponseEntity<Object> getPropertiesByPriceRange(double min, double max);

    ResponseEntity<Object> getPropertiesBySizeRange(double min, double max);

}
