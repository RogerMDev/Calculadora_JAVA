import java.awt.*;
import javax.swing.*;

public class AppUI extends JFrame{
    public static void main(String[] args) {
        // Crear la finestra
        JFrame ventana = new JFrame("Botó d'exemple");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new GridBagLayout()); 

        // Crear el botó
        JButton boto = new JButton("Fes click");
        boto.setPreferredSize(new Dimension(100, 30));

        // Afegir el botó
        ventana.add(boto);

        // Mostrar la finestra
        ventana.setVisible(true);
    }
}

