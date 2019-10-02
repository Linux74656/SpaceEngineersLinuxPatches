#File: autopatcher.py
import os
import requests
import tarfile

# REPO LOCATION
RepositoryURL = "https://raw.github.com/Linux74656/SpaceEngineersLinuxPatches/master/"
FILEEXTENSTION = '.tar.gz'
SandboxDLLName = 'Sandbox.Game.dll'
VRageDLLName = 'VRage.Scripting.dll'
SandboxPATCH = 'Sandbox.Game.dll.patch'
VRagePATCH = 'VRage.Scripting.dll.patch'

# TEMPRARY CHECKSUMS Until I can implement Onyx47 soutions
# NOTE VERSION 1.192.102 does not have a checksum so it will do nothing for now.
CheckSumListSandBox = ['00000000000000000000000000000000', 'b6d168be7e38640817f8d7f1de523346']
CheckSumListScripts = ['00000000000000000000000000000000', 'cf4b860b7917fa53d8c95e0c6a377451']
CheckSumListSandBoxMOD = ['00000000000000000000000000000000', '5cb888e13df4408806bbb03586ca68d2']
CheckSumListScriptsMOD = ['00000000000000000000000000000000', '430167f8300490a3b6492537687403c5']

# USER FRIENDLY VERSION LIST
VersionList = ['1.192.102', '1.192.103']

# ADD CHECKS TO ENSURE USER HAS bsdiff installed if not remind them to do it!

# Find the install location of the game
# Note, I'm pretty sure steam follows xdg standards, so using $XDG_DATA_DIR is probs a better idea, this will do for now.
if os.path.exists(os.environ['HOME'] + "/.local/share/Steam/steamapps/common/SpaceEngineers"):
    InstallLocation = os.path.join( os.environ['HOME'], ".local/share/Steam/steamapps/common/SpaceEngineers")
else:
    InstallLocation = input("Space Engineers not found, enter it's location:\n")
BinLocation = InstallLocation+'/Bin64/'

# Get Current MD5Checksums
SANDBOXVERSION = os.popen('md5sum '+BinLocation+'Sandbox.Game.dll').read()
VRAGEVERSION = os.popen('md5sum '+BinLocation+'VRage.Scripting.dll').read()

# REMOVE EXTRA DIRECTORY INFO FROM ENDS OF STRINGS
SANDBOXVERSION = (SANDBOXVERSION[:32])
VRAGEVERSION = (VRAGEVERSION[:32])

# SHOW CHECKSUMS
print(SANDBOXVERSION)
print(VRAGEVERSION)

# COMPARE CHECKSUMS

# CHECK IF ALREADY PATCHED
FOUNDPATCH = False
for num, POINTLESSAGAIN in enumerate(VersionList):
    if SANDBOXVERSION == CheckSumListSandBoxMOD[num]:
        if VRAGEVERSION == CheckSumListScriptsMOD[num]:
            print('VERSION FOUND: '+VersionList[num])
            print('Your game already appeares to be patched.')
            FOUNDPATCH = True
# CHECK TO SEE IF PATCH VERSION AVALIBLE
if FOUNDPATCH == False:
    for num, POINTLESS in enumerate(VersionList):
        if SANDBOXVERSION == CheckSumListSandBox[num]:
            if VRAGEVERSION == CheckSumListScripts[num]:
                print('VERSION FOUND: '+VersionList[num])
                # LETS GET SOME PATCHES!
                # CONCAT FILE NAME
                FILENAME = 'V'+VersionList[num]+'Patches'+FILEEXTENSTION
                # BUILD URL
                URL = RepositoryURL+FILENAME
                print(URL)
                req = requests.get(URL)
                with open(FILENAME, 'wb') as fil:
                    fil.write(req.content)
                    fil.close()
                # extract patches
                EXTRACT = tarfile.open(FILENAME)
                EXTRACT.extractall()
                EXTRACT.close()

                # apply patches
                os.system('bspatch '+BinLocation+SandboxDLLName+' '+BinLocation+SandboxDLLName+' '+SandboxPATCH)
                os.system('bspatch '+BinLocation+VRageDLLName+' '+BinLocation+VRageDLLName+' '+VRagePATCH)
                # REMOVE ALL UNSUSED FILES
                os.system('rm '+FILENAME)
                os.system('rm '+SandboxDLLName+'.patch')
                os.system('rm '+VRageDLLName+'.patch')
print("Program End!")
