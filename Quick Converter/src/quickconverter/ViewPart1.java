package quickconverter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import quickconverter.ASCII;
import quickconverter.Unicode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ViewPart1 extends ViewPart {
	
	
	// members
	final int int8_max=127;
	final int int8_min=-128;
	final int int16_max=32767;
	final int int16_min=-32768;
	final int int32_max=2147483647;
	final int int32_min=-2147483648;
	final int uint8_max=255;
	final int uint16_max=65535;
	final long uint32_max=4294967295L;

	boolean sign=false;
	int size=32;
	
	final char[] EXTENDED = { 
			0x00C7, 0x00FC, 0x00E9, 0x00E2, 0x00E4, 0x00E0, 0x00E5, 0x00E7,
	        0x00EA, 0x00EB, 0x00E8, 0x00EF, 0x00EE, 0x00EC, 0x00C4, 0x00C5,
	        0x00C9, 0x00E6, 0x00C6, 0x00F4, 0x00F6, 0x00F2, 0x00FB, 0x00F9,
	        0x00FF, 0x00D6, 0x00DC, 0x00A2, 0x00A3, 0x00A5, 0x20A7, 0x0192,
	        0x00E1, 0x00ED, 0x00F3, 0x00FA, 0x00F1, 0x00D1, 0x00AA, 0x00BA, 
	        0x00BF, 0x2310, 0x00AC, 0x00BD, 0x00BC, 0x00A1, 0x00AB, 0x00BB, 
	        0x2591, 0x2592, 0x2593, 0x2502, 0x2524, 0x2561, 0x2562, 0x2556,
	        0x2555, 0x2563, 0x2551, 0x2557, 0x255D, 0x255C, 0x255B, 0x2510, 
	        0x2514, 0x2534, 0x252C, 0x251C, 0x2500, 0x253C, 0x255E, 0x255F, 
	        0x255A, 0x2554, 0x2569, 0x2566, 0x2560, 0x2550, 0x256C, 0x2567, 
	        0x2568, 0x2564, 0x2565, 0x2559, 0x2558, 0x2552, 0x2553, 0x256B, 
	        0x256A, 0x2518, 0x250C, 0x2588, 0x2584, 0x258C, 0x2590, 0x2580, 
	        0x03B1, 0x00DF, 0x0393, 0x03C0, 0x03A3, 0x03C3, 0x00B5, 0x03C4,
	        0x03A6, 0x0398, 0x03A9, 0x03B4, 0x221E, 0x03C6, 0x03B5, 0x2229,
	        0x2261, 0x00B1, 0x2265, 0x2264, 0x2320, 0x2321, 0x00F7, 0x2248, 
	        0x00B0, 0x2219, 0x00B7, 0x221A, 0x207F, 0x00B2, 0x25A0, 0x00A0 };
	
	
	
	Action action_ASCII;
	Action action_Uni;
	StringBuilder sb = new StringBuilder();
	ImageDescriptor Action_A_Icon = AbstractUIPlugin.imageDescriptorFromPlugin("QuickConverter", "/icons/ASCII_red_16x16.gif");
	ImageDescriptor Action_U_Icon = AbstractUIPlugin.imageDescriptorFromPlugin("QuickConverter", "/icons/Uni_red_16x16.gif");
	Color DARKGREEN =new Color(9,114,70);
	
	Font borderfont = new Font("Tahoma", Font.PLAIN, 11);
	Panel panel = new Panel();
	JRootPane rootPane  = new JRootPane();
	JRadioButton rdbtn_8  = new JRadioButton("8 bits");
	JRadioButton rdbtn_16  = new JRadioButton("16 bits");
	JRadioButton rdbtn_32  = new JRadioButton("32 bits");
	JRadioButton rdbtn_Signed  = new JRadioButton("Signed");
	JRadioButton rdbtn_Unsigned = new JRadioButton("Unsigned");
	JPanel panel_size  = new JPanel();
	JPanel panel_sign  = new JPanel();
	JPanel panel_dec   = new JPanel();
	JPanel panel_hex   = new JPanel();
	JPanel panel_bin   = new JPanel();
	JPanel panel_ascii = new JPanel();
	JPanel panel_uni   = new JPanel();
	ButtonGroup bgr1     = new ButtonGroup();
	ButtonGroup bgr2     = new ButtonGroup();
	JTextField txt_dec   = new JTextField();
	JTextField txt_hex   = new JTextField();
	JTextField txt_bin   = new JTextField();
	JTextField txt_bin0  = new JTextField();
	JTextField txt_bin1  = new JTextField();
	JTextField txt_bin2  = new JTextField();
	JTextField txt_bin3  = new JTextField();
	JTextField txt_ascii = new JTextField();
	JTextField txt_uni   = new JTextField();	
	JLabel     lbl_info  = new JLabel();

	
	// constructor
	public ViewPart1() {
		panel_size.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Size", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_size.setBounds(10, 11, 80, 98);
		panel.setLayout(null);
		panel.setBackground(UIManager.getColor("Button.background"));
		panel_size.setLayout(null);
		rdbtn_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtn_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				action_rbtn("rdbtn_8");
			}
		});
		rdbtn_8.setBounds(6, 16, 68, 23);
		bgr1.add(rdbtn_8);
		panel_size.add(rdbtn_8);
		rdbtn_16.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtn_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action_rbtn("rdbtn_16");
			}
		});
		rdbtn_16.setBounds(6, 42, 68, 23);
		bgr1.add(rdbtn_16);
		panel_size.add(rdbtn_16);
		rdbtn_32.setSelected(true);
		rdbtn_32.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtn_32.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action_rbtn("rdbtn_32");
			}
		});
		rdbtn_32.setBounds(6, 68, 68, 23);
		bgr1.add(rdbtn_32);
		panel_size.add(rdbtn_32);		
		
		panel_sign.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sign", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_sign.setBounds(92, 11, 83, 98);
		panel_sign.setLayout(null);
		rdbtn_Signed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action_rbtn("rdbtn_Signed");
			}
		});
		rdbtn_Signed.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtn_Signed.setBounds(8, 43, 69, 23);
		bgr2.add(rdbtn_Signed);
		panel_sign.add(rdbtn_Signed);
		rdbtn_Unsigned.setSelected(true);
		rdbtn_Unsigned.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action_rbtn("rdbtn_Unsigned");
			}
		});
		rdbtn_Unsigned.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtn_Unsigned.setBounds(8, 17, 69, 23);
		bgr2.add(rdbtn_Unsigned);
		panel_sign.add(rdbtn_Unsigned);
		
		panel_dec.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "DECIMAL", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_dec.setBounds(177, 11, 127, 43);
		panel_dec.setLayout(null);
		txt_dec.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				on_keyReleased_txt_dec(txt_dec.getText());
			}
		});
		txt_dec.setBounds(6, 15, 115, 22);
		panel_dec.add(txt_dec);
		txt_dec.setHorizontalAlignment(SwingConstants.LEFT);
		txt_dec.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txt_dec.setColumns(10);
		txt_dec.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				on_change_txt_dec(txt_dec.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				on_change_txt_dec(txt_dec.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		panel_hex.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "HEXADECIMAL", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_hex.setBounds(307, 11, 107, 43);
		panel_hex.setLayout(null);
		txt_hex.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				on_keyReleased_txt_hex(txt_hex.getText());
			}
		});
		txt_hex.setBounds(6, 15, 95, 22);
		panel_hex.add(txt_hex);
		txt_hex.setHorizontalAlignment(SwingConstants.LEFT);
		txt_hex.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txt_hex.setColumns(10);
		txt_hex.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				on_change_txt_hex(txt_hex.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				on_change_txt_hex(txt_hex.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		panel_bin.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "BINARY", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_bin.setBounds(419, 11, 340, 43);
		panel.add(panel_bin);
		panel_bin.setLayout(null);
		txt_bin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				on_keyReleased_txt_bin(txt_bin.getText());
			}
		});
		txt_bin.setBounds(6, 15, 328, 22);
		txt_bin.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_bin.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txt_bin.setColumns(10);
		txt_bin.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				on_change_txt_bin(txt_bin.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				on_change_txt_bin(txt_bin.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		txt_bin0.setEditable(false);
		txt_bin0.setHorizontalAlignment(SwingConstants.CENTER);
		txt_bin0.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txt_bin0.setColumns(10);
		txt_bin0.setBounds(672, 56, 85, 22);
		
		txt_bin1.setEditable(false);
		txt_bin1.setHorizontalAlignment(SwingConstants.CENTER);
		txt_bin1.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txt_bin1.setColumns(10);
		txt_bin1.setBounds(588, 56, 85, 22);
		
		txt_bin2.setEditable(false);
		txt_bin2.setHorizontalAlignment(SwingConstants.CENTER);
		txt_bin2.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txt_bin2.setColumns(10);
		txt_bin2.setBounds(504, 56, 85, 22);

		txt_bin3.setEditable(false);
		txt_bin3.setHorizontalAlignment(SwingConstants.CENTER);
		txt_bin3.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txt_bin3.setColumns(10);
		txt_bin3.setBounds(421, 56, 84, 22);
		lbl_info.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lbl_info.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_info.setBounds(425, 87, 327, 22);
		
		panel_ascii.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ASCII", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_ascii.setBounds(185, 52, 47, 57);
		panel_ascii.setLayout(null);
		txt_ascii.setFont(new Font("Arial", Font.BOLD, 22));
		txt_ascii.setHorizontalAlignment(SwingConstants.CENTER);
		txt_ascii.setEditable(false);
		txt_ascii.setBounds(6, 16, 34, 34);
		panel_ascii.add(txt_ascii);
		txt_ascii.setColumns(10);
		
		panel_uni.setLayout(null);
		panel_uni.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "UNI", TitledBorder.LEADING, TitledBorder.TOP, borderfont, new Color(0, 0, 0)));
		panel_uni.setBounds(245, 53, 47, 57);
		txt_uni = new JTextField();
		txt_uni.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 22));
		txt_uni.setHorizontalAlignment(SwingConstants.CENTER);
		txt_uni.setEditable(false);
		txt_uni.setColumns(10);
		txt_uni.setBounds(6, 16, 34, 34);
		panel_uni.add(txt_uni);
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(composite);
		frame.add(panel);
		panel.add(rootPane);
		panel.add(panel_size);
		panel.add(panel_sign);
		panel.add(panel_dec);
		panel.add(panel_hex);
		panel_bin.add(txt_bin);
		panel.add(txt_bin0);
		panel.add(txt_bin1);
		panel.add(txt_bin2);		
		panel.add(txt_bin3);		
		panel.add(lbl_info);
		panel.add(panel_ascii);
		panel.add(panel_uni);
		
		makeActions();
		contributeToActionBars();
	}
