import java.awt.Button;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

//Begin SEMWebClient
public class SEMWebClient extends java.applet.Applet implements MouseListener, ItemListener, TextListener, ActionListener {
  static boolean bCmd = false;
  
  static boolean uAdv = true;
  static boolean inCmd = false;
  static boolean xRay = false;
  static boolean bLogin = false;
  static boolean specMode = false;
  static Long magValue = new Long(0);
  
  static Long xValue = new Long(0); 
  static Long yValue = new Long(0);
  
  String netAdrs = null;
  String netName = null;
  String netPswd = null;
   
  static SEMSocket wServer;
  static URL homeLoc = null;
  static URL url = null;

  Label userLabel = new Label("Username:");
  Label passLabel = new Label("Password:");
  TextField userName = new TextField("            ");
  TextField passWord = new TextField("            ");
  TextField loginStatus = new TextField("Please enter username and password.");
  Button doLogin = new Button("Login");

  /* ID */
  Label verLabel = new Label("PSEM Java Client V1.04",Label.CENTER);
  Label autLabel = new Label("Written by Chris Mannes",Label.CENTER);

  /* Controls */
  Label xLabel = new Label("X",Label.CENTER);
  Label yLabel = new Label("Y",Label.CENTER);
  TextField xField = new TextField("      ");
  TextField yField = new TextField("      ");
  Button xyMove = new Button("Move");
  
  Label magLabel = new Label("Zoom",Label.CENTER);
  Choice magBox = new Choice();
  Button incMagSm = new Button(">");
  Button decMagSm = new Button("<");
  Button incMagLg = new Button(">>");
  Button decMagLg = new Button("<<");
  Label brightLabel = new Label("Brightness",Label.CENTER);
  Button incBrightSm = new Button(">");
  Button decBrightSm = new Button("<");
  Button incBrightLg = new Button(">>");
  Button decBrightLg = new Button("<<");
  Label contrastLabel = new Label("Contrast",Label.CENTER);
  Button incContrastSm = new Button(">");
  Button decContrastSm = new Button("<");
  Button incContrastLg = new Button(">>");
  Button decContrastLg = new Button("<<");
  Button reFresh = new Button("Refresh");
  Button autoFocus = new Button("AutoFocus");
  Button savePic = new Button("Save");
  Label dispLabel = new Label("Screen Mode",Label.CENTER);
  Choice dispMode = new Choice();
  Label imgLabel = new Label("Image Mode",Label.CENTER);
  Choice secBack = new Choice();
  
  Button xrayLabel = new Button("Xray Map (off)");
  Button xrayStart = new Button("Start");
  Button xrayStop = new Button("Stop");
  
  Label fillSpace1 = new Label("          ");
  Label fillSpace2 = new Label("          ");
  Label fillSpace3 = new Label("          ");
  Label fillSpace4 = new Label("          ");
  Label fillSpace5 = new Label("          ");
  Label fillSpace6 = new Label("          ");
    
  SEMCanvas imageCanvas;  
   
  TextField out = new TextField("                                         ");
  TextField in = new TextField("                                         ");
  Button send = new Button("Send");
  Button end = new Button("connect");

void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int gxw, int gyw) {
  gbc.gridx = gx;
  gbc.gridy = gy;
  gbc.gridwidth = gw;
  gbc.gridheight = gh;
  gbc.weightx = gxw;
  gbc.weighty = gyw;
}

protected URL getURL(String filename) {
  URL codeBase = getCodeBase();
  URL url = null;

  try {
    url = new URL(codeBase, filename);
  } catch (java.net.MalformedURLException e) {
    System.err.println("Badly specified URL");
    return null;
  }
  
  return url;
}
  
public void fLogin() {
  GridBagLayout gridbag = new GridBagLayout();
  GridBagConstraints constraints = new GridBagConstraints();
  removeAll();
  invalidate();
  repaint();  
  setLayout(gridbag);
  constraints.fill = GridBagConstraints.NONE;
  
  buildConstraints(constraints,0,0,4,1,1,1);
  gridbag.setConstraints(verLabel,constraints);
  add(verLabel);
  
  buildConstraints(constraints,0,1,4,1,1,1);
  gridbag.setConstraints(autLabel,constraints);
  add(autLabel);
  
  buildConstraints(constraints,0,2,4,1,1,1);
  gridbag.setConstraints(fillSpace1,constraints);
  add(fillSpace1);  
  
  buildConstraints(constraints,0,3,1,1,1,1);
  gridbag.setConstraints(userLabel,constraints);
  add(userLabel);

  buildConstraints(constraints,1,3,3,1,1,1);
  gridbag.setConstraints(userName,constraints);  
  add(userName);

  buildConstraints(constraints,0,4,1,1,1,1);
  gridbag.setConstraints(passLabel,constraints);
  add(passLabel);
  
  buildConstraints(constraints,1,4,3,1,1,1);
  gridbag.setConstraints(passWord,constraints);
  add(passWord);

  buildConstraints(constraints,0,5,4,1,1,1);
  gridbag.setConstraints(doLogin,constraints);
  add(doLogin);

  buildConstraints(constraints,0,6,4,1,1,1);
  gridbag.setConstraints(loginStatus,constraints);
  add(loginStatus);

  doLogin.addActionListener((ActionListener)this);

  validate();
  repaint();
}
 
