package edu.fje.aliagam09uf1;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class FirebaseDBmanagement {
    FirebaseFirestore firebaseFirestore;
    AppCompatActivity object;
    public FirebaseDBmanagement(AppCompatActivity object) {
        this.object=object;
    }

    public void startFirebase(){
        FirebaseApp.initializeApp(this.object);
        firebaseFirestore= FirebaseFirestore.getInstance();
    }

    public void createUser(EntityUser user){
        CollectionReference collectionusers=firebaseFirestore.collection("usuarios");
        // below method is use to add data to Firebase Firestore.
        collectionusers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(object, "Your User has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(object, "Fail to add user \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createChat(EntityChat chat){
        CollectionReference collectionusers=firebaseFirestore.collection("chats");
        // below method is use to add data to Firebase Firestore.
        collectionusers.add(chat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(object, "Your Chat has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(object, "Fail to add chat \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createMessage(EntityMessage message){
        CollectionReference collectionusers=firebaseFirestore.collection("messages");
        // below method is use to add data to Firebase Firestore.
        collectionusers.add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(object, "Your mesage has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(object, "Fail to add message \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
