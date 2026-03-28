package PedCompraIntegrator.dao;

import PedCompraIntegrator.dto.Empresa;
import PedCompraIntegrator.dto.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

     public List<Empresa> listaEmpresas (Connection con) throws SQLException{

         String sql = """
                SELECT CODIGO, NOMEFANTASIA
                FROM TGEREMPRESA
                ORDER BY CODIGO""";

         List<Empresa> list = new ArrayList<>();

         try(PreparedStatement ps = con.prepareStatement(sql)){

             ResultSet rs = ps.executeQuery();

             while (rs.next()){
                 String codigo = rs.getString("CODIGO");
                 String nome = rs.getString("NOMEFANTASIA");

                 list.add(new Empresa(codigo, nome));
             }


         }
         return list;
     }

}
