package com.example.mvvm.network;

public class ApiManager {
    private static ApiManager instance;
    private static ApiService apiService;

    private ApiManager() {
        apiService = ApiServiceFactory.makeApiServiceService();
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public ApiService getApiService() {
        if (apiService == null) {
            apiService = ApiServiceFactory.makeApiServiceService();
        }
        return apiService;
    }

}
