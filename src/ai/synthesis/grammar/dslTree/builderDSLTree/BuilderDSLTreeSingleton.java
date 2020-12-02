package ai.synthesis.grammar.dslTree.builderDSLTree;

import ai.synthesis.dslForScriptGenerator.DSLTableGenerator.FunctionsforDSL;
import ai.synthesis.dslForScriptGenerator.DSLTableGenerator.ParameterDSL;
import ai.synthesis.grammar.dslTree.BooleanDSL;
import ai.synthesis.grammar.dslTree.CDSL;
import ai.synthesis.grammar.dslTree.CommandDSL;
import ai.synthesis.grammar.dslTree.EmptyDSL;
import ai.synthesis.grammar.dslTree.S1DSL;
import ai.synthesis.grammar.dslTree.S2DSL;
import ai.synthesis.grammar.dslTree.S3DSL;
import ai.synthesis.grammar.dslTree.S4DSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import ai.synthesis.grammar.dslTree.interfacesDSL.iNodeDSLTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

class nodeSearchControl {
    private int nodeCount = 1;
    public int getNodeCount() {
        return this.nodeCount;
    }
    public void incrementCounter() {
        ++this.nodeCount;
    }
}

public class BuilderDSLTreeSingleton {
    private final int C_Grammar_Depth = 4;
    private final double C_Grammar_Chances_Empty = 0.05;
    private final double S2_Grammar_Chances_Else = 0.5;
    private final int S4_Grammar_Depth = 4;
    private final int S1_Grammar_Depth = 4;
    private static BuilderDSLTreeSingleton uniqueInstance;
    private final FunctionsforDSL grammar = new FunctionsforDSL();
    private static final Random rand;

    static {
        rand = new Random();
    }

    private BuilderDSLTreeSingleton() {
    }