public void fActive() {
  GridBagLayout gridbag = new GridBagLayout();
  GridBagConstraints constraints = new GridBagConstraints();
  removeAll();
  invalidate();
  repaint();  
  setLayout(gridbag);
  constraints.fill = GridBagConstraints.HORIZONTAL;

  buildConstraints(constraints,0,0,5,1,1,1);
  gridbag.setConstraints(verLabel,constraints);
  add(verLabel);
  
  buildConstraints(constraints,0,1,5,1,1,1);
  gridbag.setConstraints(autLabel,constraints);
  add(autLabel);
  
  buildConstraints(constraints,0,2,5,1,1,1);
  gridbag.setConstraints(fillSpace1,constraints);
  add(fillSpace1);

  buildConstraints(constraints,0,3,1,1,1,1);
  gridbag.setConstraints(dispLabel,constraints);
  add(dispLabel);

  buildConstraints(constraints,1,3,4,1,1,1);
  gridbag.setConstraints(dispMode,constraints);
  dispMode.addItem("Left");
  dispMode.addItem("Right");
  dispMode.addItem("Full");
  add(dispMode);
  dispMode.addItemListener(this);
  
  buildConstraints(constraints,0,4,1,1,1,1);
  gridbag.setConstraints(imgLabel,constraints);
  add(imgLabel);
  
  buildConstraints(constraints,1,4,4,1,1,1);
  gridbag.setConstraints(secBack,constraints);
  secBack.addItem("Secondary");
  secBack.addItem("Backscatter");
  add(secBack);
  secBack.addItemListener(this);
  
  buildConstraints(constraints,0,5,5,1,1,1);
  gridbag.setConstraints(fillSpace2,constraints);
  add(fillSpace2);
  
  buildConstraints(constraints,0,6,1,1,1,1);
  gridbag.setConstraints(xyMove,constraints);
  add(xyMove);
  xyMove.addActionListener(this);
  
  buildConstraints(constraints,1,6,1,1,1,1);
  gridbag.setConstraints(xLabel,constraints);
  add(xLabel);
  
  buildConstraints(constraints,2,6,1,1,1,1);
  gridbag.setConstraints(xField,constraints);
  add(xField);
  xField.addTextListener(this);
  
  buildConstraints(constraints,3,6,1,1,1,1);
  gridbag.setConstraints(yLabel,constraints);
  add(yLabel);
  
  buildConstraints(constraints,4,6,1,1,1,1);
  gridbag.setConstraints(yField,constraints);
  add(yField);
  yField.addTextListener(this);
  
  buildConstraints(constraints,0,7,5,1,1,1);
  gridbag.setConstraints(fillSpace3,constraints);
  add(fillSpace3);
  
  buildConstraints(constraints,0,8,1,1,1,1);
  gridbag.setConstraints(magBox,constraints);
  magBox.addItem("Zoom");
  magBox.addItem("Min");
  magBox.addItem("100x");
  magBox.addItem("1000x");
  magBox.addItem("10000x");
  add(magBox);
  magBox.addItemListener(this);
  
  buildConstraints(constraints,1,8,1,1,1,1);
  gridbag.setConstraints(decMagLg,constraints);
  add(decMagLg);
  decMagLg.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,2,8,1,1,1,1);
  gridbag.setConstraints(decMagSm,constraints);
  add(decMagSm);
  decMagSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,3,8,1,1,1,1);
  gridbag.setConstraints(incMagSm,constraints);
  add(incMagSm);
  incMagSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,4,8,1,1,1,1);
  gridbag.setConstraints(incMagLg,constraints);
  add(incMagLg);
  incMagLg.addActionListener((ActionListener)this);

  
  buildConstraints(constraints,0,9,1,1,1,1);
  gridbag.setConstraints(brightLabel,constraints);
  add(brightLabel);
  
  buildConstraints(constraints,1,9,1,1,1,1);
  gridbag.setConstraints(decBrightLg,constraints);
  add(decBrightLg);
  decBrightLg.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,2,9,1,1,1,1);
  gridbag.setConstraints(decBrightSm,constraints);
  add(decBrightSm);
  decBrightSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,3,9,1,1,1,1);
  gridbag.setConstraints(incBrightSm,constraints);
  add(incBrightSm);
  incBrightSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,4,9,1,1,1,1);
  gridbag.setConstraints(incBrightLg,constraints);
  add(incBrightLg);
  incBrightLg.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,0,10,1,1,1,1);
  gridbag.setConstraints(contrastLabel,constraints);
  add(contrastLabel);
  
  buildConstraints(constraints,1,10,1,1,1,1);
  gridbag.setConstraints(decContrastLg,constraints);
  add(decContrastLg);
  decContrastLg.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,2,10,1,1,1,1);
  gridbag.setConstraints(decContrastSm,constraints);
  add(decContrastSm);
  decContrastSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,3,10,1,1,1,1);
  gridbag.setConstraints(incContrastSm,constraints);
  add(incContrastSm);
  incContrastSm.addActionListener((ActionListener)this);
  
  buildConstraints(constraints,4,10,1,1,1,1);
  gridbag.setConstraints(incContrastLg,constraints);
  add(incContrastLg);
  incContrastLg.addActionListener((ActionListener)this);

  buildConstraints(constraints,0,11,1,1,1,1);
  gridbag.setConstraints(fillSpace4,constraints);
  add(fillSpace4);

  buildConstraints(constraints,0,12,1,1,1,1);
  gridbag.setConstraints(reFresh,constraints);
  add(reFresh);
  reFresh.addActionListener(this);

  buildConstraints(constraints,1,12,2,1,1,1);
  gridbag.setConstraints(autoFocus,constraints);
  add(autoFocus);
  autoFocus.addActionListener(this);
  
  buildConstraints(constraints,3,12,2,1,1,1);
  gridbag.setConstraints(savePic,constraints);
  add(savePic);
  savePic.addActionListener(this);
  
  buildConstraints(constraints,0,13,5,1,1,1);
  gridbag.setConstraints(fillSpace5,constraints);
  add(fillSpace5);
  
  if(uAdv == true) {
    buildConstraints(constraints,0,14,1,1,1,1);
    gridbag.setConstraints(xrayLabel,constraints);
    add(xrayLabel);
    xrayLabel.addActionListener(this);
  
    buildConstraints(constraints,1,14,2,1,1,1);
    gridbag.setConstraints(xrayStart,constraints);
    add(xrayStart);
    xrayStart.addActionListener(this);
  
    buildConstraints(constraints,3,14,2,1,1,1);
    gridbag.setConstraints(xrayStop,constraints);
    add(xrayStop);
    xrayStop.addActionListener(this);
  
    buildConstraints(constraints,0,15,5,1,1,1);
    gridbag.setConstraints(fillSpace6,constraints);
    add(fillSpace6);
  }
  
  buildConstraints(constraints,0,16,5,1,1,1);
  gridbag.setConstraints(in,constraints);
  add(in);
    
  imageCanvas = new SEMCanvas(512,512,url,this);
  imageCanvas.addMouseListener(this);
  imageCanvas.read_Overlay(0);
    
  buildConstraints(constraints,5,0,1,17,1,1);
  gridbag.setConstraints(imageCanvas,constraints);
  add(imageCanvas); 
  
  getInit();
  
  validate();
  repaint();
}

