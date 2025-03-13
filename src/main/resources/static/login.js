const login = (() => {
    const api = apiClient;

    const createUser = async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const body = JSON.stringify({ username: username, password: password });
        try {
            let user = await api.createUser(body);
            alert("User created successfully: ", user);
            window.location.href = "/index.html";
        } catch (error) {
            alert("Error creating user: ", error);
        }
    }

    return {
        createUser
    }

})();