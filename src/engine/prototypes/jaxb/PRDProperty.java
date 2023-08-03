package engine.prototypes.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdName",
    "prdRange",
    "prdValue"
})
@XmlRootElement(name = "PRD-property")
public class PRDProperty {

    @XmlElement(name = "PRD-name", required = true)
    protected String prdName;
    @XmlElement(name = "PRD-range")
    protected PRDRange prdRange;
    @XmlElement(name = "PRD-value", required = true)
    protected PRDValue prdValue;
    @XmlAttribute(name = "type", required = true)
    protected String type;
}
