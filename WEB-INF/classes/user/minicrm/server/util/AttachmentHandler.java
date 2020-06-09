package user.minicrm.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;

import user.minicrm.common.beans.CRMAttachment;
import user.minicrm.common.beans.CRMCustomer;
import user.minicrm.common.beans.CRMProject;
import user.minicrm.common.beans.CRMProjectIssue;
import user.minicrm.zk.util.RoleFactory;

public class AttachmentHandler {
	private static Logger logger = Logger.getLogger(AttachmentHandler.class);
	private static Properties properties = (Properties) SessionUtil.getAttribute("properties");
	// private static Tika tika = new Tika();
	// static final private HashMap<String, String> mimeTypes = new HashMap<String,
	// String>();
	// static {
	// mimeTypes.put("application/pdf", ".pdf");
	// mimeTypes.put("text/plain", ".txt");
	// mimeTypes.put("image/jpeg", ".jpeg");
	// mimeTypes.put("audio/mpeg3", ".mp3");
	// mimeTypes.put("application/msword", ".doc");
	// mimeTypes.put("video/avi", ".avi");
	// }

	public static String getNewDirectoryPath(CRMCustomer customer, CRMProject project, CRMProjectIssue issue) {
		StringBuilder newPath = new StringBuilder();
		newPath.append(conversionToAcceptableFormat(customer.getName())).append("/");
		newPath.append(conversionToAcceptableFormat(project.getName())).append("/");
		newPath.append(issue.getId());
		return newPath.toString();
	}

	private static String conversionToAcceptableFormat(String text) {
		StringBuffer sb = new StringBuffer();
		char[] c = text.toLowerCase().toCharArray();
		for (int i = 0; i < c.length; i++) {
			switch (c[i]) {
			case '�':
				sb.append("a");
				break;
			case '�':
				sb.append("A");
				break;
			case '�':
				sb.append("e");
				break;
			case '�':
				sb.append("E");
				break;
			case '�':
				sb.append("i");
				break;
			case '�':
				sb.append("I");
				break;
			case '�':
				sb.append("o");
				break;
			case '�':
				sb.append("O");
				break;
			case '�':
				sb.append("O");
				break;
			case '�':
				sb.append("o");
				break;
			case '�':
				sb.append("O");
				break;
			case '�':
				sb.append("u");
				break;
			case '�':
				sb.append("u");
				break;
			case '�':
				sb.append("u");
				break;
			case '�':
				sb.append("u");
				break;
			case '�':
				sb.append("u");
				break;
			case ' ':
				sb.append("_");
				break;
			default:
				sb.append(c[i]);
			}
		}
		String result = sb.toString().replaceAll("[^A-Za-z0-9_]", "");
		return result;
	}

	// private static String getFileType(String path) {
	// File file = new File(path);
	// String filetype = "";
	// try {
	// filetype = tika.detect(file);
	// } catch (Exception e) {
	// logger.error(e);
	// }
	// return filetype;
	// }

