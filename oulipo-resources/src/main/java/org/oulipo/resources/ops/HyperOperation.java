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
package org.oulipo.resources.ops;

import org.oulipo.net.MalformedTumblerException;
import org.oulipo.net.TumblerAddress;

/**
 * Provides information about a specific document edit and the affected document
 * region. A HyperOperation does not know about the underlying content blob.
 */
public final class HyperOperation {

	// copy, delete, insert, swap
	// copy -> COPY (CUT, PASTE)
	// insert -> INSERT, ADD_SPACE, RETURN
	// delete -> DELETE
	// swap -> ????

	public enum OpCode {
		ADD_SPACE((byte) 0), COPY((byte) 2), CUT((byte) 3), DELETE((byte) 1), INSERT((byte) 4), PASTE((byte) 5), RETURN(
				(byte) 6), UNKNOWN((byte) -1);

		public static OpCode valueOf(byte value) {
			switch (value) {
			case 0:
				return ADD_SPACE;
			case 1:
				return DELETE;
			case 2:
				return COPY;
			case 3:
				return CUT;
			case 4:
				return INSERT;
			case 5:
				return PASTE;
			case 6:
				return RETURN;
			default:
				return UNKNOWN;
			}
		}

		public final byte value;

		OpCode(byte value) {
			this.value = value;
		}
	}

	private final HyperRegion documentRegion;
	private final OpCode operation;
	private final String text;

	/**
	 * Tumbler address of the document this hop applies to
	 */
	private final TumblerAddress tumblerAddress;

	public HyperOperation(TumblerAddress tumblerAddress, HyperRegion documentRegion, OpCode operation, String text) {
		this.documentRegion = documentRegion;
		this.operation = operation;
		this.text = text;
		this.tumblerAddress = tumblerAddress;
	}

	public HyperRegion getDocumentRegion() {
		return documentRegion;
	}

	public OpCode getOperation() {
		return operation;
	}

	public String getText() {
		return text;
	}

	public TumblerAddress getTumblerAddress() {
		return tumblerAddress;
	}

	@Override
	public String toString() {
		try {
			return "EditOperation [documentRegion=" + documentRegion + ", operation=" + operation + ", text=" + text
					+ ", tumblerAddress=" + tumblerAddress.toExternalForm() + "]";
		} catch (MalformedTumblerException e) {
			e.printStackTrace();
		}
		return "";
	}
}