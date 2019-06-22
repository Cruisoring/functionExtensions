package io.github.cruisoring.utility;

import io.github.cruisoring.logger.Logger;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static io.github.cruisoring.Asserts.assertAllNotNull;
import static io.github.cruisoring.Asserts.checkNotNull;

/**
 * Helper class for resource locating and retrieval.
 */
public class ResourceHelper {
    public final static String[] resourcePaths;
    public static String MAVEN_TARGET = "target";
    public static String MAVEN_TARGET_CLASSES = "target/classes/";
    public static String MAVEN_TARGET_TEST_CLASSES = "target/test-classes/";
    public static String MAVEN_MAIN_RESOURCES = "src/main/resources/";
    public static String MAVEN_TEST_RESOURCES = "src/test/resources/";

    static {
        resourcePaths = getResourcePaths("sun.reflect", "java.lang");
    }

    /**
     * Retrive the ORIGINAL resources folders of all modules involved with the call
     *
     * @param ignoreables keywords that shall be neglected.
     * @return String array identifying the absolute paths of related resource folders
     */
    private static String[] getResourcePaths(String... ignoreables) {
        List<String> classPaths = new PlainList<>();
        List<String> classNames = StackTraceHelper.getFilteredCallers(ignoreables);
        for (int i = 0; i < classNames.size(); i++) {
            try {
                String className = classNames.get(i);
                Class clazz = Class.forName(className);
                String classPath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();

                if (classPath.endsWith(MAVEN_TARGET_CLASSES)) {
                    classPath = classPath.replace(MAVEN_TARGET_CLASSES, MAVEN_MAIN_RESOURCES);
                } else if (classPath.endsWith(MAVEN_TARGET_TEST_CLASSES)) {
                    classPath = classPath.replace(MAVEN_TARGET_TEST_CLASSES, MAVEN_TEST_RESOURCES);
                } else {
                    continue;
                }

                if (!classPaths.contains(classPath)) {
                    classPaths.add(classPath);
                }
            } catch (Exception ex) {
                continue;
            }
        }
        Collections.reverse(classPaths);
        String[] result = classPaths.toArray(new String[0]);
        return result;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws Exception ClassNotFoundException or IOException that could be thrown
     */
    public static Class[] getClasses(String packageName)
            throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new PlainList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        PlainList<Class> classes = new PlainList<Class>();
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
        List<Class> classes = new PlainList<Class>();
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
     * @param resourceFilename The absolute path to the text file, or relative path of the reourcefile to be checked.
     * @return NULL if there is no such resource identified by the relative path, or content of the resource as a String.
     */
    public static String getTextFromResourceFile(String resourceFilename) {
        checkNotNull(resourceFilename, "no resourceFilename defined.");

        String text = null;
        try {
            Path path = Paths.get(resourceFilename);
            if(path.isAbsolute()){
                byte[] bytes = Files.readAllBytes(path);
                return new String(bytes);
            }

            URL url = ResourceHelper.class.getClassLoader().getResource(resourceFilename);
            if (url == null)
                return null;

            URI uri = url.toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(uri));
            text = new String(bytes);
        } catch (Exception e) {
            Logger.W(e.getMessage());
        }
        return text;
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
            Logger.W("Failed to find: %s", resourcePackagename);
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
            Logger.W(e.getMessage());
            return;
        }
        result.put(propertiesName, properties); //Let it throw Exception if there is duplicated keys.
    }

    /**
     * Retrieve the solic file from any possible module.
     *
     * @param filename    Name of the file to be handled.
     * @param folderNames Directory names of the file.
     * @return File instance if it exist, otherwise null.
     */
    public static File getResourceFile(String filename, String... folderNames) {
        assertAllNotNull(filename);

        String folderPath = folderNames == null ? "" : String.join("/", folderNames);

        //Try to load properties if it is not loaded by previous resourcePaths.
        for (String path : resourcePaths) {
            File resourceFolder = new File(path, folderPath);
            File file = new File(resourceFolder, filename);
            //No such resources defined, continue
            if (!file.exists()) {
                continue;
            }

            return file;
        }

        return null;
    }

    /**
     * Locate the resource identified with filename and its folder names from any possible module.
     *
     * @param filename    Name of the file to be handled.
     * @param folderNames Directory names of the file.
     * @return the absolute file path if it is found, or null when there is no such resource.
     */
    public static Path getResourcePath(String filename, String... folderNames) {
        assertAllNotNull(filename);

        String folderPath = folderNames == null ? "" : String.join("/", folderNames);

        //Try to load properties if it is not loaded by previous resourcePaths.
        for (String path : resourcePaths) {
            File resourceFolder = new File(path, folderPath);
            File file = new File(resourceFolder, filename);
            //No such resources defined, continue
            if (!file.exists()) {
                continue;
            }

            return file.toPath();
        }

        String error = String.format("Failed to locate %s in folder of %s from %s", filename, folderPath, String.join(",", resourcePaths));
        throw new RuntimeException(error);
    }

    /**
     * Retrieve the absolute file path from any possible module.
     *
     * @param filename    Name of the file to be handled.
     * @param folderNames Directory names of the file.
     * @return Path of the expected file.
     */
    public static Path getAbsoluteFilePath(String filename, String... folderNames) {
        assertAllNotNull(filename);

        String folderPath = folderNames == null ? "" : String.join("/", folderNames);

        //Output Folder in the original caller module target directory
        File folder = new File(resourcePaths[0], folderPath);

        File file = new File(folder, filename);
        return file.toPath();
    }

    /**
     * Retrieve the content of the resource file a String.
     *
     * @param resourceFilename The relative path of the reourcefile to be checked.
     * @param folders          Optional folder names.
     * @return NULL if there is no such resource identified by the relative path, or content of the resource as a String.
     */
    public static String getTextFromResourceFile(String resourceFilename, String... folders) {
        Path path = getResourcePath(resourceFilename, folders);

        if (path == null) {
            return null;
        }

        try {
            byte[] encoded = Files.readAllBytes(path);
            String text = new String(encoded, Charset.defaultCharset());
            return text;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Save the text to a file with Charset of UTF-8.
     * @param text      the content to be saved.
     * @param filepath  the absolute file path or relative file path to <tt>target</tt> folder.
     * @return          the absolute path of the file if saved successfully, otherwise the error message.
     */
    public static String saveTextToTargetFile(String text, String filepath) {
        assertAllNotNull(text, filepath);

        try {
            File savedFile;
            if(Paths.get(filepath).isAbsolute()) {
                savedFile = new File(filepath);
            } else {
                filepath = filepath.startsWith("target") ? filepath : "target\\" + filepath;
                savedFile = new File(filepath).getAbsoluteFile();
                File directory = savedFile.getParentFile();
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }

            Files.write(savedFile.toPath(), text.getBytes(StandardCharsets.UTF_8));
            return savedFile.getAbsolutePath();
        } catch (IOException e) {
            String error = e.toString();
            return error;
        }
    }

    /**
     * Save the key-value-pairs of the given {@code Map<?, ?>} instances to a given Properties
     * or otherwise new instance.
     * @param properties    an existing {@code Properties} instance, or a new one enumerating in alphabetical key order would be created
     * @param maps   Maps to be saved into the Properties.
     * @return      Properties containing all the key value pairs of the given map.
     */
    public static Properties asProperties(Properties properties, Map<?, ?>... maps) {
        if(properties == null) {
            //Would creeate a Properties sorted by the keys
            properties = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
        }

        if(maps == null) {
            return properties;
        }

        for (Map map : maps) {
            properties.putAll(map);
        }
        return properties;
    }
    /**
     * Save the given Properties to a specific .properties file.
     * @param properties    the Properties instance to be saved
     * @param filePath      the absolute file path denoting the target .properties file.
     * @return              <tt>null</tt> if saving failed, otherwise the absolute path of the saved file.
     */
    public static String saveProperties(Properties properties, String filePath) {
        assertAllNotNull(properties, filePath);

        return saveProperties(properties, new File(filePath));
    }

    /**
     * Save the given Properties to a specific .properties file.
     * @param properties    the Properties instance to be saved
     * @param file          the {@code File} instance denoting the target .properties file.
     * @return              <tt>null</tt> if saving failed, otherwise the absolute path of the saved file.
     */
    public static String saveProperties(Properties properties, File file){
        assertAllNotNull(properties, file);

        FileOutputStream fr = null;
        try {
            fr = new FileOutputStream(file);
            try {
                properties.store(fr, null);
                fr.close();
                Logger.D("Properties with %d values is saved as %s", properties.size(), file);
                return file.getAbsolutePath();
            } catch (IOException e) {
                Logger.W("failed to save to %s: %s", file, e.getMessage());
                return null;
            }
        } catch (FileNotFoundException e) {
            Logger.W("failed to locate the file: %s", file);
            return null;
        }
    }

}