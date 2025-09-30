import java.text.DecimalFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class calculadorav2 {
    public static void main(String[] args){
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (
            Exception ignored
        ) {}
        SwingUtilities.invokeLater(() ->{
            CalculatorModel model = new CalculatorModel();
            CalculatorView view = new CalculatorView();
            new CalculatorController(model,view);
            view.setVisible(true);
        });
    }
    static class CalculatorModel{
        private double accumulator = 0.0;
        private String pendingOp = null;
        private boolean overwrite = true;
        private boolean error = false;

        private final DecimalFormat fmt = new DecimalFormat("############0.########");

        public String getFormatted(double v){
            if(Double.isNaN(v) || Double.isInfinite(v)) return "Error";
            return fmt.format(v);
        }
        
        public void clearAll(){
            accumulator = 0.0; 
            pendingOp = null;
            overwrite = true;
            error = false;
        }

        public boolean isError(){
            return error;
        }

        public String applyUnary(String op, String currentText){
        double x = parse(currentText);
        if (error) return "Error";
        double r;
        switch (op){
            case "+/-":
                r = -x;
                break;
            case "%":
                r = x / 100.0;
                break;
            case "√":
                if (x < 0) { error = true; return "Error"; }
                r = Math.sqrt(x);
                break;
            default:
                r = x;
        }
        overwrite = true;
        return getFormatted(r);
        }

        public String pressOperator(String op, String currentText){
            double x = parse(currentText);
            if(error){
                return "Error";
            }
            if(pendingOp == null){
                accumulator = x;
            } else{
                accumulator = compute(accumulator, x, pendingOp);
                if(Double.isNaN(accumulator) || Double.isInfinite(accumulator)) {
                    error = true;
                    return "Error";
                }
            }
            pendingOp = op;
            overwrite = true;
            return getFormatted(accumulator);
        }

        public String pressEquals(String currentText){
            if(error){
                return "Error";
            }
            double x = parse(currentText);
            if(pendingOp != null){
                accumulator = compute(accumulator, x, pendingOp);
                pendingOp = null;
            }else{
                accumulator = x; 
            }
            if (Double.isNaN(accumulator) || Double.isInfinite(accumulator)){
                error = true;
                return "Error";
            }
            overwrite = true;
            return getFormatted(accumulator);
        }

        private double parse(String t){
            if(t == null || t.isBlank() || t.equals(".")) return 0.0;
            try{
                return Double.parseDouble(t.replace(',','.')); 
            } catch(Exception e){
                error = true; 
                return Double.NaN;
            }
        }

        private double compute(double a, double b, String op){
            return switch (op) {
                case "+" -> a+b;
                case "-" -> a-b;
                case "*" -> a*b;
                case "/" -> b == 0 ? Double.NaN : a / b;
                default -> b;
            };
        }

        public boolean shouldOverwrite(){
            return overwrite;
        }
        public void setOverwrite(boolean v){
            overwrite = v;
        }
    }
    static class CalculatorView extends JFrame { 
        JTextField display;
        JButton btnC, btnBack, btnPercent, btnDiv;
        JButton btn7, btn8, btn9, btnMul;
        JButton btn4, btn5, btn6, btnSub;
        JButton btn3, btn2, btn1, btnAdd;
        JButton btnSign, btn0, btnDot, btnEq;
        JButton btnSqrt;

        public CalculatorView(){
            setTitle("Calculadora");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(320, 440);
            setLocationRelativeTo(null);
            setResizable(false);

            display = new JTextField("0");
            display.setEditable(false);
            display.setHorizontalAlignment(JTextField.RIGHT);
            display.setFont(display.getFont().deriveFont(Font.BOLD, 20f));
            display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel keys = new JPanel(new GridLayout(6, 4, 8, 8));
            keys.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            btnSqrt = makeButton("√");
            btnC = makeButton("C");
            btnBack = makeButton("←");
            btnDiv = makeButton("/");

            btn7 = makeButton("7"); btn8 = makeButton("8"); btn9 = makeButton("9"); btnMul = makeButton("*");
            btn4 = makeButton("4"); btn5 = makeButton("5"); btn6 = makeButton("6"); btnSub = makeButton("-");
            btn3 = makeButton("3"); btn2 = makeButton("2"); btn1 = makeButton("1"); btnAdd = makeButton("+");
            btnSign = makeButton("+/-"); btn0 = makeButton("0"); btnDot = makeButton("."); btnEq = makeButton("=");
            btnPercent = makeButton("%");

            keys.add(btnSqrt); keys.add(btnC); keys.add(btnBack); keys.add(btnDiv);
            keys.add(btn7); keys.add(btn8); keys.add(btn9); keys.add(btnMul);
            keys.add(btn4); keys.add(btn5); keys.add(btn6); keys.add(btnSub);
            keys.add(btn1); keys.add(btn2); keys.add(btn3); keys.add(btnAdd);
            keys.add(btnSign); keys.add(btn0); keys.add(btnDot); keys.add(btnEq);
            keys.add(btnPercent); 
            keys.add(new JLabel()); keys.add(new JLabel()); keys.add(new JLabel());


            setLayout(new BorderLayout(0, 8));
            add(display, BorderLayout.NORTH);
            add(keys, BorderLayout.CENTER);
        }
        public JButton makeButton(String txt){
            JButton b = new JButton(txt);
            b.setFont(b.getFont().deriveFont(Font.PLAIN, 20F));
            b.setFocusPainted(false);
            b.setMargin(new Insets(8,8,8,8));
            return b;
        }
    }

    static class CalculatorController implements ActionListener, KeyListener {
        private final CalculatorModel model;
        private final CalculatorView view;

        public CalculatorController(CalculatorModel m, CalculatorView v){
            this.model = m; 
            this.view = v;
            for (Component c : ((JPanel)v.getContentPane().getComponent(1)).getComponents()) {
                if (c instanceof JButton b) b.addActionListener(this);
            }
            v.addKeyListener(this);
            v.setFocusable(true);
            v.display.addKeyListener(this);
            v.display.setFocusable(true);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            String cmd = ((JButton)e.getSource()).getText();
            handleCommand(cmd);
        }

        private void handleCommand(String cmd){
            if(model.isError()){
                if(!cmd.equals("C")) { 
                    view.display.setText("Error"); 
                    return;
                }
            }
            switch (cmd){
                case "0","1","2","3","4","5","6","7","8","9" -> appendDigit(cmd);
                case "." -> appendDot();
                case "C" -> {model.clearAll(); view.display.setText("0");}
                case "←" -> backspace();
                case "+/-","%","√" -> view.display.setText(model.applyUnary(cmd, view.display.getText()));
                case "+","-","*","/" -> view.display.setText(model.pressOperator(cmd, view.display.getText()));
                case "=" -> view.display.setText(model.pressEquals(view.display.getText()));
            }
        }
        private void appendDigit(String d){
            String t = view.display.getText();
            if (model.shouldOverwrite() || t.equals("0")) {
                t=d;
                model.setOverwrite(false);
            } else{
                t=t+d;
            }
            view.display.setText(t);
        }
        private void appendDot(){
            String t = view.display.getText();
            if (model.shouldOverwrite()) { t = "0."; model.setOverwrite(false); }
            else if (!t.contains(".")) t = t + ".";
            view.display.setText(t);
        }

        private void backspace(){
            String t = view.display.getText();
            if (model.shouldOverwrite()) return;
            if (t.length() <= 1) { view.display.setText("0"); model.setOverwrite(true); }
            else view.display.setText(t.substring(0, t.length()-1));
        }

        // ---- Keyboard support ----
        @Override public void keyTyped(KeyEvent e){}
        @Override public void keyReleased(KeyEvent e){}
        @Override public void keyPressed(KeyEvent e){
        char ch = e.getKeyChar();
        int code = e.getKeyCode();
        if (Character.isDigit(ch)) { appendDigit(String.valueOf(ch)); return; }
        switch (ch){
            case '.' -> appendDot();
            case '+' -> handleCommand("+");
            case '-' -> handleCommand("-");
            case '*' -> handleCommand("*");
            case '/' -> handleCommand("/");
            case '%' -> handleCommand("%");
            case '=' -> handleCommand("=");
        }
        switch (code){
            case KeyEvent.VK_ENTER -> handleCommand("=");
            case KeyEvent.VK_ESCAPE -> handleCommand("C");
            case KeyEvent.VK_BACK_SPACE -> handleCommand("←");
            case KeyEvent.VK_R -> handleCommand("√");
            case KeyEvent.VK_P -> handleCommand("+"); 
        }   
    }
    }
}
