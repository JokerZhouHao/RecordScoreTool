package utility.excel;

import java.io.BufferedWriter;

import entity.Student;
import utility.io.IOUtility;

public class CsvWriter {
	private BufferedWriter bw = null;
	
	public CsvWriter(String path) throws Exception{
		this.bw = IOUtility.getBW(path);
	}
	
	public void writeBOMSign() throws Exception{
		this.bw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
	}
	
	public void writeLine(Student stu) throws Exception{
		this.bw.write(stu.toString());
		this.bw.write("\n");
	}
	
	public void writeLine(String line) throws Exception{
		this.bw.write(line);
		this.bw.write("\n");
	}
	
	public void close() throws Exception{
		this.bw.close();
	}
	
}
