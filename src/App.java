import javax.swing.UIManager;
import views.TextEditorGUI;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            // Procura e aplica o tema Nimbus entre os temas instalados no Java
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                System.out.println(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new TextEditorGUI();
    }
}
