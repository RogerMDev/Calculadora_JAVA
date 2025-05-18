import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppUI extends JFrame {
    
    public static void mostrarFinestra() {
        JFrame finestra = new JFrame("Calculadora per a la Marina");
        finestra.setSize(500, 400);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Crear la pantalleta
        JTextField pantalleta = new JTextField();
        pantalleta.setEditable(false);
        pantalleta.setHorizontalAlignment(JTextField.RIGHT);
        pantalleta.setFont(new Font("Arial", Font.BOLD, 24));

        // Posicionem la pantalleta
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(pantalleta, gbc);

        // Crear el panell de botons
        JPanel panellBotons = new JPanel(new GridLayout(3, 5, 10, 10));
        panellBotons.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Crear els botons
        JButton boto1 = new JButton("1");
        JButton boto2 = new JButton("2");
        JButton boto3 = new JButton("3");
        JButton boto4 = new JButton("4");
        JButton boto5 = new JButton("5");
        JButton boto6 = new JButton("6");
        JButton boto7 = new JButton("7");
        JButton boto8 = new JButton("8");
        JButton boto9 = new JButton("9");
        JButton boto0 = new JButton("0");
        JButton botoComa = new JButton(".");
        JButton botoMes = new JButton("+");
        JButton botoMenys = new JButton("-");
        JButton botoMulti = new JButton("*");
        JButton botoDiv = new JButton("/");
        JButton botoIgual = new JButton("=");
        JButton botoAC = new JButton("AC");

        // Afegir els botons
        panellBotons.add(boto7);
        panellBotons.add(boto8);
        panellBotons.add(boto9);
        panellBotons.add(botoComa);
        panellBotons.add(botoMes);
        panellBotons.add(botoMenys);
        panellBotons.add(boto4);
        panellBotons.add(boto5);
        panellBotons.add(boto6);
        panellBotons.add(boto0);
        panellBotons.add(botoMulti);
        panellBotons.add(botoDiv);
        panellBotons.add(boto1);
        panellBotons.add(boto2);
        panellBotons.add(boto3);
        panellBotons.add(botoIgual);
        panellBotons.add(botoAC);

        // Posicionem el panell de botons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(panellBotons, gbc);

        // Mostrar finestra
        finestra.setVisible(true);
    }
}
