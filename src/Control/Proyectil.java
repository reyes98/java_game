/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Armada;
import Modelo.Globales;
import static Modelo.Globales.EXPLOSION_IMG;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastian
 */
public class Proyectil  implements Runnable,Globales {
    

    private int ANCHO_DISPARO = 5;
    private int ALTURA_DISPARO = 2; 

    private int x = 0;

    private int y = 0;

    private boolean estadoDeDisparo = true;

    Armada armada = null;
    
    Graphics g = null;  
    InvasoresEspaciales invasoresEspaciales;
    
    Thread thread;
    
    
    public Proyectil(int xVal, int yVal, Armada a , Graphics g,InvasoresEspaciales ie) {
        this.g = g;
        this.x = xVal+(ANCHO_JUGADOR);//dirrección del disparo
        this.y = yVal+(ALTURA_JUGADOR-1);
        this.armada = a;
        this.invasoresEspaciales = ie;
        
    }
    
    public void dispara(){
        thread = new Thread(this);
        thread.start();
        
    }
    
        
    private boolean moverDisparo() throws SQLException{                
        try {
            //determina si le dimos a algo
            if (armada.checkDisparo(x, y)) {
                //Le dimos!
                System.out.println("Le dimos!");
                Image explosion = new javax.swing.ImageIcon(EXPLOSION_IMG).getImage();
                g.drawImage(explosion, x, y,20,20, null); 
                estadoDeDisparo = false;//para no dibujar mas el proyectil
                return true;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Proyectil.class.getName()).log(Level.SEVERE, null, ex);
        }
        x +=  2;//cambiamos la posicion del proyectil
        dibujarProyectil(g);//lo dibujamos
        
       
        //validamos de que el disparo haya salido de la pantalla
        if (y > ANCHO_FRAME) {
            estadoDeDisparo = false;//para no dibujar mas el proyectil
            return true;
        }
        return false;
    }

    //dibujar el disparo
    public void dibujarProyectil(Graphics g){
        //validamos que el disparo se pueda dibujar
        if (estadoDeDisparo) {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, ANCHO_DISPARO,ALTURA_DISPARO );
        }
        
        
    }

    public boolean getEstadoDeDisparo() {
        return estadoDeDisparo;
    }

    
    public void run() {
        while(true) {
            try {
                Thread.sleep(VELOCIDAD_DEL_PROYECTILES);
            } catch(InterruptedException ie) {
                
            }
            try {
                if (!invasoresEspaciales.isPaused()) {
                    if (moverDisparo()) {                        
                        break;//destruir disparo                        
                    }                   
                    
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(Proyectil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

}