public void getInit() {
  sendNetCmd("9010 75");
  sendNetCmd("4011");
  sendNetCmd("1012");
  sendNetCmd("9001 9001");
  sendNetCmd("9008 9008");
}

public void fPassive() {
  GridBagLayout gridbag = new GridBagLayout();
  GridBagConstraints constraints = new GridBagConstraints();
  removeAll();
  invalidate();
  repaint();  
  setLayout(gridbag);
  constraints.fill = GridBagConstraints.HORIZONTAL;

  buildConstraints(constraints,0,0,5,1,1,1);
  gridbag.setConstraints(verLabel,constraints);
  add(verLabel);
  
  buildConstraints(constraints,0,1,5,1,1,1);
  gridbag.setConstraints(autLabel,constraints);
  add(autLabel);

  buildConstraints(constraints,0,16,5,1,1,1);
  gridbag.setConstraints(in,constraints);
  add(in);
    
  imageCanvas = new SEMCanvas(512,512,url,this);
  imageCanvas.addMouseListener(this);
  imageCanvas.read_Overlay(0);
    
  buildConstraints(constraints,5,0,1,17,1,1);
  gridbag.setConstraints(imageCanvas,constraints);
  add(imageCanvas); 
  
  validate();
  repaint();
}

  
public void init() {
  String ip = getParameter("ip_address");
  String inter = getParameter("interface");
    
  if(inter != null && inter.equals("simple")) uAdv = false;
  else uAdv = true;
  
  wServer = new SEMSocket(ip,this);
  url = getCodeBase();
  homeLoc = getURL("blank.html");
  //fLogin();
  fActive();
}

public void add_out(String s) {
  out.setText(s);
}

public void add_in(String s) {
  in.setText(s);
}

public void halt() {
    wServer.MCloseConnection();
    getAppletContext().showDocument(homeLoc);
}

