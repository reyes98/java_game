/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Modelo.Armada;
import Modelo.Globales;
import Modelo.Nave;
import Vista.VentanaRegistro;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author sebastian
 */
public final class InvasoresEspaciales extends JFrame implements Runnable,Globales,KeyListener {      
    //atributos de clase:     
    private Armada armada = null;
    private Nave nave = null;    
    private Proyectil proyectil = null;
    
    private int numAliens;
    private int vidas = CANTIDAD_VIDAS;;    
    private int puntaje = 0;
    int contador;// variable para controlar la velocidad de movimiento de los aliens
    int reguladorDeVelocidad;    
    private boolean pausa = false;
    

    private Graphics graficos;
    private BufferedImage imgBuffered;        
    
    private Image backGroundImage = null;
    private Image alienImage = null; 
    private Thread thread;
    
    
    private int ronda;
    private int velocidad;
    
    
    private VentanaRegistro ventanaRegistro;
    //-----------------------------------------

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getNumAliens() {
        return numAliens;
    }

    public void setNumAliens(int numAliens) {
        this.numAliens = numAliens;
    }
    
    
    
    
    
    

    //constructor
    public InvasoresEspaciales(String titulo) {
        super(titulo);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);        
        this.setResizable(false);
        Image iconoAplicacion = Toolkit.getDefaultToolkit().getImage(ICONO_APLICATION);
        this.setIconImage(iconoAplicacion);
        
        //Imagen de fondo
        
