package quickconverter;


import quickconverter.ASCII;
import quickconverter.Unicode;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ViewPart1 extends ViewPart {

    // controls
    private Button rdbtn_8;
    private Button rdbtn_16;
    private Button rdbtn_32;
    private Button rdbtn_signed;
    private Button rdbtn_unsigned;
    private Text txt_dec;
    private Text txt_hex;
    private Text txt_bin;
    private Text txt_ascii;
    private Text txt_uni;
    private Label lbl_bin0;
    private Label lbl_bin1;
    private Label lbl_bin2;
    private Label lbl_bin3;
    private Label lbl_err;

    StringBuilder sb = new StringBuilder();

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
            0x00B0, 0x2219, 0x00B7, 0x221A, 0x207F, 0x00B2, 0x25A0, 0x00A0 
    };
    
	Action action_ASCII;
	Action action_Uni;
	@SuppressWarnings("deprecation")
	ImageDescriptor Action_A_Icon = AbstractUIPlugin.imageDescriptorFromPlugin("QuickConverter", "/icons/ASCII_red_16x16.gif");
	@SuppressWarnings("deprecation")
	ImageDescriptor Action_U_Icon = AbstractUIPlugin.imageDescriptorFromPlugin("QuickConverter", "/icons/Uni_red_16x16.gif");


	
    // members
    final int int8_max = 127;
    final int int8_min = -128;
    final int int16_max = 32767;
    final int int16_min = -32768;
    final int int32_max = 2147483647;
    final int int32_min = -2147483648;
    final int uint8_max = 255;
    final int uint16_max = 65535;
    final long uint32_max = 4294967295L;
    
    long current_max = 0;
    int current_min = 0;
    boolean sign = false;
    int size = 32;
    int oorpos = 0; // out of range error occurs at position
    int last_parse = 0; // 1-dec, 2-hex, 3-bin

    Color bg_parent = new Color(null, 253, 253, 244);
    Color bin_bg = new Color(null, 230, 230, 230);
    Color red = new Color(null, 255, 0, 0);
    Color green = new Color(null, 10, 146, 0);
    Color black = new Color(null, 0, 0, 0);

    Font Mono12 = new Font(null, "DejaVu Sans Mono", 12, 0);
    Font Mono18 = new Font(null, "DejaVu Sans Mono", 18, 0);


    public void createPartControl(Composite parent) {
        parent.setLayout(null);
        parent.setBackground(bg_parent);
        // create a layout for all components
        // begin is at position x, y
        // space between is 10
        int comp_posx = 10;
        int comp_posy = 11;
        int comp_space = 10;
//SIZE
        int size_posx = comp_posx;
        int size_posy = comp_posy;
        int size_width = 80;
        Group size = new Group(parent, SWT.NONE);
        size.setBounds(size_posx, size_posy, size_width, 98);
        size.setLayout(new RowLayout(SWT.NONE));
        size.setText(" Size");
        rdbtn_8 = new Button(size, SWT.RADIO);
        rdbtn_8.setBounds(0, 0, 68, 23);
        rdbtn_8.setText("  8 bits");
        rdbtn_16 = new Button(size, SWT.RADIO);
        rdbtn_16.setText("16 bits");
        rdbtn_16.setBounds(0, 24, 68, 23);
        rdbtn_32 = new Button(size, SWT.RADIO);
        rdbtn_32.setBounds(0, 48, 68, 23);
        rdbtn_32.setText("32 bits");
//SIGNED
        int sign_posx = comp_posx + size_width + comp_space;
        int sign_posy = comp_posy;
        int sign_width = 86;
        Group sign = new Group(parent, SWT.NONE);
        sign.setBounds(sign_posx, sign_posy, sign_width, 98);
        sign.setLayout(new RowLayout(SWT.NONE));
        sign.setText(" Sign");
        rdbtn_signed = new Button(sign, SWT.RADIO);
        rdbtn_signed.setBounds(0, 0, 69, 23);
        rdbtn_signed.setText("Signed");
        rdbtn_unsigned = new Button(sign, SWT.RADIO);
        rdbtn_unsigned.setBounds(0, 24, 69, 23);
        rdbtn_unsigned.setText("Unsigned");
// DECIMAL
        int dec_posx = comp_posx + size_width + sign_width + 2 * comp_space;
        int dec_posy = comp_posy;
        int dec_width = 120;
        Label lbl_dec = new Label(parent, SWT.NONE);
        lbl_dec.setBounds(dec_posx, dec_posy, dec_width, 15);
        lbl_dec.setText(" DECIMAL");
        txt_dec = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        txt_dec.setFont(Mono12);
        txt_dec.setBounds(dec_posx, 26, dec_width, 25);
        // ASCII
        Label lbl_ascii = new Label(parent, SWT.NONE);
        lbl_ascii.setBounds(dec_posx, 55, dec_width, 15);
        lbl_ascii.setText("         ASCII char");
        txt_ascii = new Text(parent, SWT.BORDER | SWT.CENTER);
        txt_ascii.setFont(Mono18);
        txt_ascii.setEditable(false);
        txt_ascii.setBounds(dec_posx + 35, 70, 40, 40);
// HEXADECIMAL
        int hex_posx = comp_posx + size_width + sign_width + dec_width + 3 * comp_space;
        int hex_posy = comp_posy;
        int hex_width = 110;
        Label lbl_hex = new Label(parent, SWT.NONE);
        lbl_hex.setBounds(hex_posx, hex_posy, hex_width, 15);
        lbl_hex.setText(" HEXADECIMAL");
        txt_hex = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        txt_hex.setFont(Mono12);
        txt_hex.setBounds(hex_posx, hex_posy + 15, hex_width, 25);
        // UNIcode
        Label lbl_uni = new Label(parent, SWT.NONE);
        lbl_uni.setBounds(hex_posx, 55, dec_width, 15);
        lbl_uni.setText("     UNICODE char");
        txt_uni = new Text(parent, SWT.BORDER | SWT.CENTER);
        txt_uni.setFont(Mono18);
        txt_uni.setEditable(false);
        txt_uni.setBounds(hex_posx + 35, 70, 40, 40);
// BINARY        
        int bin_posx = comp_posx + size_width + sign_width + dec_width + hex_width + 4 * comp_space;
        int bin_posy = comp_posy;
        int bin_width = 340;
        Label lbl_bin = new Label(parent, SWT.NONE);
        lbl_bin.setBounds(bin_posx, bin_posy, bin_width, 15);
        lbl_bin.setText(" BINARY");
        txt_bin = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.RIGHT);
        txt_bin.setFont(Mono12);
        txt_bin.setBounds(bin_posx, 26, bin_width, 25);
        int binx_width = 81;
        int binx_posx = bin_posx + 9;
        int binx_posy = bin_posy + 44;
        lbl_bin3 = new Label(parent, SWT.BORDER | SWT.RIGHT);
        lbl_bin3.setBackground(bin_bg);
        lbl_bin3.setBounds(binx_posx, binx_posy, binx_width, 22);
        lbl_bin3.setFont(Mono12);
        lbl_bin3.setText("");
        lbl_bin2 = new Label(parent, SWT.BORDER | SWT.RIGHT);
        lbl_bin2.setBackground(bin_bg);
        lbl_bin2.setBounds(binx_posx + binx_width, binx_posy, binx_width, 22);
        lbl_bin2.setFont(Mono12);
        lbl_bin2.setText("");
        lbl_bin1 = new Label(parent, SWT.BORDER | SWT.RIGHT);
        lbl_bin1.setBackground(bin_bg);
        lbl_bin1.setBounds(binx_posx + 2 * binx_width, binx_posy, binx_width, 22);
        lbl_bin1.setFont(Mono12);
        lbl_bin1.setText("");
        lbl_bin0 = new Label(parent, SWT.BORDER | SWT.RIGHT);
        lbl_bin0.setBackground(bin_bg);
        lbl_bin0.setBounds(binx_posx + 3 * binx_width, binx_posy, binx_width, 22);
        lbl_bin0.setFont(Mono12);
        lbl_bin0.setText("");
        lbl_err = new Label(parent, SWT.NONE | SWT.LEFT);
        lbl_err.setBounds(bin_posx + 10, binx_posy + 40, bin_width, 22);
        lbl_err.setFont(Mono12);
