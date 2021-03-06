/*******************************************************************************
 * OulipoMachine licenses this file to you under the Apache License, Version 2.0
 * (the "License");  you may not use this file except in compliance with the License.  
 *
 * You may obtain a copy of the License at
 *   
 *       http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See the NOTICE file distributed with this work for 
 * additional information regarding copyright ownership. 
 *******************************************************************************/
package org.oulipo.streams;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

public class VariantSpanTest {

	@Test
	public void homeNoMatch() throws Exception {
		assertFalse(new VariantSpan(1, 9, "fakeHash").equals(new VariantSpan(1, 10)));
	}

	@Test
	public void illegalStart() throws Exception {
		assertThrows(MalformedSpanException.class, () -> {
			new VariantSpan(0, 100);
		});
	}

	@Test
	public void startNoMatch() throws MalformedSpanException {
		assertFalse(new VariantSpan(1, 10).equals(new VariantSpan(2, 10)));
	}

	@Test
	public void widthNoMatch() throws MalformedSpanException {
		assertFalse(new VariantSpan(1, 9).equals(new VariantSpan(1, 10)));
	}

	@Test
	public void zeroWidth() throws Exception {
		assertThrows(MalformedSpanException.class, () -> {
			new VariantSpan(100, 0);
		});
	}

}
