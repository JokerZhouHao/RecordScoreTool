package process;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Student;
import utility.Global;
import utility.excel.CsvWriter;
import utility.excel.ExcelReader;
import utility.io.IOUtility;
import utility.io.IterableBufferReader;

public class ScoreProcessor {
	private Map<String, Student> id2Stu = new HashMap<>();
	private List<String> ids = new ArrayList<>();
	
	public ScoreProcessor() throws Exception{
		load();
	}
	
	private void load() throws Exception{
		String path = Global.baseDatasetPath + "dfc_score.xlsx";
		ExcelReader reader = new ExcelReader(path);
		for(int row = 10; row <=68; row++) {
			Student stu = new Student();
			stu.setId(reader.get(row, 2));
			stu.setName(reader.get(row, 3));
			id2Stu.put(stu.getId(), stu);
			ids.add(stu.getId());
		}
		reader.close();
	}
	
	private void readRecordScoreFile(String path) throws Exception{
		IterableBufferReader<String> reader = IOUtility.getIBW(path);
		int counter = 0;
		int counterAvail = 0;
		Student stu = null;
		for(String line : reader) {
			counter++;
			String[] arr = line.split(Global.delimiterPoint);
			String id = "2016" + arr[0].trim();
			String score = arr[1];
			if(null == (stu = id2Stu.get(id))) {
				System.out.println("line " + counter + " 未找到");
			} else {
				if(!stu.getScore().equals("")) {
					System.out.println("line " + counter + " 已存在");
					continue;
				}
				counterAvail++;
				stu.setScore(score);
			}
		}
		reader.close();
		System.out.println("> total read line : " + counter + " , " + "avail line : " + counterAvail);
	}
	
	private void writeSortedScoreFile(String path) throws Exception{
		CsvWriter bw = new CsvWriter(path);
		bw.writeBOMSign();
		for(String id : ids) {
			bw.writeLine(id2Stu.get(id));
		}
		bw.close();
	}
	
	public void writeScoreList(String recodeScoreFile, String sortedScoreFile) throws Exception{
		System.out.println("> start write file [" + recodeScoreFile + "] to [" + sortedScoreFile + "] . . . ");
		this.readRecordScoreFile(recodeScoreFile);
		this.writeSortedScoreFile(sortedScoreFile);
		System.out.println("> Over");
	}
	
	public static void job(int jobIndex) throws Exception{
		String recodeScoreFile = Global.baseDatasetPath + String.valueOf(jobIndex) + ".txt";
		String sortedScoreFile = Global.baseDatasetPath + String.valueOf(jobIndex) + "_sorted.csv";
		new ScoreProcessor().writeScoreList(recodeScoreFile, sortedScoreFile);
	}
	
	public static void main(String[] args) throws Exception{
//		ScoreProcessor.job(1);
		ScoreProcessor.job(2);
		
	}
	
}
