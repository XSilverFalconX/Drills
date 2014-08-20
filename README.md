Drills
======
Description

Drills is a plugin that brings a whole new idea to how we mine. Drills allows
you to make your own drill that mines blocks directly under the drill and adds 
those blocks/ores to your inventory. Drills has an easy setup which enables users 
to create a drill very simply, as it's hard to find diamonds etc. with the drill. 
Players can use the drill parts over and over again in a new way instead of mining in caves. 
See "Starting the Drill" to find out how to make one.

Permission

The player only needs one node for creating the drill which is:

- drills.use
Starting the Drill

As stated before, the drills are very easy and simple to create.

Items needed:

    2 Iron Blocks 

    1 Cobblestone Wall 

    1 Redstone Torch 

FOR FURNACES

    1 Furnace 

    Fuel types, specified in config 

Place Fuel Types in Fuel Slot

After you have your drill looking Like the video, you can right click the fence to start!
Stopping the Drill

To stop your drill you can do the following options:

    Left click on the fence of your drill 

    Leave the Game 

    Break Any of the drill parts, including the Drill's bit 

How it works

    Drills mines 1 Block every 2 Seconds (default) But, it can be customized to any speed. 

    After Breaking 1 block you'll get an information message based on what you mined. If it's in the 'Minables' list (in the Config) you are then given
    that block or pieces of that block when it was broken 

    Otherwise, if it is on the 'UnMinables' list, the block will be broken and replaced with a drill bit but no block nor pieces will be given to the player. 

    Finally, if the block is not on the 'Minables' or 'UnMinables' list the drill will stop and display what block it could not break. 

    After a successful breakage of a block the drill will replace that block with the drill bit; either if it was minable or unminable. 

    Drill will be quiet if set to "quiet-mode" in the config and more options below in "Config Editing" 

FOR FURNACES

    If you have furnaces enabled in the config, you can place a furnace on either side of the drill on top of the Iron blocks 

    If they are enabled they will be required otherwise the drill will not run 

    The drill will burn one piece of fuel every X minutes set in the config (defaultly 1) 

Config Editing

Edit however you'd like to the following features

    WarmUp time (Before the drill starts) 

    Minable Blocks (Ones that give you the block / block pieces) 

    UnMinable Blocks (Ones that dont give you the block but are still mined) 

    Replacement Material. (When cleaning up) 

    Speed 

    Logging 

    EXP adding on whatever blocks you'd like 

    Quiet mode 

    Furnace Editting and toggling 

Config to look at here: http://dev.bukkit.org/server-mods/drills/pages/config-yml/
Installation

Installation of Drills is very simple just follow these few steps:

    Download the latest version (Choose WorldGuard or regular)
    Drag and drop to your plugins folder
    Start, reload, or restart to create files 

    Edit plugins/drills/config.yml to your liking (Optional)
    Save edits by restarting/reloading
    Enjoy :) 

Dependencies

    If you download the WorldGuard version you need the latest version of WorldGuard Found here: http://dev.bukkit.org/bukkit-plugins/worldguard/ 

    If you did not download the WorldGuard version there are no dependencies 

The difference is that if you have the WorldGuard version the drill will stop mining when it reaches a WorldGuard claimed block.
AntiGrief/Anti Troll Measures

    As soon as a player leaves the drill stops itself and replaces all drill bits with the Replacement Material (Regularly Dirt) 

    Once a drill stops it will clean itself by removing all drill bits and replacing them with the Replacement Material. 

    Whenever a player attempts to exploit the drill by getting drill bits, it will shut down and tell the user there is a break in the drill bits. 

    A warmup timer adds to any possible grief measure. 

    Only one drill can be made at a time per player. 

    The drill will stop when hitting a WorldGuard region. (With the WorldGuard Version) 

ToDo

    Maximum of 1 drill per time 

    Add optional chest for drill to place objects in. 

Source

My source is right here! You're looking at it now :o
