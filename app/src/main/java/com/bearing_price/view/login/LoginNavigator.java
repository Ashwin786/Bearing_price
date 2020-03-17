package com.bearing_price.view.login;

public interface LoginNavigator {

    void setError(int i);

    void clearError();

    void loginValidate();

    void loginSuccess();

    void loginFailed();

    void initView();
}
