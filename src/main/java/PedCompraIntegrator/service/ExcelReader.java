package PedCompraIntegrator.service;

import PedCompraIntegrator.dto.ProdutoDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ExcelReader {

       public static List<ProdutoDTO> produto (Path caminho) {
           List<ProdutoDTO> dados = new ArrayList<>();
           try (InputStream file = Files.newInputStream(caminho);
                Workbook planilha =WorkbookFactory.create(file)){
               Sheet aba = planilha.getSheetAt(0);
               for (Row row : aba){
                   Cell cellqtd = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                   Cell cellref = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                   String qtd = lertexto(cellqtd);
                   String ref = lertexto(cellref);
                   if (qtd == null || qtd.isBlank()) continue;
                   if (ref == null || ref.isBlank()) continue;
                   dados.add(new ProdutoDTO(qtd, ref));
               }
           }catch (IOException e){
               System.out.println("Erro na leitura da planilha");
           }

           System.out.println(dados);
           return dados;
       }
       private static String lertexto(Cell cell){
           if(cell == null)return null;
           return switch (cell.getCellType()){
               case STRING -> cell.getStringCellValue();
               case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
               case BLANK -> null;
               default -> null;
           };
       }

}
