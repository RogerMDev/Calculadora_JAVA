import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppUI {

    private static final JTextField pantalleta = new JTextField();
    private static final JPanel panellBotons = new JPanel(new GridLayout(3, 5, 10, 10));
    public static final String[] botons = {"7", "8", "9", ".", "+", "-", "4", "5", "6", "0", "*", "/", "1", "2", "3", "=", "AC" };
    private static boolean puntUtilitzat = false;
    private static String operacio = "";
    private static double primerNumero = 0;
    private static boolean esperantSegonNumero = false;

    public static void mostrarFinestra() {
        JFrame finestra = new JFrame("Calculadora per a la Marina");
        finestra.setSize(500, 400);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        pantalleta.setEditable(false);
        pantalleta.setHorizontalAlignment(JTextField.RIGHT);
        pantalleta.setFont(new Font("Arial", Font.BOLD, 24));
        pantalleta.setText("0.0");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(pantalleta, gbc);

        panellBotons.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Llamamos al método que añade los botones y funcionalidad
        afegirBotonsIFunc();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(panellBotons, gbc);

        finestra.setVisible(true);
    }

    public static void afegirBotonsIFunc() {
        for (String chLlegit : botons) {
            JButton boto = new JButton(chLlegit);
            panellBotons.add(boto);

            if ("0123456789".contains(chLlegit)) {
                boto.addActionListener(e -> {
                    if (esperantSegonNumero) {
                        pantalleta.setText("");
                        esperantSegonNumero = false;
                        puntUtilitzat = false;
                    } else if (pantalleta.getText().equals("0.0")) {
                        pantalleta.setText("");
                    }
                    pantalleta.setText(pantalleta.getText() + chLlegit);
                });

            } else if (chLlegit.equals(".")) {
                boto.addActionListener(e -> {
                    if (!puntUtilitzat) {
                        pantalleta.setText(pantalleta.getText() + ".");
                        puntUtilitzat = true;
                    }
                });

            } else if (chLlegit.equals("AC")) {
                boto.addActionListener(e -> {
                    pantalleta.setText("0.0");
                    puntUtilitzat = false;
                    operacio = "";
                    primerNumero = 0;
                    esperantSegonNumero = false;
                });

            } else if ("+-*/".contains(chLlegit)) {
                boto.addActionListener(e -> {
                    try {
                        primerNumero = Double.parseDouble(pantalleta.getText());
                        operacio = chLlegit;
                        esperantSegonNumero = true;
                    } catch (NumberFormatException ex) {
                        pantalleta.setText("Error");
                    }
                });

            } else if (chLlegit.equals("=")) {
                boto.addActionListener(e -> {
                    try {
                        double segonNumero = Double.parseDouble(pantalleta.getText());
                        double resultat = 0;

                        // Lógica sin switch
                        if (operacio.equals("+")) {
                            resultat = primerNumero + segonNumero;
                        } else if (operacio.equals("-")) {
                            resultat = primerNumero - segonNumero;
                        } else if (operacio.equals("*")) {
                            resultat = primerNumero * segonNumero;
                        } else if (operacio.equals("/")) {
                            if (segonNumero == 0) {
                                pantalleta.setText("Div per 0");
                                return;
                            }
                            resultat = primerNumero / segonNumero;
                        } else {
                            pantalleta.setText("Error");
                            return;
                        }

                        pantalleta.setText(String.valueOf(resultat));
                        operacio = "";
                        puntUtilitzat = false;
                        esperantSegonNumero = false;

                    } catch (NumberFormatException ex) {
                        pantalleta.setText("Error");
                    }
                });
            }
        }
    }
}
