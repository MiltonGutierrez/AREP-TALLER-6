package edu.escuelaing.arep.taller6.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.escuelaing.arep.taller6.model.Property;

@Repository
public interface PropertyListingRepository extends CrudRepository<Property, Long>  {
    List<Property> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Property> findBySizeBetween(Double minSize, Double maxSize);    
}
