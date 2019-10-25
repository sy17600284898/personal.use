package com.personal.use.excel;

import com.personal.use.excel.annotation.ExportField;
import com.personal.use.execption.AException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelWriteUtil {

    private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(ExcelWriteUtil.class);

    public static <T> HSSFWorkbook generateExcelWorkBook(List<T>... dataArray) throws Exception {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        for (List<T> dataList : dataArray) {
            if (dataList != null && dataList.size() > 0) {
                Class<T> genericClazz = ExcelWriteUtil.getActualClassFromItem(dataList);
                if (genericClazz != null) {
                    List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
                    Field[] fields = genericClazz.getDeclaredFields();
                    for (Field field : fields) {
                        ExportField exportFieldInfo = field.getAnnotation(ExportField.class);
                        if (exportFieldInfo != null) {
                            ExcelColumn header = new ExcelColumn(field, exportFieldInfo.columnName());
                            columns.add(header);
                        }
                    }

                    HSSFSheet hssfSheet = hssfWorkbook.createSheet();
                    HSSFRow headerRow = hssfSheet.createRow(0);
                    for (int c = 0; c < columns.size(); c++) {
                        ExcelColumn column = columns.get(c);
                        HSSFCell cell = headerRow.createCell(c);
                        cell.setCellValue(column.getTitle());
                    }
                    for (int i = 0; i < dataList.size(); i++) {
                        T data = dataList.get(i);
                        HSSFRow row = hssfSheet.createRow(i + 1);
                        for (int c = 0; c < columns.size(); c++) {
                            ExcelColumn excelHeader = columns.get(c);
                            HSSFCell cell = row.createCell(c);
                            PropertyDescriptor pDescriptor = new PropertyDescriptor(excelHeader.getProperty().getName(), data.getClass());
                            cell.setCellValue(transforValueToString(pDescriptor.getReadMethod().invoke(data)));
                        }
                    }
                } else {
                    throw new AException("获取泛型类型出现异常");
                }
            }
        }
        return hssfWorkbook;
    }

    public static <T, V> HSSFWorkbook generateMultiExcelWorkBook(List<T> dataList, List<V> suDataList) throws Exception {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        Class<T> genericClazz = ExcelWriteUtil.getActualClassFromItem(dataList);
        if (genericClazz != null) {
            List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
            Field[] fields = genericClazz.getDeclaredFields();
            for (Field field : fields) {
                ExportField exportFieldInfo = field.getAnnotation(ExportField.class);
                if (exportFieldInfo != null) {
                    ExcelColumn header = new ExcelColumn(field, exportFieldInfo.columnName());
                    columns.add(header);
                }
            }

            HSSFSheet hssfSheet = hssfWorkbook.createSheet("采购执行单信息");
            HSSFRow headerRow = hssfSheet.createRow(0);
            for (int c = 0; c < columns.size(); c++) {
                ExcelColumn column = columns.get(c);
                HSSFCell cell = headerRow.createCell(c);
                cell.setCellValue(column.getTitle());
            }
            for (int i = 0; i < dataList.size(); i++) {
                T data = dataList.get(i);
                HSSFRow row = hssfSheet.createRow(i + 1);
                for (int c = 0; c < columns.size(); c++) {
                    ExcelColumn excelHeader = columns.get(c);
                    HSSFCell cell = row.createCell(c);
                    PropertyDescriptor pDescriptor = new PropertyDescriptor(excelHeader.getProperty().getName(), data.getClass());
                    cell.setCellValue(transforValueToString(pDescriptor.getReadMethod().invoke(data)));
                }
            }
        } else {
            throw new Exception("获取泛型类型出现异常");
        }
        Class<V> genericClazzSub = ExcelWriteUtil.getActualClassFromItem(suDataList);
        if (genericClazzSub != null) {
            List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
            Field[] fields = genericClazzSub.getDeclaredFields();
            for (Field field : fields) {
                ExportField exportFieldInfo = field.getAnnotation(ExportField.class);
                if (exportFieldInfo != null) {
                    ExcelColumn header = new ExcelColumn(field, exportFieldInfo.columnName());
                    columns.add(header);
                }
            }

            HSSFSheet hssfSheet2 = hssfWorkbook.createSheet("海运询价单信息");
            HSSFRow headerRow = hssfSheet2.createRow(0);
            for (int c = 0; c < columns.size(); c++) {
                ExcelColumn column = columns.get(c);
                HSSFCell cell = headerRow.createCell(c);
                cell.setCellValue(column.getTitle());
            }
            for (int i = 0; i < suDataList.size(); i++) {
                V data = suDataList.get(i);
                HSSFRow row = hssfSheet2.createRow(i + 1);
                for (int c = 0; c < columns.size(); c++) {
                    ExcelColumn excelHeader = columns.get(c);
                    HSSFCell cell = row.createCell(c);
                    PropertyDescriptor pDescriptor = new PropertyDescriptor(excelHeader.getProperty().getName(), data.getClass());
                    cell.setCellValue(transforValueToString(pDescriptor.getReadMethod().invoke(data)));
                }
            }
        } else {
            //throw new Exception("获取泛型类型出现异常");
        }
        return hssfWorkbook;
    }

    public static <T> void writeDataToExcel(List<T> dataList, File file) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            HSSFWorkbook hssfWorkbook = ExcelWriteUtil.generateExcelWorkBook(dataList);
            if (hssfWorkbook != null) {
                if (file != null) {
                    fileOutputStream = new FileOutputStream(file);
                    hssfWorkbook.write(fileOutputStream);
                } else {
                    throw new Exception("file为空");
                }
            } else {
                throw new Exception("生成HSSFWorkbook出现异常");
            }
        } catch (Exception e) {
            log.error("生成excel文件出现异常：", e);
            throw e;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("生成excel文件，关闭输出流出现异常：", e);
                }
            }
        }
    }

    private static String transforValueToString(Object value) {
        String result = null;
        if (null == value) return "";
        if (value instanceof BigDecimal) {
            result = value.toString();
        }
        if (value instanceof Date) {
            result = sFormat.format((Date) value);
        }
        if (value instanceof Integer) {
            result = value.toString();
        }
        if (value instanceof Long) {
            result = value.toString();
        }
        if (value instanceof String) {
            result = value.toString();
        }
        return result;
    }

    private static <T> Class<T> getActualClass(List<T> dataList) {
        Class<T> genericClazz = null;
        if (dataList != null) {
            Type genericType = dataList.getClass().getGenericSuperclass();
            if (genericType != null) {
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type type = parameterizedType.getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        genericClazz = (Class<T>) type;
                    }
                }
            }
        }
        return genericClazz;
    }

    private static <T> Class<T> getActualClassFromItem(List<T> dataList) {
        Class<T> genericClazz = null;
        if (dataList != null && dataList.size() > 0) {
            T item = dataList.get(0);
            genericClazz = (Class<T>) item.getClass();
        }
        return genericClazz;
    }

    static class ExcelColumn {

        private Field property = null;
        private String title = null;

        public ExcelColumn(Field property, String title) {
            super();
            this.property = property;
            this.title = title;
        }

        public Field getProperty() {
            return property;
        }

        public void setProperty(Field property) {
            this.property = property;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
