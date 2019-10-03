# This is curently the easiest solution to getting Space Engineers to run smoothly using DotNet:

## Step 1:
  Create your wine prefix like you normally do with dotnet
  WINEPREFIX="INSERT/DIRECTTORY/TO/SPACEENGINEERS/pfx" winetricks --force -q dotnet472 vcrun2015 xact
## Step 2:
  Open you Space Engineers bin64 directory: usually at $HOME/.local/share/Steam/steamapps/common/SpaceEngineers/Bin64
  In this folder find the file SpaceEngineers.exe.config and open it in a text editor(Gedit, Kwrite, ETC...). 
  
  You should see somthing like this:
  ![before](https://github.com/Linux74656/SpaceEngineersLinuxPatches/blob/master/Before.png)
    
 ## Step 3:
  Now add `<gcServer enabled = "true" />` to a new line after the line that says `<runtime>`
  It should now look like this:
  ![before](https://github.com/Linux74656/SpaceEngineersLinuxPatches/blob/master/After.png)
  
  Save the file and close it.
  
  Congradulations Space Engineers should now work properly on Linux. Have fun and enjoy!
  
  Note you may have to reapply this fix if the game updates. It depends on if the file is changed durring the update.
  If you encounter issues try refolloiwing these steps.
  
  ## Special thanks to InflexCZE for takeing the time to help. Without his help it could have taken many more months to figure this out..
  ## and Huge thanks to everyone else on https://github.com/ValveSoftware/Proton/issues/1792 for helping solve these issues.
