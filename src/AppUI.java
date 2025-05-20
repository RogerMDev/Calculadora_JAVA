import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppUI {

    private static final JTextField pantalleta = new JTextField();
    private static final JLabel operacioLabel = new JLabel("");
    private static final JPanel panellBotons = new JPanel(new GridLayout(3, 5, 15, 15));
    public static final String[] botons = {"7", "8", "9", ".", "+", "-", "4", "5", "6", "0", "*", "/", "1", "2", "3", "=", "AC", "DEL"};
    private static boolean puntUtilitzat = false;
    private static String operacio = "";
    private static double primerNumero = 0;
    private static boolean esperantSegonNumero = false;

    public static void mostrarFinestra() {
        JFrame finestra = new JFrame("Calculadora per a la Marina");
        finestra.setSize(520, 450);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        operacioLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        operacioLabel.setForeground(new Color(150, 255, 255)); // Azul clarito neón
        operacioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        pantalleta.setEditable(false);
        pantalleta.setHorizontalAlignment(JTextField.RIGHT);
        pantalleta.setFont(new Font("Consolas", Font.BOLD, 28));
        pantalleta.setText("0.0");
        pantalleta.setBackground(new Color(36, 36, 48)); // Estilo LCD oscuro
        pantalleta.setForeground(new Color(0, 255, 255)); // Cyan neón
        pantalleta.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(pantalleta, gbc);

        panellBotons.setBackground(new Color(18, 18, 30)); // Fondo del panel
        panellBotons.setBorder(new EmptyBorder(30, 30, 30, 30));

        afegirBotonsIFunc();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(panellBotons, gbc);

        finestra.getContentPane().setBackground(new Color(18, 18, 30)); // Fondo general
        finestra.setVisible(true);
    }

    public static void afegirBotonsIFunc() {
        for (String chLlegit : botons) {
            JButton boto = new JButton(chLlegit);
            boto.setFocusPainted(false);
            boto.setForeground(Color.WHITE);
            boto.setFont(new Font("Consolas", Font.BOLD, 20));
            boto.setOpaque(true);
            boto.setBorderPainted(false);
            boto.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boto.setPreferredSize(new Dimension(80, 50));

            // Estilo de botón según tipo
            if ("0123456789".contains(chLlegit)) {
                boto.setBackground(new Color(0, 191, 255)); // Azul neón claro
            } else if ("+-*/".contains(chLlegit)) {
                boto.setBackground(new Color(255, 105, 180)); // Rosa neón
            } else if (chLlegit.equals("=")) {
                boto.setBackground(new Color(255, 255, 0)); // Amarillo brillante
                boto.setForeground(Color.BLACK);
            } else if (chLlegit.equals("AC") || chLlegit.equals("DEL")) {
                boto.setBackground(new Color(255, 69, 0)); // Naranja fuerte
            } else {
                boto.setBackground(new Color(186, 85, 211)); // Lila neón
            }

            boto.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));

            // Hover effect
            boto.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boto.setBackground(boto.getBackground().brighter());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if ("0123456789".contains(chLlegit)) {
                        boto.setBackground(new Color(0, 191, 255));
                    } else if ("+-*/".contains(chLlegit)) {
                        boto.setBackground(new Color(255, 105, 180));
                    } else if (chLlegit.equals("=")) {
                        boto.setBackground(new Color(255, 255, 0));
                    } else if (chLlegit.equals("AC") || chLlegit.equals("DEL")) {
                        boto.setBackground(new Color(255, 69, 0));
                    } else {
                        boto.setBackground(new Color(186, 85, 211));
                    }
                }
            });

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
                    if (pantalleta.getText().equals("0.0")) {
                        return;
                    }
                    else if (!puntUtilitzat) {
                        pantalleta.setText(pantalleta.getText() + ".");
                        puntUtilitzat = true;
                    }
                });

            } else if (chLlegit.equals("AC")) {
                boto.addActionListener(e -> {
                    pantalleta.setText("0.0");
                    operacioLabel.setText("");
                    puntUtilitzat = false;
                    operacio = "";
                    primerNumero = 0;
                    esperantSegonNumero = false;
                });

            } else if (chLlegit.equals("DEL")) {
                boto.addActionListener(e -> {
                    String textActual = pantalleta.getText();
                    if (!textActual.isEmpty() && !textActual.equals("0.0")) {
                        textActual = textActual.substring(0, textActual.length() - 1);
                        if (textActual.isEmpty()) {
                            pantalleta.setText("0.0");
                        } else {
                            pantalleta.setText(textActual);
                        }
                    }
                });

            } else if ("+-*/".contains(chLlegit)) {
                boto.addActionListener(e -> {
                    try {
                        primerNumero = Double.parseDouble(pantalleta.getText());
                        operacio = chLlegit;
                        esperantSegonNumero = true;
                        operacioLabel.setText(primerNumero + " " + chLlegit);
                    } catch (NumberFormatException ex) {
                        pantalleta.setText("Error");
                    }
                });

            } else if (chLlegit.equals("=")) {
                boto.addActionListener(e -> {
                    try {
                        double segonNumero = Double.parseDouble(pantalleta.getText());
                        
                        // Si no hi ha cap operació, cuan es prem el signe igual es manté el primer nombre
                        if (operacio.isEmpty()) {
                            pantalleta.setText(String.valueOf(segonNumero));
                            return;
                        }
                        double resultat = 0;

                        if (operacio.equals("+")) {
                            resultat = primerNumero + segonNumero;
                        } else if (operacio.equals("-")) {
                            resultat = primerNumero - segonNumero;
                        } else if (operacio.equals("*")) {
                            resultat = primerNumero * segonNumero;
                        } else if (operacio.equals("/")) {
                            if (segonNumero == 0) {
                                pantalleta.setText("Error: Div per 0");
                                return;
                            }
                            resultat = primerNumero / segonNumero;
                        } else {
                            pantalleta.setText("Error");
                            return;
                        }

                        pantalleta.setText(String.valueOf(resultat));
                        operacioLabel.setText("");
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
