package edu.escuelaing.arep.taller6.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import edu.escuelaing.arep.taller6.exception.PropertyListingException;
import edu.escuelaing.arep.taller6.model.Property;
import edu.escuelaing.arep.taller6.services.PropertyListingServices;

class PropertyListingControllerImplTest {

    @Mock
    private PropertyListingServices service;

    @InjectMocks
    private PropertyListingControllerImpl controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPropertyById_Found() throws PropertyListingException {
        Property property = new Property();
        property.setId(1L);
        when(service.getPropertyById(1L)).thenReturn(property);

        ResponseEntity<Object> response = controller.getPropertyById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(property, response.getBody());
        verify(service, times(1)).getPropertyById(1L);
    }

    @Test
    void testGetPropertyById_NotFound() throws PropertyListingException {
        when(service.getPropertyById(1L)).thenThrow(new PropertyListingException("Property not found"));

        ResponseEntity<Object> response = controller.getPropertyById(1L);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        verify(service, times(1)).getPropertyById(1L);
    }

    @Test
    void testGetProperties() {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(service.getProperties()).thenReturn(properties);

        ResponseEntity<Object> response = controller.getProperties();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(properties, response.getBody());
        verify(service, times(1)).getProperties();
    }
    
    @Test
    void testCreateProperty_Success() throws PropertyListingException {
        Property property = new Property();
        
        // Simular que el método createProperty se ejecuta sin lanzar excepciones
        doReturn(property).when(service).createProperty(any(Property.class));
    
        ResponseEntity<Object> response = controller.createProperty(property);
    
        assertEquals(201, response.getStatusCode().value());
        assertEquals(property, response.getBody());
        verify(service, times(1)).createProperty(property);
    }

    @Test
    void testCreateProperty_Failure() throws PropertyListingException {
        Property property = new Property();
        doThrow(new PropertyListingException("Invalid property"))
                .when(service).createProperty(property);

        ResponseEntity<Object> response = controller.createProperty(property);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        verify(service, times(1)).createProperty(property);
    }

    @Test
    void testUpdateProperty_Success() throws PropertyListingException {
        Property property = new Property();
        property.setAddress("Updated Address");
        Map<String, String> updates = Map.of("address", "Updated Address");
        when(service.updateProperty(1L, updates)).thenReturn(property);

        ResponseEntity<Object> response = controller.updateProperty(1L, updates);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(property, response.getBody());
        verify(service, times(1)).updateProperty(1L, updates);
    }

    @Test
    void testUpdateProperty_NotFound() throws PropertyListingException {
        Map<String, String> updates = Map.of("address", "Updated Address");
        when(service.updateProperty(1L, updates)).thenThrow(new PropertyListingException("Property not found"));

        ResponseEntity<Object> response = controller.updateProperty(1L, updates);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        verify(service, times(1)).updateProperty(1L, updates);
    }

    @Test
    void testDeleteProperty_Success() throws PropertyListingException {
        Property property = new Property();
        when(service.deleteProperty(1L)).thenReturn(property);

        ResponseEntity<Object> response = controller.deleteProperty(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(property, response.getBody());
        verify(service, times(1)).deleteProperty(1L);
    }

    @Test
    void testDeleteProperty_NotFound() throws PropertyListingException {
        when(service.deleteProperty(1L)).thenThrow(new PropertyListingException("Property not found"));

        ResponseEntity<Object> response = controller.deleteProperty(1L);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        verify(service, times(1)).deleteProperty(1L);
    }
}
