import java.awt.*;
import javax.swing.*;

public class AppUI extends JFrame{
    public static void main(String[] args) {
        // Crear la finestra
        JFrame finestra = new JFrame("Botó d'exemple");
        finestra.setSize(300, 200);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout()); 

        // Crear el botó
        JButton boto = new JButton("Fes click");
        boto.setPreferredSize(new Dimension(100, 30));

        // Afegir el botó
        finestra.add(boto);

        // Mostrar la finestra
        finestra.setVisible(true);
    }
}
