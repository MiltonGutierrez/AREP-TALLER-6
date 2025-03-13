const login = (() => {
    const api = apiClient;

    const createUser = async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        try {
            let user = await api.createUser(username, password);
            console.log(user);
            alert("User created successfully: " + JSON.stringify(user));
        } catch (error) {
            console.error("Error creating user:", error);
            alert("Error creating user: " + error.message);
        }
    };
    
    const authUser = async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        try {
            let response = await api.login(username, password);
            console.log(response);
            alert("User authenticated successfully: " + response);
            window.location.href = "/app.html";
        } catch (error) {
            console.error("Error authenticating user:", error);
            alert("Error authenticating user: " + error.message);
        }
    };
    

    return {
        createUser,
        authUser
    }

})();