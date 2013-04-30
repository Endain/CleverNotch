CleverNotch
===========

Talk to Notch (or your favorite personality) in Minecraft! This plugin allows you to create a virtual player in your Minecraft server and speak with them! The chat can be powered by Jabberwacky as well as other well-known chat bots (Cleverbot disabled until legal issues resolved). It comes with some configuration options to customize your server's resident AI to suite your needs!
Features
--------
* Talk with a chat bot as if it were another player!
* Customize the bot to fit your server's needs!
* Toggle the bot on and off easily!
* Prevents basic spam to the bot!
Commands
--------
* **/clevernotch** - Toggles the bot between the ON and OFF states.
Usage
-----
To talk with your bot simply address it by it's name with a message included! for example: With the bot named 'Notch', "Notch, How are you doing today?", will send the text "How are you doing today?" To the bot. The bot will announce it's response in chat.
Installation
------------
Simply drop the given JAR file into your plugins folder of your Bukkit installation!
A default configuration file will be generated for you the first time the plugin is run.
The configuration file contains details of the various options.
A quick list of configurable features can be found below:
* botName - Sets the name that your bot responds to.
* botColor - Set the color of the bot's name in chat.
* botSource - Selects the bot AI source.
* minResponseDelay - Set the minimum time that must pass before the bot will respond.
Download
--------
* CleverNotch-1.0-RELEASE [Download](http://dev.bukkit.org/server-mods/clevernotch/files) [Github](https://github.com/Endain/CleverNotch)
Future Plans
------------
Some ideas I am currently tossing around:
* Allow for the creation of multiple 'personalities' (bots).
* Improve algorithm for detecting when the bot is being addressed.
* Wrap a 'learning' bot implementation such as JMegaHal in to allow players to 'teach' the bot.
* Provide a global (online) JMegaHal (or other) wrapping to make a 'Cleverbot' that learns from every server running the plugin. (Security? Feasibility?)
Let me know if there is any interest in these features or other suggestions!