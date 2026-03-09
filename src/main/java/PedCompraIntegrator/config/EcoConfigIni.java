package PedCompraIntegrator.config;

import PedCompraIntegrator.dto.EcoDbConfig;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public class EcoConfigIni {
    private EcoConfigIni() {
    }

    public static EcoDbConfig load(Path caminhoEcoIni) throws IOException {

        String dadosValue = findKeyValue(caminhoEcoIni, "dados");
        if (dadosValue == null || dadosValue.isBlank()) {
            throw new IllegalStateException("Chave 'dados' não encontrada no eco.ini");
        }

        int idx = dadosValue.indexOf(':');
        if (idx <= 0) {
            throw new IllegalStateException("Formato inválido em 'dados'. Esperado HOST:CAMINHO");
        }

        String left = dadosValue.substring(0, idx).trim();
        String right = dadosValue.substring(idx + 1).trim();

        if (right.isEmpty()) {
            throw new IllegalStateException("Formato inválido em 'dados': caminho vazio");
        }

        String path = right;
        String defaultHost = "127.0.0.1";
        int defaultPort = 3050;

        String host = defaultHost;
        int port = defaultPort;

        int parte = left.lastIndexOf("/");
        if (parte > 0) {
            host = left.substring(0, parte);
            String p = left.substring(parte + 1).trim();
            port = parsePortOrDefault(p, port);
            return new EcoDbConfig(host, port, path);
        }

        int twDot = left.lastIndexOf(":");
        if(twDot > 0) {
            host = left.substring(0, twDot);
            String p = left.substring(twDot + 1).trim();
            port = parsePortOrDefault(p, port);
            return new EcoDbConfig(host, port, path);
        }

        if(!left.isEmpty()) {
            host = left;

        }

        return new EcoDbConfig(host, port, path);

    }



    private static String findKeyValue(Path CaminhoEcoIni, String key) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(CaminhoEcoIni, Charset.forName("windows-1252"))) {

            String line;
            String prefix = key.toLowerCase() + "=";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()
                        || line.startsWith(";")
                        || line.startsWith("#")
                        || line.startsWith("[") && line.endsWith("]"))
                    continue;
                if (line.regionMatches(true, 0, prefix, 0, prefix.length())) {
                    //True   =  ignora maiusc/minusc  (case insensitive)
                    // 0 = posição inicial a começar comparação
                    // prefix = dados= (segunda string para comparação)
                    // 0 = posição inicial dentro do prefix
                    //prefix.length()
                    // (quantos caracteres a comparar, nesse caso comparando o prefix)
                    return line.substring(prefix.length()).trim();

                }
            }
        }
        return null;
    }

    private static int parsePortOrDefault(String s, int def) {
        if (s == null) return def;

        String t = s.trim();
        if (t.isEmpty()) return def;
        try {
            int p = Integer.parseInt(t);
            if (p < 1 || p > 65535) return def; //portas validas
            return p;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}

