package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class DepartmentDao {
    private static Collection<Department> departments;


    private static DepartmentDao departmentDao = new DepartmentDao();

    private DepartmentDao() {
    }

    public static DepartmentDao getInstance() {
        return departmentDao;
    }


    public Collection<Department> findAll()throws SQLException {
        departments = new TreeSet<Department>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from department");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //Department，根据遍历结果中的id,description,no,remarks,school_id值
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school);
            //向departments集合中添加Department对象
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }

    public Department find(Integer id)throws SQLException {
        Department department = null;
        Connection connection = JdbcHelper.getConn();
        String deleteDepartment_sql = "SELECT * FROM department WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteDepartment_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Department对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school);
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return department;
    }

    public boolean update(Department department)throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql对象
        String updateDepartment_sql = "UPDATE department set description =? , no=?, remarks=?  WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译参数赋值
        pstmt.setString(1,department.getDescription());
        pstmt.setString(2,department.getNo());
        pstmt.setString(3,department.getRemarks());
        pstmt.setInt(4,department.getId());
        //执行预编译对象的executeUpdate方法，获取增加的记录行数
        int affectedRowNum = pstmt.executeUpdate();
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象
        connection.close();
        //如果影响的行数>1,则返回ture,否则返回false
        return affectedRowNum>0;
    }

    public boolean add(Department department) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql对象
        String addDepartment_sql = "INSERT INTO department (no,description,remarks,school_id) VALUES" + " (?,?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(addDepartment_sql);
        //为预编译参数赋值
        pstmt.setString(1, department.getNo());
        pstmt.setString(2, department.getDescription());
        pstmt.setString(3, department.getRemarks());
        pstmt.setInt(4, department.getSchool().getId());
        //执行预编译对象的executeUpdate方法，获取增加的记录行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("增加了" + affectedRowNum + "条记录");
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象
        connection.close();
        //如果影响的行数>1,则返回ture,否则返回false
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id)throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteDepartment_sql = "DELETE FROM department WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 条");
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象
        connection.close();
        return  affectedRowNum>0;
    }

    //定义方法findAllBySchool(Integer schoolId)，返回schoolId指定的系
    public Set<Department> findAllBySchool(Integer schoolId) throws SQLException {
        Set<Department> departments = new HashSet<Department>();
        Connection connection = JdbcHelper.getConn();
        String selectDepartment_sql = "SELECT * FROM department WHERE school_id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(selectDepartment_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,schoolId);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
        //若结果集中没有记录，则本方法返回null
        while (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school);
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return departments;
    }

    public static void main(String[] args)throws SQLException {
        //删
        //DepartmentDao.delete(2);
        //DepartmentDao.getInstance().findAll();
        Department department1 = DepartmentDao.getInstance().find(2);
        System.out.println(department1);
        department1.setDescription("土管");
        DepartmentDao.getInstance().update(department1);
        Department department2 = DepartmentDao.getInstance().find(2);
        System.out.println(department2.getDescription());

        School managementSchool = null;
        managementSchool = SchoolDao.getInstance().find(4);
        Department department = new Department("工业工程","05","",managementSchool);
        System.out.println(DepartmentDao.getInstance().add(department));
    }
}

