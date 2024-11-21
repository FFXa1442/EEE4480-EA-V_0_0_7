package com.sample.application.ea.ui.guest.signin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sample.application.ea.utilities.TryUtil;
import com.sample.application.ea.widget.LoadingDialog;

import java.util.Objects;

/**
 * Helper class for handling sign-in operations.
 */
public final class SignInHelper {

    @NonNull
    private final FirebaseAuth firebaseAuth;

    @NonNull
    private String email = "";

    @Nullable
    private TextInputEditText emailEditText = null;

    @NonNull
    private String password = "";

    @Nullable
    private TextInputEditText passwordEditText = null;

    @Nullable
    private OnSuccessListener<AuthResult> onSuccessListener = null;

    @Nullable
    private OnFailureListener onFailureListener = null;

    @Nullable
    private OnCompleteListener<AuthResult> onCompleteListener = null;

    /**
     * Factory method to create a new instance of SignInHelper.
     *
     * @param auth FirebaseAuth instance.
     * @return New SignInHelper instance.
     */
    @NonNull
    public static SignInHelper newInstance(@NonNull FirebaseAuth auth) {
        return new SignInHelper(auth);
    }

    private SignInHelper(@NonNull FirebaseAuth auth) {
        firebaseAuth = auth;
    }

    /**
     * Sets the email input field for the sign-in process.
     *
     * @param editText Email input field.
     * @return This SignInHelper instance.
     */
    @NonNull
    public SignInHelper setUserEmailEditText(@NonNull TextInputEditText editText) {
        emailEditText = editText;
        email = Objects.requireNonNull(editText.getText()).toString();
        return this;
    }

    /**
     * Sets the password input field for the sign-in process.
     *
     * @param editText Password input field.
     * @return This SignInHelper instance.
     */
    @NonNull
    public SignInHelper setUserPasswordEditText(@NonNull TextInputEditText editText) {
        passwordEditText = editText;
        password = Objects.requireNonNull(editText.getText()).toString();
        return this;
    }

    /**
     * Sets the success listener for the sign-in process.
     *
     * @param onSuccessListener Success listener.
     * @return This SignInHelper instance.
     */
    @NonNull
    public SignInHelper setOnSuccessListener(@Nullable OnSuccessListener<AuthResult> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
        return this;
    }

    /**
     * Sets the complete listener for the sign-in process.
     *
     * @param onCompleteListener Complete listener.
     * @return This SignInHelper instance.
     */
    @NonNull
    public SignInHelper setOnCompleteListener(@Nullable OnCompleteListener<AuthResult> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        return this;
    }

    /**
     * Sets the failure listener for the sign-in process.
     *
     * @param onFailureListener Failure listener.
     * @return This SignInHelper instance.
     */
    @NonNull
    public SignInHelper setOnFailureListener(@Nullable OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
        return this;
    }

    /**
     * Initiates the sign-in process.
     *
     * @param fragmentManager FragmentManager for displaying dialogs.
     */
    public void signIn(@NonNull FragmentManager fragmentManager) {
        final LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(fragmentManager, "SignInLoadingDialog");
        firebaseAuth.signOut();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user == null : "'user' must be null!!";
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(Objects.requireNonNull(onSuccessListener))
                .addOnFailureListener(Objects.requireNonNull(onFailureListener))
                .addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    TryUtil.tryNonNull(onCompleteListener, l -> l.onComplete(task));
                });

    }


}
