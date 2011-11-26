package test;

import java.io.File;

public class ClassPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String lib = "D:/runchain/projects/exercise/WebRoot/WEB-INF/lib";
		File file = new File(lib);
		File[] listFiles = file.listFiles();
		StringBuffer sb = new StringBuffer();
		for (File file2 : listFiles) {
			sb.append(lib + "/" + file2.getName() + ";");
		}
		System.out.println(sb.toString());
	}

}
