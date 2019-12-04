package cn.edu.sdjzu.xg.bysj.controller.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
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
@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {


    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能(id为空)
    //{"degree": {"description": "硕士","no": "01","remarks": " "},"department": {"description": "工程管理","id": 2,"no": "01","remarks": " ","school": {"description": "艺术","id": 4,"no": "04","remarks": ""}},"name": "id为null的老师","title": {"description": "教授","id": 2,"no": "01","remarks": " "}}
    //请使用以下JSON测试修改功能
    //{"degree": {"description": "硕士","id": 2,"no": "01","remarks": " "},"department": {"description": "工程管理","id": 2,"no": "01","remarks": " ","school": {"description": "艺术","id": 4,"no": "04","remarks": ""}},"id": 4,"name": "修改的老师","title": {"description": "教授","id": 2,"no": "01","remarks": " "}}

    /**
     * POST, http://localhost:8080/teacher.ctl, 增加老师
     * 增加一个老师对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String teacher_str = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_str, Teacher.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //添加try-catch块解决异常
        try{
            //增加Teacher对象
            boolean add=TeacherService.getInstance().add(teacherToAdd);
            if(add){
                //加入将要回应的数据信息
                message.put("message", "添加成功");
            }else {
                message.put("message","添加失败");
            }
        }catch(SQLException e){
            e.printStackTrace();
            message.put("message","添加失败，数据库操作异常");
        } catch(Exception e){
            e.printStackTrace();
            message.put("message", "添加失败，网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * GET, http://localhost:8080/teacher.ctl?id=1, 查询id=1的老师
     * GET, http://localhost:8080/teacher.ctl, 查询所有的老师
     * 响应一个或所有老师对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if (id_str == null) {
            responseTeachers(response);
        } else {
            int id = Integer.parseInt(id_str);
            responseTeacher(id, response);
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
    //响应一个学位对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException,SQLException {
            //根据id查找老师
            Teacher teacher = TeacherService.getInstance().find(id);
            String teacher_json = JSON.toJSONString(teacher);

            response.getWriter().println(teacher_json);
    }

    //响应所有老师对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException,SQLException {
            //获得所有老师
            Collection<Teacher> teachers = TeacherService.getInstance().findAll();
            String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);

            //响应message到前端
            response.getWriter().println(teachers_json);
    }

    /**
     *  DELETE, http://localhost:8080/teacher.ctl?id=1, 删除id=1的老师
     * 删除一个老师对象：根据来自前端请求的id，删除数据库表中id的对应记录
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
            TeacherService.getInstance().delete(id);
            message.put("message", "删除成功");
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
     * PUT, http://localhost:8080/teacher.ctl, 修改老师
     * 修改一个老师对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToUpdate = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加加Degree对象
            TeacherService.getInstance().update(teacherToUpdate);
            message.put("message", "修改成功");
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
