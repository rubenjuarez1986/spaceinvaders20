
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Rocio Soriano
 */
public class Disparo {
    Image imagen = null;
    public int posX = 0;
    public int posY =  0;
    

    
    public Disparo(int puntuacion){
            try{
               if(puntuacion<600){
                imagen=ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
               }else{
                   //disparo bonus
                   imagen=ImageIO.read(getClass().getResource("/imagenes/nixon.png"));
               }
            }
            catch(Exception e){
           
        }
    }
    
    public void mueve(){
       
            posY -= 10;
            
        }
            

    public void posicionaDisparo( Nave _nave){
        posX = _nave.posX 
                +_nave.imagenNave.getWidth(null)/2 
                - imagen.getWidth(null)/2;
        posY = _nave.posY - _nave.imagenNave.getHeight(null)/2;
        
    }
    
    
}




