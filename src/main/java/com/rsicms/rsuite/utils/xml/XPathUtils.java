package com.rsicms.rsuite.utils.xml;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.Namespace;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.XmlApiManager;

/**
 * A collection of XPath utility methods.
 */
public class XPathUtils {

  /**
   * Get an XPath evaluator, without additional namespaces
   * 
   * @param context
   * @deprecated Use {@link #getXPathEvaluator(XPathEvaluator)} instead.
   * @return XPath evaluator
   * @throws RSuiteException
   */
  public static XPathEvaluator getXPathEvaluator(ExecutionContext context) throws RSuiteException {
    return new XPathUtils().getXPathEvaluator(context.getXmlApiManager());
  }

  /**
   * Get an XPath evaluator, without additional namespaces
   * 
   * @param xmlApiManager
   * @return XPath evaluator
   * @throws RSuiteException
   */
  public XPathEvaluator getXPathEvaluator(XmlApiManager xmlApiManager) throws RSuiteException {
    Namespace namespace[] = null;
    return getXPathEvaluator(xmlApiManager, namespace);
  }

  /**
   * Get an XPath evaluator, configured with the specified namespace.
   * 
   * @param context
   * @param namespaces Optional list of namespace to configure evaluator with.
   * @deprecated Use {@link #getXPathEvaluator(XPathEvaluator, Namespace...)} instead.
   * @return XPath evaluator, configured with the specified namespace
   * @throws RSuiteException
   */
  public static XPathEvaluator getXPathEvaluator(ExecutionContext context, Namespace... namespaces)
      throws RSuiteException {
    return new XPathUtils().getXPathEvaluator(context.getXmlApiManager(), namespaces);
  }

  /**
   * Get an XPath evaluator, configured with the specified namespace.
   * 
   * @param xmlApiManager
   * @param namespaces Optional list of namespace to configure evaluator with.
   * @return XPath evaluator, configured with the specified namespace
   * @throws RSuiteException
   */
  public XPathEvaluator getXPathEvaluator(XmlApiManager xmlApiManager, Namespace... namespaces)
      throws RSuiteException {
    XPathEvaluator eval = xmlApiManager.getXPathEvaluator();
    if (namespaces != null) {
      for (Namespace namespace : namespaces) {
        if (namespace != null)
          eval.addNamespaceDeclaration(namespace);
      }
    }

    return eval;
  }

}
