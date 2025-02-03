/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Model;

import java.time.LocalDate;

/**
 *
 * @author madio
 */
public class Evenement {
     
    private  int Id ; 
    private String Titre ; 
    private LocalDate Date_Evenement ; 
    private String Lieu ; 
    
    public Evenement(int Id,String Titre, LocalDate Date_Evenement, String Lieu){
        this.Id = Id;
        this.Titre=Titre;
        this.Lieu=Lieu;
        this.Date_Evenement=Date_Evenement;
    }
    public int getId(){
        return Id; 
    }
    public String GetTitre(){
        return Titre ; 
    }
    public LocalDate getDate_Evenement(){
        return Date_Evenement;
    }
    public String getLieu(){
        return Lieu ; 
    }
    
    public void SetTitre(String Titre){
        this.Titre=Titre;
    }
    public void SetDate_Ev(LocalDate Date_Evenement){
        this.Date_Evenement=Date_Evenement;
    }
    public void SetLieu(String Lieu ){
        this.Lieu=Lieu;
    }
    
}
