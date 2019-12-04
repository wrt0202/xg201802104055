package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;


	private SchoolDao(){}

	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll()throws SQLException {
		schools = new TreeSet<>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建School对象，根据遍历结果中的id,description,no,remarks值
			School school = new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向schools集合中添加School对象
			schools.add(school);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return schools;
	}

	public School find(Integer id)throws SQLException{
		School school = null;
		Connection connection = JdbcHelper.getConn();
		String deleteSchool_sql = "SELECT * FROM school WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteSchool_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建School对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			school = new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return school;
	}

	public boolean update(School school)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateSchool_sql = " update school set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add(School school)throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql对象
		String addSchool_sql = "INSERT INTO school (no,description,remarks) VALUES" + " (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setString(1, school.getNo());
		pstmt.setString(2, school.getDescription());
		pstmt.setString(3, school.getRemarks());
		//执行预编译对象的executeUpdate方法，获取增加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了" + affectedRowNum + "条记录");
		//关闭pstmt对象
		pstmt.close();
		//关闭connection对象
		connection.close();
		//如果影响的行数>1,则返回ture,否则返回false
		return affectedRowNum>0;
	}

	public boolean delete(Integer id)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteSchool_sql = "DELETE FROM school WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
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

	public boolean delete(School school){
		return SchoolDao.schools.remove(school);
	}

	public static void main(String[] args)throws SQLException {
		//删
		//schoolDao.delete(2);
		//SchoolDao.getInstance().findAll();
		School school1 = SchoolDao.getInstance().find(2);
		System.out.println(school1);
		school1.setDescription("计算机学院");
		SchoolDao.getInstance().update(school1);
		School school2 = SchoolDao.getInstance().find(2);
		System.out.println(school2.getDescription());

		School school = new School(4,"外国语学院","04","");
		System.out.println(SchoolDao.getInstance().add(school));
	}
}