public void actionPerformed(ActionEvent evt) {
   Object source = evt.getSource();
   if(bLogin) {
     if(source == incMagSm) {
       if(imageCanvas.screenMode == 4) sendNetCmd("2013");
       else sendNetCmd("2002");
       add_in("Zoom In Small");
     } else if(source == incMagLg) {
       if(imageCanvas.screenMode == 4) sendNetCmd("2015");
       else sendNetCmd("2004");
       add_in("Zoom In Large");
     } else if(source == decMagSm) {
       if(imageCanvas.screenMode == 4) sendNetCmd("2012");
       else sendNetCmd("2001");
       add_in("Zoom Out Small");
     } else if(source == decMagLg) {
       if(imageCanvas.screenMode == 4) sendNetCmd("2014");
       else sendNetCmd("2003");
       add_in("Zoom Out Large");
     } else if(source == incBrightSm) {
       sendNetCmd("4002");
       add_in("Increase Brightness Small");
     } else if(source == incBrightLg) {
       sendNetCmd("4004");
       add_in("Increase Brightness Large");
     } else if(source == decBrightSm) {
       sendNetCmd("4001");
       add_in("Decrease Brightness Small");
     } else if(source == decBrightLg) {
       sendNetCmd("4003");
       add_in("Decrease Brightness Large");
     } else if(source == incContrastSm) {
       sendNetCmd("4006");
       add_in("Increase Contrast Small");
     } else if(source == incContrastLg) {    
       sendNetCmd("4008");
       add_in("Increase Contrast Large");
     } else if(source == decContrastSm) {
       sendNetCmd("4005");
       add_in("Decrease Contrast Small");
     } else if(source == decContrastLg) {
       sendNetCmd("4007");
       add_in("Decrease Contrast Large");
     } else if(source == autoFocus) {
       sendNetCmd("3003");
       add_in("Running AutoFocus");
     } else if(source == reFresh) {
       if(imageCanvas.screenMode == 2) sendNetCmd("5001");
       else if(imageCanvas.screenMode == 4) sendNetCmd("5002");
       else if(imageCanvas.screenMode == 5) sendNetCmd("5003");
       add_in("Refreshing Screen");
     } else if(source == xyMove) {
       sendNetCmd("1009 " + xValue + " " + yValue); //Unfinished
     } else if(source == xrayLabel) {
       if(xRay) {
         xrayLabel.setLabel("Xray Map (off)");
         xRay = false;
         sendNetCmd("8001");
       } else {
         xrayLabel.setLabel("Xray Map (on)");
         xRay = true;
         sendNetCmd("8002");
       }
       add_in("Switching to XRay Map");
     } else if(source == xrayStart) {
       if(xRay) {
         sendNetCmd("8101");
       }
       add_in("Starting XRay");
     } else if(source == xrayStop) {
       if(xRay) {
         sendNetCmd("8102");
       }
       add_in("Stopping XRay");
     } else if(source == savePic) {
       //Unfinished
       add_in("Launching Save");
     }
  } else {
    wServer.connect();
    sendNetCmd(userName.getText() + "@@" + passWord.getText());
    bLogin = true;
  }
}

public void textValueChanged(TextEvent evt) {
}

public void itemStateChanged(ItemEvent evt) {
  Object source = evt.getSource();
  sendChoiceCmd((Choice)source);
}  

void sendChoiceCmd(Choice c) {
  int index;

  index = c.getSelectedIndex();
  if (c == secBack) {
    if (index == 0) {
      sendNetCmd("4009");
      add_in("Switching to Secondary");
    } else if (index == 1) {
      sendNetCmd("4010");
      add_in("Switching to Backscatter");
    }
  } else if (c == dispMode) {
    if (index == 0) {
      sendNetCmd("5001");
      add_in("Switching to Left");
    } else if (index == 1) {
      sendNetCmd("5002");
      add_in("Switching to Right");
    } else if (index == 2) {
      sendNetCmd("5003");
      add_in("Switching to Full");
    }
  } else if (c == magBox) {
    if(index == 1) {
      sendNetCmd("2005");
      magBox.select(0);
      add_in("Zooming to Minimum");
    } else if(index == 2) {
      sendNetCmd("2006");
      magBox.select(0);
      add_in("Zooming to 100X");
    } else if(index == 3) {
      sendNetCmd("2007");
      magBox.select(0);
      add_in("Zooming to 1000X");
    } else if(index == 4) {
      sendNetCmd("2008");
      magBox.select(0);
      add_in("Zooming to 10000X");
    }
  }
}

void sendNetCmd(String s) {
  delay(500);
  wServer.sendCommand(s);
}