// Events    
        rdbtn_8.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                rbtn_hasFocus("rdbtn_8");
            }
        });
        rdbtn_16.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                rbtn_hasFocus("rdbtn_16");
            }
        });
        rdbtn_32.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                rbtn_hasFocus("rdbtn_32");
            }
        });
        rdbtn_signed.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                rbtn_hasFocus("rdbtn_signed");
            }
        });
        rdbtn_unsigned.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                rbtn_hasFocus("rdbtn_unsigned");
            }
        });
        txt_dec.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                txt_keyReleased("txt_dec");
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }
        });
        txt_hex.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                txt_keyReleased("txt_hex");
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }
        });
        txt_bin.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent arg0) {
                txt_keyReleased("txt_bin");
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }
        });
        ModifyListener listener = new ModifyListener() {
            /** {@inheritDoc} */
            public void modifyText(ModifyEvent e) {
                // Handle event
                binInfo(txt_bin.getText());
                uniInfo(txt_bin.getText());
                asciiInfo(txt_bin.getText());
            }
        };
        txt_bin.addModifyListener(listener);

//ACTIONS
		makeActions();
		contributeToActionBars();

// set defaults
        rdbtn_unsigned.setSelection(true);
        rdbtn_32.setSelection(true);
        current_max = uint32_max;
        current_min = 0;
    }

