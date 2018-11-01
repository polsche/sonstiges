package de.ba.bub.studisu.common.integration.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InvocationHandler zum Loggen der Correlation-IDs bei Webservice-Calls.
 * 
 * @author StraubP
 */
public class PfcrHandler implements InvocationHandler {

	/**
	 * Logger.
	 */
	private final static Logger LOG = LoggerFactory.getLogger(PfcrHandler.class);

	/**
	 * Holder für die aktuelle Correltation-ID.
	 */
	private CorrelationIdHolder corrIdHolder;

	/**
	 * Webservice-Port für die Aufrufe.
	 */
	private final BindingProvider port;

	/**
	 * Map zu Cachen von Collection-Zugriffsmethoden.
	 */
	private final Map<Class<?>, Method> map = new HashMap<>();

	/**
	 * Ein PfcrLogger.
	 */
	private PfcrLogger pfcrLogger;

	/**
	 * Ein MethodExecutor.
	 */
	private MethodExecutor methodExecutor;

	/**
	 * Komponente, die Pfcr-Einträge loggt.
	 */
	static class PfcrLogger {

		/**
		 * PFCR-Log.
		 */
		private final static Logger LOG_PFCR = LoggerFactory.getLogger("de.ba.bub.studisu.pfcr");

		/**
		 * Ser Service.
		 */
		private String service;

		/**
		 * Konstruktor.
		 * 
		 * @param service
		 *            Name des Webservices, der im PFCR-Log verwendet werden
		 *            soll
		 */
		public PfcrLogger(String service) {
			super();
			this.service = service;
		}

		/**
		 * Loggt einen PFCR-Eintrag.
		 * 
		 * @param methodName
		 *            Name der aufgerufenen Methode
		 * @param fehler
		 *            Exception, falls eine auftritt; sonst null
		 * @param size
		 *            Anzahl der gefundenen Elemente, falls eine Collection
		 *            geliefert wird; sonst null
		 * @param dauer
		 *            Dauer der Methodenausführung in Millis
		 * @param correlationId
		 *            aktuelle Correlation-Id
		 */
		void log(String methodName, Exception fehler, Integer size, long dauer, String correlationId) {
			LOG_PFCR.info(PfcrLogMessageUtil.getLogMessage(service, methodName, dauer, fehler, size, correlationId));
		}
	}

	/**
	 * Wrapper um method.invoke() um die Methode mocken zu können.
	 */
	public static class MethodExecutor {

