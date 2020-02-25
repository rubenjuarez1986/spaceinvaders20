
package codigo;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ruben juarez
 */
public class Nave {
    Image imagenNave = null;
    public int posX = 0;
    public int posY = 0;
    
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    
    public Nave(){
        try {
            imagenNave = ImageIO.read(getClass().getResource("/imagenes/nave.png"));
        } catch (IOException ex) {
          
        }

    }
    
    public void mueve(){
        if (pulsadoIzquierda && posX >0){
            posX -= 3;
        }
        if (pulsadoDerecha && posX 
                < VentanaJuego.ANCHOPANTALLA - imagenNave.getWidth(null)){
            posX += 3 ;
        }      
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {
        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoDerecha = false;
    }

    public boolean isPulsadoDerecha() {
        return pulsadoDerecha;
    }

    public void setPulsadoDerecha(boolean pulsadoDerecha) {
        this.pulsadoDerecha = pulsadoDerecha;
        this.pulsadoIzquierda = false;
    }


   
    
    
}




