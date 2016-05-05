__author__ = "Vrubel Dmitriy"
__date__ = "$${date} ${time}$"

from marioagent import MarioAgent

class MyAgent(MarioAgent):
    """ My first agent
    """
    action = None
    KEY_JUMP = 3
    KEY_SPEED = 4
    mayMarioJump = None
    isMarioOnGround = None
    isAbleToShoot = None
    MarioMode = None

    receptiveFieldWidth = 19
    receptiveFieldHeight = 19
    marioEgoRow = 9
    marioEgoCol = 9
    Enemies = None
    levelScene = None

    def getName(self):
        return self.agentName

    def reset(self):
        self.action = [0, 0, 0, 0, 0, 0]
        self.isEpisodeOver = False
        
    def __init__(self):
        self.reset()
        self.agentName = "My Python Agent"
    
    def setObservationDetails(self, rfWidth, rfHeight, egoRow, egoCol):
        self.receptiveFieldWidth = rfWidth
        self.receptiveFieldHeight = rfHeight
        self.marioEgoRow = egoRow;
        self.marioEgoCol = egoCol;
        print (egoCol, egoRow, rfWidth, rfHeight)

    def Jump(self):
        for i in range(1, self.receptiveFieldWidth-self.marioEgoCol):
            for j in range (1,i):
                if self.levelScene[self.marioEgoRow+j][self.marioEgoCol+i]!=0:
                    return 1
        return 0

    def Fire(self):
        if self.MarioMode!=2:
            return 1
        for i in range(1, self.receptiveFieldWidth-self.marioEgoCol-4):
            for j in range (-2,2):
                if self.Enemies[self.marioEgoRow+j][self.marioEgoCol+i]!=0:
                    return 0
        return 1

    def getAction(self):
        self.action[1] = 1
        self.action[self.KEY_SPEED] = self.Fire() or self.isAbleToShoot
        self.action[self.KEY_JUMP] = self.Jump() and self.mayMarioJump or not self.isMarioOnGround
        return tuple(self.action)


    def integrateObservation(self, squashedObservation, squashedEnemies, marioPos, enemiesPos, marioState):
        self.isMarioOnGround = marioState[2]
        self.mayMarioJump = marioState[3]
        self.isAbleToShoot = marioState[4]
        self.MarioMode = marioState[1]
        row = self.receptiveFieldHeight
        col = self.receptiveFieldWidth
        levelScene=[]
        enemiesObservation=[]
            
        for i in range(row):
            levelScene.append(squashedObservation[i*col:i*col+col])
            
        for i in range(row):
            enemiesObservation.append(squashedEnemies[i*col:i*col+col])

        self.levelScene = levelScene
        #self.marioFloats = marioPos
        #self.enemiesFloats = enemiesPos
        self.Enemies = enemiesObservation

