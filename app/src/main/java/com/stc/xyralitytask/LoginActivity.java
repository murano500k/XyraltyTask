package com.stc.xyralitytask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = "LoginActivity";

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginPresenter mPresenter;
    private Disposable mDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPresenter = new LoginPresenter();

        test();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable!=null && !mDisposable.isDisposed())mDisposable.dispose();
    }

    private void test() {
        mEmailView.setText("android.test@xyrality.com");
        mPasswordView.setText("password");
    }


    private void attemptLogin() {
        if (mDisposable!=null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            showProgress(false);
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            mDisposable=mPresenter.login(email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getOnSuccessConsumer(),getOnErrorConsumer());
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void showProgress(final boolean show) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
    }

    private void startMainActivity(List<String> worldNames) {
        ArrayList<String > list=new ArrayList<>(worldNames);
        Intent intent=new Intent(this,MainActivity.class);
        intent.putStringArrayListExtra(MainActivity.EXTRA_WORLDS_LIST,list);
        startActivity(intent);
    }


    Consumer<List<String>> getOnSuccessConsumer(){
        return new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                if(strings==null || strings.isEmpty()){
                    Log.w(TAG, "accept: empty" );
                    mPasswordView.setError("error");
                }else startMainActivity(strings);
                showProgress(false);
            }
        };
    }
    Consumer<Throwable> getOnErrorConsumer(){
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "error: ",throwable );
                mPasswordView.setError(throwable.getMessage());
                showProgress(false);
            }
        };
    }
}

