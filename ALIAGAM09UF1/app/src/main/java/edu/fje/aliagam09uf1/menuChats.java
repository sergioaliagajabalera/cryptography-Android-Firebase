package edu.fje.aliagam09uf1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class menuChats extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseDBmanagement firebaseDBmanagement;
    EntityUser user;
    ArrayList<EntityChat> chatArrayList;
    ArrayList<String> idsChats;
    LinearLayout linlayoutchats;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chats);
        linlayoutchats=findViewById(R.id.layoutChats);
        btnAdd=findViewById(R.id.btnAddchat);
        EditText editTextaddChat=findViewById(R.id.edittAddUser);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existUser(editTextaddChat.getText().toString());
            }
        });


        FirebaseApp.initializeApp(this);
        firebaseFirestore= FirebaseFirestore.getInstance();

        firebaseDBmanagement=new FirebaseDBmanagement(this);
        firebaseDBmanagement.startFirebase();

        user=(EntityUser) getIntent().getSerializableExtra("USEROBJECT");
        TextView textUsername=findViewById(R.id.usernamemenu);
        textUsername.setText(user.getNameuser());
        chatArrayList= new ArrayList<EntityChat>();
        idsChats=new ArrayList<>();
        getChats("nameuser1");
    }

    //firebase
    public void getChats(String field){
        firebaseFirestore.collection("chats").whereEqualTo(field, user.getNameuser())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() >= 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.println(Log.ERROR, "I", document.getId() + " => " + document.getData());
                            EntityChat chat = document.toObject(EntityChat.class);

                            if(chat.getNameuser1().equals(user.getNameuser()) || chat.getNameuser2().equals(user.getNameuser())) {
                                //Log.println(Log.ERROR,"I","holaaa "+chat.getNameuser1()+"| "+user.getNameuser());
                                chatArrayList.add(chat);
                                Log.println(Log.ERROR,"I","ENTROOOO "+chatArrayList.size());
                                idsChats.add(document.getId());
                            }
                        }
                    } else
                        Toast.makeText(menuChats.this, "Usuari o contrasenya incorrecta \n", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(menuChats.this, "Error al cercar usuari \n", Toast.LENGTH_SHORT).show();
                }

                if(chatArrayList.size()>=1)
                    //Log.println(Log.ERROR,"I","holaaa"+chatArrayList.get(0).getNameuser1());
                    if(field=="nameuser2")
                        gnChatsAvailables();
                if(field=="nameuser1") getChats("nameuser2");
            }
        });
    }

    public void gnChatsAvailables(){
        Log.println(Log.ERROR,"I","ENTROOOO "+chatArrayList.size());
        if(chatArrayList.size()>=1) {
            Integer i=1;
            for (EntityChat entityChat : chatArrayList) {
                Button button=new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(800, 200));
                button.setText(user.getNameuser().equals(entityChat.getNameuser1()) ?entityChat.getNameuser2():entityChat.getNameuser1());
                Integer finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //intent
                        Intent intent = new Intent(getApplicationContext(), Chat.class);
                        intent.putExtra("USEROBJECT", user);
                        intent.putExtra("CHATOBJECT", chatArrayList.get(finalI -1));
                        intent.putExtra("IDCHATOBJECT", idsChats.get(finalI-1));

                        startActivity(intent);
                    }
                });
                linlayoutchats.addView(button);
                i+=1;
            }
        }
    }


    //firebase
    public void existUser(String username){
        String nameuser=username;
        firebaseFirestore.collection("usuarios").whereEqualTo("nameuser", nameuser)
                .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() >= 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.println(Log.ERROR, "I", document.getId() + " => " + document.getData());
                            EntityUser user2 = document.toObject(EntityUser.class);
                            EntityChat chat=new EntityChat(user.getNameuser(),user2.getNameuser(),user.getPublicKey(),user2.getPublicKey());
                            firebaseDBmanagement.createChat(chat);
                        }
                    } else
                        Toast.makeText(menuChats.this, "Usuari o contrasenya incorrecta \n", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(menuChats.this, "Error al cercar usuari \n", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}