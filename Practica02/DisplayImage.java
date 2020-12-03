import java.awt.Image;

import javax.swing.*;
 
public class DisplayImage {
    public DisplayImage() {
        JFrame frame = new JFrame("Display Image");
        frame.setBounds(20, 20, 400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JPanel panel = (JPanel)frame.getContentPane();
 
        JLabel label = new JLabel();

        ImageIcon imageIcon = new ImageIcon("Complemento/Imagenes/Atomizador.jpg"); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);

        label.setIcon(imageIcon);
        
        panel.add(label);
 
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main (String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DisplayImage();
            }
        });
    }
}

/*abstract



*/
