
package mage.cards.c;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.ExileTopXMayPlayUntilEndOfTurnEffect;
import mage.abilities.effects.common.cost.SpellsCostReductionControllerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.predicate.card.CastFromZonePredicate;
import mage.game.Game;
import mage.abilities.common.AttacksWithCreaturesTriggeredAbility;
import mage.game.combat.CombatGroup;
import mage.game.events.GameEvent;

import java.util.Objects;

/**
 *
 * @author Zelane
 */
public final class CommanderLiaraPortyr extends CardImpl {

    public CommanderLiaraPortyr(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[] { CardType.CREATURE }, "{3}{R}{W}");

        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SOLDIER);

        this.power = new MageInt(5);
        this.toughness = new MageInt(3);

        // Whenever you attack, spells you cast from exile this turn cost {X} less to cast, 
        // where X is the number of players being attacked. Exile the top X cards of your library. 
        // Until end of turn, you may cast spells from among those exiled cards.
        Ability attackTrigger = new CommanderLiaraPortyrAttackAbility();
        this.addAbility(attackTrigger);
    }

    private CommanderLiaraPortyr(final CommanderLiaraPortyr card) {
        super(card);
    }

    @Override
    public CommanderLiaraPortyr copy() {
        return new CommanderLiaraPortyr(this);
    }
}

class CommanderLiaraPortyrAttackAbility extends AttacksWithCreaturesTriggeredAbility {

    private static final FilterCard filter = new FilterCard("spells you cast from exile");
    static {
        filter.add(new CastFromZonePredicate(Zone.EXILED));
    }

    public CommanderLiaraPortyrAttackAbility() {
        super(null, 0);
    }

    public CommanderLiaraPortyrAttackAbility(AttacksWithCreaturesTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        boolean result = super.checkTrigger(event, game);
        int playersBeingAttacked = game
                .getCombat()
                .getGroups()
                .stream()
                .map(CombatGroup::getDefenderId)
                .distinct()
                .map(game::getPlayer)
                .filter(Objects::nonNull)
                .mapToInt(x -> 1)
                .sum();

        if (result) {
            addEffect(new ExileTopXMayPlayUntilEndOfTurnEffect(playersBeingAttacked, false, Duration.EndOfTurn));
            addEffect(new SpellsCostReductionControllerEffect(filter, playersBeingAttacked));
        }
        return result;
    }
}
