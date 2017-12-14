/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosystemconsultoria.longevo.util;

/**
 *
 * @author Wendell
 */

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.biosystemconsultoria.longevo.model.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class CustomAuthenticationProvider implements AuthenticationProvider {
       
        private EntityManagerFactory factory;
        private EntityManager manager;
    
	public CustomAuthenticationProvider() {
		super();
	}

        
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
            
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            boolean isAutententico = false;
            
            try {
                
                    factory = Persistence.createEntityManagerFactory("BiosystemPU"); 
                    manager = factory.createEntityManager();                    
                    Usuario user = (Usuario) manager.createQuery("from Usuario u where u.username = :username")
                            .setParameter("username",username)
                            .getSingleResult();
                    
                    try {
                        
                        isAutententico  = CriptografaSenhaUsuario.compare(password,user.getSenha());
                        
                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                        manager.close();
                        factory.close();
                        return null;
                    }
                    
                    if (isAutententico) {
                        
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(user.getPerfilAcesso().getAutorizacao().name()));
			UserDetails userDetails = new User(username, password, grantedAuthorities);
			return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuthorities);
                        
                        
                    } else {
                        manager.close();
                        factory.close();
			return null;
                        
                    }
                    
            } catch (Exception e) {
                    manager.close();
                    factory.close();
                    return null;
                    
            }                
	}

        
	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
