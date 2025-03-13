const property = (() => {

    let api = apiClient;
    let filterForm = "";

    const getProperties = async () => {
        try {
            let properties = await api.getProperties();
            console.log(properties);
            let propertyList = document.getElementById("propertyList");
            propertyList.innerHTML = "";
            properties.forEach(property => {
                propertyList.appendChild(createPropertyDiv(property));
            });

            updateFilterForm("all");

            return properties;
        } catch (error) {
            alert(error);
        }
    };

    const createPropertyDiv = (property) => {
        const propertyItem = document.createElement("div");
        propertyItem.classList.add("property-item");
        propertyItem.innerHTML = `
            <p><strong>Id:</strong> ${property.id}</p>
            <p><strong>Address:</strong> ${property.address}</p>
            <p><strong>Price:</strong> $${property.price.toFixed(2)}</p>
            <p><strong>Size:</strong> ${property.size} m²</p>
            <p><strong>Description:</strong> ${property.description}</p>
            <button class="update-btn" onclick="property.openUpdateForm(${property.id})">Update</button>
            <button class="delete-btn" onclick="property.deleteProperty(${property.id})">Delete</button>`;
        return propertyItem;
    };


    const getPropertyById = async (id) => {
        try {
            let id = document.getElementById('searchId').value;
            let response = await api.getPropertyById(id);
            console.log(response);

            let propertyList = document.getElementById("propertyList");
            propertyList.innerHTML = "";
            propertyList.appendChild(createPropertyDiv(response));

            updateFilterForm("id");
            return response;

        } catch (error) {
            alert(error);
        }
    };

    const getPropertiesByPriceRange = async () => {
        try {
            let minPrice = document.getElementById('minPrice').value;
            let maxPrice = document.getElementById('maxPrice').value;
            if(!minPrice || !maxPrice){
                throw new Error('Both fields are required');
            }
            if(Number(minPrice) < 0 || Number(maxPrice) < 0){
                throw new Error('Price must be positive');
            }
            if(Number(minPrice) > Number(maxPrice)){
                throw new Error('Min price must be less than max price');
            }

            let properties = await api.getPropertiesByPriceRange(minPrice, maxPrice);
            console.log(properties);

            let propertyList = document.getElementById("propertyList");
            propertyList.innerHTML = "";
            properties.forEach(property => {
                propertyList.appendChild(createPropertyDiv(property));
            });
           
            updateFilterForm("price");
            return properties;
        } catch (error) {
            alert(error);
        }
    };

    const getPropertiesBySizeRange = async () => {
        try {
            let minSize = document.getElementById('minSize').value;
            let maxSize = document.getElementById('maxSize').value;

            if(!minSize || !maxSize){
                throw new Error('Both fields are required');
            }
            if(Number(minSize) < 0 || Number(maxSize) < 0){
                throw new Error('Range must be positive');
            }
            if(Number(minSize) > Number(maxSize)){
                throw new Error('Min range must be less than max range');
            }

            let properties = await api.getPropertiesBySizeRange(minSize, maxSize);
            console.log(properties);

            let propertyList = document.getElementById("propertyList");
            propertyList.innerHTML = "";
            properties.forEach(property => {
                propertyList.appendChild(createPropertyDiv(property));
            });

            updateFilterForm("size");
            return properties;
        } catch (error) {
            alert(error);
        }
    };


    const createProperty = async () => {
        try {
            let address = document.getElementById('address').value;
            let price = document.getElementById('price').value;
            let size = document.getElementById('size').value;
            let description = document.getElementById('description').value;
            if (!address || !price || !size || !description) {
                throw new Error('All fields are required');
            }
            console.log(address, price, size, description);
            let body = JSON.stringify({
                address: address,
                price: price,
                size: size,
                description: description
            });
            let response = await api.createProperty(body);
            console.log(response);
            return response;
        } catch (error) {
            alert(error);
        }
    }

    const openUpdateForm = (propertyId) => {
        const updateFormContainer = document.getElementById("updateFormContainer");
        updateFormContainer.innerHTML = `
            <div class="update-form">
                <h3>Update Property</h3>
                <label for="updateAddress">Address:</label>
                <input type="text" id="updateAddress" placeholder="Enter new address">
    
                <label for="updatePrice">Price ($):</label>
                <input type="number" id="updatePrice" placeholder="Enter new price" min="0" step="any">
    
                <label for="updateSize">Size (m²):</label>
                <input type="number" id="updateSize" placeholder="Enter new size" min="0" step="any">
    
                <label for="updateDescription">Description:</label>
                <textarea id="updateDescription" placeholder="Enter new description"></textarea>
    
                <button onclick="property.updateProperty(${propertyId})">Save Changes</button>
                <button onclick="closeUpdateForm()">Cancel</button>
            </div>
        `;
        updateFormContainer.style.display = "block";
    };

    const updateProperty = async (id) => {
        try {
            let address = document.getElementById('updateAddress').value;
            let price = document.getElementById('updatePrice').value;
            let size = document.getElementById('updateSize').value;
            let description = document.getElementById('updateDescription').value;

            let body = JSON.stringify({
                address: address,
                price: price,
                size: size,
                description: description
            });
            let response = await api.updateProperty(id, body);
            console.log(response);
            closeUpdateForm();
            fitlerFunctions[filterForm]();
            return response;
        } catch (error) {
            alert(error);
        }
    }

    const deleteProperty = async (id) => {
        try {
            let response = await api.deleteProperty(id);
            console.log(response);
            fitlerFunctions[filterForm]();
            return response;
        } catch (error) {
            alert(error);
        }
    }

    const closeUpdateForm = () => {
        document.getElementById("updateFormContainer").style.display = "none";
    };

    const updateFilterForm = (form) => {
        filterForm = form;
    }

    let fitlerFunctions = { "all": getProperties, "price": getPropertiesByPriceRange, "size": getPropertiesBySizeRange, "id": function () {  let propertyList = document.getElementById("propertyList");
        propertyList.innerHTML = "";} };

    return {
        getProperties,
        getPropertyById,
        getPropertiesByPriceRange,
        getPropertiesBySizeRange,
        createProperty,
        updateProperty,
        deleteProperty,
        openUpdateForm
    };

})();