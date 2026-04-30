package com.acgist.ding.action.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.wst.server.core.IServer;

import com.acgist.ding.Config;
import com.acgist.ding.Dialogs;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;

/**
 * 打开Server目录
 * 
 * @author acgist
 */
public class ServerAction implements IViewActionDelegate {

	/**
	 * 选中菜单
	 */
	private IStructuredSelection selection;
	
	/**
	 * Server ID
	 */
	private static final String SERVER_ID = "server-id";
	/**
	 * 打开Server原始目录
	 */
	private static final String SOURCE = "com.acgist.ding.action.server.source";
	/**
	 * 打开Server部署目录
	 */
	private static final String DEPLOY = "com.acgist.ding.action.server.deploy";
	/**
	 * Server部署目录正则表达式
	 */
	private static final String REGEX_DEPLOY = "catalina.base\\s*=\\s*\"(.*?)\"";
	/**
	 * Server VM参数
	 */
	private static final String SERVER_VM_ARGUMENTS = "org.eclipse.jdt.launching.VM_ARGUMENTS";
	/**
	 * Server配置
	 */
	private static final String CONFIG = "org.eclipse.jst.server.tomcat.core.launchConfigurationType";
	
	@Override
	public void init(IViewPart viewPart) {
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IStructuredSelection structuredSelection) {
			this.selection = structuredSelection;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void run(IAction action) {
		final String id = action.getId();
		this.selection.forEach(element -> {
			final IServer server = (IServer) element;
			switch (id) {
			case SOURCE -> this.source(server);
			case DEPLOY -> this.deploy(server);
			default -> Dialogs.info(Internationalization.Message.FUNCTION_DISABLE);
			}
		});
	}
	
	/**
	 * 打开Server原始目录
	 * 
	 * @param server Server
	 */
	private void source(IServer server) {
		Program.launch(server.getRuntime().getLocation().toFile().getAbsolutePath());
	}
	
	/**
	 * 打开Server部署目录
	 * 
	 * @param server Server
	 */
	private void deploy(IServer server) {
		try {
			final String serverId = server.getId();
			final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			final ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(CONFIG);
			final ILaunchConfiguration[] configs = launchManager.getLaunchConfigurations(type);
			for (ILaunchConfiguration config : configs) {
				if(serverId.equals(config.getAttribute(SERVER_ID, Config.EMPTY))) {
					final String vmArguments = config.getAttribute(SERVER_VM_ARGUMENTS, Config.EMPTY);
					final String deployPath = this.serverDeployPath(vmArguments);
					if(deployPath != null) {
						Program.launch(deployPath);
					}
				}
			}
		} catch (CoreException e) {
			Exceptions.error(this.getClass(), e);
		}
	}
	
	/**
	 * 读取Server部署目录
	 * 
	 * @param vmArguments 启动参数
	 * 
	 * @return Server部署目录
	 */
	private String serverDeployPath(String vmArguments) {
		final Pattern pattern = Pattern.compile(REGEX_DEPLOY);
		final Matcher matcher = pattern.matcher(vmArguments);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
}
