package com.rsicms.rsuite.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * A collection of static DOM utility methods.
 */
public class DomUtils {

  @SuppressWarnings("unused")
  private static Log log = LogFactory.getLog(DomUtils.class);

  /**
   * Get the qualified name of the given element.
   * 
   * @param elem
   * @return QName of given element.
   */
  public static QName getQName(Element elem) {
    String nsUri = StringUtils.EMPTY;
    String localName = StringUtils.EMPTY;
    if (elem != null) {
      nsUri = elem.getNamespaceURI();
      localName = elem.getLocalName();
    }
    return new QName(nsUri, localName);
  }

  /**
   * Get an instance of <code>Document</code>, starting from an <code>InputStream</code>.
   * <p>
   * Additional parameters may be added later, if future callers need more control.
   * 
   * @param context
   * @param inputStream
   * @return Document
   * @throws SAXException
   * @throws IOException
   */
  public static Document getDocument(ExecutionContext context, InputStream inputStream)
      throws SAXException, IOException {
    return context.getXmlApiManager().constructNonValidatingDocumentBuilder().parse(inputStream);
  }

  /**
   * Get an <code>InputStream</code> for a <code>Document</code>.
   * <p>
   * Credit to
   * http://stackoverflow.com/questions/865039/how-to-create-an-inputstream-from-a-document-or-node
   * 
   * @param doc
   * @return An input stream, of the given document.
   * @throws TransformerConfigurationException
   * @throws TransformerException
   * @throws TransformerFactoryConfigurationError
   */
  public static InputStream getInputStream(Document doc) throws TransformerConfigurationException,
      TransformerException, TransformerFactoryConfigurationError {
    ByteArrayOutputStream outputStream = null;
    try {
      outputStream = new ByteArrayOutputStream();
      Source xmlSource = new DOMSource(doc);
      Result outputTarget = new StreamResult(outputStream);
      TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } finally {
      IOUtils.closeQuietly(outputStream);
    }
  }

