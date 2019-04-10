package com.rai.ayush.wallery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rai.ayush.wallery.R;

public class SettingsFragment extends Fragment {
    private static final int GOOGLE_SIGNIN_CODE=212;
    private GoogleSignInClient googleSignInClient;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==GOOGLE_SIGNIN_CODE)){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount account) {
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mauth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_area,new SettingsFragment()).commit();
                }
                else {

                    Toast.makeText(getActivity(),"Login Failure",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleSignInClient= GoogleSignIn.getClient(getActivity(),gso);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            TextView name=view.findViewById(R.id.username);
            //TextView email=view.findViewById(R.id.email);
            ImageView imageView=view.findViewById(R.id.image_view);
            Glide.with(getActivity())
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
            .into(imageView);
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            //email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            view.findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_area,new SettingsFragment()).commit();
                    }
                });

                }
            });

        }
        else {
            view.findViewById(R.id.button_googlesignin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=googleSignInClient.getSignInIntent();
                    startActivityForResult(intent,GOOGLE_SIGNIN_CODE);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            return inflater.inflate(R.layout.fragment_setiings_def,container,false);
        }
        return inflater.inflate(R.layout.fragment_setiings_login,container,false);
    }
}
