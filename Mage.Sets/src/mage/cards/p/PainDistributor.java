package mage.cards.p;

import mage.MageInt;
import mage.abilities.common.PutIntoGraveFromBattlefieldAllTriggeredAbility;
import mage.abilities.common.SpellCastAllTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.keyword.MenaceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.TargetController;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterArtifactPermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.token.TreasureToken;
import mage.game.stack.Spell;
import mage.target.targetpointer.FixedTarget;
import mage.watchers.common.SpellsCastWatcher;
import mage.abilities.effects.common.CreateTokenTargetEffect;
import mage.abilities.effects.common.DamageTargetEffect;

import java.util.List;
import java.util.UUID;

/**
 * @author Zelane
 */
public final class PainDistributor extends CardImpl {

    private static final FilterPermanent opponentArtifactFilter = new FilterArtifactPermanent(
            "an artifact an opponent controls");

    static {
        opponentArtifactFilter.add(TargetController.OPPONENT.getControllerPredicate());
    }

    public PainDistributor(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[] { CardType.CREATURE }, "{2}{R}");
        this.subtype.add(SubType.DEVIL);
        this.subtype.add(SubType.CITIZEN);

        this.power = new MageInt(2);
        this.toughness = new MageInt(3);

        // Menace
        this.addAbility(new MenaceAbility(false));

        // Whenever a player casts their first spell each turn, they create a Treasure
        // token.
        this.addAbility(
                new PainDistributorTriggeredAbility(new CreateTokenTargetEffect(new TreasureToken()), false),
                new SpellsCastWatcher());

        // Whenever an artifact an opponent controls is put into a graveyard from the
        // battlefield, Pain Distributor deals 1 damage to that player.
        Effect damageEffect = new DamageTargetEffect(1, true, "that player");
        this.addAbility(
                new PutIntoGraveFromBattlefieldAllTriggeredAbility(damageEffect, false, opponentArtifactFilter, true));
    }

    private PainDistributor(final PainDistributor card) {
        super(card);
    }

    @Override
    public PainDistributor copy() {
        return new PainDistributor(this);
    }
}

class PainDistributorTriggeredAbility extends SpellCastAllTriggeredAbility {

    public PainDistributorTriggeredAbility(PainDistributorTriggeredAbility ability) {
        super(ability);
    }

    public PainDistributorTriggeredAbility(Effect effect, boolean optional) {
        super(effect, optional);
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (super.checkTrigger(event, game)) {
            SpellsCastWatcher watcher = game.getState().getWatcher(SpellsCastWatcher.class);
            if (watcher != null) {
                List<Spell> spells = watcher.getSpellsCastThisTurn(event.getPlayerId());
                if (spells != null && spells.size() == 1) {
                    for (Effect effect : getEffects()) {
                        effect.setTargetPointer(new FixedTarget(event.getPlayerId()));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a player casts their first spell each turn, they create a Treasure token.";
    }

    @Override
    public SpellCastAllTriggeredAbility copy() {
        return new PainDistributorTriggeredAbility(this);
    }
}
