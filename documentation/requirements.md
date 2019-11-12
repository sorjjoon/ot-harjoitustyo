## Requirements

------------------------

Basic properities
 > -Read log files provided by the game, and find the relevant information inside (see [Example logs](documentation/Example-logs) for some example logs). Also the ability to look at only specfic parts of the log (such as events happening betwen 23:10 and 23:12 as opposed to the whole log)  
 > -Display basic statistics regarding the read information, such as overall dmg dealt, DPS (dmg per sec), DTPS (dmg taken per sec), HPS (healing per sec), HTPS (healing taken per sec), crit %, etc.  
 
 > - Also this information can be divided between targets (meaning for example dmg done to a specfic target, in the event that multiple enemies have been dealt dmg in a fight)  
 > - Statistics regarding the usage of specific abilities, such as the average damage dealt by a specfic ability  
 
 > -Breakdown of dmg and healing dealt in a single fight by ability
 > - Storage of analyzed logs

Some ideas for more advanced properties
> - locating from a given time frame a highest burst window, meaning an 8 second window in which the dmg dealt was the hightest
> - Average, highest and lowest uptime for each ability used (uptime meaning time player could have used a specfic abilty but chose not to)
> - Time the player spent stunned, and dmg taken while stunned
> - Estitamting the players usage of defensive cooldowns, by studying the player's dmg taken, and seeing which defensive abilities the player had active at a certain time  
> -Same for offensive cooldowns

User Interace
> - Uncertain at this time, will start with a very simple text-based interface, which can be later replaced by a more advanced text-based one or a graphic one
