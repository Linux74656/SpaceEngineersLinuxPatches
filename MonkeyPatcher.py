import os
import json
import hashlib

# Check to see if Space Engineers Exists, if so set the install dir.
if os.path.exists(os.environ['HOME'] + "/.local/share/Steam/steamapps/common/SpaceEngineers"):
    SE_INSTALL = os.path.join(os.environ['HOME'], ".local/share/Steam/steamapps/common/SpaceEngineers")
else:
    SE_INSTALL = input("Space Engineers not found, enter it's location:\n")
PATCH_DIR = os.path.join(SE_INSTALL, "Bin64")

# Load Patch information from a json file
with open('patch.json') as file:
    PATCH_INFO = json.load(file)

# TODO: Resolve Patch version to apply (laziness at work here folks)
PATCH_TO = '1.192.103'

# Install the patches based on PATCH_TO
for files in PATCH_INFO[PATCH_TO]:

    # Grab the current md5 of each file
    md5_a = hashlib.md5()
    filepath = os.path.join(PATCH_DIR, files)
    with open(filepath, 'rb') as file:
        buf = file.read()
        md5_a.update(buf)

    # Check it against the patch in patch.json
    if PATCH_INFO[PATCH_TO][files] == str(md5_a.hexdigest()):
        print(files + " currently up to date.\nHash: " + str(md5_a.hexdigest()))
    else:
        # TODO: Check if the current file is "original" or it needs to be re-downloaded from steam
        print("Updating " + files + "...")
        os.system('bspatch '+ str(filepath) +' '+ str(filepath) +' '+ files + ".patch")