		/**
		 * Führt eine Methode aus.
		 * 
		 * @param method
		 *            die Methode
		 * @param obj
		 *            die Instanz, auf der die Methode aufgerufen werden soll
		 * @param args
		 *            die Parameter der Methode
		 * @return Erfebnis der Methode
		 * @throws IllegalAccessException
		 *             bei Fehler
		 * @throws IllegalArgumentException
		 *             bei Fehler
		 * @throws InvocationTargetException
		 *             bei Fehler
		 */
		public Object execute(Method method, Object obj, Object[] args)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return method.invoke(obj, args);
		}
	}

	/**
	 * Der Konstruktor.
	 * 
	 * @param service
	 *            Name des Webservices, der im PFCR-Log verwendet werden soll
	 * @param port
	 *            Webservice-Port für die Aufrufe
	 */
	public PfcrHandler(String service, BindingProvider port) {
		super();
		this.port = port;
		this.pfcrLogger = new PfcrLogger(service);
		this.methodExecutor = new MethodExecutor();
		corrIdHolder = CorrelationIdHelper.addCorrelationIdHandler(port);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		long startTs = System.currentTimeMillis();
		Exception fehler = null;
		Integer size = null;
		try {
			Object response = methodExecutor.execute(method, port, args);
			size = findSize(response);
			return response;
		} catch (Exception e) {
			fehler = e;
			throw e;
		} finally {
			long dauer = System.currentTimeMillis() - startTs;
			pfcrLogger.log(method.getName(), fehler, size, dauer, corrIdHolder.getCorrelationId());
			ThreadContext.put("correlationId", corrIdHolder.getCorrelationId());
		}
	}

	/**
	 * Liefert die Anzahl der gelieferten Datensätze, falls die Response eine
	 * Liste von Datensätzen liefert.
	 * 
	 * Dazu gehen wir davon aus, dass es einen public getter gibt, der eine
	 * Collection (List, Set etc.) zurückliefert.
	 * 
	 * @param response
	 *            die Webservice-Response
	 * @return null, falls kein Collection-Getter vorhanden ist
	 */
	private Integer findSize(Object response) {

		/*
		 * ERKLÄRUNG DES CODES:
		 * 
		 * Im PFCR-Log soll für Responses, die eine Liste von Objekten lieferen,
		 * deren Anzahl geloggt werden.
		 * 
		 * Um an diesen Wert zu kommen, haben wir verschiedene Möglichkeiten:
		 * 
		 * 1. Wir schauen, was der tatsächliche Typ der Response ist, casten
		 * darauf und holen daraus die Liste. Dieses Vorgehen hat allerdings den
		 * Nachteil, dass wir in dieser Klasse die möglichen Response-Typen
		 * kennen müssten. Dies wollen wir aber nicht, da der PfcrHandler
		 * komplett unabhängig von konkreten Webservices sein soll. Damit kann
		 * er nämlich einfach in beliebige Projekte eingehängt werden.
		 * 
		 * 2. Wir untersuchen die Response per Reflection auf einen
		 * Rückgabewert, der eine Liste ist, und benutzen dann diesen. Dies
		 * klappt, wenn es genau einen solchen Rückgabewert gibt. Gibt es
		 * mehrere, können wir nicht mehr entscheiden, welchen davon wir
		 * heranziehen sollen.
		 * 
		 * 3. Wir fügen im Konstruktor einen weiteren Parameter hinzu. Dieser
		 * ist ein Interface mit einer Methode: public int getSize(Object
		 * response); In der jeweiligen konkreten Implementierung, kann dann mit
		 * Wissen des entsprechenden Webservices auf den jeweiligen Response-Typ
		 * gecastet werden und daraus die Liste entnommen werden. Dies hält den
		 * PfcrHandler unabhängig von konkreten Webservices, bedeutet aber ein
		 * extra Interface mit passenden Implementierungen - was aber kein
		 * Nachteil ist.
		 * 
		 * Weil bei uns bisher die Bedingungen passen, verwenden wir im
		 * folgenden Möglichkeit 2. Am besten ist aber sicher Möglichkeit 3!
		 */

		Integer size = null;

		if (response == null) {
			return size;
		}

		Class<? extends Object> clazz = response.getClass();

		Method method = findCollectionGetter(clazz);

		if (method != null) {
			try {
				Collection<?> collection = (Collection<?>) method.invoke(response);
				if (collection != null) {
					size = collection.size();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.warn("Could not invoke method " + method.getName(), e);
			}
		}

		return size;
	}

	/**
	 * Sucht und liefert den ersten Collection-Getter einer Klasse.
	 * 
	 * @param clazz
	 *            die Klasse
	 * @return null falls keiner vorhanden
	 */
	private Method findCollectionGetter(Class<? extends Object> clazz) {

		if (map.containsKey(clazz)) {
			return map.get(clazz);
		}

		Method ret = null;

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
					&& method.getName().matches("^get[A-Z].*")
					&& Collection.class.isAssignableFrom(method.getReturnType())) {
				ret = method;
				break;
			}
		}

		map.put(clazz, ret);

		return ret;
	}

	/**
	 * Nur für Tests!
	 * 
	 * @param pfcrLogger
	 *            ein PfcrLogger
	 */
	void setPfcrLogger(PfcrLogger pfcrLogger) {
		this.pfcrLogger = pfcrLogger;
	}

	/**
	 * Nur für Tests!
	 * 
	 * @param methodExecutor
	 *            ein MethodExecutor
	 */
	void setMethodExecutor(MethodExecutor methodExecutor) {
		this.methodExecutor = methodExecutor;
	}

	/**
	 * Nur für Tests!
	 * 
	 * @param corrIdHolder
	 *            ein CorrelationIdHolder
	 */
	void setCorrIdHolder(CorrelationIdHolder corrIdHolder) {
		this.corrIdHolder = corrIdHolder;
	}
}
