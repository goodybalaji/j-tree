/* Copyright 2012 Werner Diwischek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Please include a hint to this site to all the stuff you change or generate<br/>
 * http://www.action4java.org<br/>
 * date 10.3.2012<br/>
 * @author Werner Diwischek
 * @version 0.11
 */

package org.action4java.core.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
* An exception when a value key not found
*/



public class KeyNotFoundException extends Exception {
	public KeyNotFoundException(String message) {
		super(message);
	}

}
