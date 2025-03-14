const apiClient = (() => {
    const url = "/api/";
    const urlUser = "/user";

    // GET
    const getProperties = async () => {
        const response = await fetch(url + 'property');
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.json();
    };

    const getPropertyById = async (id) => {
        const response = await fetch(url + `property/${id}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.json();
    };

    const getPropertiesByPriceRange = async (minPrice, maxPrice) => {
        const response = await fetch(url + `property/price/${minPrice}/${maxPrice}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.json();
    };

    const getPropertiesBySizeRange = async (minSize, maxSize) => {
        const response = await fetch(url + `property/size/${minSize}/${maxSize}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.json();
    }

    // POST
    const createProperty = async (body) => {
        const response = await fetch(url + 'property', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }

        return response.json();
        
    };

    const createUser = async (username, password) => {
        const response = await fetch(urlUser, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to create user");
        }
        return response.json();
    };

    const login = async (username, password) => {
        const response = await fetch(urlUser + '/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.text();
    }

    // PUT
    const updateProperty = async (id, body) => {
        const response = await fetch(url + `property/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
        return response.json();
    };

    // DELETE
    const deleteProperty = async (id) => {
        const response = await fetch(url + `property/${id}`, { method: 'DELETE' });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Invalid credentials");
        }
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
