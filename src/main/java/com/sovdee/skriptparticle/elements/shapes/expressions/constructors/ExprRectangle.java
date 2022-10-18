package com.sovdee.skriptparticle.elements.shapes.expressions.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sovdee.skriptparticle.elements.shapes.types.Rectangle;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprRectangle extends SimpleExpression<Rectangle> {

    static {
        Skript.registerExpression(ExprRectangle.class, Rectangle.class, ExpressionType.COMBINED, "[a] (rectangle) [with|of] length %number%[,] [and] width %number%");
    }

    Expression<Number> lengthExpr;
    Expression<Number> widthExpr;

    @Override
    protected @Nullable Rectangle[] get(Event event) {
        Number length = lengthExpr.getSingle(event);
        Number width = widthExpr.getSingle(event);
        if (width == null || length == null) return new Rectangle[0];
        double len = length.doubleValue();
        double wid = width.doubleValue();
        if (len <= 0 || wid <= 0) return new Rectangle[0];
        return new Rectangle[]{new Rectangle(length.doubleValue(), width.doubleValue())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Rectangle> getReturnType() {
        return Rectangle.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "rectangle";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        lengthExpr = (Expression<Number>) expressions[0];
        widthExpr = (Expression<Number>) expressions[1];
        return true;
    }
}
