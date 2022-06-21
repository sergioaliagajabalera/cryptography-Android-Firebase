package edu.fje.aliagam09uf1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseDBmanagement firebaseDBmanagement;
    LinearLayout layout;
    EntityUser user;
    EntityChat chat;
    String idchat;
    EditText editMessage;
    KeyPair myKeypair;
    PublicKey contactPublicKey;
    ArrayList<EntityMessage> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user=(EntityUser) getIntent().getSerializableExtra("USEROBJECT");
        chat=(EntityChat) getIntent().getSerializableExtra("CHATOBJECT");
        idchat=getIntent().getStringExtra("IDCHATOBJECT");
        TextView textView=findViewById(R.id.usernameChat);
        textView.setText(user.getNameuser().equals(chat.getNameuser1())?chat.getNameuser2():chat.getNameuser1());
        editMessage=findViewById(R.id.editMessage);

        myKeypair=BasicOperationsEncryption.stringtoKeypair(user.getPrivateKey(),user.getPublicKey());
        contactPublicKey=BasicOperationsEncryption.stringtoPublicKey(user.getPublicKey().equals(chat.getPublicKey1())?chat.getPublicKey2():chat.getPublicKey1());
        layout=findViewById(R.id.layoutMessages);

        Button buttonSendMessage=findViewById(R.id.btnAddMessage);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateMessage();
            }
        });

        FirebaseApp.initializeApp(this);
        firebaseFirestore= FirebaseFirestore.getInstance();

        firebaseDBmanagement=new FirebaseDBmanagement(this);
        firebaseDBmanagement.startFirebase();

        messageArrayList= new ArrayList<EntityMessage>();
        getMessages();

    }

    public void generateMessage(){
        String message=editMessage.getText().toString();
        byte[] data=message.getBytes();
        byte[][] encWrappedData =BasicOperationsEncryption.encryptWrappedData(data,contactPublicKey);
        byte[] firmadigital=BasicOperationsEncryption.signData(data,myKeypair.getPrivate());

        EntityMessage entityMessage=new EntityMessage(idchat,BasicOperationsEncryption.bytestoString(firmadigital)
        ,BasicOperationsEncryption.bytestoString(encWrappedData[0]),BasicOperationsEncryption.bytestoString(encWrappedData[1])
        ,user.getPublicKey(),user.getPublicKey().equals(chat.getPublicKey1())?chat.getPublicKey2():chat.getPublicKey1());
        firebaseDBmanagement.createMessage(entityMessage);
    }

    //firebase
    public void getMessages(){
        firebaseFirestore.collection("messages").whereEqualTo("chatid", idchat)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() >= 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.println(Log.ERROR, "I", document.getId() + " => " + document.getData());
                            EntityMessage chat = document.toObject(EntityMessage.class);
                            messageArrayList.add(chat);
                            //Log.println(Log.ERROR,"I","ENTROOOO "+messageArrayList.size());
                        }
                    } else
                        Toast.makeText(Chat.this, "Usuari o contrasenya incorrecta \n", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Chat.this, "Error al cercar usuari \n", Toast.LENGTH_SHORT).show();
                }

                if(messageArrayList.size()>=1)
                        gnMessages();
            }
        });
    }


    public void gnMessages(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if(messageArrayList.size()>=1) {
            for (EntityMessage entityMessage : messageArrayList) {
                String sdata="";
                TextView textView=new TextView(this);
                if(user.getPublicKey().equals(entityMessage.getPublicKey2())) {
                    byte[] data = BasicOperationsEncryption.desencryptWrappedData(
                            BasicOperationsEncryption.stringtoBytes(entityMessage.getMessageEncrypt()),
                            BasicOperationsEncryption.stringtoBytes(entityMessage.getPrivateEncrypt()),
                            myKeypair.getPrivate());
                    if(data != null) {
                        sdata = BasicOperationsEncryption.bytestoString(data);
                        byte[] sd = Base64.decode(sdata, Base64.DEFAULT);
                        sdata = new String(sd);
                        textView.setBackgroundColor(Color.parseColor("#DCDCDC"));
                        boolean isValid = BasicOperationsEncryption.validateSignature(sd, BasicOperationsEncryption.stringtoBytes(entityMessage.getFirmadigital()), contactPublicKey);
                        Log.println(Log.ERROR, "I", String.valueOf(isValid));
                        if (!isValid) {
                            continue;
                        }
                    }
                }else {
                    textView.setBackgroundColor(Color.parseColor("#AFE1AF"));
                    sdata = entityMessage.getMessageEncrypt();
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(displayMetrics.widthPixels/1.2),  ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,20);

                textView.setLayoutParams(params);
                textView.setText(sdata);
                textView.setTextSize(20);
                textView.setPadding(40,20,80,20);

                layout.addView(textView);
            }
        }
    }



}