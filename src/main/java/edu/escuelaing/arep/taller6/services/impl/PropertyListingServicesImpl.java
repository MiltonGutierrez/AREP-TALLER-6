package edu.escuelaing.arep.taller6.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.escuelaing.arep.taller6.exception.PropertyListingException;
import edu.escuelaing.arep.taller6.model.Property;
import edu.escuelaing.arep.taller6.repository.PropertyListingRepository;
import edu.escuelaing.arep.taller6.services.interfaces.PropertyListingServices;


@Service
public class PropertyListingServicesImpl implements PropertyListingServices{

    private PropertyListingRepository repository;


    @Autowired
    public PropertyListingServicesImpl(PropertyListingRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Property> getProperties()  {
       return (List<Property>) repository.findAll();
    }

    @Override
    public Property getPropertyById(Long id) throws PropertyListingException {
        Optional<Property> propertyInDB = repository.findById(id);
        if(propertyInDB.isEmpty()){
            throw new PropertyListingException(PropertyListingException.PROPERTY_NOT_FOUND);
        }
        return propertyInDB.get();

    }

    @Override
    public Property updateProperty(Long id, Map<String, String> values) throws PropertyListingException {
        Property propertyInDB = getPropertyById(id);
        boolean updated = false;
        if(values.get("address") != null){
            propertyInDB.setAddress(values.get("address"));
            updated = true;
        }
        if(values.get("price") != null){
            propertyInDB.setPrice(Double.valueOf(values.get("price")));
            updated = true;
        }
        if(values.get("size") != null){
            propertyInDB.setSize(Double.valueOf(values.get("size")));
            updated = true;
        }
        if(values.get("description") != null){
            propertyInDB.setDescription(values.get("description"));
            updated = true;
        }
        if(!updated){
            throw new PropertyListingException(PropertyListingException.PROPERTY_NOT_UPDATED);
        }
        return repository.save(propertyInDB);
    }

    @Override
    public Property deleteProperty(Long id) throws PropertyListingException{
        Property propertyInDB = getPropertyById(id);
        repository.deleteById(id);
        return propertyInDB;
    }

    @Override
    public Property createProperty(Property property) throws PropertyListingException {
        if(property.getAddress() == null || property.getPrice() <= 0 || property.getSize() <=0 || property.getDescription() == null){
            throw new PropertyListingException(PropertyListingException.MISSING_PROPERTY_PARAMETERS);
        }
        return repository.save(property);
    }

    @Override
    public List<Property> getPropertiesByPriceRange(double min, double max) throws PropertyListingException {
        if(min < 0 || max < 0 || min > max){
            throw new PropertyListingException(PropertyListingException.INVALID_PRICE_RANGE);
        }
        return repository.findByPriceBetween(min, max);}

    @Override
    public List<Property> getPropertiesBySizeRange(double min, double max) throws PropertyListingException {
        if(min < 0 || max < 0 || min > max){
            throw new PropertyListingException(PropertyListingException.INVALID_SIZE_RANGE);
        }
        return repository.findBySizeBetween(min, max);
    }
    
}
