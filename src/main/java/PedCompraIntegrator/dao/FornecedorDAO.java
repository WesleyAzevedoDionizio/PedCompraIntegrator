package PedCompraIntegrator.dao;

import PedCompraIntegrator.dto.Fornecedor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public List<Fornecedor> ListaFornecedores (Connection con, String trecho) throws SQLException {

        String sql = """
                SELECT NOME,
                CODIGO, EMAIL
                FROM TPAGFORNECEDOR
                WHERE NOME CONTAINING ?
                ORDER BY CODIGO """;

        List<Fornecedor> list = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, trecho);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String nome = rs.getString("NOME");
                String email = rs.getString("EMAIL");
                Integer codigo = rs.getInt("CODIGO");

                list.add(new Fornecedor(email,nome, codigo));
            }


        }
        return list;
    }



}
