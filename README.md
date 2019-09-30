Prerequisites:

NOTE: Currently this only works for the latest mainline branch of Space Engineers. (Version 1.192.102) This will NOT work for the MODSDK build eitther.

First of all we need to ensure Space Engineers is setup properly:
	This will assume you already know how to use Winetricks and where your Space Engineers 	install/Prefix is located.

0) *MAKE SURE YOU BACKUP ANY WORLDS/BLUEPRINTS YOU WANT!*
1) Delete your old prefix. 

2) Create a new prefix with necessary dependencies (vcrun2015 xact)

WINEPREFIX="/home/USER/.local/share/Steam/steamapps/compatdata/244850/pfx" winetricks --force -q vcrun2015 xact

3) Install Wine-Mono, by default wine-mono should install itself into a new prefix that does not contain dotnet. However in the name of uniformity and to remove a potential point of confusion we will install the same version manually.
	
    A) Go here: https://github.com/madewokherd/wine-mono/releases
  
    B) Get version: wine-mono-4.9.3.msi
  
    C) Now we need to install it
  
WINEPREFIX="/home/USER/.local/share/Steam/steamapps/compatdata/244850/pfx" msiexec -i "/FILEIDRECTORY/wine-mono-4.9.3.msi"

4) One last thing we need BSPATCH. For Ubuntu and Debian users, you can install it with:
sudo apt-get install bsdiff

5) You need a clean copy of the game files. Already modified copies of the patch files will not work (I found this out the hard way!). So make backups of the bin64 directory in the Space Engineers directory.

How to:

1) Download the patch files for the game. They should be part of this repository.
	A)Extract this folder containing the files to someplace easy to find, like your desktop

2) Open your preferred terminal application(Konsole(KDE), Terminal(Gnome)… etc)

3) Change directory into your Space Engineers Bin64 folder
cd /home/USER/.local/share/Steam/steamapps/common/SpaceEngineers/Bin64

4) Now apply the first patch: bspatch oringalfile newfile patchfile
bspatch Sandbox.Game.dll Sandbox.Game.dll /home/USER/Desktop/Patches/Sandbox.Game.dll.patch
bspatch VRage.Scripting.dll Vrage.Scripting.dll  /home/USER/Desktop/Patches/VRage.Scripting.dll.patch

Special notes:

1) Do not accept the anonymous data collection dialog when the game starts that will break the game very badly!
If you have done it by accident you can go ino the config file located in your prefix location /drive_C/users/steamuser/Application Data/SpaceEngineers/SpaceEngineers.cfg and change the line under <Key>GDPRConsent</Key> that looks like <Value xsi:type="xsd:string">True</Value> to this <Value xsi:type="xsd:string">False</Value>
Unforunatly it seems the window will appear everytime you start the game so just hit escape or no, and it should be fine.

That should work for now.

Some notes: The game is a bit unstable. 
1) It will probably freeze a few times during load, so forcibly quit and try loading again. You should be able to hit continue game, on the main screen, and it will load your last world. Even if you never got into the world to play.
2) The game keeps running in the background even after closed, so you will have to manually kill the process everytime you close the game.
