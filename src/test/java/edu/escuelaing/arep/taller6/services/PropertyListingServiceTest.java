package edu.escuelaing.arep.taller6.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.escuelaing.arep.taller6.exception.PropertyListingException;
import edu.escuelaing.arep.taller6.model.Property;
import edu.escuelaing.arep.taller6.repository.PropertyListingRepository;
import edu.escuelaing.arep.taller6.services.impl.PropertyListingServicesImpl;

class PropertyListingServicesImplTest {

    @Mock
    private PropertyListingRepository repository;

    @InjectMocks
    private PropertyListingServicesImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProperties() {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(repository.findAll()).thenReturn(properties);

        List<Property> result = service.getProperties();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetPropertyById_Found() throws PropertyListingException {
        Property property = new Property();
        property.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(property));

        Property result = service.getPropertyById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetPropertyById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PropertyListingException.class, () -> service.getPropertyById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProperty_Success() throws PropertyListingException {
        Property property = new Property();
        property.setId(1L);
        property.setAddress("Old Address");

        Property propertyUpdate = new Property();
        propertyUpdate.setAddress("New Address");


        when(repository.findById(1L)).thenReturn(Optional.of(property)); 
        when(repository.save(any(Property.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Property updatedProperty = service.updateProperty(1L, propertyUpdate);
       

        assertEquals("New Address", updatedProperty.getAddress());
        verify(repository, times(1)).save(any(Property.class)); 
    }

    @Test
    void testUpdateProperty_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PropertyListingException.class, () -> service.updateProperty(1L, new Property()));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProperty_Success() throws PropertyListingException {
        Property property = new Property();
        property.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(property)); 
        doNothing().when(repository).deleteById(1L);

        Property deletedProperty = service.deleteProperty(1L);

        assertEquals(1L, deletedProperty.getId());
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProperty_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty()); 

        assertThrows(PropertyListingException.class, () -> service.deleteProperty(1L));

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testCreateProperty_Success() throws PropertyListingException {
        Property property = new Property();
        property.setAddress("Test Address");
        property.setPrice(100000.0);
        property.setSize(120.0);
        property.setDescription("Test Description");

        when(repository.save(property)).thenReturn(property);

        Property savedProperty = service.createProperty(property);

        assertNotNull(savedProperty);
        verify(repository, times(1)).save(property);
    }

    @Test
    void testCreateProperty_Invalid() {
        Property invalidProperty = new Property(); // Missing required fields

        assertThrows(PropertyListingException.class, () -> service.createProperty(invalidProperty));
        verify(repository, never()).save(any(Property.class));
    }

    @Test
    void testGetPropertiesByPriceRange_Valid() throws PropertyListingException {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(repository.findByPriceBetween(50000d, 150000d)).thenReturn(properties);

        List<Property> result = service.getPropertiesByPriceRange(50000d, 150000d);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByPriceBetween(50000d, 150000d);
    }

    @Test
    void testGetPropertiesByPriceRange_Invalid() {
        assertThrows(PropertyListingException.class, () -> service.getPropertiesByPriceRange(200000d, 100000d));
        verify(repository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    @Test
    void testGetPropertiesBySizeRange_Valid() throws PropertyListingException {
        List<Property> properties = Arrays.asList(new Property(), new Property());
        when(repository.findBySizeBetween(50d, 200d)).thenReturn(properties);

        List<Property> result = service.getPropertiesBySizeRange(50d, 200d);

        assertEquals(2, result.size());
        verify(repository, times(1)).findBySizeBetween(50d, 200d);
    }

    @Test
    void testGetPropertiesBySizeRange_Invalid() {
        assertThrows(PropertyListingException.class, () -> service.getPropertiesBySizeRange(300d, 100d));
        verify(repository, never()).findBySizeBetween(anyDouble(), anyDouble());
    }
}
