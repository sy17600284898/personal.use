package com.personal.use.excel;

import com.personal.use.collection.DateTimeUtils;
import com.personal.use.model.Employee;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ExcelUtil
 *
 * @author: shiyan
 * @version: 2019-10-16 11:30
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class ExcelUtil {
    /**
     * 导入
     *
     * @param file
     * @return
     */
    public static List<Employee> Execl(File file) {
        try {
            return ExcelReadUtil.getExcelData(file, Employee.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public static void ExeclOut(HttpServletResponse response, ServletRequest request) {
        List<Employee> employeeList = new ArrayList<>();
        response.setContentType("application/msexcel");
        OutputStream os;
        try {
            os = response.getOutputStream();
            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(("导出" + DateTimeUtils.getCurrentTime()).getBytes("GB2312"), "8859_1") + ".xls");
            // 创建一个实体专门存放导出的字段
            String[] allHeaders = {"承运单号", "出库单号", "订单编号", "物流商", "物流轨迹信息", "轨迹日期", "提货日期", "预计到达目的城市日期", "预计妥投日期", "操作时间", "操作人"};
            String[] allField = {"shippingOrder", "outBoundId", "orderNumber", "logisticsCompany", "lastLogisticsContent", "lastLogisticsDate",
                    "pickUpTime", "expectedArrivalDate", "expectedRecieveDate", "lastOperateDate", "lastOperateUser"};
            HSSFWorkbook workbook = new HSSFWorkbook();
            ExcelOutUtil<Employee> excelUtil = new ExcelOutUtil<Employee>();
            excelUtil.creatAuditSheet(workbook, "sheet", employeeList, allHeaders, allField);
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
