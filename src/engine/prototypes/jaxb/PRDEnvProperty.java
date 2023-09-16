//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.01 at 01:36:47 PM UTC 
//


package engine.prototypes.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}PRD-name"/&gt;
 *         &lt;element ref="{}PRD-range" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="boolean"/&gt;
 *             &lt;enumeration value="decimal"/&gt;
 *             &lt;enumeration value="float"/&gt;
 *             &lt;enumeration value="string"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "prdName",
        "prdRange"
})
@XmlRootElement(name = "PRD-env-property")
public class PRDEnvProperty {

    @XmlElement(name = "PRD-name", required = true)
    protected String prdName;
    @XmlElement(name = "PRD-range")
    protected PRDRange prdRange;

    @XmlAttribute(name = "type", required = true)
    protected String type;

    /**
     * Gets the value of the prdName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPRDName() {
        return prdName;
    }

    /**
     * Sets the value of the prdName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPRDName(String value) {
        this.prdName = value;
    }

    /**
     * Gets the value of the prdRange property.
     *
     * @return possible object is
     * {@link PRDRange }
     */
    public PRDRange getPRDRange() {
        return prdRange;
    }

    /**
     * Sets the value of the prdRange property.
     *
     * @param value allowed object is
     *              {@link PRDRange }
     */
    public void setPRDRange(PRDRange value) {
        this.prdRange = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

}
