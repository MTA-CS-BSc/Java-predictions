//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.01 at 01:36:47 PM UTC 
//


package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="random-initialize" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="init" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "PRD-value")
public class PRDValue {

    @XmlAttribute(name = "random-initialize", required = true)
    protected boolean randomInitialize;
    @XmlAttribute(name = "init")
    protected String init;

    protected String currentValue;

    /**
     * Gets the value of the randomInitialize property.
     * 
     */
    public boolean isRandomInitialize() {
        return randomInitialize;
    }

    /**
     * Sets the value of the randomInitialize property.
     * 
     */
    public void setRandomInitialize(boolean value) {
        this.randomInitialize = value;
    }

    /**
     * Gets the value of the init property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInit() {
        return init;
    }

    /**
     * Sets the value of the init property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInit(String value) {
        this.init = value;
    }

    public void setCurrentValue(String value) { this.currentValue = value; }

    public String getCurrentValue() { return this.currentValue; }

}