//-------------RADIOBUTTON HAS BEEN CHANGED---------------------------------------------------------    

    public void rbtn_hasFocus(String rbtn) {
        switch (rbtn) {
        case "rdbtn_signed":
            sign = true;
            break;
        case "rdbtn_unsigned":
            sign = false;
            break;
        case "rdbtn_8":
            size = 8;
            break;
        case "rdbtn_16":
            size = 16;
            break;
        case "rdbtn_32":
            size = 32;
            break;
        }
        if (!sign && size == 8) {
            current_max = uint8_max;
            current_min = 0;
        }
        if (sign && size == 8) {
            current_max = int8_max;
            current_min = int8_min;
        }
        if (!sign && size == 16) {
            current_max = uint16_max;
            current_min = 0;
        }
        if (sign && size == 16) {
            current_max = int16_max;
            current_min = int16_min;
        }
        if (!sign && size == 32) {
            current_max = uint32_max;
            current_min = 0;
        }
        if (sign && size == 32) {
            current_max = int32_max;
            current_min = int32_min;
        }

        int err = 0;
        if (last_parse == 1)
            err += parse(1); // decimal
        if (last_parse == 2)
            err += parse(2); // hexadecimal
        if (last_parse == 3)
            err += parse(3); // binary
        errHandler(err);

        System.out.printf("current_max = %d\n", current_max);
        System.out.printf("current_min = %d\n", current_min);
    };

//------------TEXTFIELD HAS BEEN CHANGED-----------------------------------------------------------------    

    public void txt_keyReleased(String txt) {
        int err = 0;
        switch (txt) {
        case "txt_dec":
            err = parse(1);
            errHandler(err);
            break;
        case "txt_hex":
            err = parse(2);
            errHandler(err);
            break;
        case "txt_bin":
            err = parse(3);
            errHandler(err);
            break;
        }
    };
