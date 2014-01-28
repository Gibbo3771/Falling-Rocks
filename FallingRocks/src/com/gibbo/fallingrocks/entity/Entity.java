/**
 * Copyright 2013 Stephen Gibson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gibbo.fallingrocks.entity;

/**
 * 
 * @author Stephen Gibson
 */
public abstract class Entity {

	/** Tier of the entity */
	private Tier tier;

	public enum EntityCategory {
		BOUNDARY(0x0001), ROCK(0x0002), GEM(0x0004), PLAYER(0x0006);

		private int value;

		private EntityCategory(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	/**
	 * The Tier system is used to keep an easy to understand hierarchy, Tier
	 * level helps to balance the entities to assign value, damage and spawn
	 * chance
	 * 
	 * @author Stephen Gibson
	 * 
	 */
	public enum Tier {
		TIER1(1), TIER2(2), TIER3(3), TIER4(4);

		private int tier;

		private Tier(int tier) {

		}

		public int getTier() {
			return tier;
		}

	}

	public void setTier(Tier Tier) {
		this.tier = Tier;
	}

	public Tier getTier() {
		return tier;
	}

}
