package com.sample.application.ea.ui.guest.signup;

import static com.sample.application.ea.functions.membersystem.constant.MemberSystem.PASSWORD_INVALID_MESSAGE;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sample.application.ea.R;
import com.sample.application.ea.databinding.FragmentSignUpBinding;
import com.sample.application.ea.functions.EmailValidation;
import com.sample.application.ea.functions.PasswordValidation;
import com.sample.application.ea.ui.guest.generic.GuestBindingFragment;
import com.sample.application.ea.widget.LoadingDialog;

import java.util.Objects;

/**
 * Final class representing the Sign Up Fragment.
 */
public final class SignUpFragment extends SignUpFragmentBase {
}

/**
 * Abstract base class for the Sign Up Fragment.
 */
abstract class SignUpFragmentBase extends GuestBindingFragment<FragmentSignUpBinding> {

    private final Checker checker = new Checker() {
        @NonNull
        @Override
        protected Button registerButton() {
            return requireBinding().registerButton;
        }
    };

    public SignUpFragmentBase() {
        super();
    }

    public SignUpFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Resets all input fields in the sign-up form.
     */
    private void resetEditBox() {
        requireBinding().userNameEditText.setText("");
        requireBinding().userEmailEditText.setText("");
        requireBinding().userPasswordEditText.setText("");
    }

    // Override for view creation in the Sign Up Fragment.
    @CallSuper
    @Override
    protected void onCreateView(@NonNull FragmentSignUpBinding binding,
                                @Nullable Bundle savedInstanceState) {

        binding.registerButton.setEnabled(false);

        // Set up register button listener
        binding.registerButton.setOnClickListener(v -> {
            final String name = Objects.requireNonNull(binding.userNameEditText.getText())
                    .toString().trim();

            if (name.isEmpty()) {
                binding.userNameTextLayout.setError("User name is required.");
                return;
            }

            final String email = Objects.requireNonNull(binding.userEmailEditText.getText())
                    .toString();

            final String password = Objects.requireNonNull(binding.userPasswordEditText.getText())
                    .toString();

            final boolean emailIsValid = EmailValidation.patternMatches(email);
            if (!emailIsValid) {
                binding.userEmailTextLayout.setError("Invalid email address");
            }

            final boolean passwordIsValid = PasswordValidation.patternMatches(password);
            if (!passwordIsValid) {
                binding.userPasswordTextLayout.setError("Invalid password");
            }

            if (emailIsValid && passwordIsValid) {
                signUp(name, email, password);
            } else {
                resetEditBox();
            }

        });

        // Set up back button listener
        binding.backButton.setOnClickListener(v -> {
            navigateUp();
        });

        // Add text change listeners to input fields
        binding.userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s) {
                binding.userNameTextLayout.setError(null);
                checker.updateNameState(s.length() > 0);
            }
        });

        binding.userEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s) {
                if (EmailValidation.patternMatches(s.toString())) {
                    checker.updateEmailState(true);
                    binding.userEmailTextLayout.setError(null);
                } else {
                    checker.updateEmailState(false);
                    binding.userEmailTextLayout.setError("Invalid email address");
                }
            }
        });

        binding.userPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s) {
                if (s.length() == 0 || PasswordValidation.patternMatches(s.toString())) {
                    checker.updatePasswordState(true);
                    binding.userPasswordTextLayout.setError(null);
                } else {
                    checker.updatePasswordState(false);
                    binding.userPasswordTextLayout.setError(PASSWORD_INVALID_MESSAGE);
                }
            }
        });

    }

    /**
     * Handles the sign-up process.
     *
     * @param name     User's name.
     * @param email    User's email address.
     * @param password User's password.
     */
    private void signUp(@NonNull String name,
                        @NonNull String email,
                        @NonNull String password) {
        final LoadingDialog dialog = new LoadingDialog();

        final OnFailureListener failureListener = e -> {
            Log.e("SignUpBlock", e.getMessage(), e);
            resetEditBox();
            dialog.dismiss();
            new AlertDialog.Builder(requireContext()).setTitle("Error")
                    .setMessage(Objects.requireNonNull(e.getMessage()))
                    .setPositiveButton("OK", (dialog1, which) -> resetEditBox())
                    .create().show();
        };

        final OnSuccessListener<AuthResult> successListener = authResult -> {
            UserProfileChangeRequest.Builder userProfileBuilder = new UserProfileChangeRequest.Builder();
            userProfileBuilder.setDisplayName(name);
            Objects.requireNonNull(firebaseAuth().getCurrentUser())
                    .updateProfile(userProfileBuilder.build())
                    .addOnCompleteListener(task -> dialog.dismiss())
                    .addOnSuccessListener(unused -> navigateUp());
        };

        dialog.show(getChildFragmentManager(), "LoadingDialog");

        firebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(failureListener)
                .addOnSuccessListener(successListener);
    }

    /**
     * Returns the layout resource ID for the Sign Up Fragment.
     *
     * @return Layout resource ID.
     */
    @Override
    final protected int getLayoutId() {
        return R.layout.fragment_sign_up;
    }


    private abstract static class Checker {

        @NonNull
        protected abstract Button registerButton();

        private boolean nameIsValid = false;

        private boolean emailIsValid = false;

        private boolean passwordIsValid = false;

        public void updateNameState(boolean flag) {
            this.nameIsValid = flag;
            update();
        }

        public void updateEmailState(boolean flag) {
            this.emailIsValid = flag;
            update();
        }

        public void updatePasswordState(boolean flag) {
            this.passwordIsValid = flag;
            update();
        }

        private void update() {
            registerButton().setEnabled(nameIsValid && emailIsValid && passwordIsValid);
        }

    }

}

