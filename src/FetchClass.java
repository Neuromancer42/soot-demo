import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.Transform;

import java.util.Map;

public class FetchClass {
    public static void main(String[] args) {
        PackManager.v().getPack("wjtp").add(
                new Transform("wjtp.myTransform", new SceneTransformer() {
                    protected void internalTransform(String phaseName,
                                                     Map options) {
                        System.err.println(Scene.v().getApplicationClasses());
                    }
                }));
        soot.Main.main(args);
    }
}
