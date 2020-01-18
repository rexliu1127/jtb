package io.grx.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class PoiUtils {

    public void createExcel(String sheetName, List<Object[]> dataList, OutputStream os) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        if (dataList != null) {
            int rowNum = 0;
            for (Object[] datatype : dataList) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (Object field : datatype) {
                    Cell cell = row.createCell(colNum++);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }
            }
        }
        workbook.write(os);
    }
}
