//MINUMUM JAVA VERISON 1.8

//FFS I SHOULD JUST IMPORT EVERYTHING AND BE DONE WITH IT!!!!
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


//ADD DELETE PREFIX BUTTON, REMOVE PREFIX DELTEING FROM RUN
//        ON DELETE WARN USER TO STAR GAME WIHT STEAM AND LET IT FAIL
//                ALSO ADD DOCUMNENTAITON TO HELP

/*
Note to the user... or other such person viewing this code. No... I do not know 
what I am doing. I have only done this amount of Java programming long ago... 
and I hated it back then to. I know it can be optimized more... but I have spent
far more time than I am willing to admit... getting the foresaken program working
the way it is. If you encounter a bug that is not explicity stated in the codes 
comments feel free to report it. I'll look into it and work on a solution.
Features are mostly complete, while I am not completly against adding aditional
features if the need arrises. I would like to just focus on maintaining this code
with as little complications as possible.

Also... no I am not using a specific style format. I usually just code in a way
that is easily understandable for me... selfish I know, but i think i can live with that guilt
...sorry for the mess in advance

Thank you for your patience and understanding in advance. And good luck trying 
to decypher this mess if you are so inclined!
Enjoy!
*/

/*
Intent of this program:
This is meant to be a single file that can be easily maintained(read as hardcoded)
while allowing ease of use for the end user and reduce complications of using the
older Python based script. All while providing the same level of functionality
(and maybe a couple neat features now and then if I feel they are needed.)
*/

////////////////////////////////////////////////////////////////////////////////
/*
KNOWN BUGS THAT ARE NOT REALLY BUGS: IE IT'S NOT A BUG... IT'S A FEATURE
1) WINDOW ELEMENTS DO NOT RESIZE OR SHIFT POSITION WHEN THE WINDOW IS RESIZED... I KNOW 
IT'S INCONVENIENT, BUT I DO NOT HAVE THE PATIENCE TO MAKE A SCALABLE WINDOW RIGHT NOW. MAYBE IN THE FUTURE I'LL DO IT
2) THE TEXTFIELD IS LOCKED TO AVOID COMPLICATIONS FOR MANUALLY ENTERING AND SE LOCATION
USERS CAN PASTE THE DIRECTORY INTO THE TextField IN THE POPUP WINDOW WHEN BROWSE IS CLICKED
3)
NOTE: OTHER BUGS MAY BE LISTED IN THE CODE BELOW
*/
////////////////////////////////////////////////////////////////////////////////

//IF NEEDED NEW MESSAGES REGUARDING ISSUES THAT CAN NOT BE SOLVED BY THIS AUTOPATHER
    //(SUCH AS SUDO COMMANDS LIKE IP TABLE MODIFICATION) CAN BE ADDED IN THE FOLLOWING FUNCITON:
    //applyOtherFixes();// IT'S THE EASIEST PLACE

/*//////////////////////////////////////////////////////////////////////////////
TODO: LP= Low Priority; HP=Health Poi... I mean...High Priority

HP: DOTNET STALLS FOR LITERALLY NO APPEARANT REASON... BECAUSE... DOTNET //POSSIBLY FIXED- AN ISSUE WITH BUFFEREDSTREAM-- KEEP THIS UNTIL AFTER RELEASE

//////////////////////////////////////////////////////////////////////////////*/
public class PatchGUI 
{
    //THIS SHOULD DISABLE RUNDLL32.exe===== NOTE THIS IS HARD CODED BELOW CHANGING THIS DOES NOTHING-- KEEP IT FOR REFERENCE
    public static final String DLLOverrideCommand="wine reg add HKEY_CURRENT_USER\\Software\\Wine\\DLLOverrides /v rundll32.exe /t REG_SZ /f";
    public static JFrame MainWindow;
    // NEEDS EASY/SIMPLE ACCESS FROM VARIOUS METHODS
    public static JTextArea textareaOutput;
    public static JButton buttonBrowse;
    public static JCheckBox checkboxCreatePrefix;
    public static JCheckBox checkboxPatchXML;
    public static JCheckBox checkboxRenameVideo;
    public static JCheckBox checkboxRUNDLL32dll;
    public static JCheckBox checkboxSafeOptions;
    public static JButton buttonRun;
    public static JButton buttonBackupPrefix;
    public static JButton buttonDeletePrefix;
    public static File PrefixFolder;
    public static boolean PatchVideo = true;
    public static boolean CreatePrefix= true;
    public static boolean PatchXML = true;
    public static boolean PatchRUNDLL =true;
    public static boolean UseSafeOptions= false;
    public static String SELocation= "/.local/share/Steam/steamapps/common/SpaceEngineers/Bin64/";// DEFAULT PLACE SpaceEnginers.exe SHOULD BE-- IF NOT ASKE THE USER
    public static Font WindowFont;
    
    public static void main(String args[])
    {
        WindowFont = new Font(Font.SERIF,Font.BOLD, 14);//APPLY THIS TO WINDOWS WHERE THE USER WILL HAVE TO DO A LOT OF READING
        //CREATE A LOGFILE SO THE USER CAN UPLOAD IF THEY ENCOUNTER A PROBLEM
        LOGWRITER("-------------------------------------------", false);
        LOGWRITER("-------------NEW PROGRAM START-------------", false);
        LOGWRITER("-------------------------------------------", false);
        LOGWRITER("\n", false);
        LOGWRITER("StartDate", true);
        LOGWRITER("OS: "+System.getProperty("os.name"),false);

        checkPrereqs();
        buildGUI();
        CreateAlertPopup("Make sure you have started Space Engineers from steam at least once\n"
                + "before using this to update the prefix. Otherwise steam will try to overwrite\n"
                + "several components and fail. It will break your game, and probably tell you it failed to load the world.\n"
                + "it's components. Then you may procceed with the prefix installation.");
        
    }
    
