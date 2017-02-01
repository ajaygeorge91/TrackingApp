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

public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText fullNameEt;
    EditText mobileNumberEt;
    EditText emailAddressEt;
    EditText passwordEt;
    Button registerBtn;
    AuthService authService;
    String fullName;
    String mobileNumber;
    String emailAddress;
    String password;
    CoordinatorLayout parentView;
    ProgressDialog progress;

    public SignUpFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = ServiceGenerator.createService(AuthService.class, SharedPrefs.getToken(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        fullNameEt = (EditText) view.findViewById(R.id.fullName_et);
        mobileNumberEt = (EditText) view.findViewById(R.id.mobileNumber_et);
        emailAddressEt = (EditText) view.findViewById(R.id.emailAddress_et);
        passwordEt = (EditText) view.findViewById(R.id.passwordReg_et);
        registerBtn = (Button) view.findViewById(R.id.signUp_btn);
        parentView = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        progress = new ProgressDialog(getContext());
        progress.setMessage("loading");
        progress.setIndeterminate(true);

        registerBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_btn:
                signInUser();
                break;
        }
    }

    private void signInUser() {


        UserAuthDTO userAuthDTO = new UserAuthDTO();

        fullName = fullNameEt.getText().toString();
        emailAddress = emailAddressEt.getText().toString();
        mobileNumber = mobileNumberEt.getText().toString();
        password = passwordEt.getText().toString();

        if (!validateFullName()) {
            return;
        }

//        if (!validateMobileNumber()) {
//            return;
//        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        progress.show();

        userAuthDTO.setFullName(fullName);
        userAuthDTO.setEmail(emailAddress);
        userAuthDTO.setPassword(password);

        authService.signUp(userAuthDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> {
                    progress.dismiss();
                    if (error != null && error.getMessage() != null) {
                        Log.d("app", error.getMessage());
                        return new ResponseDTO<AuthResultDTO>(error.getMessage());
                    }
                    return new ResponseDTO<AuthResultDTO>("");
                })
                .subscribe(s -> {
                    progress.dismiss();
                    manageAuthResult(s);
                });

    }

    private void manageAuthResult(ResponseDTO<AuthResultDTO> s) {
        if (s.success) {
            Log.d("app", s.data.token);
            SharedPrefs.setToken(getActivity(), s.data.token);
            SharedPrefs.setUser(getActivity(), s.data.userDTO);
            if (s.data.userDTO != null) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(parentView, s.message, Snackbar.LENGTH_SHORT);

            snackbar.show();
        }
    }


    private boolean validateFullName() {

        if (fullName.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Please enter your full name", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    private boolean validateMobileNumber() {

        if (mobileNumber.isEmpty() || mobileNumber.length() < 10 || mobileNumber.length() > 10) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Please enter valid mobile number", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        if (emailAddress.isEmpty() || !Util.isValidEmail(emailAddress)) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Please enter valid email ID", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.isEmpty() || password.length() < 4) {
            Snackbar snackbar = Snackbar
                    .make(parentView, "Password must not be empty and has minimum 4 characters", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return false;
        }

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();


        super.onStop();
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

}
