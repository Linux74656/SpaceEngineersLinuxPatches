# Prerequisites:

## DUE TO ME BEING THEBIGDUMB<sup>tm</sup> you will need to re-follow this guide and use the newly provided patches in this repo. This should fix most of the load hanging issues.

#### NOTE: Currently this only works for the latest mainline branch of Space Engineers. (Version 1.192.102) This will NOT work for the MODSDK build either. 
#### NOTE2: You must apply the patches the the original game dlls. so copy the backups you made into the Bin64 folder, or verify your game integrity in steam.
#### NOTE3: To apply the new patches, if you already applied the old ones and had issues, you should be able to skip deleting and recreating the wine Prefix. IE Skip to How to apply the patches.

First of all we need to ensure Space Engineers is setup properly:
	This will assume you already know how to use Winetricks and where your Space Engineers 	install/Prefix is located.

0) *MAKE SURE YOU BACKUP ANY WORLDS/BLUEPRINTS YOU WANT!*

1) Delete your old prefix. 

2) Create a new prefix with necessary dependencies ( vcrun2005 vcrun2015 xact)

> WINEPREFIX="/home/USER/.local/share/Steam/steamapps/compatdata/244850/pfx" winetricks --force -q vcrun2005 vcrun2015 xact

3) Install Wine-Mono, by default wine-mono should install itself into a new prefix that does not contain dotnet. However in the name of uniformity and to remove a potential point of confusion we will install the same version manually.
	
    A) Go here: https://github.com/madewokherd/wine-mono/releases
      
    B) Get version: wine-mono-4.9.3.msi
      
    C) Now we need to install it and then set the windows version to windows xp

> WINEPREFIX="/home/USER/.local/share/Steam/steamapps/compatdata/244850/pfx" msiexec -i "/FILEIDRECTORY/wine-mono-4.9.3.msi"

> WINEPREFIX="/home/USER/.local/share/Steam/steamapps/compatdata/244850/pfx" winetricks winxp

4) One last thing we need BSPATCH. For Ubuntu and Debian users, you can install it with:
`sudo apt-get install bsdiff`

5) You need a clean copy of the game files. Already modified copies of the patch files will not work (I found this out the hard way!). So make backups of the bin64 directory in the Space Engineers directory.

# How to apply the patches:

1. Download the patch files for the game. They should be part of this repository.
   Extract this folder containing the files to someplace easy to find, like your desktop

2. Open your preferred terminal application(Konsole(KDE), Terminal(Gnome)… etc)

3. Change directory into your Space Engineers Bin64 folder

   ```
   cd /home/USER/.local/share/Steam/steamapps/common/SpaceEngineers/Bin64
   ```

4. Now apply the first patch: bspatch oringalfile newfile patchfile

   ```
   bspatch Sandbox.Game.dll Sandbox.Game.dll $HOME/Desktop/Patches/Sandbox.Game.dll.patch
   ```

   ```
   bspatch VRage.Scripting.dll VRage.Scripting.dll  $HOME/Desktop/Patches/VRage.Scripting.dll.patch
   ```

# Special notes:

1. Do not accept the anonymous data collection dialog when the game starts that will break the game very badly!
   If you have done it by accident you can go into the config file located in your prefix location /drive_C/users/steamuser/Application Data/SpaceEngineers/SpaceEngineers.cfg and change the line under <Key>GDPRConsent</Key> that looks like <Value xsi:type="xsd:string">True</Value> to this <Value xsi:type="xsd:string">False</Value>
   Unfortunately it seems the window will appear every time you start the game so just hit escape or no, and it should be fine.
2. In order to prevent the issue that freezes the game at startup until you hit escape, or click the mouse. You may find it advantageous to remove or rename the file here : /home/USER/.local/share/Steam/steamapps/common/SpaceEngineers/Content/Videos/KSH.wmv

That should work for now.

# Some more notes: IE The game is still a bit unstable.
0) Multiplayer with other non-patched games (this includes windows servers) is not possible at this time. If you try it it will most likely crash the game. However patched Linux clients connecting to patched Linux servers seems to work without issue.
1) It may freeze a few times during load, so forcibly quit and try loading again. You should be able to hit continue game, on the main screen, and it will load your last world. Even if you never got into the world to play.
2) The game keeps running in the background even after closed, so you will have to manually kill the process every time you close the game.