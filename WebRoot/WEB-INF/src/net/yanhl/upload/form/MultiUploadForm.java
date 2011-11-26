package net.yanhl.upload.form;

import java.util.ArrayList;
import java.util.List;

import net.yanhl.upload.UploadFile;

import org.apache.struts.action.ActionForm;

public class MultiUploadForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private List<UploadFile> myFiles;

	public MultiUploadForm() {
		myFiles = new ArrayList<UploadFile>();
		myFiles.add(new UploadFile());
	}

	public List<UploadFile> getMyFiles() {
		return myFiles;
	}

	//注意这个方法的定义  不加中间的循环是会出错的
	public UploadFile getUploadFile(int index) {
		int size = myFiles.size();
		if (index > size - 1) {
			for (int i = 0; i < index - size + 1; i++) {
				myFiles.add(new UploadFile());
			}
		}
		return (UploadFile) myFiles.get(index);
	}

	public void setMyFiles(List<UploadFile> myFiles) {
		this.myFiles = myFiles;
	}

}
