package quickconverter;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class Unicode {
	
	static JFrame window = new JFrame();
	static JLabel[] lbl1 = new JLabel[256];
	static StringBuilder sb = new StringBuilder();
	static Border bdr1= BorderFactory.createLineBorder(Color.black);
	static Font fnt1 = new Font("Microsoft Sans Serif", Font.BOLD, 16);
	static JTextField[] txt1 = new JTextField[256];
	static JTextField[] txt2 = new JTextField[256];
	static JSeparator sep1 =new JSeparator(SwingConstants.VERTICAL);
	static Color bg = new Color(209,241,255);
	
	static String val2="";
	static int upper_margin = 20;
	static int left_margin  = 20;
	static int lbl1_width   = 25;
	static int txt1_width   = 35;
	static int txt2_width   = 35;
	static int item_height  = 25;
	static int row_width    = lbl1_width +1 + txt1_width +1 + txt2_width + 1; 
	
	
	public static void show() {
		
		window.setTitle("Unicode, UTF-8");
		window.setLayout(null);
		window.getContentPane().setBackground(bg);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UIManager.put("ToolTip.background", Color.yellow);
		int    val1 = 32;
		String val2 = "";
		String val3 = "";
		String tt   = ""; // Tooptip
        for (int i=0;i<192;i++){
           	addItems(i);
        }     
        for (int i=0;i<192;i++){
        	sb.appendCodePoint(val1);
        	lbl1[i].setText(sb.toString());
        	if(i==95) lbl1[i].setText("");
        	sb.delete(0,sb.length());
        	txt1[i].setText(String.format("%04d", val1));
        	tt = "00" + Integer.toHexString(val1);
        	txt1[i].setToolTipText(tt.toUpperCase());
        	val2=Integer.toHexString(val1);
        	val3=Integer.toHexString(val1-0x40);
        	if(val1<0x8f) {
           		val2="00"+val2;
        		txt2[i].setText(val2.toUpperCase());
        	}
        	else if (val1<0xC0) {
        		val2="C2"+val2;
        		txt2[i].setText(val2.toUpperCase());
        	}
        	else {
        		val3="C3"+ val3;
        		txt2[i].setText(val3.toUpperCase());     		
        	}
        	val1++;
        	if(i==127-32) val1=0xA0;
         } 

        window.setSize(left_margin + ((256/32)-1) *row_width , upper_margin + 32*(item_height + 1)+55);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        
	

	}
    private static void addItems(int i) {

  	
    	
    	int j = i;
    	int lm = 0;
    	int row = 0;
    	row = i/32;
    	lm = left_margin +row * (row_width + 11);
 	
     	int lbl1_xpos    = lm;
     	int txt1_xpos    = lbl1_xpos + lbl1_width + 1;
     	int txt2_xpos    = txt1_xpos + txt1_width + 1;
     	j = i - row * 32;     	
    	
    	lbl1[i]=new JLabel();
        lbl1[i].setBorder(bdr1);
        lbl1[i].setOpaque(true);
        lbl1[i].setBackground(Color.WHITE);
        lbl1[i].setHorizontalAlignment(SwingConstants.CENTER);
        lbl1[i].setVerticalAlignment(SwingConstants.CENTER);
        lbl1[i].setBounds(lbl1_xpos, upper_margin+j *(item_height + 1),lbl1_width, item_height);
        lbl1[i].setFont(fnt1);    	
        txt1[i]= new JTextField();
        txt1[i].setHorizontalAlignment(SwingConstants.CENTER);
        txt1[i].setBounds(txt1_xpos, upper_margin +j * (item_height + 1), txt1_width, item_height);
        txt1[i].setBorder(bdr1);
        txt2[i]= new JTextField();
        txt2[i].setHorizontalAlignment(SwingConstants.CENTER);
        txt2[i].setBounds(txt2_xpos, upper_margin +j * (item_height + 1), txt2_width, item_height);
        txt2[i].setBorder(bdr1); 
        
        window.add(lbl1[i]);
        window.add(txt1[i]);
        window.add(txt2[i]);
        
    }

	
	
	
	

}
