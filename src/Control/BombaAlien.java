/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Globales;
import Modelo.Nave;
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
public final class BombaAlien implements Globales,Runnable{    
    
    private int BOMBA_ANCHO  = 5;
    private int BOMBA_ALTO = 2;    
    
    private int x = 0;

    private int y = 0;

    boolean estadoDeDisparo = true;

    Nave nave = null;
    
    Thread thread;
    InvasoresEspaciales invasoresEspaciales;
    
    public BombaAlien(int xVal, int yVal, Nave n, InvasoresEspaciales ie) {
        x = xVal-(BOMBA_ANCHO/2);
	y = yVal+(BOMBA_ALTO/2);
	nave = n;
        invasoresEspaciales = ie;
	activarBombas();
    }
    
    //iniciamos el thread
    public void activarBombas(){
        thread = new Thread(this);
	thread.start();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
    

    
    private boolean moverBomba() throws InterruptedException, SQLException {	
	//Validamos si nos dieron
	if (nave.checkBomba(x, y)) {
            //nos dieron :(
            System.out.println("Nos dieron :(");
	    nave.ImpactadoPorUnAlien();
	    estadoDeDisparo = false;
	    return true;
	}

        x -=  2;//movemos la bomba
	
	//Condicion eliminar las bombas si se salen de la pantalla
	if (x < 0) {
	    estadoDeDisparo = false;
	    return true;
	}
	
		
	return false;
    }

    //dibuja la bomba
    public void dibujarbomba(Graphics g) {
	if (estadoDeDisparo) {
            g.setColor(Color.red);
	} else {
            g.setColor(Color.black);
            if (nave.checkBomba(x, y)) {
                Image explosion = new javax.swing.ImageIcon(EXPLOSION_IMG).getImage();
                g.drawImage(explosion, x+5, y,20,20, null);                
            }
	}
        g.fillRect(x, y, BOMBA_ANCHO, BOMBA_ALTO);
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
                try {
                    if (!invasoresEspaciales.isPaused()) {
                        if (moverBomba()) {
                            break;
                        }                        
                        
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(BombaAlien.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(BombaAlien.class.getName()).log(Level.SEVERE, null, ex);
            }

	}
    }

    
}