    public static void buildGUI()
    {
        //DEFINE BUTTONS AND ELEMENTS AND THE MAINFRAME
        MainWindow = new JFrame();
        //MainWindow.getContentPane().setBackground(Color.DARK_GRAY);
        MainWindow.setSize(1000,650);
        MainWindow.setMinimumSize(new Dimension(50,50));//PREVENT WINDOW FROM BEING 1pxWIDE(HARD TO GRAB AND DRAG WHEN THAT HAPPENS)
        //ALLOW USER TO RESIZE IF THEY WANT BUT ELEMENTS WILL NOT CHANGE
        //MainWindow.setResizable(false);
        
        MainWindow.setLayout(new FlowLayout(FlowLayout.LEFT));
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setTitle("Unofficial Space Engineers Proton Prefix/Patching Tool:");

        JMenuBar topBar = new JMenuBar();
        topBar.getAccessibleContext().setAccessibleDescription("This will help you with this program.");
        topBar.setVisible(true);
        JMenu Help = new JMenu("Help");
        Help.setVisible(true);
        JMenuItem helpoption = new JMenuItem("Show Help");
        helpoption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                ShowHelpPopup();
                LOGWRITER("USER IS SHOWING HELP:", false);// SEE IF THE USER BOTHERS TO SEEK INTERNAL ASSITANCE... I AM CRANKY BECASUE I AM TIRED... GET OVER IT
                textareaOutput.append("Showing Help!");
            }
        });
        
        Help.setMnemonic(KeyEvent.VK_H);
        Help.add(helpoption);
        topBar.add(Help);
        
        JLabel lableSELOC=new JLabel("SpaceEngineers Bin64 dir:");
        //lableSELOC.setForeground(Color.WHITE);
        
        JTextField textfieldSELOC = new JTextField("",45);
        textfieldSELOC.setDisabledTextColor(Color.DARK_GRAY);
        textfieldSELOC.setText(SELocation);
        textfieldSELOC.setEnabled(false);
        
        buttonBrowse = new JButton("Browse");
        buttonBrowse.addActionListener(ShowFileChooser(MainWindow,textfieldSELOC));
        
        checkboxCreatePrefix= new JCheckBox();
        checkboxCreatePrefix.setSelected(CreatePrefix);
        checkboxCreatePrefix.setText("Create Prefix? Uncheck to only apply checked patches to current prefix.");
        checkboxCreatePrefix.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent EVENT)// 1= checked 2= not checked
            {
                if(EVENT.getStateChange()==1)
                    CreatePrefix =true;
                if(EVENT.getStateChange()==2)
                    CreatePrefix =false;
            }
         });
        
        checkboxPatchXML= new JCheckBox();
        checkboxPatchXML.setSelected(PatchXML);
        checkboxPatchXML.setText("Apply config Patch: IE, the dotnet stutter fix. (You probably want this!)");
        checkboxPatchXML.addItemListener(new ItemListener(){    
            @Override
            public void itemStateChanged(ItemEvent EVENT)// 1= checked 2= not checked
            {
                if(EVENT.getStateChange()==1)
                    PatchXML =true;
                if(EVENT.getStateChange()==2)
                    PatchXML =false;
            }
         });
        
        checkboxRenameVideo= new JCheckBox();
        checkboxRenameVideo.setSelected(PatchVideo);
        checkboxRenameVideo.setText("Apply video freeze when starting the game. (It renames the KSH.wmv file so the game ignores it.)");
        checkboxRenameVideo.addItemListener(new ItemListener(){    
            @Override
            public void itemStateChanged(ItemEvent EVENT) 
            {
                if(EVENT.getStateChange()==1)
                    PatchVideo =true;
                if(EVENT.getStateChange()==2)
                    PatchVideo =false;
            }    
         });
        
        checkboxRUNDLL32dll= new JCheckBox();
        checkboxRUNDLL32dll.setSelected(PatchRUNDLL);
        checkboxRUNDLL32dll.setText("Tell wine to disable RUNDLL32.exe: prevents annoying popup when the game starts.");
        checkboxRUNDLL32dll.addItemListener(new ItemListener(){    
            @Override
            public void itemStateChanged(ItemEvent EVENT) 
            {
                if(EVENT.getStateChange()==1)
                    PatchRUNDLL =true;
                if(EVENT.getStateChange()==2)
                    PatchRUNDLL =false;
            }    
         });
        
        checkboxSafeOptions= new JCheckBox();
        checkboxSafeOptions.setSelected(UseSafeOptions);
        checkboxSafeOptions.setText("SafeOptions? This will use dotnet472."
                + "(Use this if dotnet48 fails to install)");//WHY IS DOTNET SUCH A PAIN IN THE ASS?
        checkboxSafeOptions.addItemListener(new ItemListener(){    
            @Override
            public void itemStateChanged(ItemEvent EVENT) 
            {
                if(EVENT.getStateChange()==1)
                    UseSafeOptions =true;
                if(EVENT.getStateChange()==2)
                    UseSafeOptions =false;
            }    
         });
        
        buttonRun = new JButton("Run");
        buttonRun.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                SELocation= textfieldSELOC.getText();//THIS WILL OVERRIDE EVERYTHING IF USER PLACES CUSTOM PFX LOCATION
                if(SELocation.equals(null) || SELocation.equals(""))//IF DEFAULT NOT FOUND
                {
                    PrefixFolder = null;
                    CreateAlertPopup("SpaceEngineers not found.");
                }
                else
                {
                    LOGWRITER("-------------RUN SETTINGS--------------",false);
                    textareaOutput.append("Starting.....\n");
                    LOGWRITER("RUN: \n"+" PatchVideo: "+PatchVideo+
                            "\n CreatePrefix: "+CreatePrefix+
                            "\n PatchXML: "+PatchXML+
                            "\n PatchRUNDLL: "+PatchRUNDLL+
                            "\n UseSafeOptions: "+UseSafeOptions,false);
                    ToggleUIEnabled(false);
                    //OPTION CHECKS ARE DONE IN ALL OF THE FOLLOWING FUNCTIONS
                    applyOtherFixes();
                    applyConfigPatch();
                    createPrefix();
                }
                
            }
        });
        
        buttonDeletePrefix = new JButton("Delete Prefix");
        buttonDeletePrefix.addActionListener(new ActionListener() 
        {@Override public void actionPerformed(ActionEvent ae){deletePrefix();}});
        
        buttonBackupPrefix = new JButton("Backup Prefix");
        buttonBackupPrefix.addActionListener(new ActionListener() 
        {@Override public void actionPerformed(ActionEvent ae){backupPrefix();}});
        
        JButton buttonClose = new JButton("Close");
        buttonClose.addActionListener(new ActionListener()
        {@Override public void actionPerformed(ActionEvent ae){System.exit(0);}});
        
        textareaOutput = new JTextArea();
        textareaOutput.setText("");
        textareaOutput.setLineWrap(true);
        textareaOutput.setEditable(false);
        textareaOutput.setFont(WindowFont);
        DefaultCaret caret = (DefaultCaret)textareaOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scrollpaneWrapper = new JScrollPane(textareaOutput);
        scrollpaneWrapper.setPreferredSize(new Dimension(900, 300));
        
        //MAKE EVERYTHING BIGGER SO IT IS EASIER TO SEE
        lableSELOC.setFont(WindowFont);
        textfieldSELOC.setFont(WindowFont);
        buttonBrowse.setFont(WindowFont);
        checkboxCreatePrefix.setFont(WindowFont);
        checkboxPatchXML.setFont(WindowFont);
        checkboxRenameVideo.setFont(WindowFont);
        checkboxRUNDLL32dll.setFont(WindowFont);
        checkboxSafeOptions.setFont(WindowFont);
        buttonRun.setFont(WindowFont);
        buttonBackupPrefix.setFont(WindowFont);
        buttonDeletePrefix.setFont(WindowFont);
        buttonClose.setFont(WindowFont);
        
        MainWindow.setJMenuBar(topBar);
        MainWindow.add(lableSELOC);
        MainWindow.add(textfieldSELOC);
        MainWindow.add(buttonBrowse);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(checkboxCreatePrefix);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(checkboxPatchXML);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(checkboxRenameVideo);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(checkboxRUNDLL32dll);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(scrollpaneWrapper);
        MainWindow.add(checkboxSafeOptions);
        MainWindow.add(Box.createHorizontalStrut(2400));//PREVENT ELEMNTS FROM SHIFTING ON RESIZE
        MainWindow.add(Box.createVerticalStrut(20));
        MainWindow.add(Box.createHorizontalStrut(300));//DEFAULT MOVE BUTTONS TO CENTER...ish
        MainWindow.add(buttonRun);
        MainWindow.add(buttonBackupPrefix);
        MainWindow.add(buttonDeletePrefix);
        MainWindow.add(buttonClose);
        MainWindow.setVisible(true);
    }
    
    public static void ShowHelpPopup()
    {
        // EXPLAIN WHERE AND HOW TO FIND SE INSTALL LOCATION
        // EXPLAIN WINE AND WINETRICKS INSTALL AND UPDATES
        
/*TODO:
        Make Popup Window With Close Option And TEXT AREA
        ADD TEXT TO TEXT AREA TO A SCROLLPANE
        
        
*/
        JTextArea TextGoesHere = new JTextArea();
        TextGoesHere.setLineWrap(true);
        TextGoesHere.setEditable(false);//SHOULD STILL BE SELECTABLE THOUGH
        
        TextGoesHere.setText(
"\n" +
"This will explain how to do various tasks related to getting Space Engineers running\n" +
"on your Linux computer.\n" +
"Firstly make sure you run the game from steam each time after you delete your prefix\n"+
"Or if you have never run the game before. This will prevent steam from trying to overwrite\n"+
"the changes this application makes.\n" +
"\n" +
"Next ensure you have installed all of the necessary dependencies for this program:\n" +
"\n" +
"Winetricks:\n" +
"See relevant documentation here: \n" +
"https://wiki.winehq.org/Winetricks" +
"\n" +
"--or--\n" +
"https://github.com/Winetricks/winetricks"+
"\n" +
"\n" +
"Wine:\n" +
"https://wiki.winehq.org/Download"+
"\n" +
"\n" +
"Now if you encounter a bug with the program you can report it here:\n" +
"https://github.com/Linux74656/SpaceEngineersLinuxPatches/issues"+
"\n" +
"I'll look into the issue and fix it if necessary\n" +
"\n" +
"Lastly here are some Known issues that have solutions this program\n" +
"cannot solve:\n" +
"\n" +
"-----------------------------------------------------------------------\n" +
"\n" +
"The game will crash shortly after launch due to the KSH analytics server sending back garbage data.\n" +
"The solution is to block the analytics server your systems iptables.\n" +
"On Debian/Ubuntu/PopOS sytems this can be done with: (Highlight: ctrl+c then in terminal right click paste)\n" +
"sudo iptables -A INPUT -s 88.146.207.227 -j DROP\n" +
"\n" +
"That everything for now. If you encounter any issues with the game running\n" +
"in Proton/Steamplay you can come post here: \n" +
"\n" +
"https://github.com/ValveSoftware/Proton/issues/1792"+
"\n" +
"--or chat on the unoffical linux channel of the Keen discord--\n" +
"https://discordapp.com/invite/keenswh"+
"\n" +
"\n" +
"We will be more than glad to try to help you solve any issues." +
"\n");
        TextGoesHere.setCaretPosition(0);
        TextGoesHere.setFont(WindowFont);
        JScrollPane scrollpane = new JScrollPane(TextGoesHere);
        scrollpane.setPreferredSize(new Dimension(750, 400));
        
        JOptionPane.showConfirmDialog(MainWindow, scrollpane, "Help Section:", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
  
    }
    
    
     ///////////////////////////////////////////////////////////////////////////
///// EVERYTHING BELOW HERE SHOULD BE DONE AND TESTED... SHOULD BE... HOPEFULLY /////
     /////////////////////////////////////////////////////////////////////////// ... it definitely is not :(
    
    //WHY DOES JAVA NOT HAVE A NATIVE WAY TO DELETE A FULL DIRECTORY? 
        //IF I WANTED TO HAVE TO WRITE MY OWN FUNCTIONS FOR THE MOST BASIC OF TASK I WOULD USE C
    public static void deletePrefix()
    {
        if(PrefixFolder.exists())
        {
            ToggleUIEnabled(false);
            String PrefixVar = PrefixFolder.getAbsolutePath();
            //ASK USER IF THEY WANT TO GET REPSONSE THEN CONTINUE
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your prefix?", "Delete confirmation:", JOptionPane.YES_NO_OPTION);
            System.out.println("INT: "+confirm);//0=yes 1=no
            if(confirm == 0)
            {
                textareaOutput.append("Deleting prefix!");
                ToggleUIEnabled(false);
                ProcessBuilder procbuild = new ProcessBuilder(new String[]{"winetricks","--force", "-q", "annihilate"});
                Map<String, String> envvar = procbuild.environment();
                envvar.put("WINEPREFIX",PrefixVar);
                Process proc;
                try 
                {
                    proc = procbuild.start(); //Runtime.getRuntime().exec(COMMANDS.get(I));
                    BufferedReader bReadInStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    BufferedReader bReadErrStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                    String consoleOutputText = null;
                    while((consoleOutputText = bReadInStream.readLine()) != null) 
                    {
                        LOGWRITER(consoleOutputText, false);
                        textareaOutput.append(consoleOutputText+"\n");
                    }
                    //GET ERRORS AND WRITE THEM TO LOG AND POPUP GENERIC ERROR MESSAGE
                    while((consoleOutputText = bReadErrStream.readLine()) != null) 
                    {
                        LOGWRITER("ERROR WHEN RUNNING COMMAND: "+consoleOutputText, false);
                        textareaOutput.append(consoleOutputText+"\n");
                    }
                    CreateAlertPopup("You will need to run the game from steam and allow it to fail to install all of\n"
                            + "it's components. Then you may procceed with the prefix installation.");
                } 
                catch (IOException ex) {LOGWRITER("Prefix deletion failed!? "+ex, false);}
            }
            ToggleUIEnabled(true);
        }
        else
        {CreateAlertPopup("Prefix location not found!");}
    }
    
    public static void applyConfigPatch()
    {
        if(PatchXML)//IF FIX CONFIG FILE
        {
            LOGWRITER(" : STARTING CONFIG PATCH-------------------------", true);
            // NO NEED TO MAKE A BACKUP AS THEN WE WOULD HAVE TO KEEP TRACK OF FILE VERSIONS... IT WOULD BE EASIER TO HAVE THE USER VERIFY THIER GAME FILE INTEGRITY.
            File ConfigFile = new File(SELocation+"/SpaceEngineers.exe.config");            
            DocumentBuilderFactory docbuildrfactConfig = DocumentBuilderFactory.newInstance();
            docbuildrfactConfig.setIgnoringComments(true);// JUST INCASE COMMENTS ARE ADDED IN THE FUTURE
            try 
            {
                DocumentBuilder docbuild = docbuildrfactConfig.newDocumentBuilder();
                Document docXMLConfig = docbuild.parse(ConfigFile);
                NodeList xmllistList = docXMLConfig.getElementsByTagName("runtime");//FIND THE RUNTIME NODETREE
                Node nodeRuntime=xmllistList.item(0);//THERE SHOULD ONLY BE ONE NODE WITH THIS, SO GET THE FIRST
                NodeList nodelistRuntime = nodeRuntime.getChildNodes();//THIS SHOULD TELL US ALL OF THE SUBNODES
                boolean FOUND=false;//ITS EASIER THIS WAY... I AM TOO TIRED FOR THIS
                for(int I=0;I<nodelistRuntime.getLength();I++)
                {
                    if(nodelistRuntime.item(I).getNodeName().equals("gcServer"))
                    {
                        System.out.println("FOUND IT! ");
                        textareaOutput.append("gcServer tag found. Config patch not needed.");
                        LOGWRITER("gcServer tag found. Config patch not needed.", false);
                        FOUND=true;
                        break;
                    }
                }
                if(!FOUND)//IF NOT FOUND CREATE IT
                {
                    //NOTE THIS WILL ADD THE TAG TO THE SAME LINE AS RUNTIME... PROGRAMATICALLY IT HAS NO NEGATIVE IMPACT...
                    //IT'LL WORK JUST FINE... AND THE LEVEL OF WORK REQUIERED TO FIGURE OUT HOW TO PRETTIFY IS OUTMATCHED BY MY NEED FOR SLEEP
                    Element GCNODE= docXMLConfig.createElement("gcServer");
                    GCNODE.setAttribute("enabled", "true");
                    nodeRuntime.insertBefore(GCNODE, nodeRuntime.getFirstChild());
                    
                    TransformerFactory tfact = TransformerFactory.newInstance();//BUILDING A LOT OF CARS I SEE!
                    Transformer transformer = tfact.newTransformer();
                    DOMSource domSource = new DOMSource(docXMLConfig);
                    StreamResult stResult = new StreamResult(ConfigFile);
                    transformer.transform(domSource, stResult);
                    
                    textareaOutput.append("Config file patched.");
                    LOGWRITER("Config file patched.", false);
                }
            } 
            catch(ParserConfigurationException | SAXException | IOException | TransformerException ex) 
            {
                LOGWRITER("ERROR PARSEING CONFIG FILE "+ex, true);
            }
        }//NO ELSE NEEDED AS NO OTHER ACTIONS CAN BE TAKEN
    }
    
    public static void createPrefix()
    {
        if(CreatePrefix)
        {
            LOGWRITER(" : STARTING PREFIX CREATION-------------------------", true);
            String WinePrefix=PrefixFolder.getAbsolutePath();
            if(PrefixFolder.exists())
            {
                runThreadedWinePrefixinstall(WinePrefix,false);
                //WHY DOES JAVA NOT HAVE A NATIVE WAY TO DELETE A FULL DIRECTORY? 
                //IF I WANTED TO HAVE TO WRITE MY OWN FUNCTIONS FOR THE MOST BASIC OF TASK I WOULD USE C
            }
            else
            {
                System.out.println("PREFIXFOLDER: "+WinePrefix);
                runThreadedWinePrefixinstall(WinePrefix,false);
            }
        }
        else
        {
            //I AM FAIRLY CERTAIN THIS IS ALREADY CHECKED IN runThreadedWinePrefixinstall BUT I DONT CARE... WE ARE CHECKING IT AGAIN!
            if(PatchRUNDLL)
            {
                if(PrefixFolder.exists())
                {
                    String WinePrefix=PrefixFolder.getAbsolutePath();
                    textareaOutput.append("Applying RUNDLL32.exe PATCH");
                    LOGWRITER("Applying RUNDLL32.exe PATCH", false);
                    runThreadedWinePrefixinstall(WinePrefix,true);//ALSO SPECIAL EXCEPTION FOR RUNDLL32 MAY BE REDUNDANT
                }
                else
                {
                    CreateAlertPopup("No prefix located... \n Can not apply RUNDLL32.exe Patch. \n Please create your prefix first.");
                    textareaOutput.append("No prefix located... Can not apply RUNDLL32.exe Patch. \n    Please create your prefix first.");
                    LOGWRITER("No prefix located... \n Can not apply RUNDLL32.exe Patch. \n Please create your prefix first.", false);
                }
            }
            ToggleUIEnabled(true);
        }
    }
    
    //YA KNOW... THE OTHER ONES
    public static void applyOtherFixes()
    {
        LOGWRITER(" : APPLYING OTHER FIXES-------------------------", true);
//ADD POPUPS HERE AS THEY ARE RUN AS SOON AS THE RUN BUTTON IS CLICKED
        CreateAlertPopup("As of now Space Engineers will crash shortly after\n"
                        + "launch due to some issues with the Keen analytics server.\n"
                        + "You can fix it on debian based systems(Ubuntu/POPOS...) you can use:\n"
                        + "sudo iptables -A INPUT -s 88.146.207.227 -j DROP\n"
                        + "You can find a copyable version of this command in the help menu.");
        
        //RUNDLL32 HAS BEEN MOVED TO runThreadedWinePrefixinstall() TO MAKE SURE PREFIX STUFF IS THREADED PROPERLY
        if(PatchVideo)
        {
            File Vidfolder = new File(SELocation);
            Vidfolder = Vidfolder.getParentFile();//GOTO SE VIDEO FOLDER Content/Videos/ and rename KSH.wmv
            File VideoFile = new File(Vidfolder+"/Content/Videos/KSH.wmv");
            if(VideoFile.exists())
            {
                textareaOutput.append("VIDEO FILE RENAMED\n");
                LOGWRITER("VIDEO FILE RENAMED", false);
                File backup=new File(VideoFile+".old");
                if(backup.exists())
                    backup.delete();
                VideoFile.renameTo(backup);
            }
            else
            {
                textareaOutput.append("Video file not found. Ignore this... it is fine!\n");
                LOGWRITER("VIDEO FILE NOT FOUND-- THIS IS PROBABLY NOT AN ISSUE", false);
            }
        }
    }
    
    //THIS DISABLES MOST UI ELEMENTS TO PREVENT THE USER FROM TAMPERING WHILE THE PROGRAM IS DOING SOMTHING
    public static void ToggleUIEnabled(boolean ONorOFF)
    {
        buttonBrowse.setEnabled(ONorOFF);
        checkboxCreatePrefix.setEnabled(ONorOFF);
        checkboxPatchXML.setEnabled(ONorOFF);
        checkboxRenameVideo.setEnabled(ONorOFF);
        checkboxRUNDLL32dll.setEnabled(ONorOFF);
        checkboxSafeOptions.setEnabled(ONorOFF);
        buttonRun.setEnabled(ONorOFF);
        buttonDeletePrefix.setEnabled(ONorOFF);
        buttonBackupPrefix.setEnabled(ONorOFF);
    }
    
    public static void setPrefixFolder()
    {//ASSUMES compatdata WILL ALWAYS BE THREE LEVELS ABOVE THIS WHICH IT SHOULD BE IF IT IS INSTALLED THROUGH STEAM!
        //SET PREFIX LOCATION
        File Steamappsfolder = new File(SELocation);
        Steamappsfolder = Steamappsfolder.getParentFile().getParentFile().getParentFile();//GOTO STEAMAPPS
        PrefixFolder = new File(Steamappsfolder+"/compatdata/244850/pfx");//FIND NEW DIRECTORY LOCATION
    }
    
    public static void backupPrefix()
    {
        if(PrefixFolder.exists())//make backup addDATEANDTIME
            PrefixFolder.renameTo(new File(PrefixFolder+("-"+LocalDateTime.now())));//PREVENTS OVERRITING EXISITNG FOLDER
        else//WARN USER NO DIRECTORY EXIST
            CreateAlertPopup("No prefix could be found to backup!\n"+PrefixFolder);//WARN USER PREFIX DOES NOT EXIST
    }
    
    public static void CreateAlertPopup(String Text)
    {
        //JOptionPane popup = new JOptionPane();// MAY NOT BE NEEDED IF IT CALLS JOptionPane STATICALLY
        JOptionPane.showMessageDialog(MainWindow, Text);
        //WELL THIS WAS SIMPLER THAN IT THOUGHT IT WOULD BE...
        //IT SEEMS THIS FUNCTION IS REDUNDANT BUT I'LL LEAVE IT BECUASE I DON'T WANT TO GO BACK AND CHANGE EVERYTHING
    }
    
    public static ActionListener ShowFileChooser(JFrame frame, JTextField textfield)
    {
       ActionListener popupfilechooser = new ActionListener() 
       {
           @Override
           public void actionPerformed(ActionEvent ae) 
           {
               UIManager.put("FileChooser.readOnly", true);  
               JFileChooser filechooserBrowse= new JFileChooser();
               filechooserBrowse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               filechooserBrowse.setDialogTitle("Please choose your Space Engineers bin64 folder");
               filechooserBrowse.setFileHidingEnabled(false);
               filechooserBrowse.setAcceptAllFileFilterUsed(false);
               
               SELocation="";//NULLOUT LOCATION SO THE IF STATMENT BELOW WORKS               
               int whatsdone = filechooserBrowse.showOpenDialog(frame);
               if(whatsdone == JFileChooser.APPROVE_OPTION)
               {
                   SELocation = filechooserBrowse.getSelectedFile().toString();
                   if(new File(SELocation+"/SpaceEngineers.exe").exists()==false)
                   {
                       PrefixFolder = null;
                       textfield.setText("");//NULL THIS OUT FOR FUTURE CHECKS
                       CreateAlertPopup("SpaceEngineers.exe not found! at: \n"+SELocation);
                   }
                   else
                   {
                       setPrefixFolder();
                       textfield.setText(SELocation);
                   }
               }
           }
       };
        return popupfilechooser;
    }
    
    public static void checkPrereqs()
    {
//WINE
        LOGWRITER(" : CHECKING WINEVERSION-------------------------", true);
        String WINEVERSION= getConsoleStreamfromCommand("wine --version");
        if(WINEVERSION!=null)
        {
            LOGWRITER("WINE: "+WINEVERSION,false);//GET WINE VERSION -> Write to log writer
            // NAMEING SCHEME SHOULD BE wine-x.x.x ***    NOTE: THIS WILL ONLY WORK UNTILL VERSION 10 OF WINE... IT'LL BE FINE FOR NOW!
            int temp =Integer.parseInt(WINEVERSION.substring(5,6));            
            if(temp<5)//NOW COMPARE VERSION TO MINIMUM 5.0 **JUST FOR SIMPLICITY USE THIS AS THE START
            {
                LOGWRITER("ERROR-PATCHER: WINE VERSION OUT OF DATE!", false);
                CreateAlertPopup("Wine seems to be out of date. You should check:"
                        + "\nwiki.winehq.org/Download"
                        + "\nto see how to install the latest version 5.0 or higher.");
            }//ELSE NOT NEEDED AS IT ALREADY IS GOOD TO GO
        }
        else
        {
            System.out.println("WINE VERSION ERROR CHECK LOG!");
            CreateAlertPopup("Could not get wine version!\n Ensure wine is installed. wiki.winehq.org/Download");
        }
//WINETRICKS
        LOGWRITER(" : CHECKING WINETRICKS VERSION-------------------------", true);
        String WINETRICKSVERSION= getConsoleStreamfromCommand("winetricks --version");
        if(WINETRICKSVERSION!=null)
        {
            LOGWRITER("WINETRICKS: "+WINETRICKSVERSION, false);//GET WINETRICKS VERTISON -> Write to log writer
            //NOW COMPARE VERSION TO MINIMUM 20191224 **JUST FOR SIMPLICITY USE THIS AS THE START
            int DATECODE=Integer.parseInt(WINETRICKSVERSION.substring(0,8));//FOUR DIGIT YEAR
            if(DATECODE<20191224)//20191224 **JUST FOR SIMPLICITY USE THIS AS THE START
            {
                LOGWRITER("ERROR-PATCHER: WINETRICKS VERSION OUT OF DATE!", false);
                CreateAlertPopup("Winetricks is out of date.\nEnsure winetricks is updated.");
            }//ELSE NOT NEEDED AS IT ALREADY IS GOOD TO GO
        }
        else
        {
            System.out.println("WINETRICKS VERSION ERROR CHECK LOG!");
            CreateAlertPopup("Could not get winetricks version!\nEnsure winetricks is installed.");
        }
//CABEXTRACT-FOR WINETRICKS(WAS A PROBLEM FOR SOME USERS)
        LOGWRITER(" : CHECKING CABEXTRACT INSTALLATION-------------------------", true);
        String CABEXTRACTVERSION= getConsoleStreamfromCommand("cabextract --version");
        if(CABEXTRACTVERSION==null)
        {
            System.out.println("CABEXTRACT VERSION ERROR CHECK LOG!");
            CreateAlertPopup("Could not get cabextract version!"
                    + "\nEnsure cabextract is installed.");//SHOW POPUP WITH INFORMATION
        }//ELSE NOT NEEDED AS CABEXTRACT IS ALREADY INSTALLED
//CHECK FOR DEFAULT SE INSTALL LOCATION
        File seloc = new File(System.getProperty("user.home")+SELocation);     
        if(seloc.exists()){SELocation=seloc.getAbsolutePath();setPrefixFolder();}//IS FOUND
        else//IS NOT FOUND
        {
            SELocation="";//BLANK OUT SELocation AS IT CAN NOT BE FOUND
            CreateAlertPopup("Space Engineers default location could not be determined.\n"
                    + "Please browse for your Space Engineers.exe folder (.../SpaceEngineers/Bin64/)");
            LOGWRITER("SpaceEngineers location not found!", true);
        }
    }
    
    //THIS RUNS THE COMMAND SENT AND RETURNS A STRING OF THE OUTPUT
    public static String getConsoleStreamfromCommand(String EXECUTECOMMAND)
    {
        String returnString =null; //WILL RETURN NULL IF NOT CHANGED
        try
        {
            Process proc = Runtime.getRuntime().exec(EXECUTECOMMAND);
            BufferedReader bReadInStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader bReadErrStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String consoleOutputText;
            String TEMP="";//PREVENT null BEING PREPENDED TO STRING
            while((consoleOutputText = bReadInStream.readLine()) != null) 
            {
                TEMP+=consoleOutputText;//PREVENT null BEING PREPENDED TO STRING
                returnString=TEMP;//PREVENT null BEING PREPENDED TO STRING
                LOGWRITER(consoleOutputText, false);
            }
            //GET ERRORS AND WRITE THEM TO LOG AND POPUP GENERIC ERROR MESSAGE
            while((consoleOutputText = bReadErrStream.readLine()) != null) 
            {
                LOGWRITER("ERROR WHEN RUNNING COMMAND: "+consoleOutputText, false);
            }
        }
        catch(IOException e){LOGWRITER("StreamReaderFailure: "+e, true);}
        return returnString;
    }
    
//THIS WAS A NIGHTMARE TO GET WORKING... WELL... CALL IT WHAT YOU WANT BUT IT IS DOES STUFF... PROBABLY
    // FOR EVERYONES SANITY DO NOT TOUCH THIS METHOD EVER AGAIN
    // I HATE MULTITHREADING!!!!!!!!!!*
    // ALMOST AS MUCH AS I HATE JAVA!!!!!!!*
    // *Read this in even larger caps since all of my comments are in caps anyway
    public static void runThreadedWinePrefixinstall(String PrefixVar,
            boolean SPECAILEXEPTIONFORRUNDLL32/*READ AS SARCASTICALLY AS HUMANLY POSIBLE //ALSO SEPCIAL EXCEPTION FOR RUNDLL32 MAY BE REDUNDANT*/)
    {
        //SWING WORKER... I... I DON'T... BUT WHY? I KNOW SWING IS NOT THREAD SAFE BUT FFS!
        SwingWorker worker = new SwingWorker<Boolean, Integer>() 
        {
            @Override
            protected Boolean doInBackground() throws Exception 
            {
                //BUILD SEPERATE ARRAY LIST WITH APPLICABLE COMMANDS
                ArrayList<String[]> COMMANDS = new ArrayList<String[]>();
                COMMANDS.add(new String[]{"wineserver","-k"});//ENSURE WINESERVER IS NOT RUNNING
                COMMANDS.add(new String[]{"winetricks","--force","--unattended","vcrun2015"});
                COMMANDS.add(new String[]{"winetricks","--force","--unattended","d3dcompiler_47"});
                COMMANDS.add(new String[]{"winetricks","--force","--unattended","faudio"});
                COMMANDS.add(new String[]{"wineserver","-k"});//ENSURE WINESERVER IS NOT STILL RUNNING
                 if(UseSafeOptions)
                    COMMANDS.add(new String[]{"winetricks","--force","--unattended","dotnet472"});
                else// USE DEFAULTS
                {
                    COMMANDS.add(new String[]{"winetricks","--force","--unattended","dotnet48"});
                    COMMANDS.add(new String[]{"wineserver","-k"});//ENSURE WINESERVER IS NOT STILL RUNNING
                }                
                COMMANDS.add(new String[]{"winetricks","winxp"});
                COMMANDS.add(new String[]{"wineserver","-k"});//ENSURE WINESERVER IS NOT STILL RUNNING
                if(PatchRUNDLL == true || SPECAILEXEPTIONFORRUNDLL32 == true)
                {
                    COMMANDS.add(new String[]{"wine", "reg","add",
                            "HKEY_CURRENT_USER\\Software\\Wine\\DLLOverrides",
                            "/v","rundll32.exe","/t","REG_SZ","/f"});//FORCE OVERWRITE IF IT ALREADY EXIST
                    COMMANDS.add(new String[]{"wineserver","-k"});//ONE LAST KILL FOR GOOD MEASURE
                }
                for(int I=0;I<COMMANDS.size();I++)
                {
                    System.out.println("WINETRICKS: "+COMMANDS.get(I)[COMMANDS.get(I).length-1]);//DEBUG
                    ProcessBuilder procbuild = new ProcessBuilder(COMMANDS.get(I));
                    Map<String, String> envvar = procbuild.environment();
                    envvar.put("WINEPREFIX",PrefixVar);
                    //DOTNET'S MASSIVE WINETRICKS OUTPUT OVERLOADS THE BUFFEREDSTREAM AND CAUSES A SOFT FREEZE OF THIS THREAD ... THIS IS BULLSHIT!
                    Process proc = procbuild.start();//Runtime.getRuntime().exec(COMMANDS.get(I));
                    if(COMMANDS.get(I)[COMMANDS.get(I).length-1].equals("dotnet48") 
                    || COMMANDS.get(I)[COMMANDS.get(I).length-1].equals("dotnet472"))
                    {//THIS IS A SPECIAL CASE FOR DOTNET... BECAUSE  IT APPEARS IT OVERFLOWS THE INPUT STREAM FROM ALL OF THE BULLSHIT IS SPITS OUT THROUGH WINTRICKS
                        //THESE SHOULD OVERRIDE THE NORMAL METHODS AND PIPE THE INPUT TO THE TEXTAREA ON THE UI
                        //THIS ALLOWS THE USER TO HAVE FEEDBACK WHILE DOT NET IS RUNNING RATHER THANJUST DUMPING THE OUTPUT TO NULL
                        
                        //NOTE: NOT SURE WHY THIS WORKS. BEST GUESS IS ITPASSIND THE CONTENTS OF PROC.ERRORSTREAM TO OUT IN CHUNKS
                        //AND SUCH BUFFERED READER IS ABLE TO READ THAT AS THE END OF THE LINE AND MOVE ON BEFORE IT OVERFLOWS.
                        InputStreamReader out = new InputStreamReader(proc.getErrorStream())
                        {                            
                            @Override
                            public int read() throws IOException {return super.read();}
                        };
                        InputStreamReader err = new InputStreamReader(proc.getInputStream())
                        {                            
                            @Override
                            public int read() throws IOException {return super.read();}
                        };
                        BufferedReader bReadInStream = new BufferedReader(out);
                        BufferedReader bReadErrStream = new BufferedReader(err);
                        String consoleOutputText = null;
                        while((consoleOutputText = bReadInStream.readLine()) != null) 
                        {
                            LOGWRITER(consoleOutputText, false);
                            textareaOutput.append(consoleOutputText+"\n");
                        }
                        //GET ERRORS AND WRITE THEM TO LOG AND POPUP GENERIC ERROR MESSAGE
                        while((consoleOutputText = bReadErrStream.readLine()) != null) 
                        {
                            LOGWRITER("ERROR WHEN RUNNING COMMAND: "+consoleOutputText, false);
                            textareaOutput.append(consoleOutputText+"\n");
                        }
                    }
                    else
                    {
                        BufferedReader bReadInStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        BufferedReader bReadErrStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                        String consoleOutputText = null;
                        while((consoleOutputText = bReadInStream.readLine()) != null) 
                        {
                            LOGWRITER(consoleOutputText, false);
                            textareaOutput.append(consoleOutputText+"\n");
                        }
                        //GET ERRORS AND WRITE THEM TO LOG AND POPUP GENERIC ERROR MESSAGE
                        while((consoleOutputText = bReadErrStream.readLine()) != null) 
                        {
                            LOGWRITER("ERROR WHEN RUNNING COMMAND: "+consoleOutputText, false);
                            textareaOutput.append(consoleOutputText+"\n");
                        }
                    }
                    
                    proc.waitFor();// WAIT FOR THE PROCESS TO END BEFORE STARTING THE NEXT... 
                    //I KNOW THIS MAY STALL THE WHOLE PROGRAM BECASUE DOTNET IS A PAIN IN THE ASS...
                }
                //REWRITE ALL OF THIS CODE... IT'S THE ONLY WAY YOU'RE GONNA GET IT TO WORK
//                try
//                {
//                    Process proc= null;
//                    //BUILD AND ARRAY LIST OF COMMANDS TO RUN!
//                    
//                    for(int I=0;I<COMMANDS.size();I++)
//                    {
//                        //I KNOW THIS WILL RUN AS MANY TIMES AS THERE ARE COMMANDS... I'LL MOVE IT LATER... MAYBE
//                        if(COMMANDS.get(I).equals(DLLOverrideCommand) || SPECAILEXEPTIONFORRUNDLL32 == true)
//                        {
//                            //TO HELL WITH IT IM JUST GONNA HARD CODE IT!
//                            //"wine reg add HKEY_CURRENT_USER\\Software\\Wine\\DLLOverrides /v rundll32.exe /t REG_SZ"
//                            System.out.println("DLLOVERRIDE: ");//DEBUG
//                            proc= null;//MAKE SURE TO FLUSH PROC
//                            ProcessBuilder procbuild = new ProcessBuilder("wine", "reg","add",
//                                    "HKEY_CURRENT_USER\\Software\\Wine\\DLLOverrides",
//                                    "/v","rundll32.exe","/t","REG_SZ","/f");//FORCE IT IF IT ALREADY EXIST
//                            Map<String, String> envvar = procbuild.environment();
//                            envvar.put("WINEPREFIX",PrefixVar);
//                            proc = procbuild.start();//proc = Runtime.getRuntime().exec(COMMANDS.get(I));
//                        }
//                        //THIS HOWEVER WILL RUN AS MANY TIMES AS IT NEEDS
//                        else
//                        {
//                            System.out.println("WINETRICKS: "+COMMANDS.get(I));//DEBUG
//                            proc= null;//MAKE SURE TO FLUSH PROC
//                            ProcessBuilder procbuild = new ProcessBuilder("winetricks","--force","-q",COMMANDS.get(I));
//                            Map<String, String> envvar = procbuild.environment();
//                            envvar.put("WINEPREFIX",PrefixVar);
//                            proc = procbuild.start();//proc = Runtime.getRuntime().exec(COMMANDS.get(I));
//                        }
//                        BufferedReader bReadInStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                        BufferedReader bReadErrStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//                        String consoleOutputText;
//                        while((consoleOutputText = bReadInStream.readLine()) != null) 
//                        {
//                            LOGWRITER(consoleOutputText, false);
//                            if(textareaOutput !=null)// BECAUSE THIS METHOD MAY BECALLED BEFORE UI CREATION
//                                textareaOutput.append(consoleOutputText+"\n");
//                        }
//                        //GET ERRORS AND WRITE THEM TO LOG AND POPUP GENERIC ERROR MESSAGE
//                        while((consoleOutputText = bReadErrStream.readLine()) != null) 
//                        {
//                            LOGWRITER("ERROR WHEN RUNNING COMMAND: "+consoleOutputText, false);
//                            if(textareaOutput !=null)//BECAUSE THIS METHOD MAY BECALLED BEFORE UI CREATION
//                                textareaOutput.append(consoleOutputText+"\n");
//                        }
//                    }
//                    //MAKE SURE TO FLUSH PROC
//                    proc.destroy();
//                }
//                catch(IOException e){LOGWRITER("StreamReaderFailure: "+e, true);}
              return true;
            }
            @Override
            protected void done()
            {
                //UNLOCK ELEMENTS
                ToggleUIEnabled(true);
                CreateAlertPopup("It seems to have worked. Give it a try!");
            }
          };
        worker.execute();
    }
//DEBUG TOOLS
    public static void LOGWRITER(String TEXTTOWRITE, boolean PrependDateCode)
    {
        File LOGFILE= new File("PatcherLog.txt");
        if(PrependDateCode){
            try(FileWriter filewriter = new FileWriter(LOGFILE,true))
            {filewriter.append(LocalDateTime.now()+" :: "+TEXTTOWRITE+"\n");}
            catch(IOException e)
            {System.out.println("Well... we failed to write a log file... so..."
                    + " we're Ef'ed! Good luck fixing this one..."+e);} //probably a permission issue
        }
        else
        {
            try (FileWriter filewriter = new FileWriter(LOGFILE,true))
            {filewriter.append(TEXTTOWRITE+"\n");}
            catch(IOException e)
            {System.out.println("Well... we failed to write a log file... so..."
                    + " we're Ef'ed! Good luck fixing this one..."+e);} //probably a permission issue
        }
    }
}
