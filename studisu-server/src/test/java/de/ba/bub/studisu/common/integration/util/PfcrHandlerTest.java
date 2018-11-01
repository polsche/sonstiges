package de.ba.bub.studisu.common.integration.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.ba.bub.studisu.common.integration.util.PfcrHandler.MethodExecutor;
import de.ba.bub.studisu.common.integration.util.PfcrHandler.PfcrLogger;

public class PfcrHandlerTest {

	private PfcrHandler cut;
	private PfcrLogger pfcrLogger;
	private TestPort port;
	private Method methodGet1, methodGet2;
	private MethodExecutor methodExecutor;
	private CorrelationIdHolder correlationIdHolder;

	@Before
	public void before() throws NoSuchMethodException, SecurityException {
		pfcrLogger = mock(PfcrLogger.class);
		port = mock(TestPort.class, withSettings().extraInterfaces(BindingProvider.class));
		Binding binding = mock(Binding.class);
		when(((BindingProvider) port).getBinding()).thenReturn(binding);
		methodExecutor = mock(MethodExecutor.class);
		methodGet1 = TestPort.class.getMethod("get1");
		methodGet2 = TestPort.class.getMethod("get2");
		correlationIdHolder = mock(CorrelationIdHolder.class);
		cut = new PfcrHandler("service123", ((BindingProvider) port));
		cut.setPfcrLogger(pfcrLogger);
		cut.setMethodExecutor(methodExecutor);
		cut.setCorrIdHolder(correlationIdHolder);
	}

	@After
	public void after() {
		pfcrLogger = null;
		port = null;
		methodExecutor = null;
		methodGet1 = null;
		methodGet2 = null;
		cut = null;
		correlationIdHolder = null;
	}

	@Test
	public void testNullResult() throws Throwable {

		Object[] args = new Object[] {};
		when(methodExecutor.execute(methodGet1, port, args)).thenReturn(new ObjectResult(null));
		when(correlationIdHolder.getCorrelationId()).thenReturn("a");

		ObjectResult result = (ObjectResult) cut.invoke(null, methodGet1, args);

		assertNull(result.getObject());

		verify(pfcrLogger).log(eq("get1"), (Exception) isNull(), (Integer) isNull(),
				org.mockito.AdditionalMatchers.gt(-1L), eq("a"));
	}

	@Test
	public void testObjectResult() throws Throwable {

		Object[] args = new Object[] {};
		when(methodExecutor.execute(methodGet2, port, args)).thenAnswer(new Answer<ObjectResult>() {
			   @Override
			   public ObjectResult answer(InvocationOnMock invocation) throws InterruptedException{
			     Thread.sleep(10); // leichte Verzögerung um zu prüfen, ob dem pfcrLogger die korrekte Zeit übermittelt wird
			     return new ObjectResult("abc");
			   }
		});
		when(correlationIdHolder.getCorrelationId()).thenReturn("b");
		
		ObjectResult result = (ObjectResult) cut.invoke(null, methodGet2, args);

		assertEquals("abc", result.getObject());

		verify(pfcrLogger).log(eq("get2"), (Exception) isNull(), (Integer) isNull(),
				org.mockito.AdditionalMatchers.gt(9L), eq("b"));
	}

	@Test
	public void testListResult() throws Throwable {

		Object[] args = new Object[] {};

		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		when(methodExecutor.execute(methodGet2, port, args)).thenReturn(new ListResult(list));
		when(correlationIdHolder.getCorrelationId()).thenReturn("c");

		ListResult result = (ListResult) cut.invoke(null, methodGet2, args);

		assertEquals(list, result.getList());

		verify(pfcrLogger).log(eq("get2"), (Exception) isNull(), eq(3), org.mockito.AdditionalMatchers.gt(-1L),
				eq("c"));
	}

	@Test
	public void testSetResult() throws Throwable {

		Object[] args = new Object[] {};

		Set<String> set = new HashSet<>();
		set.add("1");
		set.add("2");
		when(methodExecutor.execute(methodGet2, port, args)).thenReturn(new SetResult(set));
		when(correlationIdHolder.getCorrelationId()).thenReturn("d");

		SetResult result = (SetResult) cut.invoke(null, methodGet2, args);

		assertEquals(set, result.getSet());

		verify(pfcrLogger).log(eq("get2"), (Exception) isNull(), eq(2), org.mockito.AdditionalMatchers.gt(-1L),
				eq("d"));
	}

	@Test
	public void testFehler() throws Throwable {

		Object[] args = new Object[] {};

		Exception exception = new RuntimeException();
		when(methodExecutor.execute(methodGet1, port, args)).thenThrow(exception);
		when(correlationIdHolder.getCorrelationId()).thenReturn("e");
		
		try {
			cut.invoke(null, methodGet1, args);
			fail("Eigentlich müsste hier eine Exception geflogen sein!");
		} catch (Exception e) {
			verify(pfcrLogger).log(eq("get1"), eq(exception), (Integer) isNull(),
					org.mockito.AdditionalMatchers.gt(-1L), eq("e"));
		}
	}

	private static interface TestPort {

		public Object get1();

		public Object get2();
	}
	
	private static class ObjectResult {

		private Object object;

		public ObjectResult(Object object) {
			super();
			this.object = object;
		}

		public Object getObject() {
			return object;
		}
	}

	private static class ListResult {

		private List<String> list;

		public ListResult(List<String> list) {
			super();
			this.list = list;
		}

		public List<String> getList() {
			return list;
		}
	}

	private static class SetResult {

		private Set<String> set;

		public SetResult(Set<String> set) {
			super();
			this.set = set;
		}

		public Set<String> getSet() {
			return set;
		}
	}

}
