package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;

	public Collection<ProfTitle> findAll()throws SQLException{
		Set<ProfTitle>profTitles = new TreeSet<>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from profTitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建ProfTitle对象，根据遍历结果中的id,description,no,remarks值
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向profTitles集合中添加ProfTitle对象
			profTitles.add(profTitle);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id)throws SQLException{
		ProfTitle profTitle = null;
		Connection connection = JdbcHelper.getConn();
		String deleteProfTitle_sql = "SELECT * FROM proftitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteProfTitle_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建ProfTitle对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle)throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql对象
		String updateDepartment_sql = "UPDATE profTitle set description =? , no=?,  remarks=? WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
		//为预编译参数赋值
		pstmt.setString(1,profTitle.getDescription());
		pstmt.setString(2,profTitle.getNo());
		pstmt.setString(3,profTitle.getRemarks());
		pstmt.setInt(4,profTitle.getId());
		//执行预编译对象的executeUpdate方法，获取增加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		//关闭pstmt对象
		pstmt.close();
		//关闭connection对象
		connection.close();
		//如果影响的行数>1,则返回ture,否则返回false
		return affectedRowNum>0;

	}

	public boolean add(ProfTitle profTitle)throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql对象
		String addProfTitle_sql = "INSERT INTO profTitle (description,no,remarks) VALUES" + " (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addProfTitle_sql);
		//为预编译参数赋值
		pstmt.setString(1,profTitle.getDescription() );
		pstmt.setString(2,profTitle.getNo() );
		pstmt.setString(3,profTitle.getRemarks() );
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
		String deleteProfTitle_sql = "DELETE FROM profTitle WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
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


	public static void main(String[] args)throws SQLException {
		//删
		//ProfTitleDao.delete(2);
		//ProfTitleDao.getInstance().findAll();
		ProfTitle profTitle1 = ProfTitleDao.getInstance().find(2);
		System.out.println(profTitle1);
		profTitle1.setDescription("副教授");
		ProfTitleDao.getInstance().update(profTitle1);
		ProfTitle profTitle2 = ProfTitleDao.getInstance().find(2);
		System.out.println(profTitle2.getDescription());

		ProfTitle profTitle = new ProfTitle(5,"教授","05","");
		System.out.println(ProfTitleDao.getInstance().add(profTitle));

	}
}

