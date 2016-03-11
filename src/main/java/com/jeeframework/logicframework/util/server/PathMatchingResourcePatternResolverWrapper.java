package com.jeeframework.logicframework.util.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PathMatchingResourcePatternResolverWrapper extends
		PathMatchingResourcePatternResolver {

	private static final Log logger = LogFactory
			.getLog(PathMatchingResourcePatternResolverWrapper.class);

	private static Method equinoxResolveMethod;

	static {
		// Detect Equinox OSGi (e.g. on WebSphere 6.1)
		try {
			Class fileLocatorClass = PathMatchingResourcePatternResolverWrapper.class
					.getClassLoader().loadClass(
							"org.eclipse.core.runtime.FileLocator");
			equinoxResolveMethod = fileLocatorClass.getMethod("resolve",
					new Class[] { URL.class });
			logger
					.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
		} catch (Throwable ex) {
			equinoxResolveMethod = null;
		}
	}

	protected Resource[] findPathMatchingResources(String locationPattern)
			throws IOException {
		String rootDirPath = determineRootDir(locationPattern);
		String subPattern = locationPattern.substring(rootDirPath.length());
		Resource[] rootDirResources = getResources(rootDirPath);

		Set result = new LinkedHashSet(16);
		for (int i = 0; i < rootDirResources.length; i++) {
			Resource rootDirResource = resolveRootDirResource(rootDirResources[i]);
			if (isJarResource(rootDirResource)) {
				result.addAll(doFindPathMatchingJarResources(rootDirResource,
						subPattern));
			} else {
				// File rootDir = rootDirResource.getFile();
				// File[] jarResources = null;
				// if (rootDir.isDirectory()) {
				// jarResources = rootDir.listFiles(new FilenameFilter() {
				// public boolean accept(File dir, String fname) {
				// return (fname.toLowerCase().endsWith(".jar"));
				//
				// }
				// });
				// if (jarResources != null) {
				// for (File jarResource : jarResources) {
				// result.addAll(doFindPathMatchingJarResources(
				// new FileSystemResource(jarResource),
				// subPattern));
				// }
				// }
				//
				// }

				result.addAll(doFindPathMatchingFileResources(rootDirResource,
						subPattern));
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Resolved location pattern [" + locationPattern
					+ "] to resources " + result);
		}
		return (Resource[]) result.toArray(new Resource[result.size()]);
	}

	public String getRelativeResourcesPath(Resource locationResource)
			throws IOException {
		Resource rootDirResource = resolveRootDirResource(locationResource);
		if (isJarResource(rootDirResource)) {
			URLConnection con = rootDirResource.getURL().openConnection();
			JarFile jarFile = null;
			String jarFileUrl = null;
			String rootEntryPath = null;
			boolean newJarFile = false;
			try {
				if (con instanceof JarURLConnection) {
					// Should usually be the case for traditional JAR files.
					JarURLConnection jarCon = (JarURLConnection) con;
					jarCon.setUseCaches(false);
					jarFile = jarCon.getJarFile();
					jarFileUrl = jarCon.getJarFileURL().toExternalForm();
					JarEntry jarEntry = jarCon.getJarEntry();
					rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
				} else {
					// No JarURLConnection -> need to resort to URL file
					// parsing.
					// We'll assume URLs of the format "jar:path!/entry", with
					// the protocol
					// being arbitrary as long as following the entry format.
					// We'll also handle paths with and without leading "file:"
					// prefix.
					String urlFile = rootDirResource.getURL().getFile();
					int separatorIndex = urlFile
							.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
					if (separatorIndex != -1) {
						jarFileUrl = urlFile.substring(0, separatorIndex);
						rootEntryPath = urlFile.substring(separatorIndex
								+ ResourceUtils.JAR_URL_SEPARATOR.length());
						jarFile = getJarFile(jarFileUrl);
					} else {
						jarFile = new JarFile(urlFile);
						jarFileUrl = urlFile;
						rootEntryPath = "";
					}
					newJarFile = true;
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Looking for matching resources in jar file ["
							+ jarFileUrl + "]");
				}
				//ȥ�����һ��б�ߣ�����ƥ��
				if (!"".equals(rootEntryPath) && rootEntryPath.endsWith("/")) {
					// Root entry path must end with slash to allow for proper
					// matching.
					// The Sun JRE does not return a slash here, but BEA JRockit
					// does.
					rootEntryPath = rootEntryPath.substring(0, rootEntryPath
							.length() - 1);
				}

			} finally {
				// Close jar file, but only if freshly obtained -
				// not from JarURLConnection, which might cache the file
				// reference.
				if (newJarFile) {
					jarFile.close();
				}
			}
			rootEntryPath = StringUtils.replace(rootEntryPath, "/", ".");
			return rootEntryPath;
		} else {
			String rootDirPath = "";
			PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
			Resource[] rootDirResources = ((ResourcePatternResolver) resourceLoader)
					.getResources("");

			if (rootDirResources != null) {
				for (Resource dirResource : rootDirResources) {
					rootDirPath = dirResource.getFile().getAbsolutePath();

					if (locationResource.getFile().getAbsolutePath().indexOf(
							rootDirPath) != -1) {
						rootDirPath = locationResource.getFile()
								.getAbsolutePath().substring(
										rootDirPath.length());
						// .substring(rootDirPath.length())
					}
					// rootDirPath = StringUtils.replace(
					// rootDir.getAbsolutePath(), File.separator, "/");
					if (!"".equals(rootDirPath)) {
						// Root entry path must end with slash to allow for
						// proper
						// matching.
						// The Sun JRE does not return a slash here, but BEA
						// JRockit
						// does.

						if (rootDirPath.endsWith(File.separator)) {
							rootDirPath = rootDirPath.substring(0, rootDirPath
									.length() - 1);
						}
						if(rootDirPath.startsWith(File.separator))
						{
							rootDirPath = rootDirPath.substring(1);
						}
						

					}
					rootDirPath = StringUtils.replace(rootDirPath,File.separator, ".");
					return rootDirPath;
				}
			}
		}

		return null;

	}

	public Resource[] doFindPathMatchingJarResourcesInDir(String dirLocation,
			String pattern) throws IOException {

		Set result = new LinkedHashSet(16);

		File rootDir = new File(dirLocation);
		File[] jarResources = null;
		if (rootDir.isDirectory()) {
			jarResources = rootDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String fname) {
					return (fname.toLowerCase().endsWith(".jar"));

				}
			});
			if (jarResources != null) {
				for (File jarResource : jarResources) {
					result.addAll(doFindPathMatchingJarResources(
							new FileSystemResource(jarResource), pattern));
				}
			}

		}

		return (Resource[]) result.toArray(new Resource[result.size()]);
	}

	protected Resource resolveRootDirResource(Resource original)
			throws IOException {
		if (equinoxResolveMethod != null) {
			URL url = original.getURL();
			if (url.getProtocol().startsWith("bundle")) {
				return new UrlResource((URL) ReflectionUtils.invokeMethod(
						equinoxResolveMethod, null, new Object[] { url }));
			}
		}
		return original;
	}

	protected Set doFindPathMatchingJarResources(Resource rootDirResource,
			String subPattern) throws IOException {
		URLConnection con = rootDirResource.getURL().openConnection();
		JarFile jarFile = null;
		String jarFileUrl = null;
		String rootEntryPath = null;
		boolean newJarFile = false;

		if (con instanceof JarURLConnection) {
			// Should usually be the case for traditional JAR files.
			JarURLConnection jarCon = (JarURLConnection) con;
			jarCon.setUseCaches(false);
			jarFile = jarCon.getJarFile();
			jarFileUrl = jarCon.getJarFileURL().toExternalForm();
			JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
		} else {
			// No JarURLConnection -> need to resort to URL file parsing.
			// We'll assume URLs of the format "jar:path!/entry", with the
			// protocol
			// being arbitrary as long as following the entry format.
			// We'll also handle paths with and without leading "file:" prefix.
			String urlFile = rootDirResource.getURL().getFile();
			int separatorIndex = urlFile
					.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
			if (separatorIndex != -1) {
				jarFileUrl = urlFile.substring(0, separatorIndex);
				rootEntryPath = urlFile.substring(separatorIndex
						+ ResourceUtils.JAR_URL_SEPARATOR.length());
				jarFile = getJarFile(jarFileUrl);
			} else {
				jarFile = new JarFile(urlFile);
				jarFileUrl = urlFile;
				rootEntryPath = "";
			}
			newJarFile = true;
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Looking for matching resources in jar file ["
						+ jarFileUrl + "]");
			}
			if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
				// Root entry path must end with slash to allow for proper
				// matching.
				// The Sun JRE does not return a slash here, but BEA JRockit
				// does.
				rootEntryPath = rootEntryPath + "/";
			}
			Set result = new LinkedHashSet(8);
			for (Enumeration entries = jarFile.entries(); entries
					.hasMoreElements();) {
				JarEntry entry = (JarEntry) entries.nextElement();
				String entryPath = entry.getName();
				if (entryPath.startsWith(rootEntryPath)) {
					String relativePath = entryPath.substring(rootEntryPath
							.length());
					// add by lanceyan ���entry ��Ŀ¼��ȥ��url�����һ��б�ߣ�����ƥ��ͨ�����һ���������ð�Ϊ
					// com.linktong.*.web.action�����������һ��б��
					if (entry.isDirectory() && relativePath.endsWith("/")) {
						relativePath = relativePath.substring(0, relativePath
								.length() - 1);
					}
					if (getPathMatcher().match(subPattern, relativePath)) {
						result
								.add(rootDirResource
										.createRelative(relativePath));
					}
				}
			}
			return result;
		} finally {
			// Close jar file, but only if freshly obtained -
			// not from JarURLConnection, which might cache the file reference.
			if (newJarFile) {
				jarFile.close();
			}
		}
	}
	
	
	public static String[] getAllResource(String resource) {
		String[] file_resource = null;
		PathMatchingResourcePatternResolverWrapper resourceLoader = new PathMatchingResourcePatternResolverWrapper();
		try {
			// ��ȡ��·��  modify bylanceyan  �����ӻ�ȡjar����classpath·���ķ���
			Resource[] actionResources = ((ResourcePatternResolver) resourceLoader)
					.getResources("classpath*:" + resource);



			if (actionResources != null) {
				file_resource = new String[actionResources.length];
				for (int i = 0; i < actionResources.length; i++) {
					file_resource[i]  = resourceLoader.getRelativeResourcesPath(actionResources[i]);
					
				}
			}
		} catch (IOException e) {
			logger.debug("Loaded action configuration from:getAllResource()");
		}
		return file_resource;
	}
}
