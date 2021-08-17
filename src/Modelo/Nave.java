/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Control.InvasoresEspaciales;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.SQLException;


/**
 *
 * @author sebastian
 */
public class Nave implements  Globales {
    
    private int posicionX = 0;
    private int posicionY = 0;

    InvasoresEspaciales invasoresEspaciales = null;    
    boolean naveHerida = false;
    
    
    
    public Nave( InvasoresEspaciales ie) {
        this.invasoresEspaciales = ie;       
        
        //Posicion inicial de la nave
        this.posicionX = 0;
        this.posicionY = (int)((ALTO_FRAME/2)-(ALTURA_JUGADOR/2));
    }
    
    

    
    public void dibujarNave(Graphics g) {        
        Image img = new javax.swing.ImageIcon(NAVE_IMG).getImage();
        g.drawImage(img, posicionX, posicionY,null);
                       
    }
           

    
    //Si una bomba impacto a la nave
    public boolean checkBomba(int xBomba, int yBomba) {
        
        //vamos a mirar si la nave ha sido impactada
        if ((yBomba >= (posicionY+20)) && (yBomba <= (posicionY+(ALTURA_JUGADOR)))) {
             //Si en el eje Y está bien, ahora miraremos en el eje X
            if ((xBomba >= posicionX) && (xBomba <= (posicionX+ANCHO_JUGADOR))) {
                //La nave fue herida!
                naveHerida = true;
                return true;
            }
        } 
        return false;
    }

    public void ImpactadoPorUnAlien()throws InterruptedException, SQLException {
        invasoresEspaciales.descontarVida();
    }

    public void setX(int x) {
        this.posicionX = x;
    }
    public void setY(int y) {
        this.posicionY = y;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }
    
    public boolean isNaveHerida() {
        return naveHerida;
    }

}