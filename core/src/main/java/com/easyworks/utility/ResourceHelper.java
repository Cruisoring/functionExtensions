package com.easyworks.utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Helper class for resource retrieval.
 */
public class ResourceHelper {

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    /**
     * Check to see if there is a relative resource identified by the resourceFilename.
     *
     * @param resourceFilename The relative path of the reourcefile to be checked.
     * @return 'True' if the relative path exists, "False" if not.
     */
    public static Boolean isResourceAvailable(String resourceFilename) {
        URL url = ResourceHelper.class.getClassLoader().getResource(resourceFilename);
        return url != null;
    }

    /**
     * Retrieve the content of the resource file a String.
     *
     * @param resourceFilename The relative path of the reourcefile to be checked.
     * @return NULL if there is no such resource identified by the relative path, or content of the resource as a String.
     */
    public static String getTextFromResourceFile(String resourceFilename) {
        URL url = ResourceHelper.class.getClassLoader().getResource(resourceFilename);
        if (url == null)
            return null;

        String sql = null;
        try {
            URI uri = url.toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(uri));
            sql = new String(bytes);
        } catch (Exception e) {
            Logger.L(e.getMessage());
        }
        return sql;
    }

    /**
     * Retrieve all properties resources as a named HashMap.
     *
     * @param resourcePackagename Relative package name containing the properties.
     * @param result              A map from the caller to be filled with the properties.
     * @return A map containing all the properties within the targeted package.
     */
    public static Map<String, Properties> getAllProperties(String resourcePackagename, Map<String, Properties> result) {
        if (result == null)
            result = new HashMap<>();
        ClassLoader classLoader = ResourceHelper.class.getClassLoader();
        String path = classLoader.getResource(resourcePackagename).getPath();
        File file = new File(path);
        if (!file.exists()) {
            Logger.L("Failed to find: %s", resourcePackagename);
            return null;
        }

        File[] propertiesFiles = new File(path).listFiles();
        for (File f : propertiesFiles) {
            if (f.isFile()) {
                addFile(f, result);
            } else if (f.isDirectory()) {
                addDirectory(f, result);
            }
        }

        return result;
    }

    /**
     * Add the properties within a directory.
     *
     * @param file   File of the target directory.
     * @param result Named dictionary to keep the retrieved properties.
     */
    protected static void addDirectory(File file, Map<String, Properties> result) {
        File[] files = new File(file.getPath()).listFiles();

        for (File f : files) {
            if (f.isFile()) {
                addFile(f, result);
            } else if (f.isDirectory()) {
                addDirectory(f, result);
            }
        }
    }

    /**
     * Add a single file as properties to the given result dictionary.
     *
     * @param file   The instance of a single properties file.
     * @param result Named dictionary to keep the retrieved properties.
     */
    protected static void addFile(File file, Map<String, Properties> result) {

        String propertiesName = file.getName();
        propertiesName = propertiesName.substring(0, propertiesName.indexOf("."));

        Properties properties = new Properties();
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            Logger.L(e.getMessage());
            return;
        }
        result.put(propertiesName, properties); //Let it throw Exception if there is duplicated keys.
    }
}