//--------------------------------------------------------------------------------------------------

    public int parse(int act) {
        String v = "";
        int l = 0;
        long num = 0;
        if (act == 1) {
            last_parse = 1;
            // from dec to hex and bin
            v = txt_dec.getText();
            l = v.length();
            if (l == 0) {
                txt_dec.setText("");
                txt_dec.setForeground(black);
                txt_hex.setText("");
                txt_hex.setForeground(black);
                txt_bin.setText("");
                txt_bin.setForeground(black);
                return 1; // ok, nothing to do
            }
            if (v.charAt(0) == '+')
                v = v.substring(1); // remove +
            v = v.toUpperCase();
            txt_dec.setText(v);
            txt_dec.setSelection(l, l); // set cursor right
            if ((v.charAt(0) == '-') && (v.length() == 1)) {
                txt_dec.setForeground(black);
                return 1; // ok, nothing to do
            }
            if (!isDecimal(txt_dec.getText())) {
                txt_dec.setForeground(red);
                return -100; // Invalid number format
            }
            num = Long.parseLong(txt_dec.getText(), 10);
            if (!isInRange(num)) {
                txt_dec.setForeground(green);
                return -1; // out of range
            }
            txt_dec.setForeground(black);
            txt_hex.setForeground(black);
            txt_bin.setForeground(black);
            num = complement_on_two(Long.parseLong(v, 10));
            System.out.printf("num = %d\n", num);
            String bin = Long.toBinaryString(num);
            String hex = Long.toHexString(num);
            if (num < 0) {
                if (size == 8) {
                    hex = hex.substring(hex.length() - 2, hex.length());
                    bin = bin.substring(bin.length() - 8, bin.length());
                }
                if (size == 16) {
                    hex = hex.substring(hex.length() - 4, hex.length());
                    bin = bin.substring(bin.length() - 16, bin.length());
                }
                if (size == 32) {
                    hex = hex.substring(hex.length() - 8, hex.length());
                    bin = bin.substring(bin.length() - 32, bin.length());
                }
            }
            txt_hex.setText(hex.toUpperCase());
            txt_bin.setText(bin);
        }

        if (act == 2) {
            last_parse = 2;
            // from hex to dec and bin
            v = txt_hex.getText();
            l = v.length();
            if (l == 0) {
                txt_dec.setText("");
                txt_dec.setForeground(black);
                txt_hex.setText("");
                txt_hex.setForeground(black);
                txt_bin.setText("");
                txt_bin.setForeground(black);
                return 1; // ok, nothing to do
            }
            v = v.toUpperCase();
            txt_hex.setText(v);
            txt_hex.setSelection(l, l); // set cursor right
            if (!isHexadecimal(v)) {
                txt_hex.setForeground(red);
                return -100; // Invalid number format
            }
            if ((l > 2 && size == 8) || (l > 4 && size == 16) || (l > 8 && size == 32)) {
                txt_hex.setForeground(green);
                return -1; // out of range
            }
            num = complement_on_two(Long.parseLong(txt_hex.getText(), 16));
            if (!isInRange(num)) {
                txt_hex.setForeground(green);
                return -1; // out of range
            }
            txt_dec.setForeground(black);
            txt_hex.setForeground(black);
            txt_bin.setForeground(black);
            txt_dec.setText(Long.toString(num));
            String bin = Long.toBinaryString(num);
            txt_bin.setText(bin);
            if (num < 0) {
                if (size == 8) {
                    bin = bin.substring(bin.length() - 8, bin.length());
                }
                if (size == 16) {
                    bin = bin.substring(bin.length() - 16, bin.length());
                }
                if (size == 32) {
                    bin = bin.substring(bin.length() - 32, bin.length());
                }
            }
            txt_bin.setText(bin);
        }
        if (act == 3) {
            last_parse = 3;
            // from bin to dec and hex
            v = txt_bin.getText();
            l = v.length();
            if (l == 0) {
                txt_dec.setText("");
                txt_dec.setForeground(black);
                txt_hex.setText("");
                txt_hex.setForeground(black);
                txt_bin.setText("");
                txt_bin.setForeground(black);
                return 1; // ok, nothing to do
            }
            if (!isBinary(v)) {
                txt_bin.setForeground(red);
                return -100; // Invalid number format
            }
            if ((l > 8 && size == 8) || (l > 16 && size == 16) || (l > 32 && size == 32)) {
                txt_bin.setForeground(green);
                return -1; // out of range
            }
            num = complement_on_two(Long.parseLong(txt_bin.getText(), 2));
            if (!isInRange(num)) {
                txt_hex.setForeground(green);
                return -1; // out of range
            }
            txt_dec.setForeground(black);
            txt_hex.setForeground(black);
            txt_bin.setForeground(black);
            txt_dec.setText(Long.toString(num));
            String hex = Long.toHexString(num);
            txt_hex.setText(hex.toUpperCase());
            if (num < 0) {
                if (size == 8) {
                    hex = hex.substring(hex.length() - 2, hex.length());
                }
                if (size == 16) {
                    hex = hex.substring(hex.length() - 4, hex.length());
                }
                if (size == 32) {
                    hex = hex.substring(hex.length() - 8, hex.length());
                }
            }
            txt_hex.setText(hex.toUpperCase());
        }
        return 0;
    }

//--------------------------------------------------------------------------------------------------

    public boolean isHexadecimal(String v) {
        if (v.matches("-?[0-9a-fA-F]+"))
            return true;
        else
            return false;
    }

    public boolean isDecimal(String v) {
        if (v.charAt(0) == '+' || v.charAt(0) == '-') {
            if (v.substring(1).matches("-?[0-9]+"))
                return true;
            else
                return false;
        } else {
            if (v.matches("-?[0-9]+"))
                return true;
            else
                return false;
        }
    }

    public boolean isBinary(String v) {
        if (v.matches("-?[0-1]+"))
            return true;
        else
            return false;
    }

    public boolean isInRange(long n) {
        if ((n > current_max) || (n < current_min))
            return false;
        else if (sign) {
            long m = complement_on_two(n);
            if ((m > current_max) || (m < current_min))
                return false;
        }
        return true;
    }

