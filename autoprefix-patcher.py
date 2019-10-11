# File: autoprefix-patcher.py
import os
import requests
import sys
import shutil
import xml.etree.ElementTree as ElTree

# Unchanged and resued variables to ensure uniformity
xmlFileName     = "SpaceEngineers.exe.config"
PrefixDir       = "/compatdata/244850/pfx"
SeBinDir        = "/common/SpaceEngineers/Bin64/"
VideoLoc        = "/common/SpaceEngineers/Content/Videos/"
WinePrefix      = "WINEPREFIX="
InstallLocation = ""

def CheckPrereqs():
    # check for winetricks
    # Potentially add a check for version number and ensure the date is later or same as needed
    # Winetricks seems to use yyyymmdd see if breaking down this string is viable
    if shutil.which("winetricks") is not None:
        print("winetricks is installed! \n")
    else:
        print("winetricks is not installed. Please install it and rerun this script!")
        sys.exit()
    # Add other checks if necissary

def ApplyPatch():
    # Check for file, if missing tell user to verify integrity of game files.
    if os.path.isfile(InstallLocation+SeBinDir+xmlFileName):
        print("Space Engineers executable config found! \n")
    else:
        print("Space Engineers files not found! Check the install location was entered correctly. "
        +"Or verify the integrity of your game files through steam.")
        sys.exit()

    # QUICK AND DRITY SOLUTION BUT IT SHOULD DO.
    configDom = ElTree.parse(InstallLocation+SeBinDir+xmlFileName)
    root = configDom.getroot()
    runtimenode = root.find("runtime")
    if runtimenode.find("gcServer") == None:
        ConfigFile = open(InstallLocation+SeBinDir+xmlFileName, 'r')
        contents = ConfigFile.readlines()
        LINENUM=0
        for line in contents:
            LINENUM+=1
            if '<runtime>' in line:
                contents.insert(LINENUM, "  <gcServer enabled = \"true\" />"+"\n")
        ConfigFile.close()
        ConfigFile = open(InstallLocation+SeBinDir+xmlFileName, 'w')
        for eachitem in contents:
            ConfigFile.write(eachitem)
        ConfigFile.close()
        print("Config Patch Applied! \n")
    else:
        print("It seems the patch is already applied! \n")
        # Continue with program to increase functionality

    # For the love of all sanity this is broken just ignore it!!!!!!!!!!!!!
    #configDom = ElTree.parse(InstallLocation+SeBinDir+xmlFileName)
    #root = configDom.getroot()
    #attribute = {'enabled': 'true'}
    #runtimenode = root.find("runtime")
    #if runtimenode.find("gcServer") == None:
    #    ElTree.SubElement(runtimenode,'gcServer', attribute)
    #    configDom.write(InstallLocation+SeBinDir+xmlFileName)
    #    print("Config Patch Applied! \n")
    #else:
    #    print("It seems the patch is already applied! \n")
        # Continue with program to increase functionality

    response = input("Would you like to apply a fix to prevent startup freezing? "
    + "It will rename one of the video files in the SE Videos folder. [y,n] \n")
    # Rename the intro AFTER asking the user.
    if response == 'y' or 'n':
        if response == 'y':
            if os.path.isfile(InstallLocation+VideoLoc+'KSH.wmv'):
                os.rename(InstallLocation+VideoLoc+'KSH.wmv', InstallLocation+VideoLoc+'KSH.wmv.old')
            else:
                print("It appears the video is already renamed or missing. You are good to go!")
        if response == 'n':
            print("Alright continuing.")
    else:
        print("Invalid response. Killing program!")
        sys.exit()

def CreatePrefix():
    # SEPERATE COMMANDS
    response = input("Creating Prefix, This may take a long time (sometimes ~10 minutes) "
    + "Sometimes it will hang on dotnet48 installation. "
    + "If it seems to not do anything for 10minutes or so hit ctrl+z and retry manually. \n"
    + "Would you like to continue? [y,n]")
    # Rename the intro AFTER asking the user.
    if response == 'y' or 'n':
        if response == 'y':
            print("Alright continuing. \n")
        if response == 'n':
            print("That is everything this program can do then... goodbye!")
            sys.exit()
    else:
        print("Invalid response. Killing program!")
        sys.exit()
    prefixloc=WinePrefix+"\""+InstallLocation+PrefixDir+"\""
    WintricksCommand=" winetricks --force -q "
    print(prefixloc+"\n")
    # WINETRICKS APPLY D3DCOMPILER_47
    os.system(prefixloc+WintricksCommand+"d3dcompiler_47")
    # WINETRICKS APPLY FAUDIO
    os.system(prefixloc+WintricksCommand+"faudio")
    # WINETRICKS APPLY VCRUN2015 (note if failed just ignore for now)
    os.system(prefixloc+WintricksCommand+"vcrun2015")
    # WINETRICKS APPLY DOTNET48 (note if failes ust ignore)
    os.system(prefixloc+WintricksCommand+"dotnet48")
    # WINETRICKS APPLY WINXP
    os.system(prefixloc+WintricksCommand+"winxp")

    # WINETRICKS DISABLE RUNDLL32.EXE
    # NOTE! There does not seem to be a way to do this easily... and I honestly can not be bothered...
    print("\n \n Prefix Created! \n \n")

#-------------------------------------------------------------------------------

# Find the install location of the game
# Assume default if not then ask:
CheckPrereqs()
SteamappsDir = os.path.join(os.environ['HOME'], '.steam/steam/steamapps')
if os.path.isfile(os.path.join(SteamappsDir, 'appmanifest_244850.acf')):
    InstallLocation = os.path.join(os.environ['HOME'], '.steam/steam/steamapps/')
    print("Found Install Location: "+InstallLocation+"\n")
    PrefixLocation = InstallLocation+PrefixDir
else:
    InstallLocation = input("Cannot locate install directory. Please input the "
    +"steamapps folder location where Space Engineers is installed. \n"
    +"For Example: if you have SE installed in a steam library on an external drive: "
    +"/mnt/SSD1/SteamFastboatFolder/steamapps/common/SpaceEngineers you would type "
    +"/mnt/SSD1/SteamFastboatFolder/steamapps/ \n")
    PrefixLocation = InstallLocation+PrefixDir

# Apply Patch before creating prefix as prefix creation may stall on some systems.
ApplyPatch()
CreatePrefix()
print("PROGRAM COMPLETE!")
