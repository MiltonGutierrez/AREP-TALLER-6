const propertyFiltering = (() => {

    const buttonFilterAll = () => {
        searchForm.innerHTML = `<button type="button" onclick="property.getProperties()">Buscar Todas</button>`;
    };

    const buttonFilterByPriceRange = () => {
        searchForm.innerHTML = `
        <label for="minPrice">Precio mínimo:</label>
        <input type="number" id="minPrice" name="minPrice" step="any" placeholder="Precio mínimo" required>
        <label for="maxPrice">Precio máximo:</label>
        <input type="number" id="maxPrice" name="maxPrice" step="any" placeholder="Precio máximo" required>
        <button type="button" onclick="property.getPropertiesByPriceRange()">Buscar por rango de precio</button>
      `;
    }

    const buttonFilterBySizeRange = () => {
        searchForm.innerHTML = `
        <label for="minSize">Tamaño mínimo (m²):</label>
        <input type="number" id="minSize" name="minSize" step="any" placeholder="Tamaño mínimo" required>
        <label for="maxSize">Tamaño máximo (m²):</label>
        <input type="number" id="maxSize" name="maxSize" step="any" placeholder="Tamaño máximo" required>
        <button type="button" onclick="property.getPropertiesBySizeRange()">Buscar por rango de tamaño</button>
      `;
    };

    const buttonFilterById = () => {
        searchForm.innerHTML = `
        <label for="searchId">ID:</label>
        <input type="number" id="searchId" name="searchId" step="1" placeholder="Ingrese el ID" required>
        <button type="button" onclick="property.getPropertyById()">Buscar por ID</button>
      `;
    }

    return {
        buttonFilterAll,
        buttonFilterByPriceRange,
        buttonFilterBySizeRange,
        buttonFilterById
    }

})();