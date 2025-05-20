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
    private static boolean bloquejat = false;

    public static void mostrarFinestra() {
        JFrame finestra = new JFrame("Calculadora Arcade");
        finestra.setSize(520, 450);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        operacioLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        operacioLabel.setForeground(new Color(255, 0, 255)); // Rosa neón
        operacioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        pantalleta.setEditable(false);
        pantalleta.setHorizontalAlignment(JTextField.RIGHT);
        pantalleta.setFont(new Font("Consolas", Font.BOLD, 28));
        pantalleta.setText("0.0");
        pantalleta.setBackground(new Color(20, 20, 20));
        pantalleta.setForeground(new Color(0, 255, 0));
        pantalleta.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 3));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        finestra.add(operacioLabel, gbc);

        gbc.gridy = 1;
        finestra.add(pantalleta, gbc);

        panellBotons.setBackground(new Color(10, 10, 30));
        panellBotons.setBorder(new EmptyBorder(30, 30, 30, 30));

        afegirBotonsIFunc();

        gbc.gridy = 2;
        finestra.add(panellBotons, gbc);

        finestra.getContentPane().setBackground(new Color(10, 10, 30));
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

            if ("0123456789".contains(chLlegit)) {
                boto.setBackground(new Color(0, 255, 255));
            } else if ("+-*/".contains(chLlegit)) {
                boto.setBackground(new Color(255, 0, 255));
            } else if (chLlegit.equals("=")) {
                boto.setBackground(Color.YELLOW);
                boto.setForeground(Color.BLACK);
            } else if (chLlegit.equals("AC") || chLlegit.equals("DEL")) {
                boto.setBackground(new Color(255, 69, 0));
            } else {
                boto.setBackground(new Color(186, 85, 211));
            }

            boto.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
            panellBotons.add(boto);

            if ("0123456789".contains(chLlegit)) {
                boto.addActionListener(e -> {
                    if (bloquejat) return;
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
                    if (bloquejat) return;
                    if (pantalleta.getText().equals("0.0")) return;
                    if (!puntUtilitzat) {
                        pantalleta.setText(pantalleta.getText() + ".");
                        puntUtilitzat = true;
                    }
                });

            } else if (chLlegit.equals("AC")) {
                boto.addActionListener(e -> {
                    if (bloquejat) return;
                    pantalleta.setText("0.0");
                    operacioLabel.setText("");
                    puntUtilitzat = false;
                    operacio = "";
                    primerNumero = 0;
                    esperantSegonNumero = false;
                });

            } else if (chLlegit.equals("DEL")) {
                boto.addActionListener(e -> {
                    if (bloquejat) return;
                    String textActual = pantalleta.getText();
                    if (!textActual.isEmpty() && !textActual.equals("0.0")) {
                        textActual = textActual.substring(0, textActual.length() - 1);
                        pantalleta.setText(textActual.isEmpty() ? "0.0" : textActual);
                    }
                });

            } else if ("+-*/".contains(chLlegit)) {
                boto.addActionListener(e -> {
                    if (bloquejat) return;
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
                    if (bloquejat) return;
                    try {
                        double segonNumero = Double.parseDouble(pantalleta.getText());

                        if (operacio.isEmpty()) {
                            pantalleta.setText(String.valueOf(segonNumero));
                            return;
                        }

                        double resultat = switch (operacio) {
                            case "+" -> primerNumero + segonNumero;
                            case "-" -> primerNumero - segonNumero;
                            case "*" -> primerNumero * segonNumero;
                            case "/" -> {
                                if (segonNumero == 0) {
                                    pantalleta.setText("Error: Div per 0");
                                    yield 0;
                                }
                                yield primerNumero / segonNumero;
                            }
                            default -> {
                                pantalleta.setText("Error");
                                yield 0;
                            }
                        };

                        mostrarResultatAmbDial(String.valueOf(resultat));
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

    // 🎰 EFECTO DIAL AL MOSTRAR RESULTADO
private static void mostrarResultatAmbDial(String resultatFinal) {
    bloquejat = true;

    // Creamos un array para mostrar el progreso de cada dígito
    char[] resultatChars = resultatFinal.toCharArray();
    char[] display = new char[resultatChars.length];

    for (int i = 0; i < display.length; i++) {
        display[i] = '0'; // empieza mostrando ceros
    }

    pantalleta.setText(String.valueOf(display));

    Timer timer = new Timer(30, null);
    int[] ticksPorDigito = new int[resultatChars.length]; // cuántos ticks lleva cada dígito
    int[] ticksMaximos = new int[resultatChars.length];   // cuántos ticks necesita cada uno

    // Configura una cantidad aleatoria de "vueltas" por dígito
    for (int i = 0; i < resultatChars.length; i++) {
        ticksMaximos[i] = 10 + (int)(Math.random() * 10); // cada dígito tarda diferente
    }

    timer.addActionListener(e -> {
        boolean terminado = true;

        for (int i = 0; i < resultatChars.length; i++) {
            if (ticksPorDigito[i] < ticksMaximos[i]) {
                display[i] = (char) ('0' + (int)(Math.random() * 10));
                ticksPorDigito[i]++;
                terminado = false;
            } else {
                display[i] = resultatChars[i];
            }
        }

        pantalleta.setText(String.valueOf(display));

        if (terminado) {
            timer.stop();
            bloquejat = false;
        }
    });

    timer.start();
    }
}
