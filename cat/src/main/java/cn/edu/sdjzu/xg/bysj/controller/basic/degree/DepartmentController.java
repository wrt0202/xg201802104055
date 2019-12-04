package cn.edu.sdjzu.xg.bysj.controller.basic.degree;


import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能(id为空)
    // {"description": "id为null的系","no": "09","remarks": "","school": {"description": "土木工程","id": 1,"no": "01","remarks": ""}}
    //请使用以下JSON测试修改功能
    // {"description": "id为7的系","id": 7,"no": "09","remarks": "","school": {"description": "土木工程","id": 1,"no": "01","remarks": ""}}

    /**
     * POST, http://localhost:8080/department.ctl, 增加老师
     * 增加一个老师对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String department_str = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Department departmentToAdd = JSON.parseObject(department_str, Department.class);


        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //添加try-catch块解决异常
        try {
            //增加Department对象
            boolean added = DepartmentService.getInstance().add(departmentToAdd);
            if (added){
            message.put("message", "增加成功");
            }else {
            message.put("message", "已被增加");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");   
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");   
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    /**
     * GET, http://localhost:8080/department.ctl?id=1, 查询id=1的系
     * GET, http://localhost:8080/department.ctl, 查询所有的系
     * 响应一个或所有学位对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String paraType = request.getParameter("paraType");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有院系对象，否则响应id指定的院系对象
            if (id_str != null) {
                int id = Integer.parseInt(id_str);
                if (paraType == null) {
                    responseDepartment(id,response);
                } else if (paraType.equals("school")){
                    responseDepartmentBySchool(response, id);
                }
            }else{
                responseDepartments(response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");   
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");   
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
        //响应一个相应Id系对象
        private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException ,SQLException{
                //根据id查找系
                Department department = DepartmentService.getInstance().find(id);
                String department_json = JSON.toJSONString(department);

                //响应message到前端
                response.getWriter().println(department_json);
        }
        //响应所有系对象
        private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException ,SQLException{
                //获得所有系
                Collection<Department> departments= DepartmentService.getInstance().findAll();
                String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
                //响应message到前端
                response.getWriter().println(departments_json);
         }
         //要求服务器响应paraType类的并对应相应Id的所有系
         private void responseDepartmentBySchool(HttpServletResponse response,int id) throws SQLException, IOException {
            Collection<Department>departments = DepartmentService.getInstance().findALLBySchool(id);
            String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
            //响应Departments_json到前端
            response.getWriter().println(departments_json);
         }

    /**
     * DELETE, http://localhost:8080/department.ctl?id=1, 删除id=1的系
     * 删除一个系对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //添加try-catch块解决异常
        try {
            boolean deleted = DepartmentService.getInstance().delete(id);
            if (deleted) {
                message.put("message", "删除成功");
            }else {
                message.put("message", "已被删除");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");   
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");   
            e.printStackTrace();
        }

        //响应message到前端
        response.getWriter().println(message);
    }
    /**
     *  PUT, http://localhost:8080/department.ctl, 修改系
     * 修改一个系对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToUpdate = JSON.parseObject(department_json, Department.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加Department对象
            boolean added = DepartmentService.getInstance().update(departmentToUpdate);
            if (added) {
                message.put("message", "修改成功");
            }else {
                message.put("message", "已被修改");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");   
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");   
            e.printStackTrace();
        }

        //响应message到前端
        response.getWriter().println(message);
    }

}
