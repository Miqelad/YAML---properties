/**

-<dependency>

<groupId>org.apache.poi</groupId>

<artifactId>poi</artifactId>

<version>3.17</version>

</dependency>


-<dependency>

<groupId>org.apache.poi</groupId>

<artifactId>poi-ooxml</artifactId>

<version>3.17</version>

</dependency>
*/


_______________________________________________________________________________________________________________________________

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Apache POI это библиотека Java с открытым исходным кодо, предоставленный Apache,
 * это сильная библиотека помогающая вам работать с документами Microsoft, как Word, Excel, Power point, Visio,...
 *
 * POI это аббревиатура "Poor Obfuscation Implementation". Форматы файлов Microsoft скрыты.
 * Инженеры Apache должны были постараться чтобы понять, и они увидели что Microsoft создал сложные форматы, когда это не необходимо.
 * И название библиотеки имеет происхождение от юмора.
 *
 * Poor Obfuscation Implementation: Плохая реализация обфускации. (Примерный перевод).
 * В данной статье мы покажем вам как использовать Apache POI для работы с Excel.
 *
 * Apache POI поддерживает вас при работе с форматами Microsoft, его классы часто имеют приставку HSSF, XSSF, HPSF, ...
 * Смотря на приставки класса, вы можете узнать какой формат поддерживает этот класс.
 * Например чтобы работать с форматом Excel (XLS) вам нужны классы:
 * HSSFWorkbook
 * HSSFSheet
 * HSSFCellStyle
 * HSSFDataFormat
 * HSSFFont
 * Apache POI предоставляет вам интерфейсы
 * Workbook, - целиком файл excel HSSFWorkbook
 * Sheet, - лист HSSFSheet
 * Row, строка HSSFRow
 * Cell - ячейка  HSSFCell
 *
 * Создать и записать файл Excel
 *
 * Пример ниже является простым примером использования POI чтобы создать файл excel.
 * Вы можете сочетать с использованием стиля (Style) в ячейках (Cell) чтобы создать красивый документ Excel.
 * @author miq 05.05.2022
 */
@Component
public class CreateExcel {

    public void createAndSaveExcel(){

        /*Create book*/
        Workbook xlsxWorkbook = new XSSFWorkbook();
        /*inside here crate sheet*/
        Sheet sheet1 = xlsxWorkbook.createSheet("Test");
        /*create Row in zero position*/
        Row row1 = sheet1.createRow(0);
        /*create Cell in row 1*/
        row1.createCell(0).setCellValue("Header 1");
        row1.createCell(1).setCellValue("Header 2");
        row1.createCell(2).setCellValue("Header 2");
        Row row2 = sheet1.createRow(1);
        /*create Cell in row 1*/
        row2.createCell(0).setCellValue("Va 1");
        row2.createCell(1).setCellValue("Va 2");
        row2.createCell(2).setCellValue("Va 3");

        /*Save this book*/
        try {

            xlsxWorkbook.write(new FileOutputStream("R:\\Excel.xlsx"));
            System.out.println("Done: file in created");
        } catch (IOException e) {
            System.err.println("Error: file is not created");
            e.printStackTrace();
        }


    }
    _______________________________________________________________________________________________________________________________
//   Изменение высоты строки
  
      public void serRowHeight() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Sheet1");
        Row row = sheet1.createRow(0);
        row.setHeight((short) 100);
        row.createCell(0).setCellValue("Row with 100");
        workbook.write(new FileOutputStream("R:\\Excel.xlsx"));



    }
    _______________________________________________________________________________________________________________________________
  // Объединение ячеек

  
      public void mergeCells() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Sheet1");
        /*задаем диапазон объединяющий ячейки
        * номер начальной строки,номер конечной строки, с какого столбца, до какого */
        //строку сольем
        sheet1.addMergedRegion(new CellRangeAddress(0,0,0,2));
        //теперь создадим строку
        Row row = sheet1.createRow(0);
        row.createCell(0).setCellValue("Merge Region");
        workbook.write(new FileOutputStream("R:\\Excel1.xlsx"));

    }
  
  _______________________________________________________________________________________________________________________________
  //Внизу будет показано два способа как прочитать данные
  
      public void readExcel(){
        try (Workbook excelWorkbook = new XSSFWorkbook(new FileInputStream("R:\\Excel.xlsx")))
        {
            /*Take sheets */
            int numberOfSheets = excelWorkbook.getNumberOfSheets();
            for(int i=0;i<numberOfSheets;i++){
                Sheet sheetAt = excelWorkbook.getSheetAt(i);
                /*values rows in file */
                int rowCount = sheetAt.getPhysicalNumberOfRows();

                for (int j=0;j<rowCount;j++){
                    /*take row in a sheet*/
                    Row row = sheetAt.getRow(j);
                    /*take cells in a row*/
                    int cells = row.getPhysicalNumberOfCells();

                    for(int k=0;k<cells;k++){
                        /*sout*/
                        System.out.println(row.getCell(k).toString()+"\t");
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Damn, we lost this file :( ");
        }

    }
//второй способ
    public void readFromIterator() throws IOException {
        Workbook excelWorkbook = new XSSFWorkbook(new FileInputStream("R:\\Excel.xlsx"));
        Iterator <Sheet> sheetIterator = excelWorkbook.iterator();
        while (sheetIterator.hasNext()){
            Sheet sheet = sheetIterator.next();
            Iterator<Row> rowIterator=sheet.iterator();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext()){
                    Cell cells = cellIterator.next();
                    System.out.println(cells.toString()+"\t");
                }

            }
        }
        excelWorkbook.close();
    }

}
_______________________________________________________________________________________________________________________________
// Проверка на цвета
    @Override
    public void run(String... args) throws Exception {
        //файл-книга
        Workbook excelBook= new XSSFWorkbook(new FileInputStream("R:\\colorer.xlsx"));
        //итератор страниц
        Iterator<Sheet> sheetIterator = excelBook.iterator();
        while (sheetIterator.hasNext()){
            //Страница
            Sheet sheet = sheetIterator.next();
            //Итератор строк
            Iterator<Row> rowIterator=sheet.iterator();
            while (rowIterator.hasNext()){
                //строка
                Row row = rowIterator.next();
                //Итератор ячеек
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    //Ячейка
                    Cell cell = cellIterator.next();
/*                    цвет обязательно врапим до xssf
                    https://javadoc.io/static/org.apache.poi/poi-ooxml/3.16/org/apache/poi/xssf/usermodel/XSSFColor.html
                    тут смотрим цвета, модель hex
                    Example:FFFFFF00 -> убираем 2-е Первые F и вставляем для проверки на сайте https://colorscheme.ru/color-converter.html  */
                    XSSFColor color = (XSSFColor) cell.getCellStyle().getFillForegroundColorColor();
                    System.out.println((color ==null)?"null": color.getARGBHex() +"  Long" + " " + cell.getNumericCellValue());
                }

            }
        }
        excelBook.close();




    }
