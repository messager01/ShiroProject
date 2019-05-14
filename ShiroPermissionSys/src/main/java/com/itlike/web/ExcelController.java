package com.itlike.web;


import com.itlike.domain.Employee;
import com.itlike.service.EmployeeService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response){
        try {
        // 从数据库当中取列表的数据
            List<Employee> allEmployee = employeeService.getAllEmployee();
            System.out.println(allEmployee);
        //创建excel  把数据写入到excel中
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("员工名单");  // 设置excel表中的sheet
        HSSFRow row = sheet.createRow(0);   // 创建 第 0 行   角标从0开始
        // 设置行的每一列数据
        row.createCell(0).setCellValue("编号");           // 设置定0行第0列的数据
        row.createCell(1).setCellValue("用户名");         // 设置定0行第1列的数据
        row.createCell(2).setCellValue("密码");           // 设置定0行第2列的数据
        row.createCell(3).setCellValue("创建时间");       // 设置定0行第3列的数据
        row.createCell(4).setCellValue("电话");           // 设置定0行第4列的数据
        row.createCell(5).setCellValue("邮箱");           // 设置定0行第5列的数据
        row.createCell(6).setCellValue("是否离职");       // 设置定0行第6列的数据
        row.createCell(7).setCellValue("是否管理员");     // 设置定0行第7列的数据
           // row.createCell(8).setCellValue("部门名称");     // 设置定0行第8列的数据
        HSSFRow employeeRow = null;
        //取出每一个员工  写入数据
        for (int i = 0; i < allEmployee.size() ; i++) {
            Employee employee = allEmployee.get(i);
            employeeRow = sheet.createRow(i + 1);  //真实数据第一行从 第i+1 开始
            employeeRow.createCell(0).setCellValue(employee.getId());
            employeeRow.createCell(1).setCellValue(employee.getUsername());
            employeeRow.createCell(2).setCellValue(employee.getPassword());
            if (employee.getInputtime() != null){  //  防止时间为空
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(employee.getInputtime());
                employeeRow.createCell(3).setCellValue(time);
            }else{
                employeeRow.createCell(3).setCellValue(" ");  // 如果为空 设置空的字符串
            }
            employeeRow.createCell(4).setCellValue(employee.getTel());
            employeeRow.createCell(5).setCellValue(employee.getEmail());
            employeeRow.createCell(6).setCellValue(employee.getState());
            employeeRow.createCell(7).setCellValue(employee.getAdmin());
           /* if(employee.getDepartment() == null || employee.getDepartment().getId() == null){
                employeeRow.createCell(8).setCellValue("无部门");
            }else{
                employeeRow.createCell(8).setCellValue(employee.getDepartment().getId());
            }*/
        }

        // 响应给浏览器

            /*
            * 很多情况下，我们在写程序的时候都会把代码设置为UTF-8的编码，
            * 可以在下载文件设置filename的时候却有违常理，设置编码格式为ISO8859-1
            * */

            //  设置编码
            String fileName = new String("ExcelTml.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+fileName);  // 设置响应头
            wb.write(response.getOutputStream());  // 响应给浏览器
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 下载模板
    @RequestMapping("/downloadExcelTpl")
    @ResponseBody
    public void downloadExcelTpl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileInputStream is = null;
        String fileName = null;
        try {
            fileName = new String("EmployeeTpl.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;filename="+fileName);  // 设置响应头
            // 获取文件路径
            String realPath = request.getSession().getServletContext().getRealPath("/static/ExcelTml.xls");
            is = new FileInputStream(realPath);
            IOUtils.copy(is,response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
            }
        }

    }
}
