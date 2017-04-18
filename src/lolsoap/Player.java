/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolsoap;

/**
 *
 * @author Magnus
 */
public class Player {
    
    int ID;
    boolean hasWon;
    int gameID;
    
    public Player(){};
    
    public Player(int id, int gameID){
        
        this.ID = id;
        hasWon = false;
        this.gameID = gameID;
          
            }
    
}
