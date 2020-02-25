/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import static java.awt.Color.green;
import static java.awt.Color.white;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author Rocio Soriano
 */
public class VentanaJuego extends javax.swing.JFrame {

    //para los mensajes
    public static Label label2 = new Label();

    //para la puntuacion
    public static Label label1 = new Label();
    int puntuacion = 0;

    boolean gameOver = false;
    //para que no se cuelgue si le doy a varios marcianos a ka vez con nixon
    boolean UnSoloDisparo = false;
    boolean FinDeJuego = false;

    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 600;

    int filasMarcianos = 6;
    int columnasMarcianos = 10;
    int contador = 0;
    //Reproduce el sonido una única vez.
    boolean acierto = true;
    BufferedImage buffer = null;

    //buffer para guardar las imágenes de todos los marcianos
    BufferedImage plantilla = null;
    Image[] imagenes = new Image[30];

    Timer temporizador = new Timer(10, new ActionListener() {//bucle de animacion del juego. refresca el contenido de la pantalla
        @Override
        public void actionPerformed(ActionEvent ae) {
            //TODO: codigo de animacion
            bucleJuego();
        }
    });

    Marciano marciano = new Marciano(ANCHOPANTALLA);//inicializo el marciano
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo(puntuacion);
    ArrayList<Disparo> listaDisparos = new ArrayList();
    ArrayList<Explosion> listaExplosion = new ArrayList();

