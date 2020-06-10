package minicrm.server.util;

import minicrm.common.beans.CRMAttachment;
import minicrm.common.beans.CRMCustomer;
import minicrm.common.beans.CRMProject;
import minicrm.common.beans.CRMProjectIssue;
import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AttachmentHandler {
	private static final Logger LOGGER = Logger.getLogger(AttachmentHandler.class);
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

//	private static String conversionToAcceptableFormat(String text) {
//		StringBuffer sb = new StringBuffer();
//		char[] c = text.toLowerCase().toCharArray();
//		for (int i = 0; i < c.length; i++) {
//			switch (c[i]) {
//			case 'á':
//				sb.append("a");
//				break;
//			case 'Á':
//				sb.append("A");
//				break;
//			case 'é':
//				sb.append("e");
//				break;
//			case 'É':
//				sb.append("E");
//				break;
//			case 'í':
//				sb.append("i");
//				break;
//			case 'Í':
//				sb.append("I");
//				break;
//			case 'ó':
//				sb.append("o");
//				break;
//			case 'Ó':
//				sb.append("O");
//				break;
//			case 'Õ':
//				sb.append("O");
//				break;
//			case 'ö':
//				sb.append("o");
//				break;
//			case 'Ö':
//				sb.append("O");
//				break;
//			case 'ú':
//				sb.append("u");
//				break;
//			case 'Ú':
//				sb.append("u");
//				break;
//			case 'ü':
//				sb.append("u");
//				break;
//			case 'Ü':
//				sb.append("u");
//				break;
//			case 'û':
//				sb.append("u");
//				break;
//			case ' ':
//				sb.append("_");
//				break;
//			default:
//				sb.append(c[i]);
//			}
//		}
//		String result = sb.toString().replaceAll("[^A-Za-z0-9_]", "");
//		return result;
//	}

	private static String conversionToAcceptableFormat(String text){
		return text.replaceAll("Á","A")
				.replaceAll("á","a")
				.replaceAll("É","é")
				.replaceAll("Í","I")
				.replaceAll("í","i")
				.replaceAll("Ó","O")
				.replaceAll("ó","o")
				.replaceAll("?","O")
				.replaceAll("?","o")
				.replaceAll("Ú","U")
				.replaceAll("ú","u")
				.replaceAll("?","U")
				.replaceAll("?","u");
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
			LOGGER.error(e);
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}
		LOGGER.debug("Fájl betöltése ideiglenes állományként almappába sikeres volt.");
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
			LOGGER.debug("A csatolmány betöltése sikeres volt a temp mappából.");
			// String extension = ".bin";
			// String fileType = getFileType(tempPath);
			// if (fileType != null) {
			// String ext = mimeTypes.get(fileType);
			// if (ext == null) {
			// logger.debug("A csatolmány formátuma nincs a listában");
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
			LOGGER.debug("A csatolmány mentése sikeres volt az alábbi helyre:" + fullPath + " " + relativePath);
		} catch (Exception e) {
			LOGGER.error("hiba", e);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outStream != null)
					outStream.close();
				File file = new File(properties.getProperty("tmpdir") + "/temp_" + attachment.getName());
				file.delete();
				LOGGER.debug("A temp állomány törlése sikeres volt az alábbi helyrõl: " + file.getAbsolutePath());
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		return attachment;
	}

	public static void deleteFile(String path) {
		File file = new File(properties.getProperty("attachment.basepath") + "/" + path);
		LOGGER.debug(file.delete() == true ? "A fájl sikeresen törölve lett" : "A fájl törlése nem sikerült");
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
				LOGGER.debug("A csatolmányok tömörítése .zip formátumba megtörtént.");
			}
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				LOGGER.error(e);
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
			LOGGER.debug("Sikeres letöltés");
		} catch (Exception e) {
			LOGGER.error(e);
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
			LOGGER.error(ex);
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
			LOGGER.debug("Sikeres letöltés");
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
}
