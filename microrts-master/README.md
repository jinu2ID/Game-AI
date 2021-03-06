For this project I created a rule-based AI to play microRTS. I began with a term class that consisted of a string that acted as the functor and two parameters of type object. Since the type of the parameter is vague I was able to pass in objects like collections. This was useful when dealing with multiple resources or groups of units. Only two parameters were used to minimize the complexity required in the inference engine. A knowledge base class was created to hold a list of terms which represented facts about the game state. A rule class was implemented to allow the creation of conditions that the inference engine could check against the knowledge base. A rule consisted of patterns which were the conditions that needed to be met. Each pattern was represented by a term. A pattern for example could be “Type(X, “Barracks)”. This pattern would search for a unit of type “Barracks”. The effects were similar. An example would be “Build(X, “Worker”). The binding of a barracks unit to X would allow the AI to build a unit of type “Worker” from X. The inference engine that handled this task was implemented in a class called rulesAI.

The class rulesAI handled reading information from the game state, updating the knowledge base, unifying rule conditions with the knowledge base, and applying the effects of those rules. Each turn rulesAI adds information such as type and number of units to the knowledge base. It then iterates through all the rules and attempts to unify their patterns with the knowledge base. Patterns successfully unify with a fact in the knowledge base if their functors and second parameters match. For consistency and ease of implementation the first parameter is always the one that is not known. If any pattern of the rule fails, then the entire rule fails. If all the patterns are matched a binding from the knowledge base will be created to update the effector. Successful rules are added to a list. One rule is randomly chosen to be executed. The rule will execute its effect type which could be training a unit, building a structure, harvesting, or attacking. Once the action is executed the knowledge base is completely cleared for the next iteration.

This implementation currently does not handle variables. For instance “Type(X, “Light”) AND Health(X, 5)” may not bind the same unit to X. Due to its complexity this feature may only be added in a future update. While creating the rules for this AI, the strategies of the WorkerRush and LightRush AIs were closely mirrored. A light rush seemed the most successful on the 24x24 maps while worker rushes seemed to quickly overwhelm on smaller maps. The current rule base contains eleven rules. First if there is no worker one is trained and sent to harvest. If the map is smaller than 24x24 then more workers are trained and sent to swarm the opponent. For the large map a barracks is created and then light and ranged units are used to seek and destroy the opponent. The light units can move quickly and are efficient when attacking in groups. The ranged units can easily harass enemy units that are engaged with the light units. Unfortunately, they are mostly useless by themselves except against workers. Currently it is randomly chosen whether a light or ranged unit should be trained at the barracks. An improvement would be adding a rule that creates only 1-2 ranged units after 3-4 light units already exist. This would allow the ranged units to snipe the enemy while they are occupied with the friendly light units.

## microrts

microRTS is a small implementation of an RTS game, designed to perform AI research. The advantage of using microRTS with respect to using a full-fledged game like Wargus or Starcraft (using BWAPI) is that microRTS is much simpler, and can be used to quickly test theoretical ideas, before moving on to full-fledged RTS games.

microRTS is deterministic and real-time (i.e. players can issue actions simultaneously, and actions are durative). It is possible to experiment both with fully-observable and partially-observable games. Thus, it is not adequate for evaluating AI techniques designed to deal with non-determinism (although future versions of microRTS might include non-determinism activated via certain flags). As part of the implementation, I include a collection of hard-coded, and game-tree search techniques (such as variants of minimax, Monte Carlo search, and Monte Carlo Tree Search).

microRTS was developed by [Santiago Ontañón](https://sites.google.com/site/santiagoontanonvillar/Home). 

For a video of how microRTS looks like when a human plays see a [youtube video](https://www.youtube.com/watch?v=ZsKKAoiD7B0)

To cite microRTS, please cite this paper:

Santiago Ontañón (2013) The Combinatorial Multi-Armed Bandit Problem and its Application to Real-Time Strategy Games, In AIIDE 2013. pp. 58 - 64.

## Contributions:

The LSI AI was contributed by Alexander Shleyfman, Antonin Komenda and Carmel Domshlak (the theory behind the AI is described in this [paper](http://iew3.technion.ac.il/~dcarmel/Papers/Sources/ecai14c.pdf).

## Instructions:

![instructions image](https://raw.githubusercontent.com/santiontanon/microrts/master/help.png)
