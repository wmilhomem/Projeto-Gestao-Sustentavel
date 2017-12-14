
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.util;

/**
 * gerar o Hash das senhas utilizando o algoritmo SHA-2 e transformando em formato hexadecimal.
 * @author Wendell
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptografaSenhaUsuario {
    
    public static String execute(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8")); 
        
        
        StringBuilder hexString = new StringBuilder(); 
        for (byte b : messageDigest) { 
            hexString.append(String.format("%02X", 0xFF & b)); 
        }
        
        return hexString.toString();
        
    }
    
    public static boolean compare(String senhaNova, String senhaUser) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

        byte messageDigestSenhaNova[] = algorithm.digest(senhaNova.getBytes("UTF-8"));
        StringBuilder hexStringSenhaNova = new StringBuilder();
        for (byte b : messageDigestSenhaNova) { 
            hexStringSenhaNova.append(String.format("%02X", 0xFF & b)); 
        }
        String senhahexNova = hexStringSenhaNova.toString(); 
        
        
     //   byte messageDigestSenhaUser[] = algorithm.digest(senhaUser.getBytes("UTF-8"));
     //   StringBuilder hexStringSenhaUser = new StringBuilder(); 
     //   for (byte b : messageDigestSenhaUser) { 
     //       hexStringSenhaUser.append(String.format("%02X", 0xFF & b)); 
     //   } 
     //   String senhahexUser = hexStringSenhaUser.toString();

        return senhahexNova.equals(senhaUser);
    }
}
