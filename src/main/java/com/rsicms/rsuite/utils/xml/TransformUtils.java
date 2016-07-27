package com.rsicms.rsuite.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * A collection of transformation-related utility methods. This class has started to undergo a
 * change from only defining static methods to also defining instance methods, in order to make it
 * easier to test code that uses this class. All static methods are deprecated, and use their
 * instance method counterparts.
 */
public class TransformUtils {

  static final String PARAM_NAME_RSUITE_SERVER_URL = "rsuite.serverurl";
  static final String PARAM_NAME_RSUITE_SESSION_KEY = "rsuite.sessionkey";
  static final String PARAM_NAME_RSUITE_USERNAME = "rsuite.username";

  /**
   * Apply the specified XSL to the given MO, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param mo The <code>ManagedObject</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @deprecated Use
   *             {@link #iTransform(ExecutionContext, Session, ManagedObject, Transformer, Map, boolean, String)}
   *             .
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public static InputStream transform(ExecutionContext context, Session session, ManagedObject mo,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return new TransformUtils().iTransform(context, session, mo, transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given MO, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param mo The <code>ManagedObject</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public InputStream iTransform(ExecutionContext context, Session session, ManagedObject mo,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return iTransform(context, session, mo.getInputStream(), transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given FileItem, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param fileItem The <code>FileItem</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @deprecated Use
   *             {@link #iTransform(ExecutionContext, Session, FileItem, Transformer, Map, boolean, String)}
   *             .
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public static InputStream transform(ExecutionContext context, Session session, FileItem fileItem,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return new TransformUtils().iTransform(context, session, fileItem, transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given FileItem, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param fileItem The <code>FileItem</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public InputStream iTransform(ExecutionContext context, Session session, FileItem fileItem,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return iTransform(context, session, fileItem.getInputStream(), transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given input stream, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputStream The <code>InputStream</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @deprecated Use
   *             {@link #iTransform(ExecutionContext, Session, InputStream, Transformer, Map, boolean, String)}
   *             .
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public static InputStream transform(ExecutionContext context, Session session,
      InputStream inputStream, Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return new TransformUtils().iTransform(context, session, inputStream, transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given input stream, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputStream The <code>InputStream</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public InputStream iTransform(ExecutionContext context, Session session, InputStream inputStream,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    // Do not simply use the likes of StreamSource as it won't include an entity resolver.
    // #thanksLukasz
    XMLReader myReader = XMLReaderFactory.createXMLReader();
    myReader.setEntityResolver(context.getXmlApiManager().getRSuiteAwareEntityResolver());

    return iTransform(context, session, new SAXSource(myReader, new InputSource(inputStream)),
        transformer, xslParams, includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given document, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputDoc The <code>Document</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @deprecated Use
   *             {@link #iTransform(ExecutionContext, Session, Document, Transformer, Map, boolean, String)}
   *             .
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public static InputStream transform(ExecutionContext context, Session session, Document inputDoc,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return new TransformUtils().iTransform(context, session, inputDoc, transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given document, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputDoc The <code>Document</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public InputStream iTransform(ExecutionContext context, Session session, Document inputDoc,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return iTransform(context, session, new DOMSource(inputDoc), transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given source, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputSource The <code>Source</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @deprecated Use
   *             {@link #iTransform(ExecutionContext, Session, Source, Transformer, Map, boolean, String)}
   *             .
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public static InputStream transform(ExecutionContext context, Session session, Source inputSource,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    return new TransformUtils().iTransform(context, session, inputSource, transformer, xslParams,
        includeStandardRSuiteXslParams, baseRSuiteUrl);

  }

  /**
   * Apply the specified XSL to the given source, and return the result's input stream.
   * 
   * @param context
   * @param session
   * @param inputSource The <code>Source</code> to apply the XSL to.
   * @param transformer The transformer which has already been given the desired XSL. Parameters to
   *        the XSL will be cleared then reset by this method.
   * @param xslParams Optional parameters to pass into the XSL. Null may be sent in. Hint: List
   *        <String> parameters are received as a sequence, at least with Saxon.
   * @param includeStandardRSuiteXslParams Submit true to ensure XSLT parameters that RSuite
   *        typically provides are included herein, specifically including the base RSuite URL and a
   *        session key.
   * @param baseRSuiteUrl Only used with includeStandardRSuiteXslParams is true.
   * @return The result <code>InputStream</code> of the transform. The caller is responsible for
   *         closing this stream.
   * @throws RSuiteException
   * @throws URISyntaxException
   * @throws TransformerException
   * @throws SAXException
   * @throws IOException
   */
  public InputStream iTransform(ExecutionContext context, Session session, Source inputSource,
      Transformer transformer, Map<String, Object> xslParams,
      boolean includeStandardRSuiteXslParams, String baseRSuiteUrl)
      throws RSuiteException, URISyntaxException, TransformerException, SAXException, IOException {

    ByteArrayOutputStream outputStream = null;

    try {
      outputStream = new ByteArrayOutputStream();
      StreamResult streamResult = new StreamResult(outputStream);
      transformer.clearParameters();

      // Pass on parameters
      if (xslParams == null) {
        xslParams = new HashMap<String, Object>();
      }
      if (includeStandardRSuiteXslParams) {
        iAddStandardRSuiteTransformParameters(session, baseRSuiteUrl, transformer);
      }
      for (Map.Entry<String, Object> entry : xslParams.entrySet()) {
        transformer.setParameter(entry.getKey(), entry.getValue());
      }

      transformer.transform(inputSource, streamResult);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } finally {
      IOUtils.closeQuietly(outputStream);
    }
  }

