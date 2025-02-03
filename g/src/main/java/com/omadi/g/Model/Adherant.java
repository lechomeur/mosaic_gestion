/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Model;

import java.sql.Date;
import java.time.LocalDate;
import javafx.collections.ObservableList;

/**
 *
 * @author madio
 */
public class Adherant {
    private int Id ; 
    private String Nom ; 
    private String Prenom ; 
    private String Adresse ; 
    private String Mail ; 
    private Integer Montant ; 
    private String Cheque_Espece ; 
    private String Telephone ; 
    private Date date_adhesion ; 
    private String associationNom ; 
    private int associationId ; 
  

    public Adherant(int Id, String Nom, String Prenom, String Adresse, String Mail, int montant, String Cheque_Espece, String telephone, Date date_adhesion1, String associationNom1 , int  associationId) {   
        this.Nom=Nom ; 
        this.Id=Id ; 
        this.Prenom = Prenom ; 
        this.Adresse=Adresse ; 
        this.Cheque_Espece = Cheque_Espece ; 
        this.Mail=Mail ; 
        this.Montant = montant ; 
        this.Telephone=telephone ; 
        this.date_adhesion = date_adhesion1 ; 
        this.associationNom = associationNom1 ; 
        this. associationId  =  associationId  ; 
    }


public int getAssociationId(){
    return associationId ;
}
  
    public String getNom() {
        return Nom;
    }
    public int getId(){
        return Id ; 
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String Mail) {
        this.Mail = Mail;
    }

    public int getMontant() {
        return Montant;
    }

    public void setMontant(int Montant) {
        this.Montant = Montant;
    }

    public String getCheque_Espece() {
        return Cheque_Espece;
    }

    public void setCheque_Espece(String Cheque_Espece) {
        this.Cheque_Espece = Cheque_Espece;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public Date getDate_adhesion() {
        return date_adhesion;
    }

    public void setDate_adhesion(Date date_adhesion) {
        this.date_adhesion = date_adhesion;
    }
      @Override
    public String toString() {
        return "Utilisateur {" +
                "Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                ", Adresse='" + Adresse + '\'' +
                ", Mail='" + Mail + '\'' +
                ", Montant=" + Montant +
                ", Cheque_Espece='" + Cheque_Espece + '\'' +
                ", Telephone='" + Telephone + '\'' +
                ", Date d'adhésion=" + date_adhesion +
                '}';
    }
    
     public String getAssociationNom() {
        return associationNom;
    }

    public void setAssociationNom(String associationNom) {
        this.associationNom = associationNom;
    }

}
    

