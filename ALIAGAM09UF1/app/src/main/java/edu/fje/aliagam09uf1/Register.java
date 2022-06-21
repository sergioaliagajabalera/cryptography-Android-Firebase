package edu.fje.aliagam09uf1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Distribution;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Register extends AppCompatActivity implements View.OnClickListener {
    FirebaseDBmanagement firebaseDBmanagement;
    Button button;
    EditText editUsername;
    EditText editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseDBmanagement=new FirebaseDBmanagement(this);
        firebaseDBmanagement.startFirebase();
        button=findViewById(R.id.sendregister);
        button.setOnClickListener(this);
        editPassword=findViewById(R.id.password);
        editUsername=findViewById(R.id.username);
    }

    @Override
    public void onClick(View view) {
        EntityUser user=new EntityUser(editUsername.getText().toString(),BasicOperationsEncryption.md5(editPassword.getText().toString()));
        user.generateKeys();
        firebaseDBmanagement.createUser(user);
    }
}