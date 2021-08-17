/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastian
 */
public class ConectaDb {
    public static int IDJUGADOR;//Se usa en el boton registro donde se instancia el objeto jugador
    //Definicion de variables de coneccion
    private String user, pass, urlConectar, driver;//Definir quien va a ser el usuario cual será la contraseña y el driver
    //Variable de la clase connect para conectar la base de datos
    //Es el puente entre el lenguaje de programacion y labase de datos
    public Connection conection;
    //estatament controla las coneciones y las ejecuciones de las sql
    public java.sql.Statement statement;//toma las ordenes de que se envian a la base d edatos y ejecutarla
    private ResultSet resultado;
    public static String sql = "SELECT * FROM jugador";
    public static String agregar = "INSERT INTO jugador(idjugador,nombrejugador,edadjugador,puntosjugador) VALUES('005','Pepito','28','25')";

    //--------------Metodo constructor para la clase conectar
    public ConectaDb() {
        user = "postgres";
        pass = "0305";
        driver = "org.postgresql.Driver";
        urlConectar = "jdbc:postgresql://localhost/juego";

        try {
            Class.forName(driver);
            conection = DriverManager.getConnection(urlConectar, user, pass);
            System.out.println("SI SE CONECTA A LA DB");
            //statement = conection.createStatement();
//executeQuery: Solo permite ejecutar sentencias de recuperación (SELECT). Si como parámetro hay una sentencia 
            //  de modificación, lanza una SQLException.
//            resultado = statement.executeQuery(sql);
//            //se recorre la base de datos con un while
//            while(resultado.next()){
//                IDJUGADOR=resultado.getInt("idjugador");
//                System.out.println("id:"+IDJUGADOR);
//            }

        } catch (SQLException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("NO SE CONECTA A LA DB");
        }
    }
    
    
    
    //consultas
    public ResultSet consultas(String cadSQL){
        ResultSet resultado = null; 
        try {
                       
            statement = conection.createStatement();
            resultado = statement.executeQuery(cadSQL);
        } catch (SQLException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return resultado;
    }
    //------------------------------------
    
    //cerrar conexión
    public void cerrar(){
        try {
            conection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //---------------------------
    
    //cierra resultado
    public void cierraResultado(ResultSet resultado){
        try {
            resultado.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //---------------------------------
    
    //metodo para verificar si se hizo una transaccion
    public boolean transaccion(String cadenaSQL){
        try {
            statement = conection.createStatement();
            statement.execute(cadenaSQL);
            statement.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConectaDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    //----------------------------------------
    

//    public static void main(String[] args) throws SQLException {
//        ConectaDb conectadb = new ConectaDb();                  
//        ResultSet resultados =conectadb.consultas("select * from jugador order by puntosjugador desc");
//        while (resultados.next()) {                
//            System.out.println("Id: "+resultados.getString(1)+ 
//                    " Nombre: "+ resultados.getString(2)+
//                    " Edad: "+ resultados.getString(3) + 
//                    " puntos: "+ resultados.getString(4));
//        }
////            conectadb.statement = conectadb.conection.createStatement();
////            conectadb.resultado = conectadb.statement.executeQuery(sql);
////            conectadb.statement.execute(agregar); // para agregar un nuevo usuario a la base de datos
//            //Forma de captura de datos hacer el statement con un insert para introducir un jugador
//       
//
//    }
    /*
    NOTA IMPORTANTE: 
    execute: ejecuta una consulta SQL que puede ser de recuperación (SELECT) o modificación (UPDATE, DELETE,INSERT).
    
    Para hacer consultas de modificación también podés usar el executeUpdate()
     */
}