//--------------------------------------------------------------------------------------------------
    public void errHandler(int err) {
        if (err >= 0) { // no error
            lbl_err.setText("");
            lbl_err.setForeground(black);

        } else {
            if (err < -99) {
                lbl_err.setText("INVALID NUMBER FORMAT");
                lbl_err.setForeground(red);
            } else {
                lbl_err.setText("OUT OF RANGE");
                lbl_err.setForeground(green);
            }
        }

    }

// ----------------COMPLEMENT ON 2------------------------------------------------------------------

    public long complement_on_two(long val) {
        long res = 0;
        if (sign == true) {
            if (size == 8) {
                byte r = 0;
                val = val & 0x00000000000000ffL;
                System.out.printf("val = %d\n", val);
                r = (byte) val;
                res = r;
            }
            if (size == 16) {
                short s = 0;
                val = val & 0x000000000000ffffL;
                s = (short) val;
                res = s;
            }
            if (size == 32) {
                int i = 0;
                val = val & 0x00000000ffffffffL;
                i = (int) val;
                res = i;
            }
        }
        if (sign == false) {
            res = val;
        }
        return res;
    }

// ----------------BININFO--------------------------------------------------------------------------

    void binInfo(String val) {
        String insert = "";
        int len = val.length();
        if (len > 33) {
            lbl_bin0.setText("");
            lbl_bin1.setText("");
            lbl_bin2.setText("");
            lbl_bin3.setText("");
            return;
        }
        if (len > 24) {
            insert = val.substring(len - 8, len);
            lbl_bin0.setText(insert);
            len -= 8;
            insert = val.substring(len - 8, len);
            lbl_bin1.setText(insert);
            len -= 8;
            insert = val.substring(len - 8, len);
            lbl_bin2.setText(insert);
            len -= 8;
            insert = val.substring(0, len);
            while (insert.length() < 8) {
                insert = ("0") + insert;
            }
            lbl_bin3.setText(insert);
        } else if (len > 16) {
            lbl_bin3.setText("");
            insert = val.substring(len - 8, len);
            lbl_bin0.setText(insert);
            len -= 8;
            insert = val.substring(len - 8, len);
            lbl_bin1.setText(insert);
            len -= 8;
            insert = val.substring(0, len);
            while (insert.length() < 8) {
                insert = ("0") + insert;
            }
            lbl_bin2.setText(insert);
        } else if (len > 8) {
            lbl_bin3.setText("");
            lbl_bin2.setText("");
            insert = val.substring(len - 8, len);
            lbl_bin0.setText(insert);
            len -= 8;
            insert = val.substring(0, len);
            while (insert.length() < 8) {
                insert = ("0") + insert;
            }
            lbl_bin1.setText(insert);
        } else {
            lbl_bin3.setText("");
            lbl_bin2.setText("");
            lbl_bin1.setText("");
            insert = val.substring(0, len);
            while (insert.length() < 8) {
                insert = ("0") + insert;
            }
            lbl_bin0.setText(insert);
        }
    }

// ----------------ASCII INFO-----------------------------------------------------------------------

    void asciiInfo(String val) {
        try {
            int i = 0;
            long j = Long.parseLong(val, 2);
            if (j >= 0x20 && j <= 0x7E) {
                sb.appendCodePoint((int) j);
                txt_ascii.setText(sb.toString());
                sb.delete(0, sb.length());
            } else if (j >= 0x80 && j <= 0xFF) {
                i = EXTENDED[(int) j - 0x80];
                sb.appendCodePoint(i);
                txt_ascii.setText(sb.toString());
                sb.delete(0, sb.length());
            } else {
                txt_ascii.setText("");
            }
        } catch (NumberFormatException x) {
            txt_ascii.setText("");
        }
    }

// ----------------UNI INFO-------------------------------------------------------------------------

    void uniInfo(String val) {
        try {
            long j = Long.parseLong(val, 2);
            if ((j >= 0x20 && j <= uint16_max) && !(j >= 127 && j <= 159) && (j <= 0x6FF)) {
                sb.appendCodePoint((int) j);
                txt_uni.setText(sb.toString());
                sb.delete(0, sb.length());
            } else {
                txt_uni.setText("");
            }
        } catch (NumberFormatException x) {
            txt_uni.setText("");
        }
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



    public void setFocus() {
    	// TODO Auto-generated method stub
    }
} // END VIEWPART1