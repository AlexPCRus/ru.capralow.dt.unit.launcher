package ru.capralow.dt.unit.launcher.plugin.core.frameworks;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.common.util.URI;
import org.osgi.framework.Bundle;

import com._1c.g5.v8.dt.core.platform.IExtensionProject;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.metadata.mdclass.CommonModule;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

import ru.capralow.dt.unit.launcher.plugin.core.UnitTestLaunchConfigurationAttributes;
import ru.capralow.dt.unit.launcher.plugin.core.launchconfigurations.model.TestFramework;
import ru.capralow.dt.unit.launcher.plugin.core.launchconfigurations.model.ulFactory;
import ru.capralow.dt.unit.launcher.plugin.internal.core.UnitLauncherCorePlugin;

public class FrameworkUtils {
	private static final String FRAMEWORK_PLUGIN = "ru.capralow.dt.unit.launcher.plugin.core"; //$NON-NLS-1$

	public static String getConfigurationFilesPath(ILaunchConfiguration configuration) {
		Bundle bundle = getFrameworkBundle();
		IPath resourcePath = Platform.getStateLocation(bundle);
		return resourcePath + "/" + configuration.getName() + "/"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static TestFramework getConfigurationFramework(ILaunchConfiguration configuration,
			Collection<TestFramework> frameworks) throws CoreException {
		if (frameworks == null)
			return null;

		String frameworkName = configuration.getAttribute(UnitTestLaunchConfigurationAttributes.FRAMEWORK,
				(String) null);

		Iterator<TestFramework> itrFrameworks = frameworks.iterator();
		while (itrFrameworks.hasNext()) {
			TestFramework candidate = itrFrameworks.next();
			if (candidate.getName().equals(frameworkName))
				return candidate;
		}

		return null;
	}

	public static TestFramework getConfigurationFramework(String frameworkName, Collection<TestFramework> frameworks) {
		if (frameworks == null)
			return null;

		Iterator<TestFramework> itrFrameworks = frameworks.iterator();
		while (itrFrameworks.hasNext()) {
			TestFramework candidate = itrFrameworks.next();
			if (candidate.getName().equals(frameworkName))
				return candidate;
		}

		return null;
	}

	public static CommonModule getConfigurationModule(ILaunchConfiguration configuration,
			IV8ProjectManager projectManager) throws CoreException {
		IProject project = getConfigurationProject(configuration, projectManager);
		if (project == null)
			return null;

		List<CommonModule> modules = getModulesForProject(project, projectManager);
		if (modules.isEmpty())
			return null;

		String extensionModuleName = configuration
				.getAttribute(UnitTestLaunchConfigurationAttributes.EXTENSION_MODULE_TO_TEST, (String) null);

		Iterator<CommonModule> itrModules = modules.iterator();
		while (itrModules.hasNext()) {
			CommonModule candidate = itrModules.next();
			if (candidate.getName().equals(extensionModuleName))
				return candidate;
		}

		return null;
	}

	public static CommonModule getConfigurationModule(String extensionModuleName, IProject project,
			IV8ProjectManager projectManager) {
		if (project == null)
			return null;

		List<CommonModule> modules = getModulesForProject(project, projectManager);
		if (modules.isEmpty())
			return null;

		Iterator<CommonModule> itrModules = modules.iterator();
		while (itrModules.hasNext()) {
			CommonModule candidate = itrModules.next();
			if (candidate.getName().equals(extensionModuleName))
				return candidate;
		}

		return null;
	}

	public static IProject getConfigurationProject(ILaunchConfiguration configuration, IV8ProjectManager projectManager)
			throws CoreException {
		String extensionProjectName = configuration
				.getAttribute(UnitTestLaunchConfigurationAttributes.EXTENSION_PROJECT_TO_TEST, (String) null);

		Collection<IProject> projects = getExtensionProjects(projectManager);

		Iterator<IProject> itrProjects = projects.iterator();
		while (itrProjects.hasNext()) {
			IProject candidate = itrProjects.next();
			if (candidate.getName().equals(extensionProjectName))
				return candidate;
		}

		return null;
	}

	public static IProject getConfigurationProject(String extensionProjectName, IV8ProjectManager projectManager) {
		Collection<IProject> projects = getExtensionProjects(projectManager);

		Iterator<IProject> itrProjects = projects.iterator();
		while (itrProjects.hasNext()) {
			IProject candidate = itrProjects.next();
			if (candidate.getName().equals(extensionProjectName))
				return candidate;
		}

		return null;
	}

	public static String getConfigurationTag(ILaunchConfiguration configuration, IV8ProjectManager projectManager)
			throws CoreException {
		IProject project = getConfigurationProject(configuration, projectManager);
		if (project == null)
			return null;

		List<String> tags = getTagsForProject(project, projectManager);
		if (tags.isEmpty())
			return null;

		String extensionTagName = configuration
				.getAttribute(UnitTestLaunchConfigurationAttributes.EXTENSION_TAG_TO_TEST, (String) null);

		Iterator<String> itrTags = tags.iterator();
		while (itrTags.hasNext()) {
			String candidate = itrTags.next();
			if (candidate.equals(extensionTagName))
				return candidate;
		}

		return null;
	}

	public static Collection<IProject> getExtensionProjects(IV8ProjectManager projectManager) {
		return projectManager.getProjects(IExtensionProject.class).stream().map(IV8Project::getProject)
				.collect(Collectors.toList());
	}

	public static Bundle getFrameworkBundle() {
		return Platform.getBundle(FRAMEWORK_PLUGIN);
	}

	public static Collection<TestFramework> getFrameworks() {
		ArrayList<TestFramework> newFrameworks = new ArrayList<>();

		String frameworksContents = readContents(getFileInputSupplier("frameworks.txt")); //$NON-NLS-1$

		for (String frameworkString : frameworksContents.split(System.lineSeparator())) {
			String[] frameworkList = frameworkString.split("[,]"); //$NON-NLS-1$

			TestFramework framework = ulFactory.eINSTANCE.createTestFramework();
			framework.setName(frameworkList[0]);
			framework.setVersion(frameworkList[1]);
			framework.setResourcePath("frameworks" + File.separator + frameworkList[2] + File.separator); //$NON-NLS-1$
			framework.setEpfName(frameworkList[3]);

			newFrameworks.add(framework);
		}

		return newFrameworks.stream().collect(Collectors.toList());
	}

	public static List<CommonModule> getModulesForProject(IProject project, IV8ProjectManager projectManager) {
		if (project == null || projectManager == null)
			return new ArrayList<>();

		IV8Project v8Project = projectManager.getProject(project);
		if (!(v8Project instanceof IExtensionProject)) {
			String msg = MessageFormat.format(Messages.FrameworkUtils_Wrong_project_class_0, v8Project.getClass());
			throw new NullPointerException(msg);
		}

		IExtensionProject extensionProject = (IExtensionProject) v8Project;

		return extensionProject.getConfiguration().getCommonModules().stream().collect(Collectors.toList());
	}

	public static URI getResourceURIforPlugin(String fileName) {
		URI uri = URI.createPlatformResourceURI(fileName, false);

		File file = getResourceFile(uri);

		return URI.createFileURI(file.getPath());
	}

	public static List<String> getTagsForProject(IProject project, IV8ProjectManager projectManager) {
		if (project == null || projectManager == null)
			return new ArrayList<>();

		IV8Project v8Project = projectManager.getProject(project);
		if (!(v8Project instanceof IExtensionProject)) {
			String msg = MessageFormat.format(Messages.FrameworkUtils_Wrong_project_class_0, v8Project.getClass());
			throw new NullPointerException(msg);
		}

		IExtensionProject extensionProject = (IExtensionProject) v8Project;

		return new ArrayList<>();
	}

	private static CharSource getFileInputSupplier(String partName) {
		return Resources.asCharSource(
				UnitTestLaunchConfigurationAttributes.class.getResource("/frameworks/" + partName), //$NON-NLS-1$
				StandardCharsets.UTF_8);
	}

	private static File getResourceFile(URI uri) {
		String[] segments = uri.segments();
		IPath resourcePath = UnitLauncherCorePlugin.getDefault().getStateLocation();
		for (Integer i = 1; i < segments.length - 1; ++i) {
			resourcePath = resourcePath.append(segments[i]);
			File file = resourcePath.toFile();
			if (file.exists() && !file.isDirectory()) {
				try {
					Files.delete(file.toPath());

				} catch (IOException e) {
					String msg = MessageFormat.format(Messages.FrameworkUtils_Unable_to_delete_framework_file_0,
							file.toPath());
					UnitLauncherCorePlugin.log(UnitLauncherCorePlugin.createErrorStatus(msg, e));

				}
			} else if (!file.exists()) {
				file.mkdir();
			}
		}
		resourcePath = resourcePath.append(segments[segments.length - 1]);
		return resourcePath.toFile();
	}

	private static String readContents(CharSource source) {
		try (Reader reader = source.openBufferedStream()) {
			return CharStreams.toString(reader);

		} catch (IOException | NullPointerException e) {
			return ""; //$NON-NLS-1$

		}
	}

	private FrameworkUtils() {
		throw new IllegalStateException(Messages.FrameworkUtils_Internal_class);
	}
}
