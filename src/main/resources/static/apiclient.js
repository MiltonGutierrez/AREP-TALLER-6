const apiClient = (() => {
    const url = "/api/";
    const urlUser = "/user";

    // GET
    const getProperties = async () => {
        const response = await fetch(url + 'property');
        return response.json();
    };

    const getPropertyById = async (id) => {
        const response = await fetch(url + `property/${id}`);
        return response.json();
    };

    const getPropertiesByPriceRange = async (minPrice, maxPrice) => {
        const response = await fetch(url + `property/price/${minPrice}/${maxPrice}`);
        return response.json();
    };

    const getPropertiesBySizeRange = async (minSize, maxSize) => {
        const response = await fetch(url + `property/size/${minSize}/${maxSize}`);
        return response.json();
    }

    // POST
    const createProperty = async (body) => {
        const response = await fetch(url + 'property', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        return response.json();
    };

    const createUser = async (body) => {
        const response = await fetch(urlUser, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        return response.json();
    };

    const login = async (body) => {
        const response = await fetch(urlUser + '/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        return response.json();
    }

    // PUT
    const updateProperty = async (id, body) => {
        const response = await fetch(url + `property/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        return response.json();
    };

    // DELETE
    const deleteProperty = async (id) => {
        const response = await fetch(url + `property/${id}`, { method: 'DELETE' });
        return response.json();
    };

    return {
        getProperties,
        getPropertyById,
        getPropertiesByPriceRange,
        getPropertiesBySizeRange,
        createProperty,
        createUser,
        login,
        updateProperty,
        deleteProperty
    };
})();
