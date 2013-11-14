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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 * The main window. Extends JFrame and implements
 * ActionListener and PropertyChangeListener interfaces.
 * Uses SwingWorker for time consuming work.
 */
public class MainFrame extends JFrame implements ActionListener, PropertyChangeListener{
    
    JPanel alap;
    JButton mehetGomb;
    JScrollPane scroller;
    JTextArea textArea;
    SwingWorker worker;
    
    //===Constructor===
    public MainFrame(String cimsor){
        super(cimsor);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 400, 400, 300);
        
        alap = new JPanel(new BorderLayout());
        setContentPane(alap);
        
        mehetGomb = new JButton("Mehet");
        mehetGomb.addActionListener(this);
        alap.add(mehetGomb, BorderLayout.NORTH);
        
        textArea = new JTextArea("Default text");
        scroller = new JScrollPane(textArea);
        alap.add(scroller, BorderLayout.CENTER);
        
        
    }
    
    //===ActionListener interface methods===
    @Override
    public void actionPerformed(ActionEvent e) {
        if(mehetGomb.equals(e.getSource())){
            textArea.setText("Working...\n");
            mehetGomb.setEnabled(false);
            
            worker = new SwingWorker<String[], String>() {
                
                @Override
                protected String[] doInBackground() throws Exception {
                    //Thread.sleep(3000);
                    PasswordGenerator pg = new PasswordGenerator(100000);
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
                String[] jelszavak = (String[]) worker.get();
                for(String jelszo:jelszavak){
                    textArea.append(jelszo + "\n");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                mehetGomb.setEnabled(true);
            }
        }
    }
    
}
