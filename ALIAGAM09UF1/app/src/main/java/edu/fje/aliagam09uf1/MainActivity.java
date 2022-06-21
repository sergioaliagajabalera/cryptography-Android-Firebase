package edu.fje.aliagam09uf1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    EditText editUsername;
    EditText editPassword;
    EntityUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        firebaseFirestore=FirebaseFirestore.getInstance();

        Button btLogin=findViewById(R.id.btlogin);
        Button btRegister=findViewById(R.id.btregister);
        editPassword=findViewById(R.id.password);
        editUsername=findViewById(R.id.username);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),Register.class);
                    startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.println(Log.ERROR,"I","holaaaaaa");
                user=new EntityUser(editUsername.getText().toString(),BasicOperationsEncryption.md5(editPassword.getText().toString()));
                existUser();
            }
        });
    }

    //firebase
    public void existUser(){
        String nameuser=user.getNameuser();
        String password=user.getUpassword();
        Log.println(Log.ERROR,"I","hola2");
        ArrayList<EntityUser> userArrayList = new ArrayList<>();
        firebaseFirestore.collection("usuarios").whereEqualTo("nameuser", nameuser)
                .whereEqualTo("upassword", password)
                .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() >= 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.println(Log.ERROR, "I", document.getId() + " => " + document.getData());
                            EntityUser user2 = document.toObject(EntityUser.class);
                            userArrayList.add(user2);
                            EntityUser user3 = userArrayList.get(0);
                            user.setPrivateKey(user3.getPrivateKey());
                            user.setPublicKey(user3.getPublicKey());
                            //intent
                            Log.println(Log.ERROR,"I",user.getPrivateKey());
                            Intent intent = new Intent(getApplicationContext(), menuChats.class);
                            intent.putExtra("USEROBJECT", user);
                            startActivity(intent);
                        }
                    } else
                    Toast.makeText(MainActivity.this, "Usuari o contrasenya incorrecta \n", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Error al cercar usuari \n", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}