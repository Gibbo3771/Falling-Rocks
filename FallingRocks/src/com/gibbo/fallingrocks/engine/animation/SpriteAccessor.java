package com.gibbo.fallingrocks.engine.animation;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

/** 
 * @author Stephen Gibson 
 * @version 30/09/2013 v2
 */

public class SpriteAccessor implements TweenAccessor<Sprite> {

	public static final int Y = 0, X = 1, ALPHA = 2, SCALE = 3;
	
	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case SCALE:
			returnValues[0] = target.getScaleY();
			return 1;
		case Y:
			returnValues[0] = target.getY();
			return 1;
		case X:
			returnValues[0] = target.getX();
			return 1;
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case SCALE:
			target.setScale(1, newValues[0]);
			break;
		case Y:
			target.setY(newValues[0]);
			break;
		case X:
			target.setX(newValues[0]);
			break;
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g,
					target.getColor().b, newValues[0]);
			break;
		default:
			assert false;
		}

	}

}
