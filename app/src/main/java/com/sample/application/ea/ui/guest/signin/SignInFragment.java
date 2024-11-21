package com.sample.application.ea.ui.guest.signin;

import static com.sample.application.ea.functions.membersystem.constant.MemberSystem.PASSWORD_INVALID_MESSAGE;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.sample.application.ea.R;
import com.sample.application.ea.databinding.FragmentSignInBinding;
import com.sample.application.ea.functions.EmailValidation;
import com.sample.application.ea.functions.PasswordValidation;
import com.sample.application.ea.ui.guest.generic.GuestBindingFragment;

import java.util.Objects;

/**
 * Final class representing the Sign In Fragment.
 */
public final class SignInFragment extends SignInFragmentDatabase {
}

/**
 * Abstract class for handling database operations within the Sign In Fragment.
 */
abstract class SignInFragmentDatabase extends SignInFragmentBase {

    private final Checker checker = new Checker() {
        @NonNull
        @Override
        protected Button signInButton() {
            return requireBinding().signInButton;
        }
    };

    public SignInFragmentDatabase() {
        super();
    }

    public SignInFragmentDatabase(int contentLayoutId) {
        super(contentLayoutId);
    }

    // Override for view creation in the Sign In Fragment.
    @Override
    protected void onCreateView(@NonNull FragmentSignInBinding binding, @Nullable Bundle container) {
        super.onCreateView(binding, container);

        // Add text change listener to email input
        binding.userEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s) {
                if (s.length() == 0) {
                    checker.updateEmailState(false);
                    binding.userEmailTextLayout.setError(null);
                    return;
                }

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


    private abstract static class Checker {

        @NonNull
        protected abstract Button signInButton();

        private boolean emailIsValid = false;

        private boolean passwordIsValid = false;

        public void updateEmailState(boolean flag) {
            this.emailIsValid = flag;
            update();
        }

        public void updatePasswordState(boolean flag) {
            this.passwordIsValid = flag;
            update();
        }

        private void update() {
            signInButton().setEnabled(emailIsValid && passwordIsValid);
        }

    }
}

/**
 * Abstract base class for the Sign In Fragment.
 */
abstract class SignInFragmentBase extends GuestBindingFragment<FragmentSignInBinding> {

    public SignInFragmentBase() {
        super();
    }

    public SignInFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    // Override for view creation in the Sign In Fragment.
    @CallSuper
    @Override
    protected void onCreateView(@NonNull FragmentSignInBinding binding, @Nullable Bundle container) {
        // Set up sign-in button listener
        binding.signInButton.setOnClickListener(v -> {
            final String email = Objects.requireNonNull(binding.userEmailEditText.getText())
                    .toString();
            final boolean emValid = EmailValidation.patternMatches(email);

            requireParentActivity().setResult(Activity.RESULT_CANCELED);

            if (!emValid) {
                binding.userEmailTextLayout.setError("Invalid Email");
                binding.userEmailEditText.setText("");
            } else {
                SignInHelper.newInstance(firebaseAuth())
                        .setUserEmailEditText(binding.userEmailEditText)
                        .setUserPasswordEditText(binding.userPasswordEditText)
                        .setOnSuccessListener(authResult -> {
                            requireParentActivity().setResult(Activity.RESULT_OK);
//                            startActivity(new Intent(requireParentActivity(), MainActivity.class));
                            requireParentActivity().finish();
                        })
                        .setOnFailureListener(e -> new AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage("The email address or password is incorrect.")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialogInterface, i) -> {
                                    binding.userEmailEditText.setText("");
                                    binding.userPasswordEditText.setText("");
                                })
                                .create().show())
                        .signIn(getChildFragmentManager());

            }


        });
        // Set up sign-up button listener
        binding.signUpButton.setOnClickListener(v -> navigate(R.id.action_nav_sign_in_to_nav_sign_up));
    }

    /**
     * Returns the layout resource ID for the Sign In Fragment.
     *
     * @return Layout resource ID.
     */
    @Override
    final protected int getLayoutId() {
        return R.layout.fragment_sign_in;
    }

}