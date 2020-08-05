package com.shapor.loftmoney.screens.login;

import com.shapor.loftmoney.remote.AuthApi;

public interface LoginPresenter {
    void performLogin(AuthApi authApi);
    void attachViewState(LoginView loginView);
    void disposeRequests();
}
