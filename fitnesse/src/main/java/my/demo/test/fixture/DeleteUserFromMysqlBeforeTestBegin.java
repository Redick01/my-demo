package my.demo.test.fixture;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import fit.ColumnFixture;
import my.demo.test.DbUtils;
import my.demo.test.Manager;

public class DeleteUserFromMysqlBeforeTestBegin extends ColumnFixture {
	private String mobile;
	public int delete() { // Returns the rows affected.
		DbUtils utils = Manager.getDbUtils();
		Connection connection = null;
		try {
			connection = utils.getUserDbConnection();
			//SQL injection risk!
			ResultSet rs = utils.execQuery(connection, "select user_id from usr_user_account where account='" + mobile + "'");
			int userId = 0;
			if(rs.next()) {
				userId = rs.getInt("user_id");
			}
			try { rs.close(); } catch (Exception e) {	}
			if(userId<=0) return 0;
			int rows = utils.executeUpdate(connection, "delete from usr_user_account where account='" + mobile + "'");
			utils.executeUpdate(connection, "delete from usr_user where user_id=" + userId);
			connection.close();
			return rows;
		} catch (ClassNotFoundException | SQLException e) {
			if(connection!=null) {
				try { connection.close(); } catch (SQLException e1) {	}
			}
			System.out.println("Delete user from MySQL: " + e.getMessage());
			return 0;
		}
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}