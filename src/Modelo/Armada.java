package Modelo;

import Control.InvasoresEspaciales;
import Control.BombaAlien;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastian
 */
public class Armada  implements Globales{
    //Armada representada en matriz
    Alien ejercitoAlienigena[][] = new Alien[FILAS_ALIENS][ALIENS_POR_FILA];  
    //Dirección hacia donde se mueven
    boolean movimientoAbajo = true;    
    //Un Vector que nos guardará las bombas de los aliens
    private Vector bombasDeAliens = new Vector();    
    
    private Nave nave;
    private InvasoresEspaciales invasoresEspaciales;
    Image alienImage = null;
	
    public Armada(Nave n, InvasoresEspaciales ie, Image ai) {
        nave = n;
	invasoresEspaciales = ie;
	alienImage = ai;
	alienImage = new javax.swing.ImageIcon(ALIEN_IMG).getImage();
	
        crearEjercito();
	posicionarEjercito();
    }

    //creo el ejercito alienigena con dos for anidados
    private void crearEjercito() {        
        //Inicializo la matriz
	for (int i = 0; i < FILAS_ALIENS; i++) {
            for (int j = 0; j < ALIENS_POR_FILA; j++) {
                ejercitoAlienigena[i][j] = new Alien(alienImage, invasoresEspaciales);
            }            
	}	        
    }
    //------------------------------------------------------
    
    //coloco la posicion inicial desde donde van a empezar a atacar los aliens
    private void posicionarEjercito() {
        int inicialY = 0;
	int inicialX = 0;
        
        for (int i = 0; i < FILAS_ALIENS; i++) {
            inicialY+=50;//
            inicialX = ANCHO_FRAME-50;// reseteo la posicion
            for (int j = 0; j < ALIENS_POR_FILA; j++) {
                ejercitoAlienigena[i][j].setPosition(inicialX, inicialY);
                inicialX -= 40;
            }            
        }		
    }
    //---------------------------------------------------------

