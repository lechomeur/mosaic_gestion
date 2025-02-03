/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Model;

/**
 *
 * @author madio
 */
public class Participation {
    private Evenement evenmentId;
    private Participant participantId ; 
    
    
      public Participation(Evenement evenementId, Participant participantId) {
        this.evenmentId = evenementId;
        this.participantId = participantId;
    }

    // Getters
    public Evenement getEvenement() {
        return evenmentId;
    }

    public Participant getParticipant() {
        return participantId;
    }

    // Setters
    public void setEvenement(Evenement evenement) {
        this.evenmentId = evenement;
    }

    public void setParticipant(Participant participant) {
        this.participantId = participant;
    }

    // toString
    @Override
    public String toString() {
        return "Participation{" +
               "evenement=" + evenmentId +
               ", participant=" + participantId +
               '}';
    }

}
