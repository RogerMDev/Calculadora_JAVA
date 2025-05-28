import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppUI {

    private static final JTextField pantalla = new JTextField();
    private static final JLabel operacioLabel = new JLabel();
    private static final JPanel panellBotons = new JPanel();
    private static final DefaultComboBoxModel<String> historialModel = new DefaultComboBoxModel<>();
    private static final JComboBox<String> historialComboBox = new JComboBox<>(historialModel);

    private static final String[] botons = {
        "C", "±", "←", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "Ans", "="
    };

    // Colors del tema arcade retro
    private static final Color ARCADE_BG = new Color(25, 30, 45);
    private static final Color DISPLAY_BG = new Color(15, 20, 35);
    private static final Color CYAN_GLOW = new Color(0, 255, 255);
    private static final Color BLUE_BUTTON = new Color(41, 98, 255);
    private static final Color PINK_BUTTON = new Color(160, 166, 162);
    private static final Color GREEN_BUTTON = new Color(52, 207, 93);
    private static final Color ORANGE_BUTTON = new Color(255, 128, 0);
    private static final Color RED_BUTTON = new Color(255, 69, 58);
    private static final Color DARK_BLUE = new Color(30, 144, 255);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_BLACK = new Color(0, 0, 0);
    private static final Color DARK_GREEN_BUTTON = new Color(45,106,79);

    private static boolean puntUtilitzat = false;
    private static String operacio = "";
    private static double primerNumero = 0;
    private static boolean esperantSegonNumero = false;
    private static boolean bloquejat = false;
    private static double segonNumeroAnterior = 0;
    private static boolean operacioRepetida = false;
    private static String ultimaOperacio = "";
    private static double ultimoResultat = 0; // Variable per guardar l'últim resultat
    private static boolean mostrarNouNombre = false;

    // Botó personalitzat estil arcade amb efectes de brillantor
    static class RoundedButton extends JButton {
        private Color backgroundColor;
        private Color glowColor;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Courier New", Font.BOLD, 20)); // Font pixelada
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setBackgroundColor(Color color) {
            this.backgroundColor = color;
            this.glowColor = color.brighter().brighter();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Efecte de brillantor quan està premut o en hover
            if (getModel().isPressed() || getModel().isRollover()) {
                // Brillantor exterior
                g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 100));
                g2.fillRoundRect(-2, -2, getWidth() + 4, getHeight() + 4, 25, 25);
            }
            
            Color bgColor = getModel().isPressed() ? backgroundColor.darker() : backgroundColor;
            
            g2.setColor(bgColor);
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
            
            // Vora brillant
            g2.setColor(glowColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
            
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(85, 75);
        }
    }

    // Variable global per mantenir referència a la finestra
    private static JFrame ventanaCalculadora;
    
    public static void mostrarFinestra() {
        ventanaCalculadora = new JFrame("CALCULATOR");
        ventanaCalculadora.setSize(400, 650);
        ventanaCalculadora.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaCalculadora.setResizable(true);
        ventanaCalculadora.getContentPane().setBackground(ARCADE_BG);
        ventanaCalculadora.setLayout(new BorderLayout());

        // Panell principal amb padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ARCADE_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panell de la pantalla
        JPanel displayPanel = createDisplayPanel();
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Panell de botons
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Panell d'historial
        JPanel historialPanel = createHistorialPanel();
        mainPanel.add(historialPanel, BorderLayout.SOUTH);

        ventanaCalculadora.add(mainPanel);
        ventanaCalculadora.setLocationRelativeTo(null);
        ventanaCalculadora.setVisible(true);
        ventanaCalculadora.setFocusable(true);
        ventanaCalculadora.requestFocusInWindow();
        
        // Afegir KeyListener per suport de teclat
        ventanaCalculadora.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No necessari
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // No necessari
            }
        });
        
        // Assegurar que la finestra tingui el focus per rebre esdeveniments de teclat
        ventanaCalculadora.setFocusable(true);
        ventanaCalculadora.requestFocus();
    }
    
    private static void handleKeyPress(KeyEvent e) {
        if (bloquejat) return;
        
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        
        // Mapejar tecles a accions
        String action = "";
        
        // Números 0-9
        if (keyChar >= '0' && keyChar <= '9') {
            action = String.valueOf(keyChar);
        }
        // Operadors
        else if (keyChar == '+') {
            action = "+";
        }
        else if (keyChar == '-') {
            action = "-";
        }
        else if (keyChar == '*') {
            action = "×";
        }
        else if (keyChar == '/') {
            action = "÷";
        }
        // Punt decimal (coma o punt)
        else if (keyChar == '.' || keyChar == ',') {
            action = ".";
        }
        // Enter per igual
        else if (keyCode == KeyEvent.VK_ENTER) {
            action = "=";
        }
        // Escape per netejar
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            action = "C";
        }
        // Backspace per esborrar
        else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            action = "←";
        }
        
        // Executar l'acció si s'ha trobat una tecla vàlida
        if (!action.isEmpty()) {
            executeAction(action);
        }
    }
    
    private static void executeAction(String ch) {
        if (bloquejat) return;
        
        // Convertir símbols moderns a operacions
        String operation = ch;
        if (ch.equals("÷")) operation = "/";
        if (ch.equals("×")) operation = "*";
        if (ch.equals("C")) operation = "AC";
        if (ch.equals("←")) operation = "DEL";
        
        switch (operation) {
            case "AC" -> {
                pantalla.setText("0");
                operacioLabel.setText("");
                puntUtilitzat = false;
                operacio = "";
                primerNumero = 0;
                esperantSegonNumero = false;
                // Restaurar el focus a la finestra després de netejar
                if (ventanaCalculadora != null) {
                    SwingUtilities.invokeLater(() -> {
                        ventanaCalculadora.requestFocus();
                        ventanaCalculadora.requestFocusInWindow();
                    });
                }
            }
            case "DEL" -> {
                String texto = pantalla.getText();
                if (!texto.isEmpty() && !texto.equals("0") && !texto.equals("Error")) {
                    if (texto.length() == 1) {
                        pantalla.setText("0");
                        puntUtilitzat = false;
                    } else {
                        String nuevoTexto = texto.substring(0, texto.length() - 1);
                        pantalla.setText(nuevoTexto);
                        // Verificar si després d'esborrar encara hi ha punt decimal
                        puntUtilitzat = nuevoTexto.contains(".");
                    }
                }
            }
            case "." -> {
                if (!puntUtilitzat) {
                    if (esperantSegonNumero) {
                        pantalla.setText("0.");
                        esperantSegonNumero = false;
                    } else if (pantalla.getText().equals("0")) {
                        pantalla.setText("0.");
                    } else {
                        pantalla.setText(pantalla.getText() + ".");
                    }
                    puntUtilitzat = true;
                }
            }
            case "±" -> {
                try {
                    double v = Double.parseDouble(pantalla.getText()) * -1;
                    pantalla.setText(formatNumero(v));
                } catch (NumberFormatException ex) {
                    pantalla.setText("Error");
                }
            }
            case "Ans" -> {
                // Inserir l'últim resultat guardat
                if (esperantSegonNumero) {
                    pantalla.setText("");
                    esperantSegonNumero = false;
                    puntUtilitzat = false;
                } else if (pantalla.getText().equals("0")) {
                    pantalla.setText("");
                }
                
                String resultadoStr = formatNumero(ultimoResultat);
                pantalla.setText(pantalla.getText() + resultadoStr);
                
                // Verificar si el resultat conté punt decimal
                if (resultadoStr.contains(".")) {
                    puntUtilitzat = true;
                }
            }
            case "+", "-", "*", "/" -> {
                try {
                    primerNumero = Double.parseDouble(pantalla.getText());
                    operacio = operation;
                    ultimaOperacio = operation;
                    esperantSegonNumero = true;
                    puntUtilitzat = false;
                    
                    // Mostrar operació amb símbols moderns
                    String displayOp = operation;
                    if (operation.equals("/")) displayOp = "÷";
                    if (operation.equals("*")) displayOp = "×";
                    
                    operacioLabel.setText(formatNumero(primerNumero) + " " + displayOp);
                } catch (NumberFormatException ex) {
                    pantalla.setText("Error");
                }
            }
            case "=" -> {
                try {
                    double segonNumero;
                    String op = operacio.isEmpty() ? ultimaOperacio : operacio;
                    if (op.isEmpty()) return;

                    if (operacio.isEmpty()) {
                        segonNumero = segonNumeroAnterior;
                    } else {
                        segonNumero = Double.parseDouble(pantalla.getText());
                        segonNumeroAnterior = segonNumero;
                        operacioRepetida = true;
                    }

                    double res = switch (op) {
                        case "+" -> primerNumero + segonNumero;
                        case "-" -> primerNumero - segonNumero;
                        case "*" -> primerNumero * segonNumero;
                        case "/" -> (segonNumero == 0) ? Double.NaN : primerNumero / segonNumero;
                        default -> Double.NaN;
                    };

                    if (Double.isNaN(res)) {
                        pantalla.setText("Error");
                        return;
                    }

                    // Mostrar a l'historial amb símbols moderns
                    String displayOp = op;
                    if (op.equals("/")) displayOp = "÷";
                    if (op.equals("*")) displayOp = "×";
                    
                    historialModel.addElement(formatNumero(primerNumero) + " " + displayOp + " " + formatNumero(segonNumero) + " = " + formatNumero(res));
                    pantalla.setText(formatNumero(res));
                    mostrarNouNombre = true;

                    // Guardar el resultat a la variable Ans
                    ultimoResultat = res;

                    primerNumero = res;
                    operacio = "";
                    operacioLabel.setText("");
                    puntUtilitzat = false;
                    esperantSegonNumero = false;

                } catch (NumberFormatException ex) {
                    pantalla.setText("Error");
                }
            }
            default -> {
                // Números
                if (bloquejat) return;

                if (mostrarNouNombre){
                    pantalla.setText("");
                    mostrarNouNombre = false;
                }else if (esperantSegonNumero) {
                    pantalla.setText("");
                    esperantSegonNumero = false;
                    puntUtilitzat = false;
                } else if (pantalla.getText().equals("0")) {
                    pantalla.setText("");
                }
                pantalla.setText(pantalla.getText() + ch);
            }

        }
    }
    
    private static JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setBackground(DISPLAY_BG);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        
        // Crear vora amb efecte de brillantor
        displayPanel.setBorder(new EmptyBorder(25, 20, 25, 20) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Brillantor exterior cyan
                g2.setColor(new Color(0, 255, 255, 50));
                g2.fillRoundRect(x-2, y-2, width+4, height+4, 30, 30);
                
                // Fons principal
                g2.setColor(DISPLAY_BG);
                g2.fillRoundRect(x, y, width, height, 25, 25);
                
                // Vora brillant
                g2.setColor(CYAN_GLOW);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x+1, y+1, width-2, height-2, 25, 25);
                
                g2.dispose();
            }
        });

        // Label d'operació amb estil arcade
        operacioLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        operacioLabel.setForeground(CYAN_GLOW);
        operacioLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        operacioLabel.setPreferredSize(new Dimension(320, 30));
        displayPanel.add(operacioLabel);

        // Espaiat
        displayPanel.add(Box.createVerticalStrut(10));

        // Pantalla principal amb efecte retro
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setFont(new Font("Courier New", Font.BOLD, 52)); // Font pixelada més gran
        pantalla.setText("0");
        pantalla.setBackground(DISPLAY_BG);
        pantalla.setForeground(TEXT_WHITE);
        pantalla.setBorder(null);
        pantalla.setCaretColor(CYAN_GLOW);
        pantalla.setPreferredSize(new Dimension(320, 70));
        
        displayPanel.add(pantalla);

        return displayPanel;
    }

    private static JPanel createButtonPanel() {
        panellBotons.setLayout(new GridLayout(5, 4, 12, 12));
        panellBotons.setBackground(ARCADE_BG);
        panellBotons.setBorder(new EmptyBorder(25, 0, 25, 0));

        afegirBotonsIFunc();
        return panellBotons;
    }

    private static JPanel createHistorialPanel() {
        JPanel historialPanel = new JPanel(new BorderLayout(10, 10));
        historialPanel.setBackground(ARCADE_BG);
        historialPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // ComboBox de l'historial amb funcionalitat interactiva
        historialComboBox.setPreferredSize(new Dimension(240, 35));
        historialComboBox.setFont(new Font("Courier New", Font.BOLD, 12));
        historialComboBox.setBackground(DISPLAY_BG);
        historialComboBox.setForeground(CYAN_GLOW);
        historialComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Afegir listener per fer l'historial interactiu
        historialComboBox.addActionListener(e -> {
            String selectedItem = (String) historialComboBox.getSelectedItem();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                carregarOperacioDelHistorial(selectedItem);
            }
        });
        
        historialPanel.add(historialComboBox, BorderLayout.CENTER);

        // Botó de netejar historial estil arcade
        RoundedButton botoClr = new RoundedButton("CLEAR");
        botoClr.setBackgroundColor(RED_BUTTON);
        botoClr.setForeground(TEXT_WHITE);
        botoClr.setFont(new Font("Courier New", Font.BOLD, 12));
        botoClr.setPreferredSize(new Dimension(80, 35));
        botoClr.addActionListener(e -> {
            historialModel.removeAllElements();
            // Restaurar el focus després de netejar historial
            if (ventanaCalculadora != null) {
                SwingUtilities.invokeLater(() -> {
                    ventanaCalculadora.requestFocus();
                    ventanaCalculadora.requestFocusInWindow();
                });
            }
        });
        historialPanel.add(botoClr, BorderLayout.EAST);

        return historialPanel;
    }

    private static void afegirBotonsIFunc() {
        for (int i = 0; i < botons.length; i++) {
            String ch = botons[i];
            RoundedButton b = new RoundedButton(ch);
            
            // Configurar colors estil arcade segons el tipus de botó
            if ("789456123".contains(ch)) {
                // Números 1-9: Blau vibrant
                b.setBackgroundColor(BLUE_BUTTON);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("0")) {
                // Número 0: Blau més fosc
                b.setBackgroundColor(DARK_BLUE);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("C")) {
                // Clear: Vermell brillant
                b.setBackgroundColor(RED_BUTTON);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("←")) {
                // Esborrar: Taronja fosc
                b.setBackgroundColor(new Color(244, 70, 17));
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("±")) {
                // Plus/Minus: Rosa vibrant
                b.setBackgroundColor(PINK_BUTTON);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("÷") || ch.equals("×") || ch.equals("-") || ch.equals("+")) {
                // Operadors: Taronja brillant
                b.setBackgroundColor(ORANGE_BUTTON);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals("=")) {
                // Igual: Verd neó
                b.setBackgroundColor(GREEN_BUTTON);
                b.setForeground(TEXT_WHITE);
            } else if (ch.equals(".")) {
                // Punt decimal: Groc brillant
                b.setBackgroundColor(new Color(255, 215, 0));
                b.setForeground(TEXT_BLACK);
            } else if (ch.equals("Ans")) {
                // Ans: Verd fosc
                b.setBackgroundColor(DARK_GREEN_BUTTON);
                b.setForeground(TEXT_WHITE);
            }

            // El botó 0 ocupa dues columnes
            if (ch.equals("0")) {
                b.setPreferredSize(new Dimension(174, 75)); // Amplada doble + gap ajustat
            }
            
            panellBotons.add(b);

            // Afegir funcionalitat
            b.addActionListener(e -> {
                executeAction(ch);
                // Restaurar el focus després de qualsevol acció de botó
                if (ventanaCalculadora != null) {
                    SwingUtilities.invokeLater(() -> {
                        ventanaCalculadora.requestFocus();
                        ventanaCalculadora.requestFocusInWindow();
                    });
                }
            });
        }
    }

    private static String formatNumero(double v) {
        if (Double.isInfinite(v) || Double.isNaN(v)) {
            return "Error";
        }
        
        // Formatar números grans amb notació científica si és necessari
        if (Math.abs(v) >= 1e10) {
            return String.format("%.2e", v);
        }
        
        return v == (long) v ? String.format("%d", (long) v) : String.valueOf(v);
    }

    private static void carregarOperacioDelHistorial(String operacionHistorial) {
        try {
            // Gestionar operacions normals
            String[] partes = operacionHistorial.split(" = ");
            if (partes.length == 2) {
                String operacionCompleta = partes[0].trim();
                String resultado = partes[1].trim();
                
                // Buscar l'operador
                String operador = "";
                int posOperador = -1;
                String[] operadores = {"÷", "×", "+", "-"};
                
                for (String op : operadores) {
                    int pos = operacionCompleta.lastIndexOf(" " + op + " ");
                    if (pos > posOperador) {
                        posOperador = pos;
                        operador = op;
                    }
                }
                
                if (posOperador > 0) {
                    // Extreure primer i segon número
                    String primerNum = operacionCompleta.substring(0, posOperador).trim();
                    String segundoNum = operacionCompleta.substring(posOperador + 3).trim();
                    
                    // Convertir operador a símbol intern
                    String opInterno = operador;
                    if (operador.equals("÷")) opInterno = "/";
                    if (operador.equals("×")) opInterno = "*";
                    
                    // Establir estat de la calculadora
                    try {
                        primerNumero = Double.parseDouble(primerNum);
                        pantalla.setText(segundoNum);
                        operacio = opInterno;
                        operacioLabel.setText(primerNum + " " + operador);
                        esperantSegonNumero = false;
                        puntUtilitzat = segundoNum.contains(".");
                        
                        // Permetre modificació immediata
                        bloquejat = false;
                        
                    } catch (NumberFormatException ex) {
                        // Si no es poden parsejar els números, almenys mostrar el resultat
                        pantalla.setText(resultado);
                        resetejarEstat();
                    }
                } else {
                    // Si no es pot parsejar l'operació, mostrar només el resultat
                    pantalla.setText(resultado);
                    resetejarEstat();
                }
            }
        } catch (Exception ex) {
            // En cas de qualsevol error, restablir
            resetejarEstat();
        }
    }
    
    private static void resetejarEstat() {
        operacio = "";
        primerNumero = 0;
        esperantSegonNumero = false;
        puntUtilitzat = false;
        bloquejat = false;
        operacioLabel.setText("");
        // No restablir ultimoResultado per mantenir el valor d'Ans
    }
}