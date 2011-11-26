package net.yanhl.upload;

import java.io.Serializable;

import org.apache.struts.upload.FormFile;

public class UploadFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private FormFile file;

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

}