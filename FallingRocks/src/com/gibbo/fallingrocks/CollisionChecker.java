package com.gibbo.fallingrocks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CollisionChecker {

	private Array<Rectangle> r1;
	private Rectangle r2;

	private Vector2 centerR1;
	private Vector2 centerR2;

	// private Array<Rectangle> rectangles;

	public CollisionChecker(Array<Rectangle> r1, Rectangle r2) {
		this.r1 = r1;
		this.r2 = r2;

		r1 = new Array<Rectangle>();
		r2 = new Rectangle();

		centerR1 = new Vector2();
		centerR2 = new Vector2();

	}

	public void update(float delta) {

		for (int i = 0; i < r1.size; i++) {
			r1.get(i).getCenter(centerR1);
			r2.getCenter(centerR2);
			System.out.println(centerR1);

			if (r2.x < r1.get(i).getX() + r1.get(i).getWidth()
					&& r2.x > r1.get(i).getX()
					&& r2.y - r1.get(i).getHeight() < r1.get(i).getY()) {
				System.out.println("I hit the right of the rectangle!");
				r2.y = r2.getY();
				r2.x = r1.get(i).getX() + r1.get(i).getWidth();
			}
			if (r2.x + r2.getWidth() > r1.get(i).getX()
					&& r2.x < r1.get(i).getX()
					&& r2.y - r1.get(i).getHeight() < r1.get(i).getY()) {
				System.out.println("I hit the left of the rectangle!");
				r2.x = r1.get(i).getX() - r2.getWidth();
				r2.y = r2.getY();
			}
			if (r2.y - r1.get(i).getHeight() < r1.get(i).getY()
					&& r2.y - r1.get(i).getHeight() > r1.get(i).getY()) {
				r2.x = r2.x;
				r2.y = r1.get(i).getY();
				System.out.println("I hit the top of the rectangle!");

			}
		}
	}
}
