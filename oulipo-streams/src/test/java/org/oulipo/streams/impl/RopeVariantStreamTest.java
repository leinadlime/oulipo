/*******************************************************************************
 * OulipoMachine licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License.  
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
package org.oulipo.streams.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.oulipo.streams.impl.NodeFactory.getA;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.oulipo.net.TumblerAddress;
import org.oulipo.streams.Span;
import org.oulipo.streams.Spans;
import org.oulipo.streams.VariantSpan;
import org.oulipo.streams.VariantStream;

/**
 * 
 * [100, 6], [200,3], [250, 2], [300, 4], [350, 1], [360, 6]
 */
public class RopeVariantStreamTest {

	public static final TumblerAddress homeDocument = TumblerAddress.createWithNoException("ted://1.2.0.2.0.23.1.1");

	@Test
	public void copy() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.copy(2, new VariantSpan(12, 3));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
	}

	@Test
	public void delete() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(12, 4));// have orphan in list which shouldn't be there [M]
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(new Span(350, 1), spans.get(3));
		assertEquals(new Span(360, 6), spans.get(4));

		assertEquals(5, spans.size());
	}

	@Test
	public void delete2() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(12, 5));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(new Span(360, 6), spans.get(3));
		assertEquals(4, spans.size());
	}

	@Test
	public void delete3() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(1, 11));
		stream.put(1, spans);
		System.out.println(stream.getSpans().getSpans());

		stream.delete(new VariantSpan(1, 5));

		List<Span> results = stream.getSpans().getSpans();
		assertEquals(new Span(6, 6), results.get(0));

	}

	@Test
	public void delete6To19() throws Exception {// 6, 19
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(6, 19));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 5), spans.get(0));
		assertEquals(1, spans.size());
	}

	@Test
	public void deleteAll() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(1, 22));
		List<Span> spans = stream.getSpans().getSpans();
		assertEquals(0, spans.size());
	}

	@Test
	public void deleteBeyondRangeOk() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(12, 100));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);

		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(3, spans.size());
	}

	@Test
	public void deleteLastCharacter() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(1, 4));
		stream.put(1, spans);
		List<Span> results2 = stream.getSpans().getSpans();
		System.out.println("PUT:" + results2);

		stream.delete(new VariantSpan(3, 1));

		List<Span> results = stream.getSpans().getSpans();
		System.out.println("DELETE:" + results);
		assertEquals(new Span(1, 3), results.get(0));
	}

	@Test
	public void deleteOneByte() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(12, 1));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(new Span(301, 3), spans.get(3));
		assertEquals(new Span(350, 1), spans.get(4));
		assertEquals(new Span(360, 6), spans.get(5));

		assertEquals(6, spans.size());
	}

	@Test
	public void deleteOneByteInFirstElement() throws Exception {// 6, 19
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(6, 1));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 5), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(new Span(300, 4), spans.get(3));
		assertEquals(new Span(350, 1), spans.get(4));
		assertEquals(new Span(360, 6), spans.get(5));

		assertEquals(6, spans.size());
	}

	@Test
	public void deleteSomeBytes() throws Exception {
		// * [100, 6], [200,3], [250, 2], [300, 4], [350, 1], [360, 6]
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(8, 7));
		List<Span> spans = stream.getSpans().getSpans();
		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 1), spans.get(1));
		assertEquals(new Span(303, 1), spans.get(2));
		assertEquals(new Span(350, 1), spans.get(3));
		assertEquals(new Span(360, 6), spans.get(4));

		assertEquals(5, spans.size());
	}

	@Test
	public void deleteWithSplit() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.delete(new VariantSpan(12, 6));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
		assertEquals(new Span(100, 6), spans.get(0));
		assertEquals(new Span(200, 3), spans.get(1));
		assertEquals(new Span(250, 2), spans.get(2));
		assertEquals(new Span(361, 5), spans.get(3));
		assertEquals(4, spans.size());
	}

	@Test
	public void getInvariantSpan() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(1, 11));
		stream.put(1, spans);

		List<Span> results = stream.getSpans(new VariantSpan(5, 6)).getSpans();

		assertEquals(new Span(6, 6, homeDocument.toExternalForm()), results.get(0));
	}

	@Test
	public void getInvariantSpansBeyondWidthOk() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(10, 700)).getSpans();
		assertEquals(new Span(250, 2, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(300, 4, homeDocument.toExternalForm()), spans.get(1));
		assertEquals(new Span(350, 1, homeDocument.toExternalForm()), spans.get(2));
		assertEquals(new Span(360, 6, homeDocument.toExternalForm()), spans.get(3));
	}

	@Test
	public void getInvariantSpansMiddle() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(10, 7)).getSpans();
		System.out.println(spans);// TODO

		assertEquals(new Span(250, 2, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(300, 4, homeDocument.toExternalForm()), spans.get(1));
		assertEquals(new Span(350, 1, homeDocument.toExternalForm()), spans.get(2));
	}

	@Test
	public void getInvariantSpansMiddle2() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(10, 6, homeDocument.toExternalForm()))
				.getSpans();
		assertEquals(new Span(250, 2, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(300, 4, homeDocument.toExternalForm()), spans.get(1));
	}

	@Test
	public void getInvariantSpansRangeEdgeLeft() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(1, 14)).getSpans();
		assertEquals(new Span(100, 6, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(200, 3, homeDocument.toExternalForm()), spans.get(1));
		assertEquals(new Span(250, 2, homeDocument.toExternalForm()), spans.get(2));
		assertEquals(new Span(300, 3, homeDocument.toExternalForm()), spans.get(3));
		assertEquals(4, spans.size());
	}

	@Test
	public void getInvariantSpansSmallWidthLeft() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(1, 1)).getSpans();
		assertEquals(new Span(100, 1, homeDocument.toExternalForm()), spans.get(0));
	}

	@Test
	public void getInvariantSpansSmallWidthRight() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		List<Span> spans = stream.getSpans(new VariantSpan(21, 1)).getSpans();
		assertEquals(new Span(365, 1, homeDocument.toExternalForm()), spans.get(0));
	}

	@Test
	public void getVariantSpansSingle() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(7, 5), new Span(4, 3),
				new Span(1, 3));
		stream.put(1, spans);
		List<VariantSpan> result = stream.getVariantSpans(new Span(7, 5));
		assertEquals(result.get(0), new VariantSpan(1, 5, homeDocument.toExternalForm()));

		Span invariantSpan = stream.getSpans(new VariantSpan(1, 5)).getSpans().get(0);
		assertEquals(new Span(7, 5, homeDocument.toExternalForm()), invariantSpan);
	}

	@Test
	public void getVariantSpansTwo() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(7, 5), new Span(4, 3),
				new Span(1, 3));
		stream.put(1, spans);
		List<VariantSpan> result = stream.getVariantSpans(new Span(1, 6));
		assertEquals(2, result.size());
		assertEquals(new VariantSpan(6, 3, homeDocument.toExternalForm()), result.get(0));
		assertEquals(new VariantSpan(9, 3, homeDocument.toExternalForm()), result.get(1));

		System.out.println("Actual:" + result);
		System.out.println("Expected:" + new VariantSpan(6, 6));

		Spans invariantSpans = stream.getSpans(new VariantSpan(6, 6));
		System.out.println(invariantSpans.getSpans());
	}

	@Test
	public void getVariantSpansTwoIntersect() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(7, 5), new Span(4, 3),
				new Span(1, 3));
		stream.put(1, spans);
		List<VariantSpan> result = stream.getVariantSpans(new Span(2, 5));
		assertEquals(2, result.size());
		assertEquals(new VariantSpan(6, 3, homeDocument.toExternalForm()), result.get(0));
		assertEquals(new VariantSpan(10, 2, homeDocument.toExternalForm()), result.get(1));
	}

	@Test
	public void getVariantSpansTwoIntersect2() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(7, 5), new Span(4, 3),
				new Span(1, 3));
		stream.put(1, spans);
		List<VariantSpan> result = stream.getVariantSpans(new Span(1, 4));
		assertEquals(2, result.size());
		assertEquals(new VariantSpan(6, 1, homeDocument.toExternalForm()), result.get(0));
		assertEquals(new VariantSpan(9, 3, homeDocument.toExternalForm()), result.get(1));
	}

	@Test
	public void index() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		Span span = stream.index(12);
		assertEquals(new Span(300, 4), span);
	}

	@Test
	public void index11() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		Span span = stream.index(11);
		assertEquals(new Span(250, 2), span);
		span = stream.index(10);
		assertEquals(new Span(250, 2), span);

	}

	@Test
	public void indexNull() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		Span span = stream.index(100);
		assertNull(span);
	}

	@Test
	public void indexStart() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		Span span = stream.index(1);
		assertEquals(new Span(100, 6), span);
	}

	@Test
	public void move() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.move(2, new VariantSpan(12, 3));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
	}

	@Test
	public void put() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.put(12, new Span(500, 34));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);
	}

	@Test
	public void putSmallParitions() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		stream.put(1, new Span(1, 1));
		stream.put(2, new Span(2, 1));
		stream.put(3, new Span(3, 1));
		stream.put(4, new Span(4, 1));
		stream.put(5, new Span(5, 1));
		stream.put(6, new Span(6, 1));
		stream.put(7, new Span(7, 1));
		stream.put(8, new Span(8, 1));

		List<Span> spans = stream.getSpans(new VariantSpan(3, 4)).getSpans();

		assertEquals(new Span(3, 1, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(4, 1, homeDocument.toExternalForm()), spans.get(1));
		assertEquals(new Span(5, 1, homeDocument.toExternalForm()), spans.get(2));
		assertEquals(new Span(6, 1, homeDocument.toExternalForm()), spans.get(3));

		assertEquals(4, spans.size());

	}
	
	@Test
	public void putSmallParitions2() throws Exception {
		RopeVariantStream stream = new RopeVariantStream(homeDocument);
		stream.put(1, new Span(1, 1));
		stream.put(2, new Span(2, 1));
		stream.put(3, new Span(3, 1));
		stream.put(4, new Span(4, 1));
		stream.put(5, new Span(5, 1));
		stream.put(6, new Span(6, 1));
		stream.put(7, new Span(7, 1));
		stream.put(8, new Span(8, 1));
		
		List<Span> spans = stream.getSpans(new VariantSpan(4, 2)).getSpans();

		assertEquals(new Span(4, 1, homeDocument.toExternalForm()), spans.get(0));
		assertEquals(new Span(5, 1, homeDocument.toExternalForm()), spans.get(1));

		assertEquals(2, spans.size());

	}


	@Test
	public void putSpans() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);
		List<Span> spans = Arrays.asList(new Span(7, 5), new Span(4, 3),
				new Span(1, 3));
		stream.put(1, spans);
		List<Span> results = stream.getSpans().getSpans();
		System.out.println(stream.getSpans().getSpans());

		assertEquals(results.get(0), spans.get(0));
		assertEquals(results.get(1), spans.get(1));
		assertEquals(results.get(2), spans.get(2));
	}

	@Test
	public void putSpansSequence() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);

		stream.put(1, new Span(1, 1));
		stream.put(2, new Span(2, 1));
		stream.put(3, new Span(3, 1));

		List<Span> results = stream.getSpans().getSpans();
		System.out.println(stream.getSpans().getSpans());

		assertEquals(results.get(0), new Span(1, 1));
		assertEquals(results.get(1), new Span(2, 1));
		assertEquals(results.get(2), new Span(3, 1));
	}

	@Test
	public void putSpansSequence2() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument);

		stream.put(1, new Span(1, 1));
		stream.put(2, new Span(2, 1));
		stream.put(1, new Span(3, 1));
		System.out.println("A:" + stream.getSpans().getSpans());

		stream.put(2, new Span(4, 1));

		List<Span> results = stream.getSpans().getSpans();
		System.out.println("B:" + stream.getSpans().getSpans());

		assertEquals(results.get(0), new Span(3, 1));// 3
		assertEquals(results.get(1), new Span(4, 1));// 1
		assertEquals(results.get(2), new Span(1, 1));// 4
		assertEquals(results.get(3), new Span(2, 1));// 2

	}

	@Test
	public void putWithSplit() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.put(5, new Span(500, 34));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);

	}

	@Test
	public void swap() throws Exception {
		VariantStream stream = new RopeVariantStream(homeDocument, getA());
		stream.swap(new VariantSpan(1, 3), new VariantSpan(12, 3));
		List<Span> spans = stream.getSpans().getSpans();
		System.out.println(spans);

		assertEquals(new Span(300, 3), spans.get(0));
		assertEquals(new Span(103, 3), spans.get(1));
		assertEquals(new Span(200, 3), spans.get(2));
		assertEquals(new Span(250, 2), spans.get(3));
		assertEquals(new Span(100, 3), spans.get(4));
		assertEquals(new Span(303, 1), spans.get(5));
		assertEquals(new Span(350, 1), spans.get(6));
		assertEquals(new Span(360, 6), spans.get(7));

		assertEquals(8, spans.size());

		// [Span [start=300, width=3], Span [start=103, width=3],
		// Span [start=200, width=3], Span [start=250, width=2],
		// Span [start=303, width=1], Span [start=100, width=3],
		// Span [start=303, width=1], Span [start=350, width=1],
		// Span [start=360, width=6]]

	}
}
