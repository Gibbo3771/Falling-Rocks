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

package com.gibbo.fallingrocks.engine;

import com.gibbo.fallingrocks.entity.Entity;

/**
 * 
 * @author Stephen Gibson
 */
public interface Spawn {

	/**
	 * Update the spawner to keep timers and arrays in sync
	 * 
	 * @param delta
	 */
	void update(float delta);

	/**
	 * Spawn an entity
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	Entity spawn();

	/** Delete and entity from the world */
	void delete(Entity entity);

}