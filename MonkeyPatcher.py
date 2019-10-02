import os
import re
import json
import hashlib
import tarfile
import requests


# Is this bad form? I'm too annoyed at the length of some of these to care
isFile = os.path.isfile
fJoin = os.path.join
parent = os.path.pardir

GH_PATCH_URL = "https://raw.githubusercontent.com/Linux74656/SpaceEngineersLinuxPatches/master/checksum.json"

# Check to see if Space Engineers Exists, if so set the install dir.
if isFile(fJoin(os.environ['HOME'], ".local/share/Steam/steamapps/appmanifest_244850.acf")):
    MANIFEST = fJoin(os.environ['HOME'], ".local/share/Steam/steamapps/appmanifest_244850.acf")
    SE_INSTALL = fJoin(os.environ['HOME'], ".local/share/Steam/steamapps/common/SpaceEngineers")
else:
    SE_INSTALL = input("Space Engineers not found, enter it's location:\n")
    MANIFEST = fJoin(parent(parent(SE_INSTALL)), "appmanifest_244850.acf")
with open(MANIFEST) as file:  # Grab the version to figure out naming
    MANIFEST_INFO = file.read()
PATCH_TO = re.search('(?<="buildid"(\t){2}").*(?=")', MANIFEST_INFO).group()
PATCH_DIR = fJoin(SE_INSTALL, "Bin64")

# Load Patch information from a json file
if isFile("patch.json"):
    with open('patch.json') as file:
        PATCH_INFO = json.load(file)
else:  # Otherwise from the web
    PATCH_INFO = requests.get(GH_PATCH_URL).json()


# TODO: Grab these files from the interwebs. Now might be a good time to make functions a thing
with tarfile.open(PATCH_TO + ".tar.gz") as tar:
    tar.extractall()

# Install the patches based on PATCH_TO

for files in PATCH_INFO[PATCH_TO]:
    # Grab the current md5 of each file
    md5_a = hashlib.md5()
    filepath = fJoin(PATCH_DIR, files)
    with open(filepath, 'rb') as file:
        buf = file.read()
        md5_a.update(buf)

    # Check it against the patch in patch.json
    if PATCH_INFO[PATCH_TO][files] == str(md5_a.hexdigest()):
        print(files + " currently up to date.\nHash: " + str(md5_a.hexdigest()))
    else:
        # TODO: Check if the current file is "original" or it needs to be re-downloaded from steam
        print("Updating " + files + "...")
        os.system('bspatch ' + str(filepath) + ' ' + str(filepath) + ' ' + files + ".patch")
    os.remove(files + ".patch")
