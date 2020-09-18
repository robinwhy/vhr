package org.why.myvhr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.why.myvhr.mapper.EmployeeMapper;
import org.why.myvhr.model.Employee;
import org.why.myvhr.model.RespPageBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");
    public RespPageBean getEmployeeByPage(Integer page, Integer size, Employee employee, Date[] beginDateScope){
        if(page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<Employee>data=employeeMapper.getEmployeeByPage(page,size,employee,beginDateScope);
        Long total =employeeMapper.getTotal();
        RespPageBean bean=new RespPageBean();bean.setData(data);bean.setTotal(total);
        return bean;
    }
    public Integer addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month / 12)));
        int result = employeeMapper.insertSelective(employee);
//        if (result == 1) {
//            Employee emp = employeeMapper.getEmployeeById(employee.getId());
//            //生成消息的唯一id
//            String msgId = UUID.randomUUID().toString();
//            MailSendLog mailSendLog = new MailSendLog();
//            mailSendLog.setMsgId(msgId);
//            mailSendLog.setCreateTime(new Date());
//            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
//            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
//            mailSendLog.setEmpId(emp.getId());
//            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
//            mailSendLogService.insert(mailSendLog);
//            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(msgId));
//        }
        return result;
//        return 1;
    }
    public Integer deleteEmpByEid(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmp(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }
    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }
    public Employee getEmployeeById(Integer empId) {
        return employeeMapper.getEmployeeById(empId);
    }
    public Integer addEmps(List<Employee> list) {
        return employeeMapper.addEmps(list);
    }
}
