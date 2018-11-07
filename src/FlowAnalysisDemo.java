import soot.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import soot.util.*;

import java.util.*;

/**
 * An analysis to check whether or not local variables have been initialised.
 *
 * @author Ganesh Sittampalam
 * @author Eric Bodden
 */
class InitAnalysis extends ForwardFlowAnalysis {
    FlowSet allLocals;

    public InitAnalysis(UnitGraph g) {
        super(g);
        Chain locs = g.getBody().getLocals();
        allLocals = new ArraySparseSet();
        Iterator it = locs.iterator();
        while (it.hasNext()) {
            Local loc = (Local) it.next();
            allLocals.add(loc);
        }

        doAnalysis();

    }

    protected Object entryInitialFlow() {
        return new ArraySparseSet();
    }

    protected Object newInitialFlow() {
        FlowSet ret = new ArraySparseSet();
        allLocals.copy(ret);
        return ret;
    }

    protected void flowThrough(Object in, Object unit, Object out) {
        FlowSet inSet = (FlowSet) in;
        FlowSet outSet = (FlowSet) out;
        Unit s = (Unit) unit;

        inSet.copy(outSet);

        List<ValueBox> defBoxes = s.getDefBoxes();
        for (ValueBox defBox : defBoxes) {
            Value lhs = defBox.getValue();
            if (lhs instanceof Local) {
                outSet.add(lhs);
            }
        }
        System.err.print("These variables has bee initialised after ");
        System.err.println(s);
        Iterator it = outSet.iterator();
        while (it.hasNext()) {
            System.err.print((Local) it.next());
            System.err.print(" ");
        }
        System.err.println();
    }

    protected void merge(Object in1, Object in2, Object out) {
        FlowSet outSet = (FlowSet) out;
        FlowSet inSet1 = (FlowSet) in1;
        FlowSet inSet2 = (FlowSet) in2;
        inSet1.intersection(inSet2, outSet);
    }

    protected void copy(Object source, Object dest) {
        FlowSet sourceSet = (FlowSet) source;
        FlowSet destSet = (FlowSet) dest;
        sourceSet.copy(destSet);
    }

}

public class FlowAnalysisDemo {
    public static void main(String[] args) {
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new BodyTransformer() {
                    @Override
                    protected void internalTransform(Body body, String s, Map<String, String> map) {
                        new InitAnalysis(new ExceptionalUnitGraph(body));
                    }
                })
        );
        soot.Main.main(args);
    }
}
