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


/**
 * 
 * @author Stephen Gibson
 */
public class Profile {

	/** Highscore */
	public double highScore;
	/** Draw background */
	public boolean drawBG;
	/** Draw Difficulty overlay */
	public boolean drawDiffOverlay;
	/** Draw FPS counter */
	public boolean drawFPSCounter;
	
	/** True of any settings are changed, forces a save */
	public boolean settingsChanged;
	
	/* Unlocks */
	/** Has hard been unlocked? */
	public boolean isHardUnlocked;
	
	/** Has impossibru been unlocked? */
	public boolean isImpossibruUnlocked;

	public Profile() {

	}

	public Profile(boolean drawBg, boolean drawDiffOverlay,
			boolean drawFPSCounter) {
		this.drawBG = drawBg;
		this.drawDiffOverlay = drawDiffOverlay;
		this.drawFPSCounter = drawFPSCounter;
	}
	
	public void reset(){
		highScore = 0;
		isHardUnlocked = false;
		isImpossibruUnlocked =false;
	}

}
