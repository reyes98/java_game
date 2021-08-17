/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import com.toedter.calendar.JDateChooser;
import java.time.LocalDate;
import java.util.Calendar;


/**
 *
 * @author sebastian
 */
public class Fecha {    
    public Fecha() {
    }
    
    
    //Método que calcula la edad del jugador teniendo como parametro la fecha que seleccionó
    public int calcularEdad(JDateChooser fechaNacimiento){
        LocalDate hoy = LocalDate.now();//toma la fecha actual
        
        int difAnio = hoy.getYear() - fechaNacimiento.getCalendar().get(Calendar.YEAR);
        int difMes = hoy.getMonthValue() - (fechaNacimiento.getCalendar().get(Calendar.MONTH)+1);
        int difDia = hoy.getDayOfMonth() - fechaNacimiento.getCalendar().get(Calendar.DATE);
        
        if(difMes<0 ||(difMes==0 && difDia<0)){//Por si ha cumplido años
            difAnio -=1;
        }
        return difAnio;// los años que tiene
    }
    //----------------------------------------------------------
    

    
}
