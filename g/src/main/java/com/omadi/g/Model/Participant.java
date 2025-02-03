package com.omadi.g.Model;

import java.sql.Date;

/**
 *
 * @author madio
 */
public class Participant {

    private int Id;
    private String Nom;
    private String Prenom;
    private int Age;
    private String Telephone;
    private int Montant;
    private String Cheque_Espece;
    private String Adherant;
    private String Genre;
    private String Mail;

    public Participant(int Id, String Nom, String Prenom, int Age, String Telephone,
             int Montant, String Cheque_Espece, String Adherant, String Genre, String Mail) {
        this.Id = Id;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Age = Age;
        this.Telephone = Telephone;
        this.Montant = Montant;
        this.Cheque_Espece = Cheque_Espece;
        this.Adherant = Adherant;
        this.Genre = Genre;
        this.Mail = Mail;
    }

    public Participant(int id, String nom, String prenom, int age, String telephone, int montant, String ce, String mail, String genre) {
         this.Id = id;
        this.Nom = nom;
        this.Prenom = prenom;
        this.Age = age;
        this.Telephone = telephone;
        this.Montant = montant;
        this.Cheque_Espece = ce;
        this.Mail = mail;
        this.Genre = genre;
    }

    public Participant(int aInt, String string, String string0) {
      this.Id=aInt;
      this.Nom=string;
      this.Prenom=string0;
    }

       public Participant(String nom, String prenom) {
        this.Nom = nom;
        this.Prenom = prenom;
    }
   
    

    public int getId() {
        return Id;
    }

    public String getMail() {
        return Mail;
    }

    public String getNom() {
        return Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public int getAge() {
        return Age;
    }

    public String getTelephone() {
        return Telephone;
    }

    public int getMontant() {
        return Montant;
    }

    public String getCheque_Espece() {
        return Cheque_Espece;
    }

    public String getAdherant() {
        return Adherant;
    }

    public String getGenre() {
        return Genre;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public void setMail(String Mail) {
        this.Mail = Mail;
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public void setTel(String Telephone) {
        this.Telephone = Telephone;
    }

    public void setMontant(int Montant) {
        this.Montant = Montant;
    }

    public void setCheque_Espece(String Cheque_Espece) {
        this.Cheque_Espece = Cheque_Espece;
    }

    public void setAdherant(String Adherant) {
        this.Adherant = Adherant;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
    }

    @Override
    public String toString() {
        return "Participant{"
                + "Id=" + Id
                + ", Nom='" + Nom + '\''
                + ", Prenom='" + Prenom + '\''
                + ", Age=" + Age
                + ", Telephone='" + Telephone + '\''
                + ", Montant=" + Montant
                + ", Cheque_Espece='" + Cheque_Espece + '\''
                + ", Adherant='" + Adherant + '\''
                + ", Genre='" + Genre + '\''
                + ",Mail='" + Mail + '\''
                + '}';
    }

}
