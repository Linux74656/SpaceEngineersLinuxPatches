# This is currently the easiest solution to getting Space Engineers to run smoothly using DotNet:
### This guide assumes you have already have Steamplay/Proton installed with all of the prerequisites and such, as well as Space Engineers, and winetricks. If not, you can check around the internet for guides that can explain it much better than I can!

## Step 0:
  Backup any save data, blueprints, ETC...
  Then verify the integrity of your game files in Steam.

## Step 1:
  Ensure winetricks is up to date.
  
  `sudo winetricks --self-update`

## Step 2:
  Create your wine prefix with dotnet
  
  `WINEPREFIX="INSERT/DIRECTORY/TO/SPACEENGINEERS/pfx" winetricks --force -q dotnet472 vcrun2015 faudio d3dcompiler_47`
  
## Step 3:
  Open your Space Engineers bin64 directory: usually at $HOME/.local/share/Steam/steamapps/common/SpaceEngineers/Bin64
  
  In this folder find the file **SpaceEngineers.exe.config** and open it in a text editor(Gedit, Kwrite, ETC...). 
  
  You should see something like this:
  ![before](https://github.com/Linux74656/SpaceEngineersLinuxPatches/blob/master/Before.png)
    
## Step 4:
  Now add `<gcServer enabled = "true"/>` to a new line after the line that says `<runtime>`
  It should now look like this:
  ![before](https://github.com/Linux74656/SpaceEngineersLinuxPatches/blob/master/After.png)
  
  Save the file and close it.
  
  ### Not mandatory but, if you want to avoid the startup freeze that requires user input to get past the splash screen, rename the file here: "LOCATION_OF_SPACE_ENGINEERS_INSTALL/SpaceEngineers/Content/Videos/KSH.wmv to KSH.wmv.old
  
  Congratulations Space Engineers should now work properly on Linux. Have fun and enjoy!
  
  Note you may have to reapply these fixes if the game updates. It depends on if the file is changed durring the update.
  If you encounter issues try following these steps again.
  
  ## Special thanks to InflexCZE for taking the time to help. Without his help it could have taken many more months to figure this out.
  ## and Huge thanks to everyone else on https://github.com/ValveSoftware/Proton/issues/1792 for helping solve these issues.
