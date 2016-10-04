package ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(urlPatterns = { "/pages/photo.view" }, initParams = {
		@WebInitParam(name = "defaultFile", value = "/img/X.png"), @WebInitParam(name = "tempdir", value = "/img") })
public class PhotoServlet extends HttpServlet {
	private Map<String, File> tempFiles = new Hashtable<String, File>();
	private File tempdir = null;
	private byte[] defaultPhoto;

	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		String defaultFile = this.getInitParameter("defaultFile");
		File file = new File(application.getRealPath(defaultFile));
		defaultPhoto = new byte[(int) file.length()];
		try (FileInputStream fis = new FileInputStream(file);) {
			fis.read(defaultPhoto);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String temp = this.getInitParameter("tempdir");
		tempdir = new File(application.getRealPath(temp));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String photoid = request.getParameter("photoid");
		byte[] photo = readPhotoFromTempDir(photoid);
		if(photo == null) {
			System.out.println("no tempDir file exist");
			photo = readPhotoFromDetailTable(photoid);
			if (photo == null) {
				photo = defaultPhoto;
			} else {
				File outputFile = new File(tempdir, photoid + ".png");
				writePhotoToTempDir(outputFile, photo);
				tempFiles.put(photoid, outputFile);
			}
		}
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		out.write(photo);
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	private void writePhotoToTempDir(File outputFile, byte[] photo) {
		try(FileOutputStream fos = new FileOutputStream(outputFile);) {
			fos.write(photo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] readPhotoFromTempDir(String photoid) {
		byte[] result = null;
		if(photoid!=null && photoid.length()!=0) {
			File tempFile = tempFiles.get(photoid);
			if (tempFile != null && tempFile.exists()) {
				try (FileInputStream fis = new FileInputStream(tempFile);) {
					result = new byte[(int) tempFile.length()];
					fis.read(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private byte[] readPhotoFromDetailTable(String photoid) {
		byte[] result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/xxx");
			conn = ds.getConnection();
			stmt = conn.prepareStatement("select * from detail where photoid = ?");
			stmt.setString(1, photoid);
			rset = stmt.executeQuery();
			if (rset.next()) {
				result = rset.getBytes("photo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
