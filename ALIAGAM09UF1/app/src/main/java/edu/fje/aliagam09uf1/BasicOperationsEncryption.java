package edu.fje.aliagam09uf1;

import android.graphics.drawable.LayerDrawable;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public  class BasicOperationsEncryption {

    static public KeyPair createKeys() {
        KeyPair keys = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048 );
            keys = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keys;
    }

    static public String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    static public KeyPair stringtoKeypair(String sprivateKey,String spublicKeyBytes){
        byte[] bytKeyprivate= Base64.decode(sprivateKey, Base64.DEFAULT);
        byte[] bytKeyPublic=Base64.decode(spublicKeyBytes, Base64.DEFAULT);
        KeyPair key2=null;
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            try {
                PKCS8EncodedKeySpec keySpecpriv = new PKCS8EncodedKeySpec(bytKeyprivate);
                PrivateKey privateKey = kf.generatePrivate(keySpecpriv);
                //Log.println(Log.ERROR,"I","holaa: |"+privateKey.toString());
                X509EncodedKeySpec keySpecpub = new X509EncodedKeySpec(bytKeyPublic);
                PublicKey publicKey = kf.generatePublic(keySpecpub);
                key2 = new KeyPair(publicKey, privateKey);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key2;
    }

    static public PublicKey stringtoPublicKey(String spublicKey){
        byte[] bytKeyPublic=Base64.decode(spublicKey,Base64.DEFAULT);
        PublicKey publicKey=null;
        KeyFactory kf=null;
        try {
            kf = KeyFactory.getInstance("RSA");
            try {
                 publicKey = kf.generatePublic(new X509EncodedKeySpec(bytKeyPublic));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    static public PrivateKey stringtoPrivateKey(String  sprivateKey){
        byte[] bytKeyprivate=Base64.decode(sprivateKey,Base64.DEFAULT);
        PrivateKey privateKey=null;
        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
            try {
                privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(bytKeyprivate));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    static public SecretKey stringtoSecretKey(String  sSecretKey){
        byte[] bytSecretKey= sSecretKey.getBytes();;
        SecretKey secretKey=null;
        SecretKeyFactory kf;
        try {
            kf = SecretKeyFactory.getInstance("AES");
            try {
                SecretKeySpec secret = new SecretKeySpec(bytSecretKey,"AES");
                secretKey = kf.generateSecret(secret);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    static public byte[] stringtoBytes(String sdata){
        byte[] data=Base64.decode(sdata,Base64.DEFAULT);
        return data;
    }

    static public String bytestoString(byte[] bdata){
        String data=Base64.encodeToString(bdata,Base64.DEFAULT);
        return data;
    }

    static public byte[][] encryptWrappedData(byte[] data, PublicKey pub) {
        byte[][] encWrappedData = new byte[2][];
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            SecretKey sKey = kgen.generateKey();
            Log.println(Log.ERROR,"I",""+Base64.encode(sKey.getEncoded(),Base64.DEFAULT));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            byte[] encMsg = cipher.doFinal(data);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.WRAP_MODE, pub);
            byte[] encKey = cipher.wrap(sKey);
            encWrappedData[0] = encMsg;
            encWrappedData[1] = encKey;
        } catch (Exception ex) {
            System.err.println("Ha succeït un error xifrant: " + ex);
        }
        return encWrappedData;
    }

    static public byte[] desencryptWrappedData(byte[] messageEncrypted,byte[] secretKey,PrivateKey priv) {
        byte[] databytes=null;
        byte[][] encunWrappedData = new byte[2][];
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.UNWRAP_MODE, priv);
            SecretKey secretKey1= (SecretKey) cipher.unwrap(secretKey,"AES",Cipher.SECRET_KEY);
            //Log.println(Log.ERROR,"I","hola "+Base64.encode(secretKey1.getEncoded(),Base64.DEFAULT));
            Log.println(Log.ERROR,"I",""+Base64.encode(secretKey1.getEncoded(),Base64.DEFAULT));
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,secretKey1);

            databytes=cipher.doFinal(messageEncrypted);
            Log.println(Log.ERROR,"I","holaaaafdgkfghkgh: "+secretKey1);

        } catch (Exception ex) {
            System.err.println("Ha succeït un error desxifrant: " + ex);
        }
        return databytes;
    }

    static public byte[] signData(byte[] data, PrivateKey priv) {
        byte[] signature = null;
        try {
            Signature signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(priv);
            signer.update(data);
            signature = signer.sign();
        } catch (Exception ex) {
            System.err.println("Error signant les dades: " + ex);
        }
        return signature;
    }

    static public boolean validateSignature(byte[] data, byte[] signature, PublicKey pub)
    {
        boolean isValid = false;
        try {
            Signature signer = Signature.getInstance("SHA1withRSA");
            signer.initVerify(pub);
            signer.update(data);
            isValid = signer.verify(signature);
        } catch (Exception ex) {
            System.err.println("Error validant les dades: " + ex);
        }
        return isValid;
    }
}
