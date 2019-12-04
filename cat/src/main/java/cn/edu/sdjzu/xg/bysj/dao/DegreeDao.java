package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DegreeDao {
	private static DegreeDao degreeDao =
			new DegreeDao();

	private DegreeDao() {
	}

	public static DegreeDao getInstance() {
		return degreeDao;
	}

    private static Collection<Degree> degrees;

	public Collection<Degree> findAll() throws SQLException {
		degrees = new TreeSet<>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from degree");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向degrees集合中添加Degree对象
			degrees.add(degree);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return degrees;
	}

	public Degree find(Integer id)throws SQLException {
		Degree degree = null;
		Connection connection = JdbcHelper.getConn();
		String deleteDegree_sql = "SELECT * FROM degree WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return degree;
	}

	public boolean update(Degree degree)throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update degree set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add(Degree degree) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql对象
		String addDegree_sql = "INSERT INTO degree (description,no,remarks) VALUES" + " (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1, degree.getDescription());
        pstmt.setString(2, degree.getNo());
		pstmt.setString(3, degree.getRemarks());
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
		String deleteDegree_sql = "DELETE FROM DEGREE WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
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
    public boolean delete(Degree degree){
	    return degrees.remove(degree);
	}

	public static void main(String[] args)throws SQLException {
		//删
		//degreeDao.delete(2);
		//DegreeDao.getInstance().findAll();
		Degree degree1 = DegreeDao.getInstance().find(2);
		System.out.println(degree1);
		degree1.setDescription("硕士");
		DegreeDao.getInstance().update(degree1);
		Degree degree2 = DegreeDao.getInstance().find(2);
		System.out.println(degree2.getDescription());

		Degree degree = new Degree(4,"博士","04","");
		System.out.println(DegreeDao.getInstance().add(degree));
	}

}

