# File: autopatcher.py
import os
import requests
import tarfile
import sys
import json
import shutil

# Unchanged and resued variables to ensure uniformity
RepositoryURL  = "https://raw.github.com/Linux74656/SpaceEngineersLinuxPatches/master/"
PatchFolderURL = "PatchFiles/"
ChecksumFile   = "checksum.json"
FILEEXTENSTION = ".tar.gz"
SandboxDLLName = "Sandbox.Game.dll"
VRageDLLName   = "VRage.Scripting.dll"
SandboxPATCH   = "Sandbox.Game.dll.patch"
VRagePATCH     = "VRage.Scripting.dll.patch"

def CheckPrereqs():
    # ADD CHECKS TO ENSURE USER HAS bsdiff installed if not remind them to do it!
    if shutil.which("bsdiff") is not None:
        print("bsdiff is installed!")
    else:
        print("bsdiff is not installed. Please install it and rerun this script!")
        sys.exit()

    # Add other checks if necissary

# HAVE Should return the build number from the acf file, quick and dirty
def GetBuildIDNumber(inloc):
    with open(inloc+"appmanifest_244850.acf") as fil:
        for line in fil:
            if line.find("buildid") == -1:
                pass
            else:
                TEMP = line
                TEMP = TEMP.replace("\"buildid\"","")
                TEMP = TEMP.replace("\"","")
                TEMP = TEMP.strip()
                print("BuildID: "+TEMP)
                return TEMP

def CheckForPatches(buildVersion):
    # If none tell user and close
    req = requests.get(RepositoryURL+buildVersion+FILEEXTENSTION)
    if req.status_code == 404:
        print("No patches found for your game version.")
        sys.exit()
    else:
        return

def DownloadPatch(buildVersion):
    # Get json FILE
    req = requests.get(RepositoryURL+ChecksumFile)
    with open("checksum.json", 'wb') as fil:
        fil.write(req.content)
        print("checksum.json Retrieved")
    jsonFile = json.loads(open("checksum.json").read())
    SBChecksum = jsonFile[buildVersion] [SandboxDLLName]
    VRChecksum = jsonFile[buildVersion] [VRageDLLName]
    SANDBOXVERSION = os.popen('md5sum '+BinLocation+'Sandbox.Game.dll').read()
    VRAGEVERSION   = os.popen('md5sum '+BinLocation+'VRage.Scripting.dll').read()
    SANDBOXVERSION = (SANDBOXVERSION[:32])
    VRAGEVERSION   = (VRAGEVERSION[:32])
    if SBChecksum != SANDBOXVERSION:
        print("Checksums do not match, verify your game integrity and try again.")
        os.system('rm '+"checksum.json")
        sys.exit()
    if VRChecksum != VRAGEVERSION:
        print("Checksums do not match, verify your game integrity and try again.")
        os.system('rm '+"checksum.json")
        sys.exit()
    req = requests.get(RepositoryURL+PatchFolderURL+buildVersion+FILEEXTENSTION)
    with open(buildVersion+FILEEXTENSTION, 'wb') as fil:
        fil.write(req.content)
        print("Patches Retrieved")

def ApplyPatch(FILENAME):
    # Extract patches
    EXTRACT = tarfile.open(FILENAME)
    EXTRACT.extractall()
    EXTRACT.close()
    # Apply patches
    os.system('bspatch '+BinLocation+SandboxDLLName+' '+BinLocation+SandboxDLLName+' '+SandboxPATCH)
    os.system('bspatch '+BinLocation+VRageDLLName+' '+BinLocation+VRageDLLName+' '+VRagePATCH)
    # REMOVE ALL UNSUSED FILES
    os.system('rm '+FILENAME)
    os.system('rm '+"checksum.json")
    os.system('rm '+SandboxDLLName+'.patch')
    os.system('rm '+VRageDLLName+'.patch')
#-------------------------------------------------------------------------------

# Find the install location of the game
# Assume default if not then ask:
CheckPrereqs()
SteamappsDir = os.path.join(os.environ['HOME'], '.steam/steam/steamapps')
if os.path.isfile(os.path.join(SteamappsDir, 'appmanifest_244850.acf')):
    InstallLocation = os.path.join(os.environ['HOME'], '.steam/steam/steamapps/')
    print("Found Install Location: "+InstallLocation)
else:
    InstallLocation = input("Cannot locate install directory. Please input the "
    +"steamapps folder location where Space Engineers is installed.\n")
BinLocation = InstallLocation+'common/SpaceEngineers/Bin64/'
# Grab the buildID
buildID=GetBuildIDNumber(InstallLocation);

CheckForPatches(buildID)
DownloadPatch(buildID)
ApplyPatch(buildID+FILEEXTENSTION)
print("Program Complete!")
