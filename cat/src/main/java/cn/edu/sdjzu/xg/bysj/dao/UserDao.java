package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import util.JdbcHelper;

import java.sql.*;
import java.util.*;
import java.util.Date;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;

	public Collection<User> findAll()throws SQLException {
			users = new TreeSet<>();
			//获得连接对象
			Connection connection = JdbcHelper.getConn();
			Statement statement = connection.createStatement();
			//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
			ResultSet resultSet = statement.executeQuery("select * from user");
			//若结果集仍然有下一条记录，则执行循环体
			while (resultSet.next()){
				//创建User对象，根据遍历结果中的id,description,no,remarks值
				User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),new Date(),TeacherDao.getInstance().find(2));
				//向users集合中添加User对象
				users.add(user);
			}
			//关闭资源
			JdbcHelper.close(resultSet,statement,connection);
			return users;
		}
	
	public User find(Integer id)throws SQLException{
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,username,password值为参数，User
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					new Date(),
					TeacherDao.getInstance().find(2));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
		}

	public User findByUsername(String username) throws SQLException {
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String selectUser_sql = "SELECT * FROM user WHERE username=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,username);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					new Date(),
					TeacherDao.getInstance().find(2));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}
	
	public boolean update(User user)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateUser_sql = " update user set username=?,password=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,user.getUsername());
		preparedStatement.setString(2,user.getPassword());
		preparedStatement.setInt(3,user.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	
	public boolean add(User user)throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql对象
		String addUser_sql = "INSERT INTO user (username,password,teacher_id) VALUES" + " (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
		//为预编译参数赋值
		pstmt.setString(1, user.getPassword());
		pstmt.setString(2, user.getUsername());
		pstmt.setInt(3,user.getTeacher().getId());
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
		String deleteUser_sql = "DELETE FROM USER WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
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

	public boolean delete(User user){
		return users.remove(user);
	}



	public User login(String username,String password) throws SQLException{
	    User user =null;
        Connection connection = JdbcHelper.getConn();
        String findByUsername_sql = "SELECT * FROM user WHERE username=? and password=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findByUsername_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
            resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id"))
                    );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}

	public static void main(String[] args)throws SQLException{
//		UserDao dao = new UserDao();
//		Collection<User> users = dao.findAll();
//		display(users);
		//测试增加
//		Teacher teacher = TeacherDao.getInstance().find(3);
//		User user = new User(1,"wrt","123",new Date(),teacher);
//		System.out.println(UserDao.getInstance().add(user));
		//测试修改
//		User user1 = UserDao.getInstance().find(2);
//		System.out.println(user1);
//		user1.setPassword("123");
//		UserDao.getInstance().update(user1);
//		System.out.println(user1);
		//测试删除
		//测试查找
//		User user2 = UserDao.getInstance().find(2);
//		System.out.println("按照id查询find"+user2);
//		User user3 = UserDao.getInstance().findByUsername("wrt");
//		System.out.println("按照用户名查找"+user3);
		//测试登录
		User user4 = UserDao.getInstance().login("wrt","wrt");
		System.out.println("这是错误的账号密码登录"+user4);
		User user5 = UserDao.getInstance().login("wrt","123");
		System.out.println("这是正确的账号密码登录"+user5);

	}
}