//-------------------------------------------------------------------------------------------------
//		EVENTS
//-------------------------------------------------------------------------------------------------	
	
	public void action_rbtn(String rbtn) {
		switch(rbtn) {
			case "rdbtn_Signed"  : sign=true;  break;
			case "rdbtn_Unsigned": sign=false; break;
			case "rdbtn_8"       : size=8;     break;
			case "rdbtn_16"      : size=16;    break;
			case "rdbtn_32"      : size=32;    break;
		}
		if(lbl_info.getText().startsWith("Invalid")==true) return;
		String str=txt_bin.getText();
    	int sl=str.length();
    	bin2dec();
    	if(sl>size) {
			lbl_info.setForeground(DARKGREEN);
			lbl_info.setText("Out of Range");
		}
		else {
			lbl_info.setText("");
			isnum_dec();
			isnum_hex();
			isnum_bin();
		}
	}
	
	public void on_keyReleased_txt_dec(String txt) {
		txt_dec.setForeground(Color.BLACK);
		String str=txt_dec.getText();
		if(isnum_dec()==false) {return;}
		lbl_info.setText("");
		if(str.length()==0) {
			txt_bin.setText("");
			txt_hex.setText("");
			return;
		}
		if(str.charAt(0)=='-' && str.length()==1) {return;}
		long tst= Long.parseLong(str, 10);
		txt_dec.setForeground(Color.BLACK);
		enable_all();
		if(!test_range(tst)) {
			disable_all();
			txt_dec.setEnabled(true);
			txt_dec.requestFocus();
			txt_dec.setForeground(DARKGREEN);
			lbl_info.setForeground(DARKGREEN);
			lbl_info.setText("Out of Range");
			return;
		}
		long i = Long.parseLong(str);
		String bin = Long.toBinaryString(i);
		String hex=Long.toHexString(i);
		if(i<0) {
			if(size==8) {
				hex=hex.substring(6, hex.length());
				bin=bin.substring(24, bin.length());
			}
			if(size==16) {
				hex=hex.substring(4, hex.length());
				bin=bin.substring(16, bin.length());
			}
		}
		txt_bin.setText(bin);				
		txt_hex.setText(hex.toUpperCase());
		isnum_bin();
		isnum_hex();
	}
	
	public void on_keyReleased_txt_hex(String txt) {
		txt_hex.setForeground(Color.BLACK);
		String str=txt_hex.getText();
		if(isnum_hex()==false) {return;}
		lbl_info.setText("");
    	int sl=str.length();
    	enable_all();
		if((sl>8)||(size==8 && sl>2)||(size==16 && sl>4)||(size==32 && sl>8)) {
			disable_all();
			txt_hex.setEnabled(true);
			txt_hex.requestFocus();
			txt_hex.setForeground(DARKGREEN);
			lbl_info.setForeground(DARKGREEN);
			lbl_info.setText("Out of Range");
			return;
		}
		if(str.length()==0) {
			txt_dec.setText("");
			txt_bin.setText("");
			return;
		}
		long cot=complement_on_two(Long.parseLong(str,16));
		String res= ""+ cot;
		txt_dec.setText(res);
		String lng=Long.toBinaryString(Long.parseLong(str,16));
		txt_bin.setText(lng);
		isnum_bin();
		isnum_dec();		
	}
	
	public void on_keyReleased_txt_bin(String bin) {
		txt_bin.setForeground(Color.BLACK);
		String str=txt_bin.getText();
		if(isnum_bin()==false) {return;}
		lbl_info.setText("");
    	int sl=str.length();
		enable_all();
    	if((sl>32)||(size==8 && sl>8)||(size==16 && sl>16)) {
    		disable_all();
    		txt_bin.setEnabled(true);
    		txt_bin.requestFocus();
			txt_bin.setForeground(DARKGREEN);
			lbl_info.setForeground(DARKGREEN);
			lbl_info.setText("Out of Range");
			return;
		}
		if(str.length()==0) {
			txt_dec.setText("");
			txt_hex.setText("");
			return;
		}
		long bin1= Long.parseLong(str, 2);
		String res= "" +  complement_on_two(bin1);
		txt_dec.setText(res);
		String lng=Long.toHexString(bin1);
		txt_hex.setText(lng.toUpperCase());	
		isnum_hex();
		isnum_dec();
	}
	
	
	public void on_change_txt_bin(String txt) {
		binInfo(txt);
	}
	public void on_change_txt_hex(String txt) {
		asciiInfo(txt);
		uniInfo(txt);
	}
	public void on_change_txt_dec(String txt) {
	}
	