void getNetCmd(String s) {
  String cmd = null;
  String cmd1 = null;
  String args = null;
  String arg1 = null;
  String arg2 = null;
  int sp1 = 0;
  if(s.length() <= 0) return;
  s = s.trim();
  if(s.length() > 4) {
    cmd = s;
    sp1 = s.indexOf(" ");
    if(sp1 >= 0) {
      cmd1 = cmd.substring(0, sp1).trim();
      args = cmd.substring(sp1).trim();
      sp1 = args.indexOf(" ");
      if (sp1 <= 0) {
        arg1 = args.trim();
        arg2 = null;
      } else {
        arg1 = args.substring(0,sp1).trim();
        arg2 = args.substring(sp1).trim();
      }
    } else {
      cmd1 = s;
      arg1 = null;
      arg2 = null;
    }
  } else {
    cmd1 = cmd = s;
  }
  //delay(100);
  if(cmd1.equals("0108")) {
    sendNetCmd("0109");
  } else if(s.equals("Active")) {
    fActive();
  } else if(s.equals("Passive")) {
    fPassive();
  } else if(cmd1.equals("5001")) {
    imageCanvas.switchMode(2);
    sendNetCmd("9001 9001");
  } else if(cmd1.equals("5002")) {
    imageCanvas.switchMode(4);
    sendNetCmd("9001 9001");
  } else if(cmd1.equals("5003")) {
    imageCanvas.switchMode(5);
    sendNetCmd("9001 9001");
  } else if(cmd1.equals("9999")) {
      add_in("Closing");
      halt();
  } else if(cmd1.equals("9001")) {
    //delay(1000);
    imageCanvas.updateScreen();
    add_in("Ready");
  } else if(cmd1.equals("1009") || 
            cmd1.equals("1010") ||
            cmd1.equals("1011") ||
            cmd1.equals("1012")) {
    xValue = xValue.valueOf(arg1);
    yValue = yValue.valueOf(arg2);
    xField.setText("" + ((float)xValue.floatValue()/100.0f));
    yField.setText("" + ((float)yValue.floatValue()/100.0f));
    sendNetCmd("9001 9001");
  } else if(cmd1.substring(0,2).equals("20")) {
    magValue = magValue.valueOf(arg1);
    sendNetCmd("9001 9001");
  } else if(cmd1.equals("4011")) {
    if(arg1.equals("0")) {
      secBack.select(0);
      sendNetCmd("4009");
    } else if(arg1.equals("1")) {
      secBack.select(1);
      sendNetCmd("4010");
    }
    if(arg2.equals("1")) {
      dispMode.select(0);
      if(imageCanvas.screenMode != 1 && imageCanvas.screenMode != 2) sendNetCmd("5001");
    } else if(arg2.equals("2")) {
      dispMode.select(1);
      if(imageCanvas.screenMode != 4) sendNetCmd("5002");
    } else if(arg2.equals("3")) {
      dispMode.select(2);
      if(imageCanvas.screenMode != 5) sendNetCmd("5003");
    }
    sendNetCmd("9001 9001");
  } else if(cmd1.equals("3003")) {
    sendNetCmd("9001 9001");
  } 
}

public void mouseReleased(MouseEvent e) {
  int x,y;
  int scrMode;
  x = e.getX();                 // current mouse x,y position
  y = e.getY();

  scrMode = imageCanvas.screenMode;

  if (scrMode == 2 || scrMode == 1) { //Left Move
    if (x < 256 && y >= 40 && y <= 295) {
      sendNetCmd("1010 "+x+" "+y);
      add_in("Moving...");
    } else if (x >= 64 && x <= 192 && y >= 324 && y <= 452) { // Macro Move
      sendNetCmd("1011 "+x+" "+y);
      add_in("Moving...");
    }
  } else if (scrMode == 4) { // Right move
    if (x < 256 && y >= 40 && y <= 295) {
      sendNetCmd("2016 "+x+" "+y);
      add_in("Moving...");
    } else if (x >= 64 && x <= 192 && y >= 324 && y <= 452) { // Macro Move
      sendNetCmd("1011 "+x+" "+y);
      add_in("Moving...");
    } else if (x < 255 && y >= 40 && y <= 295) {
      if(specMode == false) {
        sendNetCmd("6001 "+x+" "+y);
        specMode = true;
        add_in("Starting Spectrum");
      } else {
        sendNetCmd("6003");
        specMode = false;
        add_in("Ending Spectrum");
      } 
    }  
  } else if (scrMode == 5) { // Full Move
     sendNetCmd("1010 "+x+" "+y);
  }
}

public void mouseEntered(MouseEvent e) {
  imageCanvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
}

public void mouseExited(MouseEvent e) {
  imageCanvas.setCursor(Cursor.getDefaultCursor());
}

public void mouseClicked(MouseEvent e) {
  
}

public void mousePressed(MouseEvent e) {
  
}

public void delay(int millis) {
  try {
    Thread.sleep(millis);
  } catch (Exception e) {}
}

} //End SEMWebClient

//Begin SEMListener
class SEMListener extends Thread {

public String inputString;
BufferedReader rdr;
SEMWebClient pc;
SEMSocket sc;
boolean loop = true;

SEMListener(BufferedReader reader, SEMWebClient console, SEMSocket send) {
  inputString = new String();
  this.rdr = reader;
  this.pc = console;
  this.sc = send;
  loop = true;
}
   
public void run() {
  while (loop) {
    inputString = "";
    try {
      inputString = rdr.readLine();  
    } catch (IOException e) {
      System.out.println("IOException error: "+e);
    }
    if (inputString != null)
      processCommand();
  }
}

public void processCommand() {
   pc.getNetCmd(inputString);
}
} // End ListenerThread4

