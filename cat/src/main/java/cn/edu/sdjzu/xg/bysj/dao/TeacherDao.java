package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;

	public Collection<Teacher> findAll()throws SQLException{
		teachers = new TreeSet<>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建Teacher对象，根据遍历结果中的id,name,profTitle_id,degree_id,department_id值
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			Teacher teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					profTitle,degree,department);
			//向teachers集合中添加Teacher对象
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}

	public Teacher find(Integer id)throws SQLException{
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		String deleteTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,name,profTitle_id,degree_id,department_id值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			//创建Teacher对象，根据遍历结果中的id,name,profTitle_id,degree_id,department_id值
			ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					profTitle,degree,department);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}

	public boolean update(Teacher teacher)throws SQLException{
			Connection connection = JdbcHelper.getConn();
			//写sql语句
			String updateTeacher_sql = " update teacher set no=?,name=?,profTitle_id=?,degree_id=? ,department_id=? where id=?";
			//在该连接上创建预编译语句对象
			PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
			//为预编译参数赋值
		preparedStatement.setString(1,teacher.getNo());

		preparedStatement.setString(2,teacher.getName());
			preparedStatement.setInt(3,teacher.getTitle().getId());
			preparedStatement.setInt(4,teacher.getDegree().getId());
			preparedStatement.setInt(5,teacher.getDepartment().getId());
			preparedStatement.setInt(6,teacher.getId());
			//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
			int affectedRows = preparedStatement.executeUpdate();
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
			return affectedRows>0;
	}
	// 先为Teacher表增加一条记录，获得新增记录的id，然后为本方法的teacher参数赋id。
	// 然后创建一个User对象，关联已经拥有id的Teacher对象，然后将User对象保存到表中。
	public boolean add(Teacher teacher)throws SQLException{
		//声明变量
		PreparedStatement pstmt=null;
		Connection connection=null;
		Boolean affected =null;
		try{
			//获得连接对象
			connection=JdbcHelper.getConn();
			//关闭自动提交
			connection.setAutoCommit(false);
			//创建sql语句
			String addTeacher_sql = "insert into teacher(no,name,proftitle_id,degree_id,department_id) values " + "(?,?,?,?,?)";
			//在该连接上创建预编译语句对象
			pstmt = connection.prepareStatement(addTeacher_sql);
			//为预编译参数赋值
			pstmt.setString(1,teacher.getNo());
			pstmt.setString(2,teacher.getName());
			pstmt.setInt(3,teacher.getTitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());
			//执行预编译对象的executeUpdate方法，获取添加的记录行数
			int affectedRowNum=pstmt.executeUpdate();
			System.out.println("添加了"+affectedRowNum+"行");
			//执行当前连接所做的操作
			connection.commit();
			//在该连接上执行sql语句
			PreparedStatement getNewTeacher=connection.prepareStatement("select id from teacher where no=?");
			getNewTeacher.setString(1,teacher.getNo());
			//创建ResultSet对象，执行预编译语句
			ResultSet resultSet=getNewTeacher.executeQuery();
			if(resultSet.next()){
				Teacher teacher1=TeacherDao.getInstance().find(resultSet.getInt("id"));
				System.out.println(teacher1);
				//创建user对象
				User user=new User(2,teacher.getNo(),teacher.getNo(),new Date(),teacher1);
				UserDao.getInstance().add(user);
			}
			affected=affectedRowNum>0;
		}catch (Exception e){
			try{
				//回滚当前连接所做的操作
				if(connection!=null){
					//事务以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try{
				//恢复自动提交
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}
		return affected;
	}

	// 在user表中先删teacherId为当前teacher的id的user记录，然后再删teacher记录
	//如果使用UserDao中的方法增加/删除User时，需要保证TeacherDao和UserDao使用同一个Connection对象来完成事务
	public boolean delete(Integer id) throws SQLException {
		//获得连接对象
		Connection connection=null;
		PreparedStatement preparedStatement = null;
		Boolean affected=null;
		try{
			//获得连接对象
			connection=JdbcHelper.getConn();
			//关闭自动提交
			connection.setAutoCommit(false);
			//创建语句
			preparedStatement=connection.prepareStatement("SELECT * FROM user where teacher_id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				UserDao.getInstance().delete(resultSet.getInt("id"));
			}

			//创建sql语句
			String deleteTeacher_sql="DELETE FROM teacher WHERE id=?";
			//在该连接上创建预编译语句对象
			preparedStatement=connection.prepareStatement(deleteTeacher_sql);
			//为预编译参数赋值
			preparedStatement.setInt(1,id);
			//执行预编译对象的executeUpdate()方法，获取删除记录的行数
			int affectedRowNum=preparedStatement.executeUpdate();
			//执行当前连接所做的操作
			connection.commit();
			affected=affectedRowNum>0;
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
			try{
				//回滚当前连接所做的操作
				if(connection!=null){
					//事务以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try{
				//恢复自动提交
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
		}
		return affected;
	}


//	public static void main(String[] args)throws SQLException {
//		//删
//		//TeacherDao.delete(2);
//		//TeacherDao.getInstance().findAll();
//		Teacher teacher1 = TeacherDao.getInstance().find(2);
//		System.out.println(teacher1);
//		teacher1.setName("王宁");
//		TeacherDao.getInstance().update(teacher1);
//		Teacher teacher2 = TeacherDao.getInstance().find(2);
//		System.out.println(teacher2.getName());
//
//		ProfTitle profTitle = ProfTitleDao.getInstance().find(2);
//		Degree degree = DegreeDao.getInstance().find(3);
//		Department department = DepartmentDao.getInstance().find(4);
//		Teacher teacher = new Teacher("01","刘怡",profTitle,degree,department);
//		System.out.println(TeacherDao.getInstance().add(teacher));

	//}

}
