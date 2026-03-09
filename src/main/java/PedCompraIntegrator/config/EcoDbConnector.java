package PedCompraIntegrator.config;

import PedCompraIntegrator.dto.EcoDbConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class EcoDbConnector {
    private EcoDbConnector(){}

    public static Connection connect (EcoDbConfig cfg, String user, String password) throws SQLException {

        String dbPath = cfg.databasePath().replace('\\', '/');

        String url = "jdbc:firebirdsql://" + cfg.host() + ":" + cfg.porta() + "/" + dbPath;

        return DriverManager.getConnection(url, user, password);

    }
}
