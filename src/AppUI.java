import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppUI {

    /* ----------  ESTADO Y DATOS  ---------- */
    private static final JTextField pantalleta   = new JTextField();
    private static final JLabel    operacioLabel = new JLabel();
    private static final JPanel    panellBotons  = new JPanel(new GridLayout(3, 7, 15, 15));
    private static final DefaultComboBoxModel<String> historialModel = new DefaultComboBoxModel<>();
    private static final JComboBox<String> historialComboBox = new JComboBox<>(historialModel);

    private static final String[] botons = {
        "7","8","9",".","+","/","√",
        "4","5","6","0","-","*","x!",
        "1","2","3","=","±","AC","DEL"
    };

    private static boolean puntUtilitzat      = false;
    private static String  operacio           = "";
    private static double  primerNumero       = 0;
    private static boolean esperantSegonNumero= false;
    private static boolean bloquejat          = false;
    private static double  segonNumeroAnterior= 0;
    private static boolean operacioRepetida   = false;
    private static String  ultimaOperacio     = "";

    /* ----------  INTERFAZ  ---------- */
    public static void mostrarFinestra() {

        JFrame finestra = new JFrame("H4CK3R C4LC");
        finestra.setSize(560, 540);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10);

        /* --- Pantalla y etiqueta operación --- */
        operacioLabel.setFont(new Font("Courier New", Font.PLAIN, 16));
        operacioLabel.setForeground(Color.GREEN);
        operacioLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        pantalleta.setEditable(false);
        pantalleta.setHorizontalAlignment(JTextField.RIGHT);
        pantalleta.setFont(new Font("Courier New", Font.BOLD, 28));
        pantalleta.setText("0");
        pantalleta.setBackground(Color.BLACK);
        pantalleta.setForeground(Color.GREEN);
        pantalleta.setCaretColor(Color.GREEN);
        pantalleta.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=GridBagConstraints.REMAINDER;
        finestra.add(operacioLabel, gbc);
        gbc.gridy=1;
        finestra.add(pantalleta, gbc);

        /* --- Panel de botones --- */
        panellBotons.setBackground(Color.BLACK);
        panellBotons.setBorder(new EmptyBorder(30,30,30,30));
        afegirBotonsIFunc();
        gbc.gridy=2;
        finestra.add(panellBotons, gbc);

        /* --- Historial --- */
        JPanel historialPanel = new JPanel(new BorderLayout(10,10));
        historialPanel.setBackground(Color.BLACK);
        historialComboBox.setPreferredSize(new Dimension(200,30));
        historialComboBox.setFont(new Font("Courier New", Font.PLAIN, 14));
        historialComboBox.setBackground(Color.BLACK);
        historialComboBox.setForeground(Color.GREEN);
        historialPanel.add(historialComboBox, BorderLayout.CENTER);

        historialComboBox.addActionListener(e -> {
            String sel=(String)historialComboBox.getSelectedItem();
            if(sel==null || !sel.contains("=")) return;
            try{
                String[] partes = sel.split("=");
                String operacion = partes[0].trim();                 // "12 + 3"
                String operador  = "";
                for(String op : new String[]{"+","-","*","/"}){
                    if(operacion.contains(op)){ operador=op; break; }
                }
                if(operador.isEmpty()) return;
                String[] nums = operacion.split("\\Q"+operador+"\\E");
                if(nums.length!=2) return;
                double num1 = Double.parseDouble(nums[0].trim());
                double num2 = Double.parseDouble(nums[1].trim());

                primerNumero = num1;
                operacio     = operador;
                ultimaOperacio = operador;
                esperantSegonNumero = true;
                operacioLabel.setText(formatNumero(num1)+" "+operador);
                pantalleta.setText(formatNumero(num2));
                puntUtilitzat = pantalleta.getText().contains(".");
            }catch(Exception ex){ pantalleta.setText("Error"); }
        });

        JButton botoClr = new JButton("CLR HIST");
        botoClr.setFocusPainted(false);
        botoClr.setBackground(Color.RED);
        botoClr.setForeground(Color.WHITE);
        botoClr.setFont(new Font("Courier New", Font.BOLD, 14));
        botoClr.addActionListener(e->historialModel.removeAllElements());
        historialPanel.add(botoClr, BorderLayout.EAST);

        gbc.gridy=3;
        finestra.add(historialPanel, gbc);

        finestra.getContentPane().setBackground(Color.BLACK);
        finestra.setVisible(true);
    }

    /* ----------  BOTONES Y FUNCIONALIDAD  ---------- */
    private static void afegirBotonsIFunc() {

        panellBotons.setLayout(new GridLayout(3,7,15,15));

        for(String ch : botons){
            JButton b = new JButton(ch);
            b.setFont(new Font("Courier New", Font.BOLD, 20));
            b.setForeground(Color.GREEN);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));

            /* --- Colores por tipo --- */
            if("0123456789".contains(ch)) b.setBackground(new Color(0,64,0));        // números
            else if("+-*/".contains(ch)) b.setBackground(new Color(64,0,64));        // operadores
            else if(ch.equals("="))      { b.setBackground(Color.GREEN); b.setForeground(Color.BLACK); }
            else if(ch.equals("AC")||ch.equals("DEL")) b.setBackground(new Color(128,0,0));
            else if(ch.equals("√")||ch.equals("±")||ch.equals("x!")) b.setBackground(new Color(32,32,32));
            else b.setBackground(new Color(20,20,20));

            b.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
            panellBotons.add(b);

            /* --- Listeners --- */
            if("0123456789".contains(ch)){
                b.addActionListener(e->{
                    if(bloquejat) return;
                    if(esperantSegonNumero){
                        pantalleta.setText("");
                        esperantSegonNumero=false; puntUtilitzat=false;
                    }else if(pantalleta.getText().equals("0")){
                        pantalleta.setText("");
                    }
                    pantalleta.setText(pantalleta.getText()+ch);
                });

            }else if(ch.equals(".")){
                b.addActionListener(e->{
                    if(bloquejat) return;
                    if(!puntUtilitzat){
                        pantalleta.setText(pantalleta.getText()+".");
                        puntUtilitzat=true;
                    }
                });

            }else if(ch.equals("AC")){
                b.addActionListener(e->{
                    if(bloquejat) return;
                    pantalleta.setText("0");
                    operacioLabel.setText("");
                    puntUtilitzat=false; operacio=""; primerNumero=0;
                    esperantSegonNumero=false;
                });

            }else if(ch.equals("DEL")){
                b.addActionListener(e->{
                    if(bloquejat) return;
                    String t=pantalleta.getText();
                    if(!t.isEmpty()&&!t.equals("0")){
                        t=t.substring(0,t.length()-1);
                        pantalleta.setText(t.isEmpty()?"0":t);
                    }
                });

            }else if("+-*/".contains(ch)){
                b.addActionListener(e->{
                    operacioRepetida=false;
                    if(bloquejat) return;
                    try{
                        primerNumero = Double.parseDouble(pantalleta.getText());
                        operacio = ch; ultimaOperacio=ch;
                        esperantSegonNumero=true;
                        operacioLabel.setText(formatNumero(primerNumero)+" "+ch);
                    }catch(NumberFormatException ex){ pantalleta.setText("Error"); }
                });

            }else if(ch.equals("=")){
                b.addActionListener(e->{
                    if(bloquejat) return;
                    try{
                        double segonNumero;
                        String op = operacio.isEmpty()? ultimaOperacio : operacio;
                        if(op.isEmpty()) return;

                        if(operacio.isEmpty()){
                            segonNumero=segonNumeroAnterior;
                        }else{
                            segonNumero = Double.parseDouble(pantalleta.getText());
                            segonNumeroAnterior = segonNumero;
                            operacioRepetida=true;
                        }

                        double res = switch(op){
                            case "+": yield primerNumero + segonNumero;
                            case "-": yield primerNumero - segonNumero;
                            case "*": yield primerNumero * segonNumero;
                            case "/": yield (segonNumero==0)?Double.NaN:primerNumero/segonNumero;
                            default : yield Double.NaN;
                        };

                        if(Double.isNaN(res)){ pantalleta.setText("Error"); return; }

                        historialModel.addElement(formatNumero(primerNumero)+" "+op+" "+formatNumero(segonNumero)+" = "+formatNumero(res));
                        mostrarResultatAmbDial(formatNumero(res));

                        primerNumero = res;
                        operacio=""; operacioLabel.setText("");
                        puntUtilitzat=false; esperantSegonNumero=false;

                    }catch(NumberFormatException ex){ pantalleta.setText("Error"); }
                });

            }else if(ch.equals("±")){
                b.addActionListener(e->{
                    try{
                        double v=Double.parseDouble(pantalleta.getText())*-1;
                        pantalleta.setText(formatNumero(v));
                    }catch(NumberFormatException ex){ pantalleta.setText("Error"); }
                });

            }else if(ch.equals("√")){
                b.addActionListener(e->{
                    try{
                        double v=Double.parseDouble(pantalleta.getText());
                        pantalleta.setText(v<0?"Error":formatNumero(Math.sqrt(v)));
                    }catch(NumberFormatException ex){ pantalleta.setText("Error"); }
                });

            }else if(ch.equals("x!")){
                b.addActionListener(e->{
                    try{
                        double v=Double.parseDouble(pantalleta.getText());
                        if(v<0||v!=(int)v){ pantalleta.setText("Error"); return; }
                        long r=1; for(int i=2;i<=v;i++) r*=i;
                        pantalleta.setText(formatNumero(r));
                    }catch(NumberFormatException ex){ pantalleta.setText("Error"); }
                });
            }
        }
    }

    /* ----------  EFECTO DIAL  ---------- */
    private static void mostrarResultatAmbDial(String res){
        bloquejat=true;
        char[] chars=res.toCharArray();
        char[] disp=new char[chars.length];
        java.util.Arrays.fill(disp,'0');
        pantalleta.setText(new String(disp));

        Timer t=new Timer(30,null);
        int[] ticks=new int[chars.length];
        int[] max = new int[chars.length];
        for(int i=0;i<chars.length;i++) max[i]=10+(int)(Math.random()*10);

        t.addActionListener(e->{
            boolean fin=true;
            for(int i=0;i<chars.length;i++){
                if(ticks[i]<max[i]){
                    disp[i]=(char)('0'+(int)(Math.random()*10));
                    ticks[i]++; fin=false;
                }else disp[i]=chars[i];
            }
            pantalleta.setText(new String(disp));
            if(fin){ t.stop(); bloquejat=false; }
        });
        t.start();
    }

    /* ----------  FORMATEO  ---------- */
    private static String formatNumero(double v){
        return v==(long)v ? String.format("%d",(long)v) : String.valueOf(v);
    }
}
