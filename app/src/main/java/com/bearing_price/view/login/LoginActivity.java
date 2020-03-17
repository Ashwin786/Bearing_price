package com.bearing_price.view.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bearing_price.R;
import com.bearing_price.databinding.ActivityLoginBinding;
import com.bearing_price.view.price.Price_Activity;
import com.bearing_price.view.product.ProductActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginNavigator {

    private LoginViewModel mLoginViewModel;
    View focusView = null;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.setNavigator(this);
        mLoginViewModel.isAlreadyLoggedIn();
    }


    @Override
    public void setError(int i) {
        if (i == 0) {
            binding.loginId.setError(getString(R.string.error_field_required));
            focusView = binding.loginId;
        } else {
            binding.password.setError(getString(R.string.error_invalid_password));
            focusView = binding.password;
        }
        focusView.requestFocus();
    }

    @Override
    public void clearError() {
        // Reset errors.
        binding.loginId.setError(null);
        binding.password.setError(null);
    }

    @Override
    public void loginValidate() {
        clearError();
        String loginId = binding.loginId.getText().toString();
        String passWord = binding.password.getText().toString();
        mLoginViewModel.validateLogin(loginId, passWord);
    }

    @Override
    public void loginSuccess() {
        finish();
//        startActivity(new Intent(this, Price_Activity.class));
        startActivity(new Intent(this, ProductActivity.class));
    }

    @Override
    public void loginFailed() {
        Toast toast = Toast.makeText(LoginActivity.this, "Login Id or Password is Incorrect", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void initView() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(mLoginViewModel);
//        binding.setVariable(getBindingVariable(), mLoginViewModel);
        binding.executePendingBindings();
        mLoginViewModel.copydatabase();

    }




}

