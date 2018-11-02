package window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import entity.Student;
import utility.Global;
import utility.excel.CsvWriter;

public class MainWindow {
	private JFrame frame = null;
	private int width = 1300;
	private int height = 800;
	private Font globalFont = new Font("楷体", Font.PLAIN, 17);
	private Font globalTFFont = new Font("微软雅黑", Font.PLAIN, 15);
	
	private JTextField numberTrialTf = new JTextField(8);
	private JTextField numberLastTf = new JTextField();
	private JTextField scoreTf = new JTextField(10);
	private JButton entryBtn = null;
	private JButton exportBtn = this.setExportBtn();
	private TextArea logTA = new TextArea(7, 30);
	
	// 创建表格组件
	StudentTableModel stuTableModel = null;
	JTable dataTable = null;
	
	/**
	 * 设置全局字体
	 * @param font
	 */
	public void initGobalFont(Font font) {  
	    FontUIResource fontResource = new FontUIResource(font);  
	    for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {  
	        Object key = keys.nextElement();  
	        Object value = UIManager.get(key);  
	        if(value instanceof FontUIResource) {  
//	            System.out.println(key);  
	            UIManager.put(key, fontResource);  
	        }  
	    }  
	} 
	
	/**
	 * 添加分数
	 */
	public void addScore() {
		String id = numberTrialTf.getText().trim() + numberLastTf.getText().trim();
		Student stu = null;
		if(null == (stu=stuTableModel.getStudent(id))) {
			JOptionPane.showMessageDialog(frame, "学号 : " + id + "不存在", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else {
			if(!stu.getScore().equals("")) {
//				JOptionPane.showMessageDialog(frame, "学生" + stu.toString() + "已存在", "提示消息", JOptionPane.WARNING_MESSAGE);
				int reCode = JOptionPane.showConfirmDialog(frame, "学生" + stu.toString() + "已存在, 确定要替换当前分数吗？", "提示消息", JOptionPane.WARNING_MESSAGE);
				if(reCode != JOptionPane.OK_OPTION) {
					return;
				}
			}
			stu.setScore(scoreTf.getText().trim());
			int row = Integer.parseInt(stu.getNumber()) - 1;
			stuTableModel.fireTableCellUpdated(row, 3);
			numberLastTf.setText("");
			scoreTf.setText("");
			numberLastTf.requestFocusInWindow();
			logTA.append(stu.toString() + "\n");
			dataTable.clearSelection();
			dataTable.setRowSelectionInterval(row, row);
		}
	}
	
	/*
	 * setEntryBtn
	 */
	public JButton setEntryBtn() {
		entryBtn = new JButton("录入");
		entryBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addScore();
			}
		});
		
		this.entryBtn.registerKeyboardAction(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addScore();
				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		return entryBtn;
	}
	
	/**
	 * 输出所有学生
	 * @param path
	 * @throws Exception
	 */
	private int writeStus(String path) throws Exception{
		CsvWriter bw = new CsvWriter(path);
		bw.writeBOMSign();
		List<Student> stus = stuTableModel.getStus();
		int counterAvail = 0;
		for(Student stu : stus) {
			bw.writeLine(stu);
			if(!stu.getScore().equals("")) {
				counterAvail++;
			}
		}
		bw.close();
		return counterAvail;
	}
	
	/**
	 * setExportBtn
	 * @return
	 */
	public JButton setExportBtn(){
		exportBtn = new JButton("导出");
		exportBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser jfc = new JFileChooser(Global.baseDatasetPath);
				jfc.showSaveDialog(frame);
				String filePath = jfc.getSelectedFile().getPath();
				int counter = 0;
				try {
					counter = writeStus(filePath);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
				logTA.append("成功记录" + String.valueOf(counter) + "条 , 文件路径 : " + filePath + "\n");
			}
		});
		
		
		
		return exportBtn;
	}
	
	/**
	 * getTopPanel
	 * @return
	 */
	public JPanel getTopPanel() {
		JPanel topPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		topPanel.setLayout(gbl);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = gbc.CENTER  ;
		JLabel numberTriaLbl = new JLabel("学号前缀");
		topPanel.add(numberTriaLbl, gbc);
		
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		numberTrialTf.setFont(this.globalTFFont);
		topPanel.add(numberTrialTf, gbc);
		
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		JLabel numberLastLbl = new JLabel("剩余学号");
		topPanel.add(numberLastLbl, gbc);
		
		gbc.weightx = 2;
		gbc.fill = GridBagConstraints.BOTH;
		numberLastTf.setFont(this.globalTFFont);
		topPanel.add(numberLastTf, gbc);
		
		
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		JLabel scoreLbl = new JLabel("分数");
		topPanel.add(scoreLbl, gbc);
		
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		scoreTf.setFont(this.globalTFFont);
		topPanel.add(scoreTf, gbc);
		
		gbc.weightx = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		this.setEntryBtn();
		topPanel.add(this.entryBtn, gbc);
		
		gbc.weightx = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		topPanel.add(exportBtn, gbc);
		
		return topPanel;
	}
	
	/**
	 * getCenterPanel
	 * @return
	 */
	public JPanel getCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
        JTableHeader head = dataTable.getTableHeader(); // 创建表格标题对象
        head.setPreferredSize(new Dimension(head.getWidth(), 35));// 设置表头大小
        head.setFont(new Font("楷体", Font.PLAIN, 18));// 设置表格字体
        dataTable.setFont(new Font("楷体", Font.PLAIN, 18));
        dataTable.setRowHeight(18);// 设置表格行宽
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);// 以下设置表格列宽
        
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		dataTable.setDefaultRenderer(Object.class, r);
		
		JScrollPane centerScrollPanel = new JScrollPane(dataTable);
		centerPanel.add(centerScrollPanel, BorderLayout.CENTER);
		return centerPanel;
	}
	
	/**
	 * @param filePath 带有学号，姓名信息的xlsx格式的表格路径
	 * @param startRow 从1开始
	 * @param startCol 从1开始
	 * @param numStu 学生数
	 * @throws Exception
	 */
	public MainWindow(String filePath, int startRow, int startCol, int numStu) throws Exception{
		initGobalFont(globalFont);
		
		this.stuTableModel = new StudentTableModel(filePath, startRow, startCol, numStu);
		this.dataTable = new JTable(stuTableModel);
		
		this.frame = new JFrame("学生分数录入");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setLocation(dim.width/2 - width/2, dim.height/2 - height/2);
		this.frame.setSize(new Dimension(width, height));
		
		BorderLayout bLayout = new BorderLayout();
		this.frame.setLayout(bLayout);
		
		this.frame.add(this.getTopPanel(), BorderLayout.NORTH);
		this.frame.add(this.getCenterPanel(), BorderLayout.CENTER);
		this.frame.add(logTA, BorderLayout.SOUTH);
		
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * show
	 */
	public void show() {
		this.frame.setVisible(true);
	}
	
	public static void main(String[] args) throws Exception{
		new MainWindow(Global.baseDatasetPath + "dfc_score.xlsx", 10, 2, 59).show();
	}
}
