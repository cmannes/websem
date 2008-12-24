import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class OldVB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private int ComPort = 0;
	private String desImagePath = null;
	private String srcImagePath = null;
	private String masterPassword = null;
	private int filenum = 0;
	private String filename = null;
	private String CmdBuffer = null;
	private String lastCmd = null;

	private String actUser = null;
	private String actPword = null;

	private String psvUser = null;
	private String psvPword = null;

	private int NumSockets = 0;
	private int activeIndex = 0;
	private boolean socket[];
	private boolean vsocket[];
	private String vtData = null;
	private String socketCmd = null;
	private int Gen_num = 0;
	private int MaxSockets = 0;

	public void doLogin(String phrase, int i) {
		if (CheckUser(phrase) == 1) {
			// Active User
			if (activeIndex == 0) {
				activeIndex = i;
				SendSocketCmd(i, "Active");
				GenMsg("Socket " + i + " is active user.");
				vsocket[i] = true;
				return;
			}
			if (activeIndex == i) {
				SendSocketCmd( i, "Active");	
				GenMsg("Active User Reinit.");
				vsocket[i] = true;
				return;
			} else {
				SendSocketCmd(i, "Passive");
				GenMsg("Active user already logged in.  Socket " + i + " is passive user.");
				vsocket[i] = true;
				return;
			}
		}
		if(CheckUser(phrase) == 2) {
			//Passive User
			SendSocketCmd(i, "Passive");
			GenMsg("Socket " + i + " is passive user.");
			vsocket[i] = true;
			SendSocketCmd(i, "9001 9001");
			return;
		}
		//'Bad User
		SendSocketCmd(i, "9999");
		GenMsg("Socket " + i + " login failure.");
	}


	private void SendSocketCmd(int i, String string) {
		// TODO Auto-generated method stub
		// Was a command from Java Client to VB Server
		// Since this is the VB Server...
		
	}


	public int CheckUser(String phrase) {
	  int CheckUser = 0;
	  if(phrase.split("@@")[0].trim().equals(actUser) &&
			  phrase.split("@@")[1].trim().equals(actPword)) {
		  CheckUser = 1;
	  }
	  if(phrase.split("@@")[0].trim().equals(psvUser) &&
			  phrase.split("@@")[1].trim().equals(psvPword)) {
		  CheckUser = 2;
	  }
	  return CheckUser;
	}

	private void DoEvents() {
		// TODO Auto-generated method stub
		
	}
	
	public void WriteIni() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("server.ini")));
			bw.write(srcImagePath);
			bw.write(desImagePath);
			bw.write(actPword);
			bw.write(psvPword);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			System.out.println("Error opening SERVER.INI");
			return;
		}
	}

	public void doCmdHandler(String cmd) {
		DoEvents();
		SendSerialCmd(cmd);
	}

	private void InitSerial_Click() {
	  InitializeComPort();
	}

	private void MsgBox(String string) {
		// TODO Auto-generated method stub
		
	}

	//DataArrival event is fired when the client sends data
	private void Winsock1_DataArrival(int index, long bytesTotal) {
//	  Dim login
	   //get command from socket
	   //Winsock1[index].GetData(vtData, vbString);
	   DoEvents();
	   vtData = StrReverse(vtData);
	   while (Int(Asc(vtData)) < 15) {
	     vtData = Mid(vtData, 2);
	     if (Len(vtData) <= 0) {
	       break;
	     }
	   }
	   vtData = StrReverse(Trim(vtData));
	   
	   if (vtData.equals("9999")) {
	     return;
	   }
	   
	   if (vsocket[index] == false) {
	     if (InStr(vtData, "@@") > 0) {
	       doLogin(vtData, index);
	       //vsocket(index) = CheckUser(vtData, index)
	     } else {
	       SendSocketCmd(index, "9999");
	     }
	   } else {
	     if (index == activeIndex) {
	       doCmdHandler (vtData);
	     } else {
	       GenMsg ("Cmd recieved from Non-Active Client!");
	     }
	   }
	   
//	   if(Err){
//	      GenMsg ("Winsocket Getdata Error:" + Str(Err));
//	   }
	}


	private void GenMsg(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
		
	}

	//Serial Communication

	private void MSComm1_OnComm() {

//		String inString = null;
//		String vtAnswer = null;
//		long cmd = 0;
//		String nullChar = null;
//		int strLen = 0;
//	int strlen1 = 0;
//	int nWait = 0;
//	int foundCRLF = 0;
//	String tmpString = null;
//
//	foundCRLF = 0;
//	nullChar = new Character((char)0).toString();
//	nWait = 1;
//
//	switch(MSComm1.CommEvent) {
//	//Error cases
//	   case comEventBreak:
//	      Beep();
//	      MsgBox ("Event Break");
////	      On Error Resume Next
//	   case comEventCDTO:
//	      Beep();
//	      MsgBox ("Event CDTO");
////	      On Error Resume Next
//	   case comEventCTSTO:
//	      Beep();
//	      MsgBox ("Event CTSTO");
////	      On Error Resume Next
//	   case comEventFrame:
//	      Beep();
//	      MsgBox ("Event Frame");
////	      On Error Resume Next
//	   case comEventOverrun:
//	      Beep();
//	      MsgBox ("Com Overrun Error - character lost");
////	      On Error Resume Next
//	   case comEventRxOver:
//	      Beep();
//	      MsgBox ("Com Receive Buffer Full");
////	      On Error Resume Next
//	   case comEventTxFull:
//	      Beep();
//	      MsgBox ("Com Transmit Buffer Full");
////	      On Error Resume Next
//	   case comEventDCB:
//	      Beep();
//	      MsgBox ("Event DCB");
////	      On Error Resume Next
//	   case comEventRxParity:
//	      Beep();
//	      MsgBox ("Com Parity Error");
////	      On Error Resume Next
//	//Event cases
//	   case comEvCD:
//	   case comEvCTS:
//	   case comEvDSR:
//	   case comEvRing:
//	   case comEvReceive:
////	      On Error Resume Next
//	      if(MSComm1.InBufferCount) {
//	        CmdBuffer = CmdBuffer + MSComm1.Input;
//	        if (Int(Asc(StrReverse(CmdBuffer))) == 13 || Int(Asc(StrReverse(CmdBuffer))) == 10) {
//	          ProcessSerial (CmdBuffer);
//	          CmdBuffer = "";
//	        }
//	      }
//	   case comEvSend:
//	   case comEvEOF:
//	     GenMsg ("EOF");
//	      //EOF detected at end of data received
//	      MSComm1.InputLen = 0;
//	      if(MSComm1.InBufferCount) {
//	         inString = MSComm1.Input;
//	         ProcessSerial (inString);
//	      }
//	}
//	//DoEvents
//	   //Exit Sub
	}

	  private void Beep() {
		// TODO Auto-generated method stub
		
	}


	public String StrReverse(String source) {
		    int i, len = source.length();
		    StringBuffer dest = new StringBuffer(len);

		    for (i = (len - 1); i >= 0; i--)
		      dest.append(source.charAt(i));
		    return dest.toString();
		  }
	
	  public String Asc(String inStr) {
		  StringBuffer sbReturn = new StringBuffer();
		  for ( int i = 0; i < inStr.length(); ++i ) {
		       char c = inStr.charAt( i );
		       int j = (int) c;
		       sbReturn.append(j);
		       System.out.println(j);
		       }
		  return sbReturn.toString();
	  }
	  
	  public int Int(String inStr) {
		  return Integer.parseInt(inStr);
	  }
	  
	  public String Mid(String inString, int start) {
		  return inString.substring(start);
	  }
	  
	  public int Len(String inString) {
		  return inString.length();
	  }
	  
	  public String Trim(String inString) {
		  return inString.trim();
	  }
	  
	  public int InStr(String inStr, String look) {
		  return inStr.indexOf(look);
	  }
	  
	  private String Left(String inStr, int iLen) {
		  return inStr.substring(0,iLen);
	  }
	  
	  private String Right(String inStr, int iLen) {
		  return inStr.substring(inStr.length() - iLen);
	  }
	  
	public void ProcessSerial(String inSer) {
	  int sp1 = 0;
	  int sp2 = 0;
	  String cmd = null;
	  String cmd1 = null;
	  String arg1 = null;
	  String arg2 = null;
	  String args = null;
	  String cmdBuff = null;
	      
	  long ErrorNumber = 0;
	  String Source = null;
	  String destination = null;
	  int filecopy_ctr = 0;
	     
	  filecopy_ctr = 1;
	  inSer = StrReverse(inSer);
	  while(Int(Asc(inSer)) < 15) {
	    inSer = Mid(inSer, 2);
	    if (Len(inSer) <= 0) {
	      break;
	    }
	}
	  inSer = Trim(StrReverse(inSer));
	  if (Len(inSer) > 4) {
	    cmd = inSer;
	    sp1 = InStr(cmd, " ");
	    cmd1 = Trim(Left(cmd, sp1 - 1));
	    args = Trim(Right(cmd, Len(cmd) - sp1));
	    sp1 = InStr(args, " ");
	    if (sp1 <= 0) {
	      arg1 = Trim(args);
	      arg2 = "";
	    } else {
	      arg1 = Trim(Left(args, sp1 - 1));
	      arg2 = Trim(Right(args, Len(args) - sp1));
	    }
	  } else {
	    cmd1 = inSer;
	    arg1 = "";
	    arg2 = "";
	  }
	    
	  GenMsg ("Recv Serial: '" + cmd1 + "' '" + arg1 + "' '" + arg2);
	  
//	ReTry:
	                 
//	   On Error GoTo FileCheck
	  try {
		   if (cmd1.equals("9001")) {
		      destination = desImagePath + "initial.jpg";
		      Source = srcImagePath + "initial.jpg";
		      FileCopy(Source, destination);
		      DoEvents();
		      GenMsg ("FileLen " + FileLen(Source));
		      SendSocketsCmd ("9091 " + FileLen(Source));
		      GenMsg ("Copy " + Source + " " + destination);
		      return;
		   } else if (cmd1 == "9005" || cmd1 == "9008") {
		      Source = srcImagePath + "olay.rle";
		      destination = desImagePath + "olay.rle";
		      FileCopy(Source, destination);
		      DoEvents();
		      SendSocketsCmd ("9098");
		      GenMsg ("Copy " + Source + " " + destination);
		      return;
		   } else if (cmd1 == "9006") {
		      Source = srcImagePath + "spec.rle";
		      destination = desImagePath + "olay.rle";
		      FileCopy(Source, destination);
		      DoEvents();
		      SendSocketsCmd ("9096");
		      GenMsg ("Copy " + Source + " " + destination);
		      return;
		   } else if (cmd1 == "0108") {
		      GenMsg ("Ping Received");
		      SendSerialCmd ("0109");
		   }
		   DoEvents();
		   if (cmd1 == "" || Len(cmd1) < 4) {
		     return;
		   }
		   if (arg2 == "" || Len(arg2) < 1) {
		     if (arg1 == "" || Len(arg1) < 1) {
		       cmdBuff = cmd1;
		     } else {
		       cmdBuff = cmd1 + " " + arg1;
		       SendSocketsCmd (cmd1 + " " + arg1);
		       return;
		     }
		   } else {
		     cmdBuff = cmd1 + " " + arg1 + " " + arg2;
		   }
		   SendSocketsCmd (cmdBuff);
	  } catch (Exception e) {
		   GenMsg ("File Copy Error");
//		   ErrorNumber = Err.Number;
//		   switch(ErrorNumber) {
//		   case 70://Permission denied - file open
		        filecopy_ctr = filecopy_ctr + 1;
		        if (filecopy_ctr > 10) {
		        	for(int i = 0 ; i < 300 ; i++) {
		        	}
		        }
//		        Resume ReTry		  
	  }
	}


	private void SendSocketsCmd(String string) {
		// TODO Auto-generated method stub
		
	}


	private long FileLen(String source) {
		File fTmp = new File(source);
		
		return fTmp.length();
	}


	private void FileCopy(String source, String destination) {
		
		File fTmp = new File(source);
		fTmp.renameTo(new File(destination));
		
	}


	public void SendSerialCmd(String cmd) {
	  GenMsg ("Sending Serial Cmd: " + cmd);
//	  MSComm1.Output = cmd + vbLf;
	  //SendSocketsCmd (cmd)
	}


	public void InitializeComPort() {
//	   if (MSComm1.PortOpen == true) {
//	      MSComm1.PortOpen = false;
//	   }
//	   
//	   MSComm1.CommPort = ComPort;//comport serial port
//	   MSComm1.Settings = "9600,N,8,1";
//	   MSComm1.InputLen = 0;
//	   MSComm1.EOFEnable = false;
//	   MSComm1.NullDiscard = false;
//	   MSComm1.InBufferSize = 2048;
//	   MSComm1.OutBufferSize = 2048;
//	   MSComm1.RThreshold = 1;
//	   MSComm1.SThreshold = 1;
//	   MSComm1.PortOpen = true;
	   GenMsg ("Opening COM" + ComPort + " port");

	}
}