  /**
   * Construct a new Document.
   * <p>
   * An alternative implementation of com.reallysi.tools.DomUtils.newDocument().
   * 
   * @return a new Document
   * @throws ParserConfigurationException
   */
  public static Document newDocument() throws ParserConfigurationException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  }

  /**
   * Get the node value of the first child having the specified name.
   * <p>
   * An alternative implementation of com.reallysi.tools.DomUtils.findFirstChildString().
   * 
   * @param elem
   * @param childElemName
   * @return the qualifying child's node value, or null when there is no such child.
   */
  public static String findFirstChildString(Element elem, String childElemName) {
    NodeList children = elem.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if (child.getNodeName().equals(childElemName)) {
        return child.getNodeValue();
      }
    }
    return null;
  }

  /**
   * Convert a <code>Document</code> to a string, which can be helpful for debugging purposes.
   * <p>
   * The XML declaration is included in the response by default. To override, submit true into
   * {@link #toString(Document, boolean)}.
   * <p>
   * Credit: http://stackoverflow.com/questions/2567416/document-to-string
   * 
   * @param doc
   * 
   * @return String representation of <code>Document</code>
   */
  public static String toString(Document doc) {
    return toString(doc, false);
  }

  /**
   * Convert a <code>Document</code> to a string, which can be helpful for debugging purposes.
   * <p>
   * Credit: http://stackoverflow.com/questions/2567416/document-to-string
   * 
   * @param doc
   * @param omitXmlDeclaration Submit true to exclude the XML declaration.
   * 
   * @return String representation of <code>Document</code>
   */
  public static String toString(Document doc, boolean omitXmlDeclaration) {
    try {
      StringWriter sw = new StringWriter();
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
          omitXmlDeclaration ? "yes" : "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      transformer.transform(new DOMSource(doc), new StreamResult(sw));
      return sw.toString();
    } catch (Exception ex) {
      throw new RuntimeException("Error converting to String", ex);
    }
  }

  /**
   * @deprecated Please switch to
   *             {@link #serializeToString(Transformer, Node, boolean, boolean, String)} or even
   *             {@link #serializeToString(Node)}.
   */
  public static String serializeToString(ExecutionContext context, Node node,
      boolean includeXMLDeclaration, boolean includeDoctypeDeclaration, String encoding)
      throws RSuiteException, TransformerException {
    return serializeToString(context.getXmlApiManager().getTransformer((File) null), node,
        includeXMLDeclaration, includeDoctypeDeclaration, encoding);
  }

  /**
   * Serialize a node the same way ManagedObject#getInputStream does, but allow caller to specify
   * some options.
   * 
   * @param transformer
   * @param node
   * @param includeXMLDeclaration
   * @param includeDoctypeDeclaration
   * @param encoding
   * @since RSuite 3.6.2.2
   * @return A string representation of the given Node, after applying options.
   * @throws RSuiteException
   * @throws TransformerException
   */
  public static String serializeToString(Transformer transformer, Node node,
      boolean includeXMLDeclaration, boolean includeDoctypeDeclaration, String encoding)
      throws RSuiteException, TransformerException {
    if (node == null)
      return null;

    DOMSource ds = new DOMSource(node);
    StringWriter sw = new StringWriter();
    StreamResult sr = new StreamResult(sw);
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
        includeXMLDeclaration ? "no" : "yes");
    Document doc = node.getOwnerDocument();

    if (null != doc) {
      String docxmlenc = doc.getXmlEncoding();
      if (null != docxmlenc) {
        encoding = docxmlenc;
      }
    }

    transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
    if (includeDoctypeDeclaration && doc.getDoctype() != null) {
      DocumentType doctype = doc.getDoctype();
      if (doctype.getPublicId() != null)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
      if (doctype.getSystemId() != null)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
    }

    transformer.transform(ds, sr);

    return sw.toString();
  }

  /**
   * Serialize the node to a string. This signature uses DOMImplementationLS. Use
   * {@link #serializeToString(Transformer, Node, boolean, boolean, String)} for additional control
   * of the output.
   * 
   * @param node
   * @return A string representation of the given node.
   */
  public static String serializeToString(Node node) {
    Document document = node.getOwnerDocument();
    DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
    LSSerializer serializer = domImplLS.createLSSerializer();
    return serializer.writeToString(node);
  }

  /**
   * Control the XML declaration, doctype declaration, and encoding of the provided Element. Same as
   * {@link #serializeToString(Transformer, Node, boolean, boolean, String)} but serves up the
   * return as an Element.
   * 
   * @param transformer
   * @param elem
   * @param includeXMLDeclaration
   * @param includeDoctypeDeclaration
   * @param encoding
   * @return A transformed version of the given Element that abides by the specified parameter
   *         values.
   * @throws TransformerException
   */
  public static Element getElement(Transformer transformer, Element elem,
      boolean includeXMLDeclaration, boolean includeDoctypeDeclaration, String encoding)
      throws TransformerException {
    if (elem == null)
      return null;

    DOMSource ds = new DOMSource(elem);
    DOMResult dr = new DOMResult();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
        includeXMLDeclaration ? "no" : "yes");
    Document doc = elem.getOwnerDocument();

    if (null != doc) {
      String docxmlenc = doc.getXmlEncoding();
      if (null != docxmlenc) {
        encoding = docxmlenc;
      }
    }

    transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
    if (includeDoctypeDeclaration && doc.getDoctype() != null) {
      DocumentType doctype = doc.getDoctype();
      if (doctype.getPublicId() != null)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
      if (doctype.getSystemId() != null)
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
    }

    transformer.transform(ds, dr);

    if (Document.class.isAssignableFrom(dr.getNode().getClass())) {
      return ((Document) dr.getNode()).getDocumentElement();
    }
    throw new TransformerException("DOMResult is not a Document.");
  }

}
