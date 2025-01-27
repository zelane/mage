package mage.game.permanent.token;

import mage.MageInt;
import mage.constants.CardType;
import mage.constants.SubType;

/**
 * @author spjspj
 */
public final class HammerOfPurphorosGolemToken extends TokenImpl {

    public HammerOfPurphorosGolemToken() {
        super("Golem Token", "3/3 colorless Golem enchantment artifact creature token");
        cardType.add(CardType.ENCHANTMENT);
        cardType.add(CardType.ARTIFACT);
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.GOLEM);
        power = new MageInt(3);
        toughness = new MageInt(3);
    }

    public HammerOfPurphorosGolemToken(final HammerOfPurphorosGolemToken token) {
        super(token);
    }

    public HammerOfPurphorosGolemToken copy() {
        return new HammerOfPurphorosGolemToken(this);
    }
}
