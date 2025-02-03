/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Model;

import javafx.collections.ObservableList;

/**
 *
 * @author madio
 */
public class Association {
    private String Nom ; 
    private int Id ;
    
    public Association(int Id, String Nom){
        this.Id=Id;
        this.Nom=Nom;
    }
    public String GetNom(){
        return Nom;
    }
    public int GetId(){
        return Id ; 
    }
    public void SetNom(String Nom){
        this.Nom=Nom;
    }
    public void SetId(int Id){
        this.Id=Id;
    }
     public String toString() {
        return Nom ; 
    }
}