//----------------BIN2DEC--------------------------------------------------------------------	

	private void bin2dec() {
    	String str=txt_bin.getText();
    	try {
    		long bin= Long.parseLong(str, 2);
    		String res= ""+  complement_on_two(bin);
    		txt_dec.setText(res);
    	}
    	catch(NumberFormatException x){
    		lbl_info.setForeground(Color.RED);
    		lbl_info.setText("Invalid number format"); 
    	}	
    }
	
//----------------PARSE_XXX--------------------------------------------------------------------	
	
	private boolean isnum_bin() {
		txt_bin.setForeground(Color.BLACK);
		if(txt_bin.getText().length()==0) {
			clear_all_txt();
			return true;
		}
		try {
			Long.parseLong(txt_bin.getText(), 2);
		}
		catch(NumberFormatException x){
			txt_bin.setForeground(Color.red);
			lbl_info.setForeground(Color.RED);
			lbl_info.setText("Invalid number format"); 
			return false;
		}
		return true;
	}
	
	private boolean isnum_hex() {
		txt_hex.setForeground(Color.BLACK);
		if(txt_hex.getText().length()==0) {
			clear_all_txt();
			return true;
		}
		try {
			Long.parseLong(txt_hex.getText(), 16);
		}
		catch(NumberFormatException x){
			txt_hex.setForeground(Color.red);
			lbl_info.setForeground(Color.RED);
			lbl_info.setText("Invalid number format"); 
			return false;
		}
		return true;
	}
	
	private boolean isnum_dec() {
		txt_dec.setForeground(Color.BLACK);
		if(txt_dec.getText().length()==0) {
			clear_all_txt();
			return true;
		}
		try {
			Long.parseLong(txt_dec.getText(), 10);
		}
		catch(NumberFormatException x){
			txt_dec.setForeground(Color.red);
			lbl_info.setForeground(Color.RED);
			lbl_info.setText("Invalid number format"); 
			return false;
		}
		return true;
	}
	
	private void clear_all_txt() {
		txt_bin.setText("");
		txt_bin0.setText("");
		txt_bin1.setText("");
		txt_bin2.setText("");
		txt_bin3.setText("");		
		txt_dec.setText("");
		txt_hex.setText("");
	}
	
	private void disable_all() {
		txt_bin.setEnabled(false);
		txt_bin0.setEnabled(false);
		txt_bin1.setEnabled(false);
		txt_bin2.setEnabled(false);
		txt_bin3.setEnabled(false);
		txt_dec.setEnabled(false);
		txt_hex.setEnabled(false);
		rdbtn_8.setEnabled(false);
		rdbtn_16.setEnabled(false);
		rdbtn_32.setEnabled(false);
		rdbtn_Signed.setEnabled(false);
		rdbtn_Unsigned.setEnabled(false);
	}
	
	private void enable_all() {
		txt_bin.setEnabled(true);
		txt_bin0.setEnabled(true);
		txt_bin1.setEnabled(true);
		txt_bin2.setEnabled(true);
		txt_bin3.setEnabled(true);
		txt_dec.setEnabled(true);
		txt_hex.setEnabled(true);
		rdbtn_8.setEnabled(true);
		rdbtn_16.setEnabled(true);
		rdbtn_32.setEnabled(true);
		rdbtn_Signed.setEnabled(true);
		rdbtn_Unsigned.setEnabled(true);
	}
	
	

