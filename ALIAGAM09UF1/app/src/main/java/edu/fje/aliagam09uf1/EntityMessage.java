package edu.fje.aliagam09uf1;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class EntityMessage implements Serializable {
    private String chatid="";
    private String firmadigital="";
    private String messageEncrypt="";
    private String privateEncrypt="";
    private String publicKey1="";
    private String publicKey2="";

    public EntityMessage() {
    }

    public EntityMessage(String chatid, String firmadigital, String messageEncrypt, String privateEncrypt, String publicKey1, String publicKey2) {
        this.chatid = chatid;
        this.firmadigital = firmadigital;
        this.messageEncrypt = messageEncrypt;
        this.privateEncrypt = privateEncrypt;
        this.publicKey1 = publicKey1;
        this.publicKey2 = publicKey2;
    }

    //getters and setters

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getFirmadigital() {
        return firmadigital;
    }

    public void setFirmadigital(String firmadigital) {
        this.firmadigital = firmadigital;
    }

    public String getMessageEncrypt() {
        return messageEncrypt;
    }

    public void setMessageEncrypt(String messageEncrypt) {
        this.messageEncrypt = messageEncrypt;
    }

    public String getPrivateEncrypt() {
        return privateEncrypt;
    }

    public void setPrivateEncrypt(String privateEncrypt) {
        this.privateEncrypt = privateEncrypt;
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
