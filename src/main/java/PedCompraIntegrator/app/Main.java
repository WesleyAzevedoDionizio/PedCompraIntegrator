package PedCompraIntegrator.app;

import PedCompraIntegrator.config.EcoConfigIni;
import PedCompraIntegrator.config.EcoDbConnector;
import PedCompraIntegrator.dto.EcoDbConfig;
import com.formdev.flatlaf.FlatDarkLaf;
import PedCompraIntegrator.ui.PedCompraMainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.IOException;


import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Properties;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Path configPath = Path.of("config.ini");

        Properties props = new Properties();

        // System.out.println("configPath = " + configPath);
        // System.out.println("absoluto = " + configPath.toAbsolutePath());
        // System.out.println("existe = " + Files.exists(configPath));

        try (InputStream in = Files.newInputStream(configPath)){
            props.load(in);
        } catch (Exception e){
            System.err.println("Erro ao ler config.ini");
            e.printStackTrace();
        }

        String caminhoEcoIni = props.getProperty("eco_ini_path");
        Path iniPath = Path.of(caminhoEcoIni);


        try {
            // Define o visual do programa com FlatLaf.
            // "Look and Feel" = aparência visual dos componentes Swing.
           UIManager.setLookAndFeel(new FlatDarkLaf());
        }catch (Exception e) {
            System.err.println("Não foi possível aplicar o FlatLaf.");
            e.printStackTrace();
        }

        try {
            EcoDbConfig config = EcoConfigIni.load(iniPath);

            Connection connection = EcoDbConnector.connect(config, "SYSDBA", "masterkey");
            System.out.println("Conectado ao firebird");

            // SwingUtilities.invokeLater:
            // garante que a interface gráfica seja criada na thread correta do Swing.
            // isso é uma boa prática importante.

            SwingUtilities.invokeLater(() -> {
                PedCompraMainFrame frame = new PedCompraMainFrame(connection);
                frame.setVisible(true);
            });
        }catch (Exception e){
             e.printStackTrace();
        }
    }

}