//Begin SEMSocket
class SEMSocket extends Thread implements Runnable {
private BufferedReader reader;
private PrintWriter writer;
private SEMClient client;
public String fHostName;
public int fHostPort;
public SEMListener runner;
public SEMWebClient pc; 
private boolean connect = false;

public SEMSocket(String hostName, SEMWebClient console) {
  fHostPort = 8000;
  fHostName = new String();
  fHostName = hostName;
  pc = console;
}

public void connect() {       
   if (MOpenConnection()) {
      runner = new SEMListener(reader,pc,this);
      runner.start();
      }
   else
      System.out.println("Connection failed");
}
  
public boolean MOpenConnection() {
  boolean success = true;

  try {
    client = new SEMClient(fHostName,fHostPort);
    reader = new BufferedReader(new InputStreamReader(client.in));
    writer = new PrintWriter(new OutputStreamWriter(client.out));
  } catch (IOException e) {
    success = false;
    System.out.println(e+" Connection failed!");
  }
      
  connect = success;   
  return success;
}
   
public void MCloseConnection() {
  connect = false;
  client.close();
  reader = null;
  writer = null;
}

public void MReopenConnection() {
  if (MOpenConnection()) {
    runner = new SEMListener(reader,pc,this);
    runner.start();
  } else 
    System.out.println("Connection failed");
}   

public void sendCommand(String psem_str) {
  if (writer != null && connect) {
    writer.println(psem_str);
    writer.flush();
  }
}
} // End SEMSocket

//Begin SEMClient  
class SEMClient {

public InputStream in;
public OutputStream out;

private Socket client;

public SEMClient(String host, int port) throws IOException {
  try {
    client = new Socket(host,port);
    out = client.getOutputStream();
    in = client.getInputStream();
  } catch (IOException e) {
    System.out.println(e);
    throw e;
  }
}

public void close() {
  try {
    client.close();
  } catch (IOException e) {
    System.out.println("Error closing socket");
  }
  in = null;
  out = null;
} 
}//End SEMClient