	private static String loadAttachmentToTempDirectory(byte[] data, CRMAttachment attachment) {
		OutputStream outStream = null;
		try {
			File dir = new File(properties.getProperty("tmpdir"));
			dir.mkdirs();
			File tmp = new File(dir, "/temp_" + attachment.getName());
			tmp.createNewFile();
			tmp.setExecutable(true);
			tmp.setWritable(true);
			tmp.setReadable(true);
			outStream = new FileOutputStream(tmp);
			outStream.write(data);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.debug("F�jl bet�lt�se ideiglenes �llom�nyk�nt almapp�ba sikeres volt.");
		return properties.getProperty("tmpdir") + "/temp_" + attachment.getName();
	}

	public static CRMAttachment saveAttachment(byte[] data, CRMAttachment attachment, CRMCustomer customer,
			CRMProject project, CRMProjectIssue issue) {
		String tempPath = loadAttachmentToTempDirectory(data, attachment);
		InputStream inputStream = null;
		OutputStream outStream = null;
		try {
			inputStream = new FileInputStream(new File(tempPath));
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			logger.debug("A csatolm�ny bet�lt�se sikeres volt a temp mapp�b�l.");
			// String extension = ".bin";
			// String fileType = getFileType(tempPath);
			// if (fileType != null) {
			// String ext = mimeTypes.get(fileType);
			// if (ext == null) {
			// logger.debug("A csatolm�ny form�tuma nincs a list�ban");
			// } else
			// extension = ext;
			// }
			String directoryName = getNewDirectoryPath(customer, project, issue);
			String fileName = attachment.getName();
			String relativePath = directoryName + "/" + fileName;
			String fullPath = properties.getProperty("attachment.basepath") + "/" + directoryName;
			File dir = new File(fullPath);
			dir.mkdirs();
			File targetFile = new File(dir, fileName);
			outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			attachment.setFilePath(relativePath);
			logger.debug("A csatolm�ny ment�se sikeres volt az al�bbi helyre:" + fullPath + " " + relativePath);
		} catch (Exception e) {
			logger.error("hiba", e);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outStream != null)
					outStream.close();
				File file = new File(properties.getProperty("tmpdir") + "/temp_" + attachment.getName());
				file.delete();
				logger.debug("A temp �llom�ny t�rl�se sikeres volt az al�bbi helyr�l: " + file.getAbsolutePath());
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return attachment;
	}

	public static void deleteFile(String path) {
		File file = new File(properties.getProperty("attachment.basepath") + "/" + path);
		logger.debug(file.delete() == true ? "A f�jl sikeresen t�r�lve lett" : "A f�jl t�rl�se nem siker�lt");
		File folder = file.getParentFile();
		removeDirRecursive(folder);
	}

	private static void removeDirRecursive(File folder) {
		if (folder.list().length == 0) {
			folder.delete();
			removeDirRecursive(folder.getParentFile());
		} else
			return;
	}

	public static String extractAllAttachmentsToZip(CRMProjectIssue issue) {
		FileInputStream in = null;
		ZipOutputStream out = null;
		String path = null;
		byte[] buffer = new byte[1024];
		int len;
		String userName = conversionToAcceptableFormat(AuthenticationLoginService.getActualUserName());
		try {
			path = properties.getProperty("attachment.basepath") + "/tmp_" + userName + "_" + issue.getShortName()
					+ ".zip";
			out = new ZipOutputStream(new FileOutputStream(path));
			for (CRMAttachment attachment : issue.getAttachments()) {
				in = new FileInputStream(
						properties.getProperty("attachment.basepath") + "/" + attachment.getFilePath());
				out.putNextEntry(new ZipEntry(attachment.getFilePath()));
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.closeEntry();
				logger.debug("A csatolm�nyok t�m�r�t�se .zip form�tumba megt�rt�nt.");
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return path;
	}

	public static void downloadZip(CRMProjectIssue issue) {
		String path = extractAllAttachmentsToZip(issue);
		File file = new File(path);
		AMedia zip = null;
		try {
			zip = new AMedia(file, "application/zip", null);
			zip.setContentDisposition(true);
			Filedownload.save(zip, issue.getShortName() + "_csatolmanyok");
			logger.debug("Sikeres let�lt�s");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static boolean deleteTempFiles(String userName) {
		boolean ret = false;
		try {
			File dir = new File(properties.getProperty("attachment.basepath"));
			for (File file : dir.listFiles()) {
				if (file.isFile() && file.getName().contains(conversionToAcceptableFormat(userName)))
					ret = file.delete();
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ret;
	}

	public static void downloadAttachment(CRMAttachment attachment) {
		String path = properties.getProperty("attachment.basepath") + "/" + attachment.getFilePath();
		AMedia downloadAttachment = null;
		try {
			downloadAttachment = new AMedia(new File(path), null, null);
			downloadAttachment.setContentDisposition(true);
			Filedownload.save(downloadAttachment, attachment.getName());
			logger.debug("Sikeres let�lt�s");
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