//-------------TOOLBAR--------------------------------------------------------------------
	
    private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		manager.add(action_ASCII);
		manager.add(action_Uni);
	}
     
    private void makeActions() {
		action_ASCII = new Action() {
			public void run() {
				ASCII.show();
			}
		};
		action_ASCII.setText("ASCII");
		action_ASCII.setToolTipText("ASCII Code");
		action_ASCII.setImageDescriptor(Action_A_Icon);
		
		action_Uni = new Action() {
			public void run() {
				Unicode.show();
			}
		};
		action_Uni.setText("Uni");
		action_Uni.setToolTipText("Uni Code");
		action_Uni.setImageDescriptor(Action_U_Icon);
	}


//----------------COMPLEMENT ON 2------------------------------------------------------------
	
	public long complement_on_two (long val) {
		long res=0;
		if(sign==true) {
			if(size==8) {
				byte r=0;
				val = val & 0x00000000000000ffL;
				r=(byte)val;
			res = r;
			}
			if(size==16) {
				short s=0;
				val = val & 0x000000000000ffffL;
				s=(short)val;
			res = s;
			}
			if(size==32) {
				int i=0;
				val = val & 0x00000000ffffffffL;
				i=(int)val;
			res = i;
			}
		}
		if(sign==false) {
			res = val; 
		}
		return res;
	}

