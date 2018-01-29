package com.stc.xyralitytask;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by artem on 1/28/18.
 */

public interface LoginContract {
    interface Presenter {
        Single<List<String>> login(String email, String password);

    }
    interface View {
        void onSuccess(List<String> names);
        void onError(String error);
        void showProgress(final boolean show);
        void setPresenter(LoginContract.Presenter presenter);
    }
}
