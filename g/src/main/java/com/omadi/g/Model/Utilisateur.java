/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Model;

/**
 *
 * @author madio
 */
public class Utilisateur {
     private String login;
    private String nom;
    private String prenom;
    String type;
    boolean actif ;
    private String mdp ;

    public Utilisateur(String login, String nom, String prenom) {
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.type = type;
        this.actif = actif;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMdp() {
        return mdp;
    }
    
    

  
   
    public String getLogin() {
        return login;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

}