//Begin SEMCanvas
class SEMCanvas extends Canvas {
  SEMWebClient parent;
  int screenMode = 0;
  Image image;
  MediaTracker tracker;
  int diskWidth;
  int diskHeight;
  int pixel[];
  int membuf[];
  int olaybuf[];
  int x=0,y=0;
  int combine;
  int olayStart,olayEnd;
  URL codebase = null;
  int alpha = 255;
  int WHITE = 0;
  int CLEAR = 0;
  boolean displayStatus = false;

public SEMCanvas(int width, int height, URL url1, SEMWebClient dad) {
  parent = dad;
  setSize(width,height);
  pixel = new int[262144];
  if (pixel == null)
    System.out.println("Error allocating pixel array");
  membuf = new int[262144];
  if (membuf == null)
    System.out.println("Error allocating membuf array");
  olaybuf = new int[262144];
  if (olaybuf == null)
    System.out.println("Error allocating olaybuf array");
  image = null;
  codebase = url1;
  tracker = new MediaTracker(this);
  CLEAR = (alpha << 24)|(WHITE << 16)|(WHITE << 8)|WHITE;      // black
}

private void delay(int millis) {
  try {
    Thread.sleep(millis);
  } catch (Exception e) {}
}

public void update(Graphics g) {
  paint(g);
}

public void paint(Graphics g) {
   g.drawImage(image,0,0,this);
}

public Insets getInsets() {
  return new Insets(10,10,10,10);
}

public void switchMode(int NewMode) {
  screenMode = NewMode;
}

public void updateScreen() {
  boolean olay = false;
  
  int imageSize=0;
  URL url=null;
  BufferedInputStream bs=null;
  DataInputStream ds = null;
	int i = 0;

  delay(1000);

  String imageName = new String();     
  imageName = "initial.jpg";
  diskWidth = 512;
  diskHeight = 512;
  x = 0;
  y = 0;


  if(screenMode != 5) { 
    olay = true;
    read_Overlay(0);
  } else {
    eraseAll();
    olay = false;
  }
  
  imageSize = diskWidth*diskHeight;
   
  try {
    url = new URL(codebase,imageName);
  } catch (MalformedURLException e1) {
    System.out.println("URL Error");
  }             

  if (screenMode == 6)                  // Spectrum
    read_Overlay(1);
  else if (screenMode == 7)             // X-Ray Map 
    getJpegImage(url,x,y,diskWidth,diskHeight);
  else if (screenMode != 9)             // All else
    readJpegImage(url,x,y,diskWidth,diskHeight);

  if (screenMode != 7 )                 // Combine Mem and Overlay except for X-Ray Map
    combine_Mem_Olay(screenMode,olay);

  MemoryImageSource mis = new MemoryImageSource(512,512,pixel,0,512);
  image = createImage(mis);
  tracker.addImage(image,0,512,512);
        
  try {tracker.waitForAll();}
  catch (InterruptedException e) {}

  repaint();
  
  tracker.removeImage(image,0);
}

public void getJpegImage(URL url,int x, int y, int diskWidth, int diskHeight) {
  Image img = null;
  int[] jpgpix = null;
  PixelGrabber pg = null;
  int i,ret;
  int index;
  boolean results=false;
  
  byte[] image;
  byte[] realImage;
  DataInputStream gifImage = null;
  int count = 0;

  Toolkit tk = Toolkit.getDefaultToolkit();
  //img = tk.getImage(url);
  
  image = new byte[80 * 1024];
  try {
    gifImage = new DataInputStream(url.openStream());
  } catch (IOException e) {System.out.println("getJpegImage Error1");}
  try {
    count = gifImage.read( image );
  } catch (IOException e) {System.out.println("getJpegImage Error2");}
  realImage = new byte[ count ];
  System.arraycopy( image, 0, realImage, 0, count );
  img = tk.createImage(realImage);
    
  tracker.addImage(img,0);
   
  try { 
	  tracker.waitForAll();
	} catch (InterruptedException e) {
		System.out.println("Error waiting for image load");
  }
      
  jpgpix = new int[diskWidth * diskHeight];
  pg = new PixelGrabber(img,0,0,diskWidth,diskHeight,jpgpix,0,diskWidth);
   
  try {
    results = pg.grabPixels();
  } catch (InterruptedException e) {
    System.out.println("Grabbing interrupted "+e);
  } catch (Exception e1) {
    System.out.println("General exception grabbing "+e1);
  }
      
  
  for (i=0;i<diskWidth*diskHeight;i++) {
    if (diskHeight == 256)
      index = 512*y + x + i/256*512 + i%256;
    else
      index = 512*y + i;
    pixel[index] = jpgpix[i];
  }
	tracker.removeImage(img,0);
	  try {
    gifImage.close();
  } catch (IOException e){}
}
   
public void readJpegImage(URL url,int x, int y, int diskWidth, int diskHeight) {
  Image img = null;
  PixelGrabber pg = null;
  int jpgpix[] = null;
  int i,ret;
  int index;
  boolean results=false;
  
  byte[] image;
  byte[] realImage;
  DataInputStream gifImage = null;
  int count = 0;

  Toolkit tk = Toolkit.getDefaultToolkit();
  img = tk.getImage(url);
  
  image = new byte[80 * 1024];
  try {
    gifImage = new DataInputStream(url.openStream());
  } catch (IOException e) {
    System.out.println("readJpegImage error1");
    return;
  }
  try {
    count = gifImage.read( image );
  } catch (IOException e) {System.out.println("readJpegImage error2");}
  
  realImage = new byte[ count ];
  System.arraycopy( image, 0, realImage, 0, count );
  img = tk.createImage(realImage);
  realImage = null;
  count = 0;
    
  tracker.addImage(img,0);
  
  try { 
    tracker.waitForAll();
  } catch (InterruptedException e) {
    System.out.println("Error wait for image load");
  }
   
  jpgpix = new int[diskWidth*diskHeight];
  pg = new PixelGrabber(img,0,0,diskWidth,diskHeight,jpgpix,0,diskWidth);
     
  try {
    results = pg.grabPixels();
  } catch (InterruptedException e) {
    System.out.println("Grabbing interrupted "+e);
  } catch (Exception e1) {
    System.out.println("General exception grabbing "+e1);
  }
   
  for (i=0;i<diskWidth*diskHeight;i++) {
    if (diskHeight == 256)
      index = 512*y + x + i/256*512 + i%256;
    else
      index = 512*y + i;
    membuf[index] = jpgpix[i];
    pixel[index] = jpgpix[i];       // added 7/15/99
  }
  tracker.removeImage(img,0);
    try {
    gifImage.close();
  } catch (IOException e){System.out.println("Can't Close gifImage");}
}

public void read_Overlay(int type) {
  int i=0;
  int y0 = 0, x0 = 0;
  int byteValue = 0;
  int curr_color = 0;
  int curr_run = 0;
  int index = 0;
  final int RUN_4BIT = 0x00;
  final int RUN_8BIT = 0x40;
  final int RUN_16BIT = 0x80;
  final int RUN_32BIT = 0xc0;
  int BLACK = 255;
  int WHITE = 0;
  int alpha = 255;
  int red,green,yellow,black;
  URL url=null;
  DataInputStream ds = null;

  try {
    if (type == 0)
      url = new URL(codebase,"olay.rle");
    else
      url = new URL(codebase,"spec.rle");
  } catch (MalformedURLException e1) {
    System.out.println("URL Error");
  }
      
  try {
    ds = new DataInputStream(url.openStream());
  } catch (IOException e3) {
    System.out.println("I/O Error getting image");
    if (ds != null) {
      try {
        ds.close();
      } catch (IOException e1){}
    } 
    return;
  }
  
  catch (Exception e) {
    System.out.println("Exception error : "+e);
    if (ds != null) {
      try {
        ds.close();
      } catch (IOException e2){}
    }
    return;
  }
                   
  y0 = getShort(ds);            // read starting line #
  olayStart = y0;

  black = (alpha << 24)|(WHITE << 16)|(WHITE << 8)|WHITE;      // black
  red = (alpha << 24)|(BLACK << 16)|(WHITE << 8)|WHITE;      // red
  green = (alpha << 24)|(WHITE << 16)|(BLACK << 8)|WHITE;      // green
  yellow = (alpha << 24)|(BLACK << 16)|(BLACK << 8)|WHITE;      // yellow
   
  while (y0 > -1 && y0 < 512) {
    byteValue = getByte(ds);          // Read next value
    if (byteValue == -1) {              // EOF flag (-1)
      System.out.println("EOF found");
      try {
        ds.close();
      } catch (IOException e){}
      return;
    }
    curr_color = (byteValue >> 4) & 0x03;     // get color (0,1,2,3)
         
    switch(byteValue& 0xc0) {
      case RUN_4BIT:
        curr_run = byteValue & 0x0f;
        break;
      case RUN_8BIT:
        curr_run = getByte(ds);
        break;
      case RUN_16BIT:
        curr_run = getUShort(ds);
        break;
      case RUN_32BIT:
        curr_run = getInt(ds);
        break;
    }

    for (i = 0; i<curr_run; i++) {              // Paint pixels in image  
      index = 512*y0 + x0;                   // calculate index into image
      if (curr_color == 0)
        olaybuf[index] = black;      // black
      else if (curr_color == 1)
        olaybuf[index] = red;      // red
      else if (curr_color == 2)
        olaybuf[index] = green;      // green
      else if (curr_color == 3)
        olaybuf[index] = yellow;      // yellow
      if (++x0 > 511) {                        // adjust x0,y0 when x0 > 511 (new line)
        x0 = 0;
        y0++;
      }
      if (y0 == 512)
        break;
    }
  }   
  
  try {
    ds.close();
  } catch (IOException e){}
  y0 = olayEnd;
}

public void combine_Mem_Olay(int mode, boolean olay) {
  int i;
  
  for (i=0;i<511*512;i++) {
    if (olaybuf[i] == CLEAR) pixel[i] = membuf[i];
    else pixel[i] = olaybuf[i];
  }
}

public void eraseAll() {
  int x,y,i;
  image = null;
  for(y=0;y<511;y++) {
    for(x=0;x<511;x++) {
      i = 512*y + x;
      pixel[i] = CLEAR;
      membuf[i] = CLEAR;
      olaybuf[i] = CLEAR;
    }
  }
}

public void eraseMemRight() {
  int i,x0,y0,x1,y1,x,y;

  x0 = 256;
  y0 = 40;
  x1 = 511;
  y1 = 295;

  for (y = y0; y<y1; y++) {
    for (x = x0; x<x1; x++) {
      i = 512*y + x;
      membuf[i] = CLEAR;
    }
  }
}

public void eraseMemMacro() {
  int i,x0,y0,x1,y1,x,y;

  x0 = 64;
  y0 = 324;
  x1 = 192;
  y1 = 452;

  for (y = y0; y<y1; y++) {
    for (x = x0; x<x1; x++) {
      i = 512*y + x;
      membuf[i] = CLEAR;
    }
  }
}

private int getUShort(DataInputStream ds) {
  int y;
  int hi,lo;

  try {                                // read starting line #
    y = ds.readUnsignedShort();
  } catch (IOException e) {
    System.out.println("Error reading short : "+e);
    return(-1);
  }

  lo = y & 0x00ff;                        // swap byte order
  hi = y & 0xff00;
  y = (lo << 8) + (hi >> 8);     
  return(y);
}

private int getShort(DataInputStream ds) {
  int y;
  int lo,hi;

  try {                                // read starting line #
    y = ds.readShort();
  } catch (IOException e) {
    System.out.println("Error reading short : "+e);
    return(-1);
  }
  
  lo = y & 0x00ff;                        // swap byte order
  hi = (y >> 8) & 0x00ff;
  y = (lo << 8) + hi;  
  return(y);
}

private int getByte(DataInputStream ds) {
  int y;

  try {                                // read starting line #
    y = ds.readUnsignedByte();
  } catch (IOException e) {
    System.out.println("Error reading byte : "+e);
    return(-1);
  }
  return(y);
}

private int getInt(DataInputStream ds) {
  int y,i;

  int byteArray[] = new int[4];
  try {                                // read starting line #
    y = ds.readInt();
  } catch (IOException e) {
    System.out.println("Error reading integer : "+e);
    return(-1);
  }

  byteArray[0] = (y & 0xff);
  for (i = 1; i<4; i++) {
    y = (y >> 8);
    byteArray[i] = (y & 0xff);
  }
   
  y = 0;
  y = (byteArray[0] & 0xff);
  for (i = 1; i < 4; i++)
    y = (y << 8) + byteArray[i];

  return(y);
}

} //End SEMCanvas