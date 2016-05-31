package ai.abstraction.rulebased;

import ai.abstraction.pathfinding.BFSPathFinding;
import rts.units.Unit;
import rts.units.UnitTypeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinuj on 5/26/2016.
 * Instantiates a rulesAI using the rules below
 */
public class setRules {

    public static rulesAI instantiate(UnitTypeTable utt) {

        // Setup rulesAI ---------------------------------------------
        // Rule 1: Have base & no workers -> train worker
        String functor = "Type";
        Unit u = new Unit();
        String uType = "Base";
        Term pattern = new Term(functor, u, uType);

        String functor2 = "noUnitType";
        String uType2 = "Worker";
        Term pattern2 = new Term(functor2, false, uType2);

        Term[] patterns = new Term[]{pattern, pattern2};

        String effectFunctor = "Build";
        Term effect = new Term(effectFunctor, u, "Worker");

        Term[] effects = new Term[]{effect};
        int eType = 1;

        Rule r = new Rule(patterns, effects, eType);


        // Rule 2: Have worker & no barracks -> build barracks (24 x 24 Map Only)
        String R2Functor1 = "Type";
        Unit R2U1 = new Unit();
        String R2UType1 = "Worker";
        Term R2Pattern1 = new Term(R2Functor1, R2U1, R2UType1);

        String R2Functor2 = "noUnitType";
        String R2uType2 = "Barracks";
        Term R2Pattern2 = new Term(R2Functor2, false, R2uType2);

        String R2Functor3 = "MapSize";
        String R2Size = "LargeMap";
        Term R2Pattern3 = new Term(R2Functor3, R2Size, R2Size);

        Term[] R2Patterns = new Term[]{R2Pattern1, R2Pattern2, R2Pattern3};

        String R2effectFunctor = "Build";
        Term R2effect = new Term(R2effectFunctor, R2U1, "Barracks");

        Term[] R2effects = new Term[]{R2effect};
        int R2eType = 2;

        Rule r2 = new Rule(R2Patterns, R2effects, R2eType);


        // Rule 3: Have worker -> harvest resources
        String R3Functor1 = "Type";
        Unit R3U1 = new Unit();
        String R3Utype1 = "Worker";
        Term R3Pattern1 = new Term(R3Functor1, R3U1, R3Utype1);

        Term[] R3Patterns = new Term[]{R3Pattern1};

        String R3effectFunctor1 = "Worker";
        Term R3effect1 = new Term(R3effectFunctor1, R3U1, null);

        Term[] R3effects = new Term[]{R3effect1};
        int R3eType = 3;

        Rule r3 = new Rule(R3Patterns, R3effects, R3eType);


        // Rule 4: Have barracks -> train Light
        String R4Functor1 = "Type";
        Unit R4U1 = new Unit();
        String R4Utype1 = "Barracks";
        Term R4Pattern1 = new Term(R4Functor1, R4U1, R4Utype1);

        Term[] R4Patterns = new Term[]{R4Pattern1};

        String R4effectFunctor1 = "Train";
        Term R4effect1 = new Term(R4effectFunctor1, R4U1, "Light");

        Term[] R4effects = new Term[]{R4effect1};
        int R4eType = 1;

        Rule r4 = new Rule(R4Patterns, R4effects, R4eType);


        // Rule 5: Have Light -> Attack
        String R5Functor1 = "Type";
        Unit R5U1 = new Unit();
        String R5Utype1 = "Light";
        Term R5Pattern1 = new Term(R5Functor1, R5U1, R5Utype1);

        Term[] R5Patterns = new Term[]{R5Pattern1};

        String R5effectFunctor1 = "Light";
        Term R5effect1 = new Term(R5effectFunctor1, R5U1, null);

        Term[] R5effects = new Term[]{R5effect1};
        int R5eType = 4;

        Rule r5 = new Rule(R5Patterns, R5effects, R5eType);


        // Rule 6: Have light squad -> attack with squad
        String R6Functor1 = "Squad";
        List<Unit> R6U1 = new ArrayList<>();
        String R6Utype1 = "Light";
        Term R6Pattern1 = new Term(R6Functor1, R6U1, R6Utype1);


        Term[] R6Patterns = new Term[]{R6Pattern1};

        String R6effectFunctor1 = "SquadAttack";
        Term R6effect1 = new Term(R6effectFunctor1, R6U1, null);

        Term[] R6effects = new Term[]{R6effect1};
        int R6eType = 5;

        Rule r6 = new Rule(R6Patterns, R6effects, R6eType);


        // Rule 7: Small Map -> Train Workers
        String R7functor1 = "Type";
        Unit R7U1 = new Unit();
        String R7uType = "Base";
        Term R7Pattern1 = new Term(R7functor1, R7U1, R7uType);

        String R7Functor2 = "MapSize";
        String R7Size = "SmallMap";
        Term R7Pattern2 = new Term(R7Functor2, R7Size, R7Size);

        Term[] R7Patterns = new Term[]{R7Pattern1, R7Pattern2};

        String R7effectFunctor1 = "Build";
        Term R7effect1 = new Term(R7effectFunctor1, R7U1, "Worker");

        Term[] R7effects = new Term[]{R7effect1};
        int R7eType = 1;

        Rule r7 = new Rule(R7Patterns, R7effects, R7eType);


        // Rule 8: Have worker Squad -> attack with squad
        String R8Functor1 = "Squad";
        List<Unit> R8U1 = new ArrayList<>();
        String R8Utype1 = "Worker";
        Term R8Pattern1 = new Term(R8Functor1, R8U1, R8Utype1);

        Term[] R8Patterns = new Term[]{R8Pattern1};

        String R8effectFunctor1 = "SquadAttack";
        Term R8effect1 = new Term(R8effectFunctor1, R8U1, null);

        Term[] R8effects = new Term[]{R8effect1};
        int R8eType = 5;

        Rule r8 = new Rule(R8Patterns, R8effects, R8eType);


        // Rule 9: Have barracks -> train Ranged
        String R9Functor1 = "Type";
        Unit R9U1 = new Unit();
        String R9Utype1 = "Barracks";
        Term R9Pattern1 = new Term(R9Functor1, R9U1, R9Utype1);

        String R9Functor2 = "3ofType";
        String R9Utype2 = "Light";
        Term R9Pattern2 = new Term(R9Functor2, true, R9Utype2);

        Term[] R9Patterns = new Term[]{R9Pattern1, R9Pattern2};

        String R9effectFunctor1 = "Train";
        Term R9effect1 = new Term(R9effectFunctor1, R9U1, "Ranged");

        Term[] R9effects = new Term[]{R9effect1};
        int R9eType = 1;

        Rule r9 = new Rule(R9Patterns, R9effects, R9eType);


        // Rule 10: Have Ranged-> Attack
        String R10Functor1 = "Type";
        Unit R10U1 = new Unit();
        String R10Utype1 = "Ranged";
        Term R10Pattern1 = new Term(R10Functor1, R10U1, R10Utype1);

        Term[] R10Patterns = new Term[]{R10Pattern1};

        String R10effectFunctor1 = "Ranged";
        Term R10effect1 = new Term(R10effectFunctor1, R10U1, null);

        Term[] R10effects = new Term[]{R10effect1};
        int R10eType = 4;

        Rule r10 = new Rule(R10Patterns, R10effects, R10eType);


        // Rule 11: Have ranged squad -> attack with squad
        String R11Functor1 = "Squad";
        List<Unit> R11U1 = new ArrayList<>();
        String R11Utype1 = "Ranged";
        Term R11Pattern1 = new Term(R11Functor1, R11U1, R11Utype1);


        Term[] R11Patterns = new Term[]{R11Pattern1};

        String R11effectFunctor1 = "SquadAttack";
        Term R11effect1 = new Term(R11effectFunctor1, R11U1, null);

        Term[] R11effects = new Term[]{R11effect1};
        int R11eType = 5;

        Rule r11 = new Rule(R11Patterns, R11effects, R11eType);

        Rule[] rules = new Rule[]{r, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11};

        KnaawledgeBase kb = new KnaawledgeBase();

        return new rulesAI(utt, new BFSPathFinding(), kb, rules);
    }
}

