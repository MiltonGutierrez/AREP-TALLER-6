package edu.escuelaing.arep.taller6.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.arep.taller6.exception.PropertyListingException;
import edu.escuelaing.arep.taller6.model.Property;
import edu.escuelaing.arep.taller6.services.PropertyListingServices;

@RestController
@RequestMapping("/api")
public class PropertyListingControllerImpl implements PropertyListingController {

    private static final String ERROR_KEY = "error";
    private PropertyListingServices propertyListingService;

    @Autowired
    public PropertyListingControllerImpl(PropertyListingServices propertyListingServices) {
        this.propertyListingService = propertyListingServices;
    }

    @Override
    @GetMapping("/property/{id}")
    public ResponseEntity<Object> getPropertyById(@PathVariable Long id) {
        Property property;
        try {
            property = propertyListingService.getPropertyById(id);
            return new ResponseEntity<>(property, HttpStatus.OK);
        } catch (PropertyListingException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @GetMapping("/property")
    public ResponseEntity<Object> getProperties(){
        List<Property> properties;
        properties = propertyListingService.getProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


    @Override
    @GetMapping("/property/price/{min}/{max}")
    public ResponseEntity<Object> getPropertiesByPriceRange(@PathVariable double min, @PathVariable double max) {
        List<Property> properties;
        try {
            properties = propertyListingService.getPropertiesByPriceRange(min, max);
            return new ResponseEntity<>(properties, HttpStatus.OK);
        } catch (PropertyListingException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @GetMapping("/property/size/{min}/{max}")
    public ResponseEntity<Object> getPropertiesBySizeRange(@PathVariable double min, @PathVariable double max) {
        List<Property> properties;
        try {
            properties = propertyListingService.getPropertiesBySizeRange(min, max);
            return new ResponseEntity<>(properties, HttpStatus.OK);
        } catch (PropertyListingException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    @PutMapping("/property/{id}")
    public ResponseEntity<Object> updateProperty(@PathVariable Long id, @RequestBody Map<String, String> queryParams) {
        Property property;
        try {
            property = propertyListingService.updateProperty(id, queryParams);
            return new ResponseEntity<>(property, HttpStatus.OK);
        } catch (PropertyListingException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @DeleteMapping("/property/{id}")
    public ResponseEntity<Object> deleteProperty(@PathVariable Long id) {
        Property property;
        try {
            property = propertyListingService.deleteProperty(id);
            return new ResponseEntity<>(property, HttpStatus.OK);
        } catch (PropertyListingException e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PostMapping("/property")
    public ResponseEntity<Object> createProperty(@RequestBody Property property) {
        try {
            propertyListingService.createProperty(property);
            return new ResponseEntity<>(property,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(ERROR_KEY, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}