  /**
   * Get a map of the transformer RSuite includes by default.
   * 
   * @param context Unused.
   * @param session
   * @param baseRSuiteUrl
   * @deprecated Use {@link #iGetStandardRSuiteTransformParameters(Session, String)}.
   * @return Map of the transformer RSuite includes by default.
   */
  public static Map<String, Object> getStandardRSuiteTransformParameters(ExecutionContext context,
      Session session, String baseRSuiteUrl) {
    return new TransformUtils().iGetStandardRSuiteTransformParameters(session, baseRSuiteUrl);
  }

  /**
   * Get a map of the transformer RSuite includes by default.
   * 
   * @param session
   * @param baseRSuiteUrl
   * @return Map of the transformer RSuite includes by default.
   */
  public Map<String, Object> iGetStandardRSuiteTransformParameters(Session session,
      String baseRSuiteUrl) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(PARAM_NAME_RSUITE_SERVER_URL, baseRSuiteUrl);
    params.put(PARAM_NAME_RSUITE_SESSION_KEY, session.getKey());
    params.put(PARAM_NAME_RSUITE_USERNAME, session.getUser().getUserId());
    return params;
  }

  /**
   * Add the parameters that RSuite adds by default.
   * 
   * @param context Unused.
   * @param session
   * @param baseRSuiteUrl
   * @param transformer
   * @deprecated Use {@link #iAddStandardRSuiteTransformParameters(Session, String, Transformer)}.
   */
  public static void addStandardRSuiteTransformParameters(ExecutionContext context, Session session,
      String baseRSuiteUrl, Transformer transformer) {
    new TransformUtils().iAddStandardRSuiteTransformParameters(session, baseRSuiteUrl, transformer);
  }

  /**
   * Add the parameters that RSuite adds by default.
   * 
   * @param session
   * @param baseRSuiteUrl
   * @param transformer
   */
  public void iAddStandardRSuiteTransformParameters(Session session, String baseRSuiteUrl,
      Transformer transformer) {
    if (transformer != null) {
      Map<String, Object> params = iGetStandardRSuiteTransformParameters(session, baseRSuiteUrl);
      if (params != null) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
          transformer.setParameter(entry.getKey(), entry.getValue());
        }
      }
    }
  }
}
