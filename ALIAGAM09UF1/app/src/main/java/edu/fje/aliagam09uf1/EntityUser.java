package edu.fje.aliagam09uf1;


import android.util.Base64;
import android.util.Log;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;


@IgnoreExtraProperties
public class EntityUser implements Serializable {
    private String nameuser="";
    private String upassword="";
    private String privateKey="";
    private String publicKey="";

    public EntityUser() {
    }

    public EntityUser(String nameuser, String upassword) {
        this.nameuser = nameuser;
        this.upassword = upassword;
    }

    public EntityUser(String nameuser, String upassword, String privateKey, String publicKey) {
        this.nameuser = nameuser;
        this.upassword = upassword;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    public void generateKeys(){
        KeyPair keypair=BasicOperationsEncryption.createKeys();
        byte[] publicKeyBytes = Base64.encode(keypair.getPublic().getEncoded(), Base64.DEFAULT);
        this.publicKey = new String(publicKeyBytes);
        byte[] privateKeyBytes = Base64.encode(keypair.getPrivate().getEncoded(), Base64.DEFAULT);
        this.privateKey = new String(privateKeyBytes);

        /*byte[] keyby=this.privateKey.getBytes(StandardCharsets.UTF_8);
        KeyPair key2;
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            try {
                PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
                PublicKey publicKey = kf.generatePublic(new PKCS8EncodedKeySpec(publicKeyBytes));
                key2=new KeyPair(publicKey,privateKey);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        Log.println(Log.INFO,"I",new String(this.privateKey));
        Log.println(Log.INFO,"I",new String(this.publicKey));
    }
}

