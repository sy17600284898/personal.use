package com.personal.use.excel;

import com.personal.use.excel.annotation.PrimaryField;
import com.personal.use.execption.AException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelReadUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelReadUtil.class);

    public static List<String[]> getExcelData(MultipartFile file) throws Exception {
        Workbook workbook = openWorkBook(file);
        return analysisWorkBook(workbook, true);
    }

    public static List<String[]> getExcelData(File file) throws Exception {
        Workbook workbook = openWorkBook(file);
        return analysisWorkBook(workbook, true);
    }

    /**
     * 不建议再使用，这个实现方式依赖class的field读取顺序与excel的column定义顺序，这种关联很弱，而且受jvm实现影响
     * 推荐使用getExcelDataByColumnName，通过注解描述field与column的对应关系，强关联
     *
     * @param file
     * @param targetClass
     * @param <E>
     * @return
     * @throws Exception
     */
    @Deprecated
    public static <E> List<E> getExcelData(File file, Class<E> targetClass) throws Exception {
        List<E> result = new ArrayList<E>();
        Field[] fields = targetClass.getDeclaredFields();
        List<String> fieldList = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(PrimaryField.class) != null) {
                fieldList.add(fields[i].getName());
            }
        }
        String[] columns = new String[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            columns[i] = fieldList.get(i);
        }
        if (columns.length > 0) {
            List<Map<String, String>> excelDataList = ExcelReadUtil.getExcelData(file, columns);
            for (Map<String, String> dataItem : excelDataList) {
                E targetObject = targetClass.newInstance();
                BeanUtils.copyProperties(targetObject, dataItem);
                result.add(targetObject);
            }
        } else {
            throw new AException("解析Excel文件出现异常，字段列表为空");
        }
        return result;
    }

    /**
     * 根据列名解析数据
     *
     * @param file
     * @param targetClass
     * @param <E>
     * @return
     * @throws Exception
     */
    public static <E> List<E> getExcelDataByColumnName(File file, Class<E> targetClass) throws Exception {
        List<E> result = new ArrayList<E>();
        Field[] fields = targetClass.getDeclaredFields();
        List<PrimaryFieldInfo> fieldList = new ArrayList<PrimaryFieldInfo>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(PrimaryField.class) != null) {
                PrimaryField primaryFieldAnn = fields[i].getAnnotation(PrimaryField.class);
                PrimaryFieldInfo primaryFieldInfo = new PrimaryFieldInfo(primaryFieldAnn.columnName(), fields[i].getName());
                fieldList.add(primaryFieldInfo);
            }
        }
        if (fieldList.size() > 0) {
            List<Map<String, String>> excelDataList = ExcelReadUtil.getExcelDataByPrimaryFieldInfo(file, fieldList);
            for (Map<String, String> dataItem : excelDataList) {
                E targetObject = targetClass.newInstance();
                BeanUtils.copyProperties(targetObject, dataItem);
                result.add(targetObject);
            }
        } else {
            throw new AException("解析Excel文件出现异常，字段列表为空");
        }
        return result;
    }

    /**
     * 根据传入的columns与excel的column根据index做对应来将excel解析成map
     *
     * @param file
     * @param columns
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(File file, String[] columns) throws Exception {
        if (columns != null && columns.length > 0) {
            List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
            Workbook workbook = openWorkBook(file);
            List<String[]> rowList = analysisWorkBook(workbook, true);
            if (rowList.size() > 0) {
                for (String[] fieldsValues : rowList) {
                    if (fieldsValues.length == columns.length) {
                        Map<String, String> rowMap = new HashMap<String, String>();
                        for (int i = 0; i < fieldsValues.length; i++) {
                            rowMap.put(columns[i], fieldsValues[i]);
                        }
                        resultList.add(rowMap);
                    } else {
                        throw new AException("column数量与cell数量不一致");
                    }
                }
            }
            return resultList;
        } else {
            throw new AException("列不能为空");
        }
    }

    /**
     * 根据列信息解析数据
     *
     * @param file
     * @param columns
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelDataByPrimaryFieldInfo(File file, List<PrimaryFieldInfo> columns) throws Exception {
        if (columns != null && columns.size() > 0) {
            List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
            Workbook workbook = openWorkBook(file);
            List<String[]> rowList = analysisWorkBook(workbook, false);
            Map<String, String> titleMapColumns = new HashMap<>();
            for (PrimaryFieldInfo info : columns) {
                titleMapColumns.put(info.getTitleName(), info.getColumnName());
            }
            if (rowList.size() > 0) {
                String[] titleInfos = rowList.get(0);
                //删除title行
                rowList.remove(0);
                for (String[] fieldsValues : rowList) {
                    if (fieldsValues.length == columns.size()) {
                        Map<String, String> rowMap = new HashMap<String, String>();
                        for (int i = 0; i < fieldsValues.length; i++) {
                            rowMap.put(titleMapColumns.get(titleInfos[i]), fieldsValues[i]);
                        }
                        resultList.add(rowMap);
                    } else {
                        throw new AException("column数量与cell数量不一致");
                    }
                }
            }
            return resultList;
        } else {
            throw new AException("列不能为空");
        }
    }

    public static List<String[]> analysisWorkBook(Workbook wb, boolean skipTitle) throws Exception {
        if (wb != null) {
            Sheet sheet = wb.getSheetAt(0);//获取sheet页，只支持单页
            List<String[]> rowlist = new ArrayList<String[]>();
            Row row = null;
            Cell cell = null;
            int totalRows = sheet.getPhysicalNumberOfRows(); // 总行数
            int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();//总列数
            int index = skipTitle ? 1 : 0;//是否跳过表头
            //略过表头
            for (int r = index; r < totalRows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    String[] cellArr = new String[totalCells];
                    for (int c = 0; c < totalCells; c++) {
                        cell = row.getCell(c);
                        cellArr[c] = ExcelReadUtil.getStringValue(cell);
                    }
                    rowlist.add(cellArr);
                } else {
                    throw new AException("Excel文件中存在空行，请删除后再进行导入");
                }
            }
            return rowlist;
        } else {
            throw new Exception("解析Excel文件出现异常，Workbook对象为null");
        }
    }

    private static Workbook openWorkBook(MultipartFile file) throws Exception {
        Workbook wb = null;
        if (file != null) {
            try {
                if (file.getName().toLowerCase().endsWith(".xls")) {
                    wb = new HSSFWorkbook(file.getInputStream());
                } else if (file.getName().toLowerCase().endsWith(".xlsx")) {
                    wb = new XSSFWorkbook(file.getInputStream());
                }
            } catch (Exception e1) {
                log.error("解析Excel出现异常：", e1);
            }
            return wb;
        } else {
            throw new Exception("excel文件为null");
        }
    }

    private static Workbook openWorkBook(File file) throws Exception {
        Workbook wb = null;
        if (file != null) {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                if (file.getName().toLowerCase().endsWith(".xls")) {
                    wb = new HSSFWorkbook(fileInputStream);
                } else if (file.getName().toLowerCase().endsWith(".xlsx")) {
                    wb = new XSSFWorkbook(fileInputStream);
                }
            } catch (Exception e1) {
                log.error("解析Excel出现异常：", e1);
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
            return wb;
        } else {
            throw new Exception("excel文件为null");
        }
    }


    public static String getStringValue(Cell cell) {
        if (null == cell) {
            return null;
        }
//        int type = cell.getCellType();
        if (0 == cell.getCellType()) {
            //判断是否为日期类型
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                //用于转化为日期格式
                Date d = cell.getDateCellValue();
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                return formater.format(d);
            } else {
//                // 用于格式化数字，只保留数字的整数部分
//                DecimalFormat df = new DecimalFormat("########");
//                return df.format(cell.getNumericCellValue());
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        return cell.getStringCellValue();
    }

    static class PrimaryFieldInfo {
        String titleName = null;//excel列名
        String columnName = null;//java bean属性名

        public PrimaryFieldInfo(String titleName, String columnName) {
            this.titleName = titleName;
            this.columnName = columnName;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
    }


}
