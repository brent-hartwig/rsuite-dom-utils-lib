package com.rsicms.rsuite.utils.xml;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.Namespace;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

/**
 * A collection of static XPath utility methods.
 */
public class XPathUtils {

	/**
	 * Get an XPath evaluator, without additional namespaces
	 * 
	 * @param context
	 * @return XPath evaluator
	 * @throws RSuiteException
	 */
	public static XPathEvaluator getXPathEvaluator(ExecutionContext context) throws RSuiteException {
		Namespace namespace[] = null;
		return getXPathEvaluator(context, namespace);
	}

	/**
	 * Get an XPath evaluator, configured with the specified namespace.
	 * 
	 * @param context
	 * @param namespaces
	 *            Optional list of namespace to configure evaluator with.
	 * @return XPath evaluator, configured with the specified namespace
	 * @throws RSuiteException
	 */
	public static XPathEvaluator getXPathEvaluator(ExecutionContext context, Namespace... namespaces)
			throws RSuiteException {
		XPathEvaluator eval = context.getXmlApiManager().getXPathEvaluator();
		if (namespaces != null) {
			for (Namespace namespace : namespaces) {
				if (namespace != null)
					eval.addNamespaceDeclaration(namespace);
			}
		}

		return eval;
	}

}
