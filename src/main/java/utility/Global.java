package utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import utility.io.IOUtility;

/**
 * 
 * @author ZhouHao
 * provide some global variables
 * 2018/10/24
 */
public class Global {
	
	private static Properties configProps = null;
	
	public static String projectName = "RecordScoreTool";
	public static String basePath = null;
	public static String baseDatasetPath = null;
	
	/* file content delimiter sign */
	public static String delimiterLevel1 = ": ";
	public static String delimiterLevel2 = ",";
	public static String delimiterSpace = " ";
	public static String delimiterPound = "#";
	public static String delimiterCsv = ",";
	public static String delimiterPoint = "\\.";
	
	// initBasePath
	public static void initBasePath() throws Exception{
		basePath = getBasePath();
		IOUtility.existsOrThrowsException(basePath);
		
		baseDatasetPath = getBaseDatasetPath();
		IOUtility.existsOrThrowsException(baseDatasetPath);
	}
	
	// getBasePath
	public static String getBasePath() {
		if(null==basePath) {
			basePath = Global.class.getResource("").getPath();
			basePath = basePath.substring(1, basePath.indexOf(projectName) + projectName.length() + 1).replace("/", File.separator);
			if(File.separator.equals("/")) basePath = File.separator + basePath;
		}
		return basePath;
	}
	
	// getBaseDatasetPath
	public static String getBaseDatasetPath() {
		if(null == baseDatasetPath) {
			baseDatasetPath = Global.getBasePath() + "data" + File.separator;
		}
		return baseDatasetPath;
	}
	
	// setAllPathsForDataset
	public static void setAllPaths() throws Exception{
		
	}
	
	public static void display() {
		System.out.println("--------------------------- base path --------------------------");
		System.out.println("projectName : " + Global.projectName);
		System.out.println("basePath : " + Global.basePath);
		System.out.println("baseDatasetPath : " + Global.baseDatasetPath);
	}
	
	static {
		try {
			// set paths
			initBasePath();
			setAllPaths();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws Exception{
		Global.display();
	}
	
}
