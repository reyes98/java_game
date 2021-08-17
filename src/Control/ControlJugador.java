/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sebastian
 */
public class ControlJugador {
    private int idJugador,edadJugador,puntosJugador;
    private String nombreJugador;
    private String sqlEjecutar;
    public ConectaDb conexion;

    public ControlJugador() {
    }

    public int getIdJugador() {
        return idJugador;
    }

    public ConectaDb getConexion() {
        return conexion;
    }

    public void setConexion(ConectaDb conexion) {
        this.conexion = conexion;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public int getEdadJugador() {
        return edadJugador;
    }

    public void setEdadJugador(int edadJugador) {
        this.edadJugador = edadJugador;
    }

    public int getPuntosJugador() {
        return puntosJugador;
    }

    public void setPuntosJugador(int puntosJugador) {
        this.puntosJugador = puntosJugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getSqlEjecutar() {
        return sqlEjecutar;
    }

    public void setSqlEjecutar(String sqlEjecutar) {
        this.sqlEjecutar = sqlEjecutar;
    }
    //-------------------------------------------
    
    //metodo de regitro de usuario
    public boolean registroUsuario(int id, String nombre, int edad, int puntos ){
        conexion = new ConectaDb();
        boolean trans = false;
        sqlEjecutar = "insert into jugador values ("+id+", '"+nombre+"',"+edad+","+puntos+")";
        if(conexion.transaccion(sqlEjecutar)){
            trans = true;
        }
        conexion.cerrar();
        return trans;
        
    }            
    //------------------------------------------
    
    //Método para generar un autoincremento
    public long retornarId() throws SQLException{
        long idJugador=0;
        conexion = new ConectaDb();
        sqlEjecutar = "Select max(idjugador)+1 as idjugador from jugador";
        ResultSet resultado = conexion.consultas(sqlEjecutar);
        
        while (resultado.next()) {            
            idJugador = Long.parseLong(resultado.getString("idjugador"));
        }
        resultado.close();
        conexion.cerrar();
        
        return idJugador;
    }
    //--------------------------------------
    
    
    //Metodo que retorna el modelo de una tabla(para estudiantes) 
    public DefaultTableModel puntajes() throws SQLException{
        DefaultTableModel modeloTabla = new DefaultTableModel();//Crea un nuevo modelo       
        conexion = new ConectaDb();
        sqlEjecutar = "select * from jugador order by puntosjugador desc limit 10";
        ResultSet resultado = conexion.consultas(sqlEjecutar);
        
        //Adiciona a las columnas su titulo
        modeloTabla.addColumn("Id");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Edad");
        modeloTabla.addColumn("Puntos");
        
        
        
        int i=0;
        while (resultado.next()) {//Ciclo que recorre el contenido de los datos en la base de datos            
           
            
            Object[] row = new Object[1];//Crea una nueva fila
            
            modeloTabla.addRow(row);//la adiciona al modelo de la tabla
            
            
            //Adiciona el valor a la fila(valor, fila , columna)                         
            modeloTabla.setValueAt(resultado.getString(1), i, 0);
            modeloTabla.setValueAt(resultado.getString(2), i, 1);
            modeloTabla.setValueAt(resultado.getString(3), i, 2);
            modeloTabla.setValueAt(resultado.getString(4), i, 3);
            //----------------------------------------------------------
                        
            i++;
        }        
        resultado.close();
        conexion.cerrar();
        
        
        return modeloTabla;//retorno del modelo de la tabla
    }
    
}
