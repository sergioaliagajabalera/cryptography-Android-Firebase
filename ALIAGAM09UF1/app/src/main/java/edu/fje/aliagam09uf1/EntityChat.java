package edu.fje.aliagam09uf1;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class EntityChat implements Serializable {
    private String nameuser1="";
    private String nameuser2="";
    private String publicKey1="";
    private String publicKey2="";

    public EntityChat() {
    }

    public EntityChat(String nameuser1, String nameuser2, String publicKey1, String publicKey2) {
        this.nameuser1 = nameuser1;
        this.nameuser2 = nameuser2;
        this.publicKey1 = publicKey1;
        this.publicKey2 = publicKey2;
    }

    public String getNameuser1() {
        return nameuser1;
    }

    public void setNameuser1(String nameuser1) {
        this.nameuser1 = nameuser1;
    }

    public String getNameuser2() {
        return nameuser2;
    }

    public void setNameuser2(String nameuser2) {
        this.nameuser2 = nameuser2;
    }

    public String getPublicKey1() {
        return publicKey1;
    }

    public void setPublicKey1(String publicKey1) {
        this.publicKey1 = publicKey1;
    }

    public String getPublicKey2() {
        return publicKey2;
    }

    public void setPublicKey2(String publicKey2) {
        this.publicKey2 = publicKey2;
    }
}
