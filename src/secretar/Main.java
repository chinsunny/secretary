package secretar;

import javax.swing.UIManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import secretar.dao.DelegationDao;
import secretar.ui.MainFrame;

public class Main {

    private static final String[] LOCATIONS = {"spring-context.xml"};

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            ApplicationContext context = new ClassPathXmlApplicationContext(LOCATIONS);
            final MainFrame mainFrame = (MainFrame) context.getBean("mainFrame");
            DelegationDao delegationDao = (DelegationDao) context.getBean("delegationDao");
            mainFrame.setDelegationDao(delegationDao);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    mainFrame.init();
                    mainFrame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
