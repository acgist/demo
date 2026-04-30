package com.acgist.ding;

import java.nio.file.Paths;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

/**
 * 提示框工具
 * 
 * @author acgist
 */
public final class Dialogs {

	private Dialogs() {
	}
	
	/**
	 * 提示
	 * 
	 * @param name I18N名称
	 */
	public static final void info(String name) {
		MessageDialog.openInformation(
			Display.getDefault().getActiveShell(),
			Internationalization.get(Internationalization.Message.TITLE_INFO),
			Internationalization.get(name)
		);
	}
	
	/**
	 * 选择文件
	 * 
	 * @param extensions 文件后缀：*.jpg;*.png
	 * 
	 * @return 文件路径
	 */
	public static final String file(String ... extensions) {
		final FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		fileDialog.setText(Internationalization.get(Internationalization.Message.FILE_SELECT));
		fileDialog.setFilterExtensions(extensions);
		return fileDialog.open();
	}
	
	/**
	 * 选择多个文件
	 * 
	 * @param extensions 文件后缀：*.jpg;*.png
	 * 
	 * @return 文件路径
	 */
	public static final String[] files(String ... extensions) {
		final FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN | SWT.MULTI);
		fileDialog.setText(Internationalization.get(Internationalization.Message.FILE_SELECT));
		fileDialog.setFilterExtensions(extensions);
		if(fileDialog.open() != null) {
			final String path = fileDialog.getFilterPath();
			final String[] fileNames = fileDialog.getFileNames();
			if(path != null && Arrays.isNotEmpty(fileNames)) {
				final String[] files = new String[fileNames.length];
				for (int index = 0; index < fileNames.length; index++) {
					final String fileName = fileNames[index];
					files[index] = Paths.get(path, fileName).toString();
				}
				return files;
			}
		}
		return null;
	}
	
	/**
	 * 选择目录
	 * 
	 * @return 目录
	 */
	public static final String folder() {
		final DirectoryDialog directoryDialog = new DirectoryDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
		directoryDialog.setText(Internationalization.get(Internationalization.Message.FOLDER_SELECT));
		directoryDialog.setMessage(Internationalization.get(Internationalization.Message.FOLDER_SELECT));
		return directoryDialog.open();
	}
	
}
