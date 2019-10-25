package com.personal.use.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Excel util, create excel sheet, cell and style.
 *
 * @param <T> generic class.
 */
public class ExcelOutUtil<T> {

    private static final long MAXDATASHEET = 60000;
    private static final int COLUMNWIDTH = 4000;
    private static final String BLANK = "MINGYU";

    public HSSFCellStyle getCellStyle(HSSFWorkbook workbook, boolean isHeader) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setLocked(true);
        style.setWrapText(true);
        if (isHeader) {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFont(font);
        }
        return style;
    }

    public void generateHeader(HSSFWorkbook workbook, HSSFSheet sheet,
                               String[] headerColumns) {
        HSSFCellStyle style = getCellStyle(workbook, true);
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        for (int i = 0; i < headerColumns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headerColumns[i]);
            sheet.autoSizeColumn((short) i); //自动调节宽度
        }
    }

    /**
     * 自定义Excel列宽，每一列列宽相同
     * Edit By Mingyu 2017-8-17
     */
    public void generateHeaderSelfDefineColumnWidth(HSSFWorkbook workbook, HSSFSheet sheet,
                                                    String[] headerColumns, int width) {
        HSSFCellStyle style = getCellStyle(workbook, true);
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        for (int i = 0; i < headerColumns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headerColumns[i]);
            sheet.setColumnWidth(i, width); //自定义宽度
        }
    }

    /**
     * 自定义Excel列宽，每一列列宽相同
     * Edit By Mingyu 2017-8-17
     */
    public void generateHeaderSelfDefineColumnWidthSimple(HSSFWorkbook workbook, int width) {
        HSSFCellStyle style = getCellStyle(workbook, true);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        row.setHeightInPoints(30);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, width); //自定义宽度
        }
    }

    /**
     * 自定义Excel列宽，每一列列宽可自定义
     * 输入与列长度一致的数组
     * Edit By Mingyu 2017-8-17
     */
    public void generateHeaderSelfDefineColumnWidth(HSSFWorkbook workbook, HSSFSheet sheet,
                                                    String[] headerColumns, int[] width) {
        HSSFCellStyle style = getCellStyle(workbook, true);
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        int headerLength = 0;
        int widthLength = 0;
        if (null != headerColumns && null != width) {
            //如果列宽数组长度与表头列数不一致，则将宽度数组填充
            if ((widthLength = width.length) < (headerLength = headerColumns.length)) {
                width = Arrays.copyOf(width, headerLength);
                Arrays.fill(width, widthLength == 0 ? COLUMNWIDTH : width[0]);
            }
            for (int i = 0; i < headerLength; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(headerColumns[i]);
                sheet.setColumnWidth(i, width[i]); //自定义宽度
            }
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public HSSFSheet creatAuditSheet(HSSFWorkbook workbook, String sheetName,
                                     List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;

        int rowNum = 0;
        HSSFSheet sheet = workbook.createSheet(sheetName + sheetIndex);
        generateHeader(workbook, sheet, headerColumns);
        HSSFCellStyle style = getCellStyle(workbook, false);
        if (dataset != null) {
            for (T t : dataset) {

                if (dataIndex % MAXDATASHEET == 0) {
                    rowNum = 0;
                    sheetIndex = dataIndex / MAXDATASHEET + 1;
                    sheet = workbook.createSheet(sheetName + sheetIndex);
                    generateHeader(workbook, sheet, headerColumns);
                    style = getCellStyle(workbook, false);
                }
                // 创建工作表
                // sheet.protectSheet("1234");//设置Excel保护密码

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataIndex++;
                rowNum++;
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);
                for (int i = 0; i < fieldColumns.length; i++) {
                    String fieldName = fieldColumns[i];
                    String cellValue = "";
                    String getMethodName = "";
                    try {
                        if (!BLANK.equals(fieldName)) {
                            getMethodName = "get"
                                    + fieldName.substring(0, 1).toUpperCase()
                                    + fieldName.substring(1);
                            Class clazz = t.getClass();
                            Method getMethod;
                            getMethod = clazz.getMethod(getMethodName, new Class[]{});
                            Object value = getMethod.invoke(t, new Object[]{});
                            if (value instanceof Date) {
                                Date date = (Date) value;
                                cellValue = sd.format(date);
                            } else {
                                cellValue = null != value ? value.toString() : "";
                            }
                        }

                        Cell cell = row.createCell(i);
                        cell.setCellStyle(style);
                        if (!"".equals(cellValue) && ("rankAchievement".equals(fieldName) || "performance".equals(fieldName)
                        )) {
                            cell.setCellValue(new BigDecimal(cellValue).doubleValue());
                        } else {
                            cell.setCellValue(cellValue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sheet;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public HSSFSheet creatAuditSheetByCompatible(HSSFWorkbook workbook, String sheetName,
                                                 List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;

        int rowNum = 0;
        HSSFSheet sheet = workbook.createSheet(sheetName + sheetIndex);
        generateHeader(workbook, sheet, headerColumns);
        HSSFCellStyle style = getCellStyle(workbook, false);
        if (dataset != null) {
            for (T t : dataset) {

                if (dataIndex % MAXDATASHEET == 0) {
                    rowNum = 0;
                    sheetIndex = dataIndex / MAXDATASHEET + 1;
                    sheet = workbook.createSheet(sheetName + sheetIndex);
                    generateHeader(workbook, sheet, headerColumns);
                    style = getCellStyle(workbook, false);
                }
                // 创建工作表
                // sheet.protectSheet("1234");//设置Excel保护密码

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataIndex++;
                rowNum++;
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);
                for (int i = 0; i < fieldColumns.length; i++) {
                    String fieldName = fieldColumns[i];
                    String fieldFirstName = "";
                    String fieldSecondName = "";
                    String cellValue = "";
                    String getMethodName = "";
                    boolean isContain = false;
                    try {
                        if (!BLANK.equals(fieldName)) {
                            fieldFirstName = fieldName;
                            if (fieldName.contains(".")) {
                                fieldFirstName = StringUtils.split(fieldName, ".")[0];
                                isContain = true;
                            }

                            getMethodName = "get"
                                    + fieldFirstName.substring(0, 1).toUpperCase()
                                    + fieldFirstName.substring(1);
                            Class clazz = t.getClass();
                            Method getMethod;
                            getMethod = clazz.getMethod(getMethodName, new Class[]{});
                            Object value = getMethod.invoke(t, new Object[]{});
                            if (isContain) {
                                fieldSecondName = StringUtils.split(fieldName, ".")[1];
                                Class<?> targetClazz = value.getClass();
                                getMethodName = "get"
                                        + fieldSecondName.substring(0, 1).toUpperCase()
                                        + fieldSecondName.substring(1);
                                getMethod = targetClazz.getMethod(getMethodName, new Class[]{});
                                value = getMethod.invoke(value, new Object[]{});
                            }
                            if (value instanceof Date) {
                                Date date = (Date) value;
                                cellValue = sd.format(date);
                            } else {
                                cellValue = null != value ? value.toString() : "";
                            }
                        }
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(style);
                        cell.setCellValue(cellValue);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return sheet;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HSSFSheet creatAuditSheetGroup(HSSFWorkbook workbook, String sheetName,
                                          List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;
        int rowNum = 0;
        HSSFSheet sheet = workbook.createSheet(sheetName + sheetIndex);
        generateHeader(workbook, sheet, headerColumns);
        HSSFCellStyle style = getCellStyle(workbook, false);
        if (dataset != null) {
            for (T t : dataset) {
                int count = 0;
                String countName = "getCount";
                Class clazzcount = t.getClass();
                Method getMethodCount;
                getMethodCount = clazzcount.getMethod(countName, new Class[]{});
                Object valuecount = getMethodCount.invoke(t, new Object[]{});
                count = (int) valuecount;
                if (dataIndex % MAXDATASHEET == 0) {
                    rowNum = 0;
                    sheetIndex = dataIndex / MAXDATASHEET + 1;
                    sheet = workbook.createSheet(sheetName + sheetIndex);
                    generateHeader(workbook, sheet, headerColumns);
                    style = getCellStyle(workbook, false);
                }
                // 创建工作表
                // sheet.protectSheet("1234");//设置Excel保护密码

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataIndex++;
                rowNum++;
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);
                for (int i = 0; i < fieldColumns.length; i++) {
                    String fieldName = fieldColumns[i];
                    String cellValue = "";
                    String getMethodName = "";
                    try {
                        if (!BLANK.equals(fieldName)) {
                            getMethodName = "get"
                                    + fieldName.substring(0, 1).toUpperCase()
                                    + fieldName.substring(1);
                            Class clazz = t.getClass();
                            Method getMethod;
                            getMethod = clazz.getMethod(getMethodName, new Class[]{});
                            Object value = getMethod.invoke(t, new Object[]{});
                            if (value instanceof Date) {
                                Date date = (Date) value;
                                cellValue = sd.format(date);
                            } else {
                                cellValue = null != value ? value.toString() : "";
                            }

                        }
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(style);
                        if (("rankAchievementTotal".equals(fieldName) || "performanceTotal".equals(fieldName) || "regionalManager".equals(fieldName)) && (cellValue != null && !"".equals(cellValue)) && count != 0) {
                            CellRangeAddress addAddress = new CellRangeAddress((rowNum + 1) - count, rowNum, i, i);
                            sheet.addMergedRegion(addAddress);
                            Row firstRow = sheet.getRow((rowNum + 1) - count);
                            Cell targetCell = firstRow.getCell(i);
                            if ("regionalManager".equals(fieldName)) {
                                targetCell.setCellValue(cellValue);
                            } else {
                                targetCell.setCellValue(new BigDecimal(cellValue).doubleValue());
                            }
                        }
                        if (!"".equals(cellValue) && ("rankAchievement".equals(fieldName) || "performance".equals(fieldName)
                                || "rankAchievementTotal".equals(fieldName) || "performanceTotal".equals(fieldName))) {
                            cell.setCellValue(new BigDecimal(cellValue).doubleValue());
                        } else {
                            cell.setCellValue(cellValue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return sheet;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public HSSFSheet creatAuditSheetForPlan(HSSFWorkbook workbook, String sheetName,
                                            List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;

        int rowNum = 0;
        HSSFSheet sheet = workbook.getSheet("sheet1");
        rowNum = sheet.getLastRowNum() + 2;
        if (sheet != null) {
            HSSFCellStyle headStyle = getCellStyle(workbook, true);
            Row headRow = sheet.createRow(rowNum);
            headRow.setHeightInPoints(30);
            for (int i = 0; i < headerColumns.length; i++) {
                Cell cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(headerColumns[i]);
                sheet.autoSizeColumn((short) i); //自动调节宽度
            }
            HSSFCellStyle style = getCellStyle(workbook, false);
            for (T t : dataset) {

                if (dataIndex % MAXDATASHEET == 0) {
                    rowNum = 0;
                    sheetIndex = dataIndex / MAXDATASHEET + 1;
                    sheet = workbook.createSheet(sheetName + sheetIndex);
                    generateHeader(workbook, sheet, headerColumns);
                    style = getCellStyle(workbook, false);
                }

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataIndex++;
                rowNum++;
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);
                for (int i = 0; i < fieldColumns.length; i++) {
                    String fieldName = fieldColumns[i];

                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    try {
                        Class clazz = t.getClass();
                        Method getMethod;
                        getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        String cellValue = "";
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            cellValue = sd.format(date);
                        } else {
                            cellValue = null != value ? value.toString() : "";
                        }
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(style);
                        cell.setCellValue(cellValue);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return sheet;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public HSSFSheet creatAuditSheetForQuestion(HSSFWorkbook workbook, String sheetName,
                                                List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;

        int rowNum = 0;
        HSSFSheet sheet = workbook.getSheet("sheet1");
        rowNum = sheet.getLastRowNum() + 2;
        if (sheet != null) {
            HSSFCellStyle headStyle = getCellStyle(workbook, true);
            Row headRow = sheet.createRow(rowNum);
            headRow.setHeightInPoints(30);
            for (int i = 0; i < headerColumns.length; i++) {
                Cell cell = headRow.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(headerColumns[i]);
                sheet.autoSizeColumn((short) i); //自动调节宽度
            }
            HSSFCellStyle style = getCellStyle(workbook, false);
            for (T t : dataset) {

                if (dataIndex % MAXDATASHEET == 0) {
                    rowNum = 0;
                    sheetIndex = dataIndex / MAXDATASHEET + 1;
                    sheet = workbook.createSheet(sheetName + sheetIndex);
                    generateHeader(workbook, sheet, headerColumns);
                    style = getCellStyle(workbook, false);
                }

                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataIndex++;
                rowNum++;
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(25);
                for (int i = 0; i < fieldColumns.length; i++) {
                    String fieldName = fieldColumns[i];
                    String getMethodName = "";
                    if (fieldName != null) {
                        getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                    }

                    try {
                        Class clazz = t.getClass();
                        Method getMethod;
                        getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        String cellValue = "";
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            cellValue = sd.format(date);
                        } else {
                            cellValue = null != value ? value.toString() : "";
                        }
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(style);
                        cell.setCellValue(cellValue);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return sheet;

    }

    public static void main(String[] args) {
        System.out.println(HSSFWorkbook.class.getProtectionDomain()
                .getCodeSource().getLocation());
    }

    public void generateHeaders(HSSFWorkbook workbook, HSSFSheet sheet,
                                String[] headerColumns) {
        HSSFCellStyle style = getCellStyle(workbook, true);
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        for (int i = 0; i < headerColumns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headerColumns[i]);
            sheet.setColumnWidth((short) i, 4000); //自动调节宽度


        }
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(headerColumns.length - 1, 10000);

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HSSFSheet creatAuditSheets(HSSFWorkbook workbook, String sheetName,
                                      List<T> dataset, String[] headerColumns, String[] fieldColumns)
            throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        long sheetIndex = 1;
        long dataIndex = 1;

        int rowNum = 0;
        HSSFSheet sheet = workbook.createSheet(sheetName + sheetIndex);
        generateHeaders(workbook, sheet, headerColumns);
        HSSFCellStyle style = getCellStyle(workbook, false);
        for (T t : dataset) {

            if (dataIndex % MAXDATASHEET == 0) {
                rowNum = 0;
                sheetIndex = dataIndex / MAXDATASHEET + 1;
                sheet = workbook.createSheet(sheetName + sheetIndex);
                generateHeaders(workbook, sheet, headerColumns);
                style = getCellStyle(workbook, false);
            }
            // 创建工作表
            // sheet.protectSheet("1234");//设置Excel保护密码

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataIndex++;
            rowNum++;
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(25);
            for (int i = 0; i < fieldColumns.length; i++) {
                String fieldName = fieldColumns[i];
                String cellValue = "";
                String getMethodName = "";
                try {
                    if (!BLANK.equals(fieldName)) {
                        getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);
                        Class clazz = t.getClass();
                        Method getMethod;
                        getMethod = clazz.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            cellValue = sd.format(date);
                        } else {
                            cellValue = null != value ? value.toString() : "";
                        }
                    }
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(style);
                    cell.setCellValue(cellValue);
                } catch (Exception e) {

                }
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, headerColumns.length - 1));
        return sheet;
    }
}