    //el array de dos dimensiones que guarda la lista de marcianos
    Marciano[][] listaMarcianos = new Marciano[filasMarcianos][columnasMarcianos];
    Image fondo;
    Image naveFoto;
    //dirección en la que se mueve el grupo de marcianos
    boolean direccionMarcianos = true;

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {

        initComponents();
        try {
            fondo = ImageIO.read(getClass().getResource("/imagenes/fondo.jpg"));
        } catch (IOException ex) {
        }

        Font font1;
        Font font2 = null;
        font1 = new Font("Courier New", Font.BOLD, 30);
        font2 = new Font("Calibri", Font.BOLD, 30);
        //puntuacion
        label1.setFont(font1);
        label1.setForeground(white);
        Color blue = null;
        label1.setBackground(blue);
        label1.setBounds(700, 0, 100, 30);
        label1.setText("     0");
        jPanel1.add(label1);
        jPanel1.add(label2);
        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        } catch (IOException ex) {
        }
        //cargo las 30 imágenes del spritesheet en el array de bufferedimages
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                imagenes[i * 4 + j] = plantilla
                        .getSubimage(j * 64, i * 64, 64, 64)
                        .getScaledInstance(35, 35, Image.SCALE_SMOOTH);

            }
        }
        imagenes[19] = miNave.imagenNave.getScaledInstance(150, 80, 100);
        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32); //sprite de la nave
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);
        imagenes[23] = plantilla.getSubimage(255, 320, 32, 32);//explosion parteB
        imagenes[22] = plantilla.getSubimage(255, 289, 32, 32);//explosion parteA

        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        jPanel1.setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);//inicializo el buffer
        buffer.createGraphics();

        temporizador.start();//arranco el temporizador

        miNave.imagenNave = imagenes[19];
        miNave.posX = ANCHOPANTALLA / 2 - miNave.imagenNave.getWidth(this) / 2;
        miNave.posY = ALTOPANTALLA - 150;
        //creamos el array de marcianos
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j] = new Marciano(ANCHOPANTALLA);
                listaMarcianos[i][j].imagen1 = imagenes[2 * i];
                listaMarcianos[i][j].imagen2 = imagenes[2 * i + 1];
                listaMarcianos[i][j].posX = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].posY = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));

            }
        }
        miDisparo.posY = -2000;
    }

    private void pintaMarcianos(Graphics2D _g2) {
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j].mueve(direccionMarcianos);
                if (contador < 50) {
                    _g2.drawImage(listaMarcianos[i][j].imagen1, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                } else if (contador < 100) {
                    _g2.drawImage(listaMarcianos[i][j].imagen2, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                } else {
                    contador = 0;
                }

                if (listaMarcianos[i][j].posX == ANCHOPANTALLA - listaMarcianos[i][j].imagen1.getWidth(null) || listaMarcianos[i][j].posX == 0) {
                    direccionMarcianos = !direccionMarcianos;
                    for (int k = 0; k < filasMarcianos; k++) {
                        for (int m = 0; m < columnasMarcianos; m++) {
                            listaMarcianos[k][m].posY += listaMarcianos[k][m].imagen1.getHeight(null);
                        }
                    }
                }
            }
        }
    }

    private void pintaDisparo(Graphics2D g2) {
        //pinta todos los disparos
        Disparo disparoAux;
        for (int i = 0; i < listaDisparos.size(); i++) {
            disparoAux = listaDisparos.get(i);
            disparoAux.mueve();
            if (disparoAux.posY < 0) {
                listaDisparos.remove(i);
            } else {
                g2.drawImage(disparoAux.imagen, disparoAux.posX, disparoAux.posY, null);

            }
        }
    }

    private void pintaExplosiones(Graphics2D g2) {
        //pinta todas las explosiones
        Explosion explosionAux;
        for (int i = 0; i < listaExplosion.size(); i++) {
            explosionAux = listaExplosion.get(i);
            explosionAux.tiempoDeVida--;
            if (explosionAux.tiempoDeVida > 25) {
                g2.drawImage(explosionAux.imagen1,
                        explosionAux.posX,
                        explosionAux.posY, null);
            } else {
                g2.drawImage(explosionAux.imagen2,
                        explosionAux.posX,
                        explosionAux.posY, null);
            }
            //si el tiempo de vida de la explosión es menor o igual a 0 la elimino
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosion.remove(i);
            }
        }
    }

    private void bucleJuego() {//redibuja los objetos en el jPanel1

        contador++;
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();//borro todo lo que ahi en el buffer

        if (puntuacion >= 2900) {
            FinDeJuego = true;
        }
        if (!gameOver && !FinDeJuego) {
            g2.setColor(Color.BLACK);//doy el color negro a la pantalla
            g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            g2.drawImage(fondo, 0, 0, null);
            ///////////////////////////////////////////////////
            pintaMarcianos(g2);
            //dibujo la nave
            g2.drawImage(miNave.imagenNave, miNave.posX, miNave.posY, null);
            pintaDisparo(g2);
            pintaExplosiones(g2);

            miNave.mueve();
            chequeaColision();
            colisionNaveMarciano();
            ///////////////////////////////////////////////////
        } else if (gameOver && !FinDeJuego) {
            gameOver(g2);
        }
        if (FinDeJuego) {
            Fin(g2);
        }
        g2 = (Graphics2D) jPanel1.getGraphics();//dibujo de golpe el buffer sobre el jPanel
        g2.drawImage(buffer, 0, 0, null);
    }

    private void Fin(Graphics2D g4) {
        try {
            fondo = ImageIO.read(getClass().getResource("/imagenes/victoria.jpg"));
            g4.setColor(Color.BLACK);//doy el color negro a la pantalla
            g4.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            g4.drawImage(fondo, 0, 0, null);
            /*buffer.getGraphics().drawImage(gameOver, 0, 0, ANCHOPANTALLA, ALTOPANTALLA, null);*/

        } catch (IOException ex) {
        }
    }

    private void gameOver(Graphics2D g3) {

        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                //listaMarcianos[i][j] = null;
                listaMarcianos[i][j].posY = 2000;
            }
        }

        try {
            fondo = ImageIO.read(getClass().getResource("/imagenes/derrota.jpg"));
            g3.setColor(Color.BLACK);//doy el color negro a la pantalla
            g3.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            g3.drawImage(fondo, 0, 0, null);
            /*buffer.getGraphics().drawImage(gameOver, 0, 0, ANCHOPANTALLA, ALTOPANTALLA, null);*/

        } catch (IOException ex) {
        }
    }

    private void reproduce(String cancion) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(cancion)));
            clip.loop(0);
            Thread one = new Thread() {
                public void run() {
                    while (clip.getFramePosition() < clip.getFrameLength()) {
                        Thread.yield();
                    }
                }
            };
            one.start();
        } catch (Exception e) {
        }
    }

    //chequea si un disparo y un marciano colisionan
    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        for (int k = 0; k < listaDisparos.size(); k++) {
            UnSoloDisparo = true;
            //calculo el rectangulo que contiene al disparo correspondiente
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX,
                    listaDisparos.get(k).posY,
                    listaDisparos.get(k).imagen.getWidth(null),
                    listaDisparos.get(k).imagen.getHeight(null));

            for (int i = 0; i < filasMarcianos; i++) {
                for (int j = 0; j < columnasMarcianos; j++) {
                    //calculo el rectángulo corresponmdiente al marciano que estoy comprobando
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].posX,
                            listaMarcianos[i][j].posY,
                            listaMarcianos[i][j].imagen1.getWidth(null),
                            listaMarcianos[i][j].imagen1.getHeight(null));

                    if (rectanguloDisparo.intersects(rectanguloMarciano) && UnSoloDisparo) {
                        //si entra aquí es porque han chocado un marciano y el disparo
                        UnSoloDisparo = false;
                        Explosion e = new Explosion();
                        e.posX = listaMarcianos[i][j].posX;
                        e.posY = listaMarcianos[i][j].posY;
                        e.imagen1 = imagenes[23];
                        e.imagen2 = imagenes[22];
                        listaExplosion.add(e);
                        e.sonidoExplosion.start();
                        listaMarcianos[i][j].posY = 2000;
                        listaDisparos.remove(k);
                        puntuacion = puntuacion + 50;
                        label1.setText("" + puntuacion);
                    }
                }
            }
        }
    }

    private void colisionNaveMarciano() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        rectanguloNave.setFrame(miNave.posX,
                miNave.posY,
                miNave.imagenNave.getWidth(null),
                miNave.imagenNave.getHeight(null));
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                //calculo el rectángulo corresponmdiente al marciano que estoy comprobando
                rectanguloMarciano.setFrame(listaMarcianos[i][j].posX,
                        listaMarcianos[i][j].posY,
                        listaMarcianos[i][j].imagen1.getWidth(null),
                        listaMarcianos[i][j].imagen1.getHeight(null));

                //si entra aqui es xk el marciano ha interactuado con la nave
                if (rectanguloMarciano.intersects(rectanguloNave)) {
                    gameOver = true;
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 633));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 619, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 921, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                Disparo d = new Disparo(puntuacion);
                d.posicionaDisparo(miNave);
                //agregamos el disparo a la lista de disparos
                listaDisparos.add(d);
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;
        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