//----------------RANGETEST-------------------------------------------------------------------------	
	
	public boolean test_range(long val) {
		if(sign==false) {
			if(size==8) {
				if(val>uint8_max) {
					return false;
				}
				if(val<0) {
					return false;
				}
			}
			if(size==16) {
				if(val>uint16_max) {
					return false;
				}
				if(val<0) {
					return false;
				}
			}
			if(size==32) {
				if(val>uint32_max) {
					return false;
				}
				if(val<0) {
					return false;
				}
			}
		}
		if(sign==true) {
			if(size==8) {
				if(val>int8_max) {
					return false;
				}
				if(val<int8_min) {
					return false;
				}
			}
			if(size==16) {
				if(val>int16_max) {
					return false;
				}
				if(val<int16_min) {
					return false;
				}
			}
			if(size==32) {
				if(val>int32_max) {
					return false;
				}
				if(val<int32_min) {
					return false;
				}
			}
		}
		return true;
	}

//----------------BININFO-------------------------------------------------------------------------	
	
	void binInfo(String val) {
		String insert="";
		int len=val.length();
		if(len>33) {
			txt_bin0.setText("");
			txt_bin1.setText("");
			txt_bin2.setText("");
			txt_bin3.setText("");
			return;
		}
		if(len>24) {
			insert=val.substring(len-8, len);
			txt_bin0.setText(insert);
			len-=8;
			insert=val.substring(len-8, len);
			txt_bin1.setText(insert);
			len-=8;
			insert=val.substring(len-8, len);
			txt_bin2.setText(insert);
			len-=8;
			insert=val.substring(0, len);
			while(insert.length()<8) {insert= ("0")+insert;}
			txt_bin3.setText(insert);
		}
		else if(len>16) {
			txt_bin3.setText("");
			insert=val.substring(len-8, len);
			txt_bin0.setText(insert);
			len-=8;
			insert=val.substring(len-8, len);
			txt_bin1.setText(insert);
			len-=8;
			insert=val.substring(0, len);
			while(insert.length()<8) {insert= ("0")+insert;}
			txt_bin2.setText(insert);
		}
		else if(len>8 ) {
			txt_bin3.setText("");
			txt_bin2.setText("");
			insert=val.substring(len-8, len);
			txt_bin0.setText(insert);
			len-=8;
			insert=val.substring(0, len);
			while(insert.length()<8) {insert= ("0")+insert;}
			txt_bin1.setText(insert);
		}
		else {
			txt_bin3.setText("");
			txt_bin2.setText("");
			txt_bin1.setText("");
			insert=val.substring(0, len);
			while(insert.length()<8) {insert= ("0")+insert;}
			txt_bin0.setText(insert);
		}
	}
	
