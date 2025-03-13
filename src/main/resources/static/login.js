const login = (() => {
    const api = apiClient;

    const createUser = async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const body = JSON.stringify({ username: username, password: password });
        try {
            let user = await api.createUser(body);
            alert("User created successfully: ", user);
        } catch (error) {
            alert("Error creating user: ", error);
        }
    }

    const authUser = async () => {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const body = JSON.stringify({ username: username, password: password });
        try {
            let user = await api.login(body);
            alert("User authenticated successfully: ", user);
            window.location.href = "/app.html";
        } catch (error) {
            alert("Error authenticating user: ", error);
        }
    }

    return {
        createUser,
        authUser
    }

})();