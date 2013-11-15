package pwgen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

/**
 * The main window. Extends JFrame and implements
 * ActionListener and PropertyChangeListener interfaces.
 * Uses SwingWorker for time consuming work.
 */
public class MainFrame extends JFrame implements ActionListener, PropertyChangeListener{
    
    JPanel base;
    JMenuBar menuBar;
    
    JLabel countLabel;
    JSpinner countSpinner;
    JLabel lengthLabel;
    JSpinner lengthSpinner;
    JButton goButton;
    
    JScrollPane scroller;
    JTextArea textArea;
    SwingWorker worker;
    
    
    //===Constructor===
    public MainFrame(String title){
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 400, 400, 300);
        
        base = new JPanel(new BorderLayout());
        setContentPane(base);
        
        menuBar = new JMenuBar(); 
        
        countLabel = new JLabel("Count:");
        menuBar.add(countLabel);
        countSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000000, 1));
        menuBar.add(countSpinner);
        
        lengthLabel = new JLabel("Length:");
        menuBar.add(lengthLabel);
        lengthSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 32, 1));
        menuBar.add(lengthSpinner);
        
        goButton = new JButton("GO");
        goButton.addActionListener(this);
        menuBar.add(goButton);
        
        
        base.add(menuBar, BorderLayout.NORTH);
        
        textArea = new JTextArea("Click GO to generate passwords");
        scroller = new JScrollPane(textArea);
        base.add(scroller, BorderLayout.CENTER);
        
        
    }
    
    //===ActionListener interface methods===
    @Override
    public void actionPerformed(ActionEvent e) {
        if(goButton.equals(e.getSource())){
            textArea.setText("Working...\n");
            goButton.setEnabled(false);
            
            final int c = (int)countSpinner.getValue();
            final int l = (int)lengthSpinner.getValue();
            
            worker = new SwingWorker<String[], String>() {
                
                @Override
                protected String[] doInBackground() throws Exception {
                    PasswordGenerator pg = new PasswordGenerator(c, l);
                    return pg.getPasswords();
                }
            };
            worker.addPropertyChangeListener(this);
            worker.execute();
        }
    }

    //===PropertyChangeListener interface methods===
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("state".equals(evt.getPropertyName()) && evt.getNewValue() == SwingWorker.StateValue.DONE){
            textArea.setText("");
            try {
                String[] passwords = (String[]) worker.get();
                StringBuilder builder = new StringBuilder();
                for(String password:passwords){
                    builder.append(password + "\n");
                }
                /* The JTextArea's append is much time consuming than
                 * setText, its blocking the gui. That's why we use a 
                 * StringBuilder.
                 */
                textArea.setText(builder.toString());
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                goButton.setEnabled(true);
            }
        }
    }
    
}
