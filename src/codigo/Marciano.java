/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Ruben juarez
 */
public class Marciano {

    public Image imagen1 = null;//creo imagenes
    public Image imagen2 = null;

    public int posX = 0;
    public int posY = 0;

    private int anchoPantalla;

    public int vida = 100;

    public Marciano(int _anchoPantalla) {
        anchoPantalla = _anchoPantalla;
    }

    //método para mover a la nave 
    public void mueve(boolean direccion) {
        if (direccion) {
            posX=posX+3;
        } else {
            posX = posX-3;
        }
    }
}
