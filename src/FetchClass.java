import soot.*;
import soot.util.*;

import java.util.*;

public class FetchClass {
    public static void main(String[] args) {
        PackManager.v().getPack("wjtp").add(
                new Transform("wjtp.myTransform", new SceneTransformer() {
                    protected void internalTransform(String phaseName,
                                                     Map options) {
                        System.err.println(Scene.v().getApplicationClasses());
                        System.err.println(Scene.v().getApplicationClasses().
                                getFirst().getMethodByName("main"));
                        Chain<SootClass> sClasses = Scene.v().getApplicationClasses();
                        Iterator classIt = sClasses.iterator();
                        while (classIt.hasNext()) {
                            SootClass sClass = (SootClass) classIt.next();
                            System.err.println(sClass.getName());
                            Chain<SootField> sFields = sClass.getFields();
                            Iterator fieldIt = sFields.iterator();
                            while (fieldIt.hasNext()) {
                                SootField sField = (SootField) fieldIt.next();
                                System.err.println(sField.getSignature());
                            }
                        }
                    }
                }));
        soot.Main.main(args);
    }
}
