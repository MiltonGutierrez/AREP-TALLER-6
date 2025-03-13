const login = (() => {
    const api = apiClient;

    const createUser = async (username, password) => {
        const body = JSON.stringify({ username: username, password: password });
        return api.createUser(body);
    }

    return {
        createUser
    }

})();