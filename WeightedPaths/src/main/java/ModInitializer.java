import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpireInitializer
public class ModInitializer implements PostDrawSubscriber {

    public ModInitializer() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        ModInitializer modInitializer = new ModInitializer();
    }

    @Override
    public void receivePostDraw(AbstractCard card) {
        System.out.println(card.name + " was drawn!");
    }
}
