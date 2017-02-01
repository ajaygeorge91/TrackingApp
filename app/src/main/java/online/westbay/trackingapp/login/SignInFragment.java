package online.westbay.trackingapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import online.westbay.trackingapp.App;
import online.westbay.trackingapp.mainpage.MainActivity;
import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.AuthResultDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserAuthDTO;
import online.westbay.trackingapp.services.AuthService;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.utils.SharedPrefs;
import online.westbay.trackingapp.utils.Util;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInFragment extends Fragment implements View.OnClickListener {

    EditText emailEt;
    EditText passwordEt;
    TextView forgotPasswordTv;
    Button signInBtn;
    String email;
    String password;
    ProgressDialog progress;
    CoordinatorLayout parentView;

    AuthService authService;

    public SignInFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = ServiceGenerator.createService(AuthService.class, SharedPrefs.getToken(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        emailEt = (EditText) view.findViewById(R.id.email_et);
        passwordEt = (EditText) view.findViewById(R.id.password_et);
        forgotPasswordTv = (TextView) view.findViewById(R.id.forgotPassword_tv);
        signInBtn = (Button) view.findViewById(R.id.signInBtn);
        parentView = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading");
        progress.setIndeterminate(true);

        signInBtn.setOnClickListener(this);
        forgotPasswordTv.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:
                signInUser();
                break;
            case R.id.forgotPassword_tv:
//                forgotPassword();
        }
    }
//
//    private void forgotPassword() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.forgot_password_dialog, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText emailEditText = (EditText) dialogView.findViewById(R.id.edit1);
//
//        dialogBuilder.setTitle("Forgot Password ? ");
//        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
//            String emailValue = emailEditText.getText().toString();
//            authService.forgotPassword(emailValue)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .onErrorReturn(error -> {
//                        progress.dismiss();
//                        if (error != null && error.getMessage() != null) {
//                            Log.d("app", error.getMessage());
//                            return new ResponseDTO<>(error.getMessage());
//                        }
//                        return new ResponseDTO<>("Unknown error");
//                    })
//                    .subscribe(s -> {
//                        progress.dismiss();
//                        if (s.success) {
//                            Toast.makeText(getActivity(), "Kindly check your email for your new password", Toast.LENGTH_SHORT).show();
//                        }
//
//                    });
//
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pasa
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }

    private void signInUser() {

        UserAuthDTO userAuthDTO = new UserAuthDTO();

        email = emailEt.getText().toString();
        password = passwordEt.getText().toString();

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        progress.show();

        userAuthDTO.setEmail(email);
        userAuthDTO.setPassword(password);
        authService.signIn(userAuthDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> {
                    progress.dismiss();
                    if (error != null && error.getMessage() != null) {
                        Log.d("app", error.getMessage());
                        return new ResponseDTO<>(error.getMessage());
                    }
                    return new ResponseDTO<>("Unknown error");
                })
                .subscribe(s -> {
                    progress.dismiss();
                    if (s.success) {
                        manageAuthResult(s);
                    } else {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), s.message, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    private void manageAuthResult(ResponseDTO<AuthResultDTO> s) {
        if (s.success) {
            Log.d("app", s.data.token);
            SharedPrefs.setToken(App.getInstance(), s.data.token);
            SharedPrefs.setUser(App.getInstance(), s.data.userDTO);
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        } else {
            Snackbar snackbar = Snackbar
                    .make(parentView, s.message, Snackbar.LENGTH_SHORT);

            snackbar.show();

        }
    }


    private boolean validateEmail() {
        if (email.isEmpty() || !Util.isValidEmail(email)) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Please enter valid email ID", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Password must not be empty", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }
}