        thread = new Thread(this);
        ronda=0;
        contador =  0;
        reguladorDeVelocidad = 5;
        velocidad=VELOCIDAD_DEL_JUEGO;
     }
    //----------------------------------------------------------
    
    //Método que crea los componentes del juego
    public void nuevaRonda(){
        //Crea la nave
        nave = new Nave(this);

        //Crea la armada
        armada = new Armada(nave, this, alienImage);          
        
        //cantadidad de aliens
        numAliens = FILAS_ALIENS*ALIENS_POR_FILA;
        
        //crear gráficos
        imgBuffered = new BufferedImage(ANCHO_FRAME, ALTO_FRAME,BufferedImage.TYPE_INT_RGB);
        graficos = imgBuffered.createGraphics();
        
        //random usado para las imagenes de fondo
        Random random = new Random();
        int numeroRandom = random.nextInt(FONDOS_IMG.length);

        //imagen de fondo
        backGroundImage = new javax.swing.ImageIcon(FONDOS_IMG[numeroRandom]).getImage();
        
        //componentes del frame
        this.setBackground(Color.black);        
        this.setSize(ANCHO_FRAME, ALTO_FRAME);
        this.setLocationRelativeTo(this);        
        this.setVisible(true);
        
        //acumulador de rondas sobrevividas
        ronda++;
        
        //comienza a correr el thread
        iniciaJuego();
        
    }

    
    //método que cambia el estado del atributo pausa
    public void pausarJuego(boolean estado) {
        pausa = estado;
    }
    //-------------------------------------------------

    //método para acumular puntos
    public void addPuntos() throws InterruptedException, SQLException { 
        puntaje += 5;
        numAliens--;
        System.out.println("Puntos = "+puntaje);
        if (numAliens==0) {//para cuando no hayan aliens pare la ejecución            
            stopGame();
            roundOver();
            
        } 
    }
    //-----------------------------------
    
    //ronda finalizada
    public void roundOver() throws SQLException, InterruptedException{
        //resumen de la ronda
        JOptionPane.showMessageDialog(this,"Fin de la ronda "+ronda
                + "\nAlien abatidos: "+ ((FILAS_ALIENS*ALIENS_POR_FILA)-numAliens)
                + "\nPuntaje por aliens abatidos: "+puntaje
                + "\nMultiplicador de ronda: "+ronda
                + "\nPuntaje total: "+ determinarPuntajeTotal()
                +"\nVidas restantes: "+ vidas,"Sobreviviste!",JOptionPane.INFORMATION_MESSAGE);
        
        int op;
        op = JOptionPane.showConfirmDialog(this, "Preparado para la ronda "+(ronda+1)+"? ", "Elige: ",JOptionPane.YES_NO_OPTION);
        if (op==0) {//comienza nueva ronda
            velocidad-=5;
            nuevaRonda();
            pausarJuego(false);
        }else{  
            registro();            
        }
    }
    //---------------------------
    
    //Juego terminado
    public  void gameOver() throws InterruptedException, SQLException{     
        //preguntamos si quiere volver a jugar
        int op;
        op = JOptionPane.showConfirmDialog(this, "Deseas volver a comenzar una nueva partida? ", "Elige: ",JOptionPane.YES_NO_OPTION);        
        if (op==0) {//vuelve a iniciar una nueva partida            
            ronda=0;
            puntaje = 0;
            vidas = 5;
            reguladorDeVelocidad = 5;
            velocidad=VELOCIDAD_DEL_JUEGO;
            nuevaRonda();
            pausarJuego(false);
        }else{            
            System.exit(0);//fin de la ejecución
        }
    }
    //-----------------------------------
    
    //registrar puntaje
    public void registro() throws SQLException, InterruptedException{
        //primero se le pregunta si quiere registrar el puntaje
        int op;
            op = JOptionPane.showConfirmDialog(this,"Deseas registrar este puntaje?:"
                + "\nAlien totales: "+ (puntaje/5)
                + "\nPuntaje por aliens abatidos: "+puntaje
                + "\nMultiplicador de ronda: "+ronda
                + "\nPuntaje total: "+ determinarPuntajeTotal()
                +"\nVidas restantes: "+ vidas,"Game over!",JOptionPane.YES_NO_OPTION);
            
        if (op==0) {
            //Abrimos la ventana de registro y cerramos la del juego
            ventanaRegistro = new VentanaRegistro(determinarPuntajeTotal());  
            ventanaRegistro.setVisible(true);
            this.dispose();
        }else{
            gameOver();
        }
        
    }
    //--------------------------------------
    
    //método para descontar vidas
     public void descontarVida() throws InterruptedException, SQLException{
         if (numAliens!=0) {
            vidas--;
            System.out.println("Vidas = "+vidas);
         }
 
                
         if (vidas==0) {//perdió
            pausarJuego(true);
            stopGame();
            registro();                   
            vidas=5;
            puntaje=0;                       
         }
        
        
    }
    //---------------------------------------- 
     
    //Método para iniciar nuestro hilo
    public void iniciaJuego() {  
        if (!thread.isAlive()) {
            thread.start();
        }
                
    }
    //------------------------------------
    
    // para interrumpr este hilo 
    public void stopGame() throws InterruptedException{
        thread.interrupt();        
    }
    //--------------------------- 
    
    //Método que pinta los gráficos en el frame 
    public void paint(Graphics g) {   
        //El fondo del frame de juego
        graficos.setColor(Color.black);
        graficos.fillRect(0,0, ANCHO_FRAME, ALTO_FRAME);
        graficos.drawImage(backGroundImage, 0, 0, this);
        
        //dibujamos la armada y la nave con los mismo graficos
        armada.dibujarArmada(graficos);
        nave.dibujarNave(graficos); 
        
        //cadenas de texto dibujadas en el frame
        graficos.setColor(Color.CYAN);
        graficos.drawString("Ronda: "+ronda, 20, 50);
        graficos.drawString("Vidas: "+vidas, 20, 70);
        graficos.drawString("Aliens: "+numAliens, 20, 90);        
        
        //dibujamos el buffer que contiene todo
        g.drawImage(imgBuffered,0,0,this); 
     }
    //-----------------------------------

    //vuelve a ejecutar el metodo paint()
    public void update(Graphics g) {
        paint(g);
    } 
    //--------------------------------
    
    //llama al método moverArmada() de la clase Armada
     public void moverAliens() {
         armada.moverArmada();
     }
     //----------------------------------------------
     
     //Método que determinará el puntaje de la ronda
     public long determinarPuntajeTotal(){
        long puntajeTotal= (long)(puntaje*(ronda));            
        return puntajeTotal;
     }
     //-------------------------------------

    //esta pausado?
    public boolean isPaused() {
        return pausa;
    }
    //-------------------------
    
    //Método que controla los niveles
    public void controlNivel(){    
        if (ronda>=1 && ronda<4) {            
            reguladorDeVelocidad=5;
        }
        if (ronda>=4 && ronda<7) {
            reguladorDeVelocidad=4;
        }
        if (ronda>=7 && ronda<10) {
            reguladorDeVelocidad=3;
        }
        if (ronda>=10 && ronda<13) {
            reguladorDeVelocidad=2;
        }
        if (ronda>=13) {
            reguladorDeVelocidad=1;
        }
        
    }
    //---------------------------
    
    //run juego
    public void run() {        
        addKeyListener(this);//activo los eventos de teclado
        controlNivel();
        while(true) {
            
            try {
                Thread.sleep(velocidad);//tiempo de espera de movimiento de los aliens
            } catch (InterruptedException ex) {
                
            }
                                 
            if (!pausa) {//determino si está en pausa el juego
                if (contador >= reguladorDeVelocidad) {                    
                    moverAliens();
                    contador = 0;                    
                }
                repaint();//Actualiza el frame
                contador ++;
            }
            
        }
    }
    //--------------------------------------- 
    
    
    
    //Eventos de teclado    
    @Override
    public void keyPressed(KeyEvent e) {
        //primero validamos que no esten en los bordes y que no este en pausa la ejecución        
        if (!pausa && e.getKeyCode()== KeyEvent.VK_LEFT && !(nave.getPosicionX() < (1))) {
            nave.setX(nave.getPosicionX()-DESPLAZAMIENTO_JUGADOR);//nos movemos unos pixeles a la izquierda         
        }
        if (!pausa && e.getKeyCode()== KeyEvent.VK_RIGHT && !(nave.getPosicionX() > (ANCHO_FRAME-ANCHO_JUGADOR-32))) {
            nave.setX(nave.getPosicionX()+DESPLAZAMIENTO_JUGADOR);//nos movemos unos pixeles a la derecha
        }
        if (!pausa && e.getKeyCode()== KeyEvent.VK_UP && !(nave.getPosicionY() < (ALTURA_JUGADOR/2))) {
            nave.setY(nave.getPosicionY()-DESPLAZAMIENTO_JUGADOR);//nos movemos unos pixeles hacia arriba
        }
        if (!pausa && e.getKeyCode()== KeyEvent.VK_DOWN && !(nave.getPosicionY() > (ALTO_FRAME-(ALTURA_JUGADOR+20)))) {
            nave.setY(nave.getPosicionY()+DESPLAZAMIENTO_JUGADOR);//nos movemos unos pixeles hacia abajo
        }
        
        
        //evento de disparo
        if (!pausa && e.getKeyCode()== KeyEvent.VK_SPACE) {            
            //creamos un proyectil
            proyectil = new Proyectil(nave.getPosicionX(), nave.getPosicionY(), armada, getGraphics(),this);            
            if (proyectil.getEstadoDeDisparo()) {                   
                proyectil.dispara();//disparamos
                
            }                            
        }
        
        if ( e.getKeyChar()== 'p') { //pausamos el juego presionando 'p'
            if (pausa) {
                pausarJuego(false);                          
            }
            else{
                pausarJuego(true);
                try {
                    stopGame();
                } catch (InterruptedException ex) {
                    Logger.getLogger(InvasoresEspaciales.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
                
    }

    @Override
    public void keyTyped(KeyEvent e) {        
    }

    @Override
    public void keyReleased(KeyEvent e) {        
    }    
    //-----------------------------------------------------------------------------------------------------
    
    
}