    //En este método movemos a la armada
    public void moverArmada() {
	
        if (movimientoAbajo) {
            //nos movemos para la abajo
            
            //Primero: determinamos si algún alien esta en el borde de abajo
            for (int i = 0; i < FILAS_ALIENS; i++) {
                for (int j = ALIENS_POR_FILA-1; j >= 0; j--) {// en decremento por que empezamos a verificar desde el último
                    //Si el alien no ha sido impactado
                    if (!ejercitoAlienigena[i][j].haSidoImpactado()) {
                        
                        //Validamos si algún alien esta en el borde
                        if (ejercitoAlienigena[i][j].getYPos() > (ALTO_FRAME - (Alien.ALTURA_ALIEN+5))) {
                            //Cambiamos la direccion
                            movimientoAbajo = false;

                            //Coloca la nueva posición en el eje 
                            for (int x = 0; x < FILAS_ALIENS; x++) {
                                for (int y = ALIENS_POR_FILA-1; y >= 0; y--) {
                                    ejercitoAlienigena[x][y].setPosition(ejercitoAlienigena[x][y].getXPos()-DESPLAZAMIENTO_ALIENS, ejercitoAlienigena[x][y].getYPos());                                                  
                                }
                            }
                            return;//Si se cumple esta condición no se debe mover mas a hacia abajo ya que está en el borde

                        }
                    }

                } 
            }
            //Segundo: Mover todos hacia abajo
            for (int i = 0; i < FILAS_ALIENS; i++) {                
                for (int j = 0; j < ALIENS_POR_FILA; j++) {
                    ejercitoAlienigena[i][j].setPosition(ejercitoAlienigena[i][j].getXPos(), ejercitoAlienigena[i][j].getYPos()+DESPLAZAMIENTO_ALIENS);                    
                }
            }                 		    

	}
        else {
            //Nos movemos para arriba
            
            //Primero: determinamos si algún alien esta en el borde de arriba
            for (int i = 0; i < FILAS_ALIENS; i++) {
                for (int j = 0; j < ALIENS_POR_FILA; j++) {
                    //Si el alien no ha sido impactado 
                    if (!ejercitoAlienigena[i][j].haSidoImpactado()) {
                        
                        //Validamos si algún alien esta en el borde
                        if (ejercitoAlienigena[i][j].getYPos() < (Alien.ALTURA_ALIEN)) {
                            //Cambiamos la direccion
                            movimientoAbajo = true;

                            //Coloca la nueva posición en el eje y (más abajo)
                            for (int x = 0; x < FILAS_ALIENS; x++) {
                                for (int y = 0; y < ALIENS_POR_FILA; y++) {
                                    ejercitoAlienigena[x][y].setPosition(ejercitoAlienigena[x][y].getXPos()-DESPLAZAMIENTO_ALIENS, ejercitoAlienigena[x][y].getYPos());                                                  
                                }
                            }
                            return;//Si se cumple esta condición no se debe mover mas hacia abajo ya que está en el borde

                        }
                    }

                } 
            }
            //Segundo: Mover todos hacia arriba
            for (int i = 0; i < FILAS_ALIENS; i++) {                
                for (int j = 0; j < ALIENS_POR_FILA; j++) {
                    ejercitoAlienigena[i][j].setPosition(ejercitoAlienigena[i][j].getXPos(), ejercitoAlienigena[i][j].getYPos() - DESPLAZAMIENTO_ALIENS);                    
                }
            }                       	    	    
	}
        if (invasoresEspaciales.getNumAliens()!=0) {
            for (int i = 0; i < FILAS_ALIENS; i++) {
                for (int j = 0; j < ALIENS_POR_FILA; j++) {
                    if (!ejercitoAlienigena[i][j].haSidoImpactado()) {
                        if (ejercitoAlienigena[i][j].getXPos()<= 5) {
                            try {
                                invasoresEspaciales.pausarJuego(true);
                                invasoresEspaciales.stopGame();
                                invasoresEspaciales.registro();

                            } catch (InterruptedException | SQLException ex) {
                                Logger.getLogger(Armada.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }            
            }
            
        }
        
	//Bombardeo de aliens!
        comenzarBombardeo();	
    }
    
    //método de bombardeo aleatorio
    public void comenzarBombardeo(){        
        Random random = new Random();// creamos un random 
        int bombardero[] = new int[ALIENS_POR_FILA];//creamos un arreglo para determinar al alien que soltara la bomba
        for (int i = 0; i < bombardero.length; i++) {
            bombardero[i] = random.nextInt(FILAS_ALIENS);//elejimos un alien al azar de la fila
        }
        
        for (int i = 0; i < ALIENS_POR_FILA; i++) {
            int numRandom =random.nextInt(FILAS_ALIENS);
            if (!ejercitoAlienigena[numRandom][i].haSidoImpactado()) {//verificamos que ese alien no haya sido impactado antes
                //creamos una nueva bomba para nuestro alienigena elegido aletoriamente
                BombaAlien ba = new BombaAlien(ejercitoAlienigena[numRandom][i].getXPos()+(int)(ANCHO_ALIEN/2), ejercitoAlienigena[numRandom][i].getYPos(), nave,invasoresEspaciales);
                bombasDeAliens.add(ba);//adicionamos la bomba al vector
            }
        }    
    }
    //------------------------------------------------

    public void dibujarArmada(Graphics g) {
        //Dibujamos a los aliens
	for (int i = 0; i < FILAS_ALIENS; i++) {
            for (int j = 0; j < ALIENS_POR_FILA; j++) {
                ejercitoAlienigena[i][j].dibujarAlien(g);
            }            
	} 
        //Dibujamos la bomba
	Vector tmp = new Vector();
	for (int i = 0; i < bombasDeAliens.size(); i++) {
            BombaAlien ba = (BombaAlien)bombasDeAliens.elementAt(i);
	    
	    if (ba.getEstadoDeDisparo()) {                
		tmp.addElement(ba);
	    }	    
	    ba.dibujarbomba(g);	    	    
	}
	bombasDeAliens = tmp;
    }

    //Detectamos si el disparo impactó un alien y anotamos un punto
    public boolean checkDisparo(int x, int y) throws InterruptedException, SQLException{
        for (int i = 0; i < ALIENS_POR_FILA; i++) {            
            for (int j = 0; j < FILAS_ALIENS; j++) {
                if (ejercitoAlienigena[j][i].alienAbatido(x, y)) {
                    invasoresEspaciales.addPuntos();
                    return true;
                }
            }            	    
	}
	return false;
    }
    
}