//----------------ASCII INFO-------------------------------------------------------------------------	
	
	void asciiInfo(String val) {
		try
		{
			int  i = 0;
			long j = Long.parseLong(val, 16);
			if (j >= 0x20 && j <= 0x7E) {
				sb.appendCodePoint((int)j);
				txt_ascii.setText(sb.toString());
				sb.delete(0, sb.length());				
			}
			else if (j >= 0x80 && j <= 0xFF) {
				i=EXTENDED[(int)j-0x80];
				sb.appendCodePoint(i);
				txt_ascii.setText(sb.toString());
				sb.delete(0, sb.length());
			}
			else {
				txt_ascii.setText("");
			}
		}
		catch(NumberFormatException x){
			txt_ascii.setText("");
		}
	}
	
//----------------UNI INFO-------------------------------------------------------------------------	
	
	void uniInfo(String val ) {
		try
		{
			long j = Long.parseLong(val, 16);
			if ((j >= 0x20 && j <= uint16_max) && !(j>=127 && j <= 159) && (j<= 0x6FF)) {
				sb.appendCodePoint((int)j);
				txt_uni.setText(sb.toString());
				sb.delete(0, sb.length());				
			}
			else {
				txt_uni.setText("");
			}
		}
		catch(NumberFormatException x){
			txt_uni.setText("");
		}
	}

//----------------SET FOCUS-------------------------------------------------------------------------	
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
