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

public class ASCII {
	
	final static char[] EXTENDED = { 
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
	
	static JFrame window = new JFrame();
	static JLabel[] lbl1 = new JLabel[256];
	static StringBuilder sb = new StringBuilder();
	static Border bdr1= BorderFactory.createLineBorder(Color.black);
	static Font fnt1 = new Font("Arial", Font.BOLD, 16);
	static JTextField[] txt1 = new JTextField[256];
	static JSeparator sep1 =new JSeparator(SwingConstants.VERTICAL);
	static Color bg = new Color(209,241,255);
	
	static String val2="";
	static int upper_margin = 20;
	static int left_margin  = 20;
	static int lbl1_width   = 25;
	static int txt1_width   = 35;
	static int item_height  = 25;
	static int row_width    = lbl1_width +1 + txt1_width +1 ; 
	
public static void show() {
		
		window.setTitle("ASCII");
		window.setLayout(null);
		window.getContentPane().setBackground(bg);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		UIManager.put("ToolTip.background", Color.yellow);
		int    val1 = 32;
		int    val2 = 0;
		String tt   = ""; // Tooptip
        for (int i=0;i<224;i++){
           	addItems(i);
        }     
        for (int i=0;i<224;i++){
 			if (val1 >= 0x20 && val1 <= 0x7E) {
				sb.appendCodePoint(val1);
				lbl1[i].setText(sb.toString());
				sb.delete(0, sb.length());
			} else if (val1 >= 0x80 && val1 <= 0xFF) {
				val2 = EXTENDED[(int) val1 - 0x80];
				sb.appendCodePoint(val2);
				lbl1[i].setText(sb.toString());
				sb.delete(0, sb.length());
			} else {
				lbl1[i].setText("");
			}
         	txt1[i].setText(String.format("%04d", val1));
        	tt = "00" + Integer.toHexString(val1);
        	txt1[i].setToolTipText(tt.toUpperCase());
        	val1++;
        } 
        window.setSize(2*left_margin + 8 *(row_width+5) +50, upper_margin + 28*(item_height + 1)+55);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
   	}

	private static void addItems(int i) {
		int j = i;
		int lm = 0;
		int row = 0;
		row = i / 28;
		lm = left_margin + row * (row_width + 11);

		int lbl1_xpos = lm;
		int txt1_xpos = lbl1_xpos + lbl1_width + 1;

		j = i - row * 28;

		lbl1[i] = new JLabel();
		lbl1[i].setBorder(bdr1);
		lbl1[i].setOpaque(true);
		lbl1[i].setBackground(Color.WHITE);
		lbl1[i].setHorizontalAlignment(SwingConstants.CENTER);
		lbl1[i].setVerticalAlignment(SwingConstants.CENTER);
		lbl1[i].setBounds(lbl1_xpos, upper_margin + j * (item_height + 1), lbl1_width, item_height);
		lbl1[i].setFont(fnt1);
		txt1[i] = new JTextField();
		txt1[i].setHorizontalAlignment(SwingConstants.CENTER);
		txt1[i].setBounds(txt1_xpos, upper_margin + j * (item_height + 1), txt1_width, item_height);
		txt1[i].setBorder(bdr1);

		window.add(lbl1[i]);
		window.add(txt1[i]);
	}
}