    public static synchronized BuilderDSLTreeSingleton getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new BuilderDSLTreeSingleton();
        }
        return uniqueInstance;
    }

    public FunctionsforDSL getGrammar() {
        return this.grammar;
    }

    public EmptyDSL buildEmptyDSL() {
        return new EmptyDSL();
    }

    public CDSL buildCGramar(boolean includeUnit) {
        CDSL root = this.buildRandomlyCGramar(includeUnit);
        if (root.getRealCommand() instanceof EmptyDSL) {
            return root;
        }
        CDSL child = root;
        for (int j = 1; j < rand.nextInt(4) + 1; ++j) {
            CDSL next = this.buildRandomlyCGramar(includeUnit);
            child.setNextCommand(next);
            child = next;
            if (!(child.getRealCommand() instanceof EmptyDSL)) continue;
            return root;
        }
        return root;
    }

    public CDSL buildRandomlyCGramar(boolean includeUnit) {
        if (Math.random() < 0.05) {
            return new CDSL(new EmptyDSL());
        }
        return this.buildTerminalCGrammar(includeUnit);
    }

    public CDSL buildTerminalCGrammar(boolean includeUnit) {
        String sCommand = "";
        FunctionsforDSL cGrammar = this.getCommandGrammar(includeUnit);
        sCommand = String.valueOf(sCommand) + cGrammar.getNameFunction() + "(";
        for (ParameterDSL p : cGrammar.getParameters()) {
            if ("u".equals(p.getParameterName())) {
                sCommand = String.valueOf(sCommand) + "u,";
                continue;
            }
            if (p.getDiscreteSpecificValues() == null) {
                int infLim = (int)p.getInferiorLimit();
                int supLim = (int)p.getSuperiorLimit();
                int parametherValueChosen = supLim != infLim ? rand.nextInt(supLim - infLim) + infLim : supLim;
                sCommand = String.valueOf(sCommand) + parametherValueChosen + ",";
                continue;
            }
            int idChosen = rand.nextInt(p.getDiscreteSpecificValues().size());
            String discreteValue = p.getDiscreteSpecificValues().get(idChosen);
            sCommand = String.valueOf(sCommand) + discreteValue + ",";
        }
        sCommand = sCommand.substring(0, sCommand.length() - 1);
        sCommand = String.valueOf(sCommand) + ") ";
        CommandDSL c = new CommandDSL(sCommand);
        return new CDSL(c);
    }

    public BooleanDSL buildBGrammar(boolean includeUnit) {
        FunctionsforDSL tBoolean = this.getBooleanGrammar(includeUnit);
        String tName = String.valueOf(tBoolean.getNameFunction()) + "(";
        for (ParameterDSL p : tBoolean.getParameters()) {
            if ("u".equals(p.getParameterName())) {
                tName = String.valueOf(tName) + "u,";
                continue;
            }
            if (p.getDiscreteSpecificValues() == null) {
                int infLimit = (int)p.getInferiorLimit();
                int supLimit = (int)p.getSuperiorLimit();
                int parametherValueChosen = rand.nextInt(supLimit - infLimit) + infLimit;
                tName = String.valueOf(tName) + parametherValueChosen + ",";
                continue;
            }
            int idChosen = rand.nextInt(p.getDiscreteSpecificValues().size());
            String discreteValue = p.getDiscreteSpecificValues().get(idChosen);
            tName = String.valueOf(tName) + discreteValue + ",";
        }
        tName = tName.substring(0, tName.length() - 1).concat(")");
        return new BooleanDSL(tName);
    }

    public FunctionsforDSL getBooleanGrammar(boolean includeUnit) {
        if (!includeUnit) {
            int idBGrammar = rand.nextInt(this.grammar.getConditionalsForGrammar().size());
            return this.grammar.getConditionalsForGrammar().get(idBGrammar);
        }
        int idBGrammar = rand.nextInt(this.grammar.getConditionalsForGrammarUnit().size());
        return this.grammar.getConditionalsForGrammarUnit().get(idBGrammar);
    }

    public FunctionsforDSL getCommandGrammar(boolean includeUnit) {
        if (!includeUnit) {
            int idBasicActionSelected = rand.nextInt(this.grammar.getBasicFunctionsForGrammar().size());
            return this.grammar.getBasicFunctionsForGrammar().get(idBasicActionSelected);
        }
        int idBasicActionSelected = rand.nextInt(this.grammar.getBasicFunctionsForGrammarUnit().size());
        return this.grammar.getBasicFunctionsForGrammarUnit().get(idBasicActionSelected);
    }

    public S2DSL buildS2ThenGrammar(boolean includeUnit) {
        CDSL C = this.buildCGramar(includeUnit);
        while (C.getRealCommand() instanceof EmptyDSL || C.translate().equals("")) {
            C = this.buildCGramar(includeUnit);
        }
        S2DSL S2 = new S2DSL(this.buildBGrammar(includeUnit), C);
        return S2;
    }

    public S2DSL buildS2ElseGrammar(boolean includeUnit) {
        S2DSL S2 = this.buildS2ThenGrammar(includeUnit);
        S2.setElseCommand(this.buildCGramar(includeUnit));
        return S2;
    }

    public S2DSL buildS2Grammar(boolean includeUnit) {
        if (Math.random() < 0.5) {
            return this.buildS2ThenGrammar(includeUnit);
        }
        return this.buildS2ElseGrammar(includeUnit);
    }

    public S4DSL buildTerminalS4Grammar(boolean includeUnit) {
        return new S4DSL(this.buildEmptyDSL());
    }

    public S4DSL buildS4WithCGrammar(boolean includeUnit) {
        return new S4DSL(this.buildCGramar(includeUnit));
    }

    public S4DSL buildS4WithS2Grammar(boolean includeUnit) {
        return new S4DSL(this.buildS2Grammar(includeUnit));
    }

    public S4DSL buildS4RandomlyGrammar(boolean includeUnit) {
        double p = Math.random();
        if (p < 0.45) {
            return this.buildS4WithCGrammar(includeUnit);
        }
        if (p > 0.45 && p < 0.95) {
            return this.buildS4WithS2Grammar(includeUnit);
        }
        return this.buildTerminalS4Grammar(includeUnit);
    }

    public S4DSL buildS4Grammar(boolean includeUnit) {
        S4DSL root = this.buildS4RandomlyGrammar(includeUnit);
        if (root.getFirstDSL() instanceof EmptyDSL) {
            return root;
        }
        S4DSL child = root;
        for (int j = 1; j < rand.nextInt(4) + 1; ++j) {
            S4DSL next = this.buildS4RandomlyGrammar(includeUnit);
            child.setNextCommand(next);
            child = next;
            if (!(child.getFirstDSL() instanceof EmptyDSL)) continue;
            return root;
        }
        return root;
    }

    public S3DSL buildS3Grammar() {
        S3DSL s3 = new S3DSL(this.buildS4Grammar(true));
        while (s3.translate().equals("for(u) ()")) {
            s3 = new S3DSL(this.buildS4Grammar(true));
        }
        return s3;
    }

    public S1DSL buildTerminalS1Grammar() {
        return new S1DSL(this.buildEmptyDSL());
    }

    public S1DSL buildS1WithCGrammar() {
        return new S1DSL(this.buildCGramar(false));
    }

    public S1DSL buildS1WithS2Grammar() {
        return new S1DSL(this.buildS2Grammar(false));
    }

    public S1DSL buildS1WithS3Grammar() {
        return new S1DSL(this.buildS3Grammar());
    }

    public S1DSL buildS1RandomlyGrammar() {
        double p = Math.random();
        if (p < 0.3) {
            return this.buildS1WithCGrammar();
        }
        if (p > 0.3 && p < 0.6) {
            return this.buildS1WithS2Grammar();
        }
        if (p > 0.6 && p < 0.9) {
            return this.buildS1WithS3Grammar();
        }
        return this.buildTerminalS1Grammar();
    }

    public S1DSL buildS1Grammar() {
        S1DSL root = this.buildS1RandomlyGrammar();
        while (root.translate().equals("")) {
            root = this.buildS1RandomlyGrammar();
        }
        S1DSL child = root;
        for (int j = 1; j < rand.nextInt(4) + 1; ++j) {
            S1DSL next = this.buildS1RandomlyGrammar();
            child.setNextCommand(next);
            child = next;
            if (!(child.getCommandS1() instanceof EmptyDSL)) continue;
            return root;
        }
        return root;
    }

    public static int getNumberofNodes(iNodeDSLTree root) {
        if (root == null) {
            return 0;
        }
        return 1 + BuilderDSLTreeSingleton.getNumberofNodes(root.getRightNode()) + BuilderDSLTreeSingleton.getNumberofNodes(root.getLeftNode());
    }

    public static void simpleTreePreOrderPrint(iNodeDSLTree root) {
        StringBuilder sb = new StringBuilder();
        BuilderDSLTreeSingleton.traversePreOrder(sb, root);
        System.out.println(sb.toString());
    }

    public static void traversePreOrder(StringBuilder sb, iNodeDSLTree node) {
        if (node != null) {
            iDSL temp = (iDSL)((Object)node);
            sb.append(String.valueOf(temp.getClass().getName()) + " " + temp.translate());
            sb.append("\n");
            BuilderDSLTreeSingleton.traversePreOrder(sb, node.getRightNode());
            BuilderDSLTreeSingleton.traversePreOrder(sb, node.getLeftNode());
        }
    }

    public static void fullPreOrderPrint(iNodeDSLTree root) {
        BuilderDSLTreeSingleton.formatedFullTreePreOrderPrint(root);
        BuilderDSLTreeSingleton.formatedDSLTreePreOrderPrint(root);
        BuilderDSLTreeSingleton.formatedGrammarTreePreOrderPrint(root);
        BuilderDSLTreeSingleton.formatedStructuredDSLTreePreOrderPrint(root);
    }

    public static void formatedFullTreePreOrderPrint(iNodeDSLTree root) {
        StringBuilder sb = new StringBuilder();
        BuilderDSLTreeSingleton.traversePreOrderFormated(sb, "", "", root, 1);
        System.out.println(sb.toString());
    }

    public static void formatedDSLTreePreOrderPrint(iNodeDSLTree root) {
        StringBuilder sb = new StringBuilder();
        BuilderDSLTreeSingleton.traversePreOrderFormated(sb, "", "", root, 2);
        System.out.println(sb.toString());
    }

    public static void formatedGrammarTreePreOrderPrint(iNodeDSLTree root) {
        StringBuilder sb = new StringBuilder();
        BuilderDSLTreeSingleton.traversePreOrderFormated(sb, "", "", root, 3);
        System.out.println(sb.toString());
    }

    public static void formatedStructuredDSLTreePreOrderPrint(iNodeDSLTree root) {
        StringBuilder sb = new StringBuilder();
        BuilderDSLTreeSingleton.traversePreOrderFormated(sb, "", "", root, 4);
        System.out.println(sb.toString());
    }

    public static void traversePreOrderFormated(StringBuilder sb, String padding, String pointer, iNodeDSLTree node, int idForm) {
        if (node != null) {
            iDSL temp = (iDSL)((Object)node);
            sb.append(padding);
            sb.append(pointer);
            if (idForm == 1) {
                sb.append(String.valueOf(temp.getClass().getName()) + " " + temp.translate());
            } else if (idForm == 2) {
                sb.append(temp.getClass().getName());
            } else if (idForm == 3) {
                sb.append(temp.translate());
            } else {
                sb.append(node.getFantasyName());
            }
            sb.append("\n");
            StringBuilder paddingBuilder = new StringBuilder(padding);
            paddingBuilder.append("\u2502  ");
            String paddingForBoth = paddingBuilder.toString();
            String pointerForRight = "\u2514\u2500\u2500";
            String pointerForLeft = node.getRightNode() != null ? "\u251c\u2500\u2500" : "\u2514\u2500\u2500";
            BuilderDSLTreeSingleton.traversePreOrderFormated(sb, paddingForBoth, pointerForLeft, node.getLeftNode(), idForm);
            BuilderDSLTreeSingleton.traversePreOrderFormated(sb, paddingForBoth, pointerForRight, node.getRightNode(), idForm);
        }
    }

    public static iNodeDSLTree getNodeByNumber(int nNode, iNodeDSLTree root) {
        if (nNode > BuilderDSLTreeSingleton.getNumberofNodes(root)) {
            return null;
        }
        nodeSearchControl counter = new nodeSearchControl();
        HashSet<iNodeDSLTree> list = new HashSet<iNodeDSLTree>();
        return BuilderDSLTreeSingleton.walkTree(root, counter, nNode, list);
    }

    public static HashSet<iNodeDSLTree> getAllNodes(iNodeDSLTree root) {
        nodeSearchControl counter = new nodeSearchControl();
        HashSet<iNodeDSLTree> list = new HashSet<iNodeDSLTree>();
        BuilderDSLTreeSingleton.walkTree(root, counter, BuilderDSLTreeSingleton.getNumberofNodes(root), list);
        return list;
    }

    public static iNodeDSLTree walkTree(iNodeDSLTree root, nodeSearchControl count, int target, HashSet<iNodeDSLTree> list) {
        iNodeDSLTree left;
        if (root == null) {
            return null;
        }
        if (target == count.getNodeCount()) {
            return root;
        }
        if (!list.contains(root)) {
            list.add(root);
            count.incrementCounter();
        }
        if ((left = BuilderDSLTreeSingleton.walkTree(root.getLeftNode(), count, target, list)) != null) {
            return left;
        }
        iNodeDSLTree right = BuilderDSLTreeSingleton.walkTree(root.getRightNode(), count, target, list);
        return right;
    }

    public boolean hasS3asFather(iNodeDSLTree node) {
        if (node == null) {
            return false;
        }
        if (node.getFather() instanceof S3DSL) {
            return true;
        }
        return this.hasS3asFather((iNodeDSLTree)((Object)node.getFather()));
    }

    public boolean hasS2asFather(iNodeDSLTree node) {
        if (node == null) {
            return false;
        }
        if (node.getFather() instanceof S2DSL) {
            return true;
        }
        return this.hasS2asFather((iNodeDSLTree)((Object)node.getFather()));
    }

    public iDSL generateNewCommand(iDSL dsl, boolean hasS3Father) {
        if (dsl instanceof BooleanDSL) {
            BooleanDSL temp = (BooleanDSL)dsl;
            String newBooleanCommand = this.modifyBoolean(temp, hasS3Father);
            temp.setBooleanCommand(newBooleanCommand);
            return temp;
        }
        if (dsl instanceof CommandDSL) {
            CommandDSL temp = (CommandDSL)dsl;
            if (this.hasS2asFather((iNodeDSLTree)((Object)dsl))) {
                String newCommand = this.modifyCommand(temp, hasS3Father);
                temp.setGrammarDSF(newCommand);
                while (temp.translate().equals("")) {
                    newCommand = this.modifyCommand(temp, hasS3Father);
                    temp.setGrammarDSF(newCommand);
                }
            }
            return temp;
        }
        if (dsl instanceof CDSL) {
            CDSL temp = (CDSL)dsl;
            if (this.hasS2asFather((iNodeDSLTree)((Object)dsl))) {
                CDSL n = this.buildCGramar(hasS3Father);
                temp.setNextCommand(n.getNextCommand());
                temp.setRealCommand(n.getRealCommand());
                while (temp.translate().equals("")) {
                    n = this.buildCGramar(hasS3Father);
                    temp.setNextCommand(n.getNextCommand());
                    temp.setRealCommand(n.getRealCommand());
                }
            }
            return temp;
        }
        if (dsl instanceof EmptyDSL) {
            return this.generateNewCommand(((EmptyDSL)dsl).getFather(), hasS3Father);
        }
        if (dsl instanceof S1DSL) {
            S1DSL temp = (S1DSL)dsl;
            S1DSL n = this.buildS1Grammar();
            temp.setNextCommand(n.getNextCommand());
            temp.setCommandS1(n.getCommandS1());
            return temp;
        }
        if (dsl instanceof S2DSL) {
            S2DSL temp = (S2DSL)dsl;
            S2DSL n = this.modifyS2DLS(temp.clone(), hasS3Father);
            temp.setBoolCommand(n.getBoolCommand());
            temp.setThenCommand(n.getThenCommand());
            temp.setElseCommand(n.getElseCommand());
            return temp;
        }
        if (dsl instanceof S3DSL) {
            S3DSL temp = (S3DSL)dsl;
            S3DSL n = this.buildS3Grammar();
            temp.setForCommand(n.getForCommand());
            return temp;
        }
        if (dsl instanceof S4DSL) {
            S4DSL temp = (S4DSL)dsl;
            S4DSL n = this.buildS4Grammar(hasS3Father);
            temp.setFirstDSL(n.getFirstDSL());
            temp.setNextCommand(n.getNextCommand());
            return temp;
        }
        return null;
    }

    public String modifyBoolean(BooleanDSL bGrammar, boolean hasS3Father) {
        double p = Math.random();
        if (p < 0.4) {
            return this.buildBGrammar(hasS3Father).getBooleanCommand();
        }
        String boolName = bGrammar.getBooleanCommand();
        return this.changeJustBooleanParameters(boolName, hasS3Father);
    }

    public String changeJustBooleanParameters(String boolName, boolean includeUnit) {
        String sName = boolName.substring(0, boolName.indexOf("("));
        List<ParameterDSL> parameters = this.getParameterForBoolean(sName, includeUnit);
        sName = String.valueOf(sName) + "(";
        int iParam = rand.nextInt(parameters.size());
        String[] params = boolName.replace(sName, "").replace("(", "").replace(")", "").split(",");
        for (int i = 0; i < parameters.size(); ++i) {
            sName = sName.trim();
            if (i != iParam) {
                sName = String.valueOf(sName) + params[i] + ",";
                continue;
            }
            ParameterDSL p = (ParameterDSL)parameters.toArray()[iParam];
            if ("u".equals(p.getParameterName())) {
                sName = String.valueOf(sName) + "u,";
                continue;
            }
            if (p.getDiscreteSpecificValues() == null) {
                int infLimit = (int)p.getInferiorLimit();
                int supLimit = (int)p.getSuperiorLimit();
                int parametherValueChosen = rand.nextInt(supLimit - infLimit) + infLimit;
                sName = String.valueOf(sName) + parametherValueChosen + ",";
                continue;
            }
            int idChosen = rand.nextInt(p.getDiscreteSpecificValues().size());
            String discreteValue = p.getDiscreteSpecificValues().get(idChosen);
            sName = String.valueOf(sName) + discreteValue + ",";
        }
        if (boolName.equals(sName = sName.substring(0, sName.length() - 1).trim().concat(")"))) {
            sName = this.changeJustBooleanParameters(boolName, includeUnit);
        }
        return sName;
    }

    public List<ParameterDSL> getParameterForBoolean(String boolName, boolean includeUnit) {
        if (includeUnit) {
            for (FunctionsforDSL f : this.grammar.getConditionalsForGrammarUnit()) {
                if (!f.getNameFunction().equals(boolName)) continue;
                return f.getParameters();
            }
        } else {
            for (FunctionsforDSL f : this.grammar.getConditionalsForGrammar()) {
                if (!f.getNameFunction().equals(boolName)) continue;
                return f.getParameters();
            }
        }
        return null;
    }

    public String modifyCommand(CommandDSL temp, boolean hasS3Father) {
        double p = Math.random();
        if (p < 0.4) {
            return this.buildTerminalCGrammar(hasS3Father).getRealCommand().translate();
        }
        String cName = temp.getGrammarDSF();
        return this.changeJustCommandParameters(cName, hasS3Father);
    }

    public String changeJustCommandParameters(String cName, boolean hasS3Father) {
        String sName = cName.substring(0, cName.indexOf("("));
        List<ParameterDSL> parameters = this.getParameterForCommand(sName, hasS3Father);
        sName = String.valueOf(sName) + "(";
        int iParam = rand.nextInt(parameters.size());
        String[] params = cName.replace(sName, "").replace("(", "").replace(")", "").split(",");
        for (int i = 0; i < parameters.size(); ++i) {
            sName = sName.trim();
            if (i != iParam) {
                sName = String.valueOf(sName) + params[i] + ",";
                continue;
            }
            ParameterDSL p = (ParameterDSL)parameters.toArray()[iParam];
            if ("u".equals(p.getParameterName())) {
                sName = String.valueOf(sName) + "u,";
                continue;
            }
            if (p.getDiscreteSpecificValues() == null) {
                int infLim = (int)p.getInferiorLimit();
                int supLim = (int)p.getSuperiorLimit();
                int parametherValueChosen = supLim != infLim ? rand.nextInt(supLim - infLim) + infLim : supLim;
                sName = String.valueOf(sName) + parametherValueChosen + ",";
                continue;
            }
            int idChosen = rand.nextInt(p.getDiscreteSpecificValues().size());
            String discreteValue = p.getDiscreteSpecificValues().get(idChosen);
            sName = String.valueOf(sName) + discreteValue + ",";
        }
        if (cName.equals(sName = sName.substring(0, sName.length() - 1).trim().concat(")"))) {
            sName = this.changeJustCommandParameters(cName, hasS3Father);
        }
        return sName;
    }

    public List<ParameterDSL> getParameterForCommand(String comName, boolean includeUnit) {
        if (includeUnit) {
            for (FunctionsforDSL f : this.grammar.getBasicFunctionsForGrammarUnit()) {
                if (!f.getNameFunction().equals(comName)) continue;
                return f.getParameters();
            }
        } else {
            for (FunctionsforDSL f : this.grammar.getBasicFunctionsForGrammar()) {
                if (!f.getNameFunction().equals(comName)) continue;
                return f.getParameters();
            }
        }
        return null;
    }

    public S2DSL modifyS2DLS(S2DSL temp, boolean hasS3Father) {
        double p = Math.random();
        if (p < 0.25) {
            return this.buildS2Grammar(hasS3Father);
        }
        if (p >= 0.25 && p < 0.5) {
            BooleanDSL b = new BooleanDSL(this.modifyBoolean((BooleanDSL)temp.getBoolCommand(), hasS3Father));
            temp.setBoolCommand(b);
            return temp;
        }
        if (p >= 0.5 && p < 0.75) {
            CDSL then = this.buildCGramar(hasS3Father);
            while (then.translate().equals("")) {
                then = this.buildCGramar(hasS3Father);
            }
            temp.setThenCommand(then);
            return temp;
        }
        if (p >= 0.75 && p < 1.0) {
            CDSL els = this.buildCGramar(hasS3Father);
            temp.setElseCommand(els);
            return temp;
        }
        return this.buildS2Grammar(hasS3Father);
    }

    public iDSL composeNeighbour(iDSL dsl) {
        String originalDSL = dsl.translate();
        int nNode = rand.nextInt(BuilderDSLTreeSingleton.getNumberofNodes((iNodeDSLTree)((Object)dsl))) + 1;
        iNodeDSLTree targetNode = BuilderDSLTreeSingleton.getNodeByNumber(nNode, (iNodeDSLTree)((Object)dsl));
        this.generateNewCommand((iDSL)((Object)targetNode), this.hasS3asFather(targetNode));
        while (dsl.translate().equals(originalDSL) || dsl.translate().equals("")) {
            dsl = this.composeNeighbour(dsl);
        }
        return dsl;
    }

    public static iDSL getNeighbourAgressively(iDSL dsl) {
        return BuilderDSLTreeSingleton.getInstance().composeNeighbour(dsl);
    }

    public iDSL composeNeighbourPassively(iDSL dsl) {
        String originalDSL = dsl.translate();
        HashSet<iNodeDSLTree> nodes = this.getNodesWithoutDuplicity(dsl);
        List<iNodeDSLTree> fullNodes = this.countNodes(nodes);
        Collections.shuffle(fullNodes);
        int nNode = rand.nextInt(nodes.size());
        iNodeDSLTree targetNode = (iNodeDSLTree)nodes.toArray()[nNode];
        this.generateNewCommand((iDSL)((Object)targetNode), this.hasS3asFather(targetNode));
        while (dsl.translate().equals(originalDSL) || dsl.translate().equals("")) {
            dsl = this.composeNeighbourPassively(dsl);
        }
        return dsl;
    }

    public HashSet<iNodeDSLTree> getNodesWithoutDuplicity(iDSL dsl) {
        HashSet<iNodeDSLTree> list = new HashSet<iNodeDSLTree>();
        BuilderDSLTreeSingleton.collectNodes((iNodeDSLTree)((Object)dsl), list);
        return list;
    }

    public static iNodeDSLTree collectNodes(iNodeDSLTree root, HashSet<iNodeDSLTree> list) {
        iNodeDSLTree left;
        if (root == null) {
            return null;
        }
        if (!list.contains(root)) {
            list.add(root);
        }
        if ((left = BuilderDSLTreeSingleton.collectNodes(root.getLeftNode(), list)) != null) {
            return left;
        }
        iNodeDSLTree right = BuilderDSLTreeSingleton.collectNodes(root.getRightNode(), list);
        return right;
    }

    public List<iNodeDSLTree> countNodes(HashSet<iNodeDSLTree> nodes) {
        ArrayList<iNodeDSLTree> counted = new ArrayList<iNodeDSLTree>();
        for (iNodeDSLTree node : nodes) {
            int total = 0;
            if (node instanceof BooleanDSL) {
                total = this.countParameters(((BooleanDSL)node).getBooleanCommand()) - 1;
            } else if (node instanceof CommandDSL) {
                total = this.countParameters(((CommandDSL)node).getGrammarDSF()) - 1;
            } else if (node instanceof S2DSL) {
                S2DSL s2 = (S2DSL)node;
                total = this.countParameters(s2.getBoolCommand().translate());
                for (int i = 0; i < total; ++i) {
                    counted.add(s2.getBoolCommand());
                }
                total = 0;
            }
            counted.add(node);
            for (int i = 0; i < total; ++i) {
                counted.add(node);
            }
        }
        return counted;
    }

    public int countParameters(String command) {
        String[] items = command.replace("(", ",").split(",");
        return items.length;
    }

    public static iDSL changeNeighbourPassively(iDSL dsl) {
        return BuilderDSLTreeSingleton.getInstance().composeNeighbourPassively(dsl);
    }
}