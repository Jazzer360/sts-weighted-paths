
//When all External Libraries are added from the pom.xml using maven the code can be uncommented.

//import basemod.BaseMod;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

//@SpireInitializer
public class ModInitializer {


    public ModInitializer(){
        //Use this for when you subscribe to any hooks offered by BaseMod.
        //BaseMod.subscribe(this)
    }

    //Used by @SpireInitializer
    public static void initialize(){

        //This creates an instance of our classes and gets our code going after BaseMod and ModTheSpire initialize.
        ModInitializer modInitializer = new ModInitializer();

        //Look at the BaseMod wiki for getting started. (Skip the decompiling part. It's not needed right now)

    }

}
