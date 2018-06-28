package ingressive.tutorial.com.alcchallengejournalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;

import ingressive.tutorial.com.alcchallengejournalapp.R;
import ingressive.tutorial.com.alcchallengejournalapp.model.User;
import ingressive.tutorial.com.alcchallengejournalapp.utils.Constants;
import ingressive.tutorial.com.alcchallengejournalapp.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    public ProgressDialog mProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String name, email;
    private String photo;
    private Uri photoUri;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);

        //Button listeners
        //findViewById(R.id.sign_in_button).setOnClickListener(this);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    for (UserInfo profile : user.getProviderData()){
                        String providerId = profile.getProviderId();

                        if (providerId.equals("google.com")){
                            String userName = user.getDisplayName();
                            String emailAddress = user.getEmail();
                            Uri photoUrl = user.getPhotoUrl();

                            Bundle bundle = new Bundle();
                            bundle.putString("uniqueIdentity", "fromGoogle");
                            bundle.putString("userName", userName);
                            bundle.putString("emailAddress", emailAddress);
                            bundle.putString("photoUrl", photoUrl.toString());

                            Log.d(TAG, "checkEmail:" + emailAddress);

                            /* Go to next activity */

//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            intent.putExtras(bundle);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                }

            }
        };
    }

    private void createUserInFirebaseHelper(){

        //Since Firebase does not allow "." in the key name, we'll have to encode and change the "." to ","
        // using the encodeEmail method in class Utils
        final String encodedEmail = Utils.encodeEmail(email.toLowerCase());


        //create an object of Firebase database and pass the the Firebase URL
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);

        userLocation.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){

                    /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    // Insert into Firebase database
                    User newUser = new User(name, photo, encodedEmail, timestampJoined);
                    userLocation.setValue(newUser);

                    Snackbar.make(findViewById(R.id.sign_in_layout), "Account Created", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.d(TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());

                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN){
                }
                else {
                    Snackbar.make(findViewById(R.id.sign_in_layout), firebaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                name = account.getDisplayName();
                email = account.getEmail();
                photoUri = account.getPhotoUrl();
                photo = photoUri.toString();

                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
                //updateUI(null);
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            // Sign in success, update UI with the signed-in user's information
                            Snackbar.make(findViewById(R.id.sign_in_layout), "SignIn Failed", Snackbar.LENGTH_SHORT).show();
                        } else {
                            createUserInFirebaseHelper();
                            Snackbar.make(findViewById(R.id.sign_in_layout), "Login Successful.", Snackbar.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_in_button){
            signIn();
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
