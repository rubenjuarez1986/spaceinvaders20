
package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Jorge Cisneros
 */
public class Nave {
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;
    
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    
    public Nave(){
            try{
                imagen=ImageIO.read(getClass().getResource("/imagenes/nave.png"));
            }
            catch(Exception e){
            }
    }
    
    public void mueve(){
        if (pulsadoIzquierda){
            posX--;
        }
        if (pulsadoDerecha){
            posX++;
        }      
    }
    
    
    
